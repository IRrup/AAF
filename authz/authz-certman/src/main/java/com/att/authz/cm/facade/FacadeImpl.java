/*******************************************************************************
 * Copyright (c) 2016 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.authz.cm.facade;

import static com.att.authz.layer.Result.ERR_ActionNotCompleted;
import static com.att.authz.layer.Result.ERR_BadData;
import static com.att.authz.layer.Result.ERR_ConflictAlreadyExists;
import static com.att.authz.layer.Result.ERR_Denied;
import static com.att.authz.layer.Result.ERR_NotFound;
import static com.att.authz.layer.Result.ERR_NotImplemented;
import static com.att.authz.layer.Result.ERR_Policy;
import static com.att.authz.layer.Result.ERR_Security;
import static com.att.authz.layer.Result.OK;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.att.authz.cm.api.API_Cert;
import com.att.authz.cm.ca.CA;
import com.att.authz.cm.data.CertResp;
import com.att.authz.cm.mapper.Mapper;
import com.att.authz.cm.mapper.Mapper.API;
import com.att.authz.cm.service.CMService;
import com.att.authz.cm.service.CertManAPI;
import com.att.authz.env.AuthzEnv;
import com.att.authz.env.AuthzTrans;
import com.att.authz.layer.Result;
import com.att.cadi.aaf.AAFPermission;
import com.att.dao.aaf.cass.ArtiDAO;
import com.att.dao.aaf.cass.Status;
import com.att.inno.env.APIException;
import com.att.inno.env.Data;
import com.att.inno.env.Env;
import com.att.inno.env.Slot;
import com.att.inno.env.TimeTaken;
import com.att.inno.env.util.Split;
import com.att.rosetta.env.RosettaDF;
import com.att.rosetta.env.RosettaData;

/**
 * AuthzFacade
 * 
 * This Service Facade encapsulates the essence of the API Service can do, and provides
 * a single created object for elements such as RosettaDF.
 *
 * The Responsibilities of this class are to:
 * 1) Interact with the Service Implementation (which might be supported by various kinds of Backend Storage)
 * 2) Validate incoming data (if applicable)
 * 3) Convert the Service response into the right Format, and mark the Content Type
 * 		a) In the future, we may support multiple Response Formats, aka JSON or XML, based on User Request.
 * 4) Log Service info, warnings and exceptions as necessary
 * 5) When asked by the API layer, this will create and write Error content to the OutputStream
 * 
 * Note: This Class does NOT set the HTTP Status Code.  That is up to the API layer, so that it can be 
 * clearly coordinated with the API Documentation
 * 
 *
 */
public abstract class FacadeImpl<REQ,CERT,ARTIFACTS,ERROR> extends com.att.authz.layer.FacadeImpl implements Facade<REQ,CERT,ARTIFACTS,ERROR> 
	{
	private static final String REQUEST_CERT = "Request New Certificate";
	private static final String RENEW_CERT = "Renew Certificate";
	private static final String DROP_CERT = "Drop Certificate";
	private static final String CREATE_ARTIFACTS = "Create Deployment Artifact";
	private static final String READ_ARTIFACTS = "Read Deployment Artifact";
	private static final String UPDATE_ARTIFACTS = "Update Deployment Artifact";
	private static final String DELETE_ARTIFACTS = "Delete Deployment Artifact";

	private CMService service;

	private final RosettaDF<ERROR>	 	errDF;
	private final RosettaDF<REQ> 		certRequestDF, certRenewDF, certDropDF;
	private final RosettaDF<CERT>		certDF;
	private final RosettaDF<ARTIFACTS>	artiDF;
	private Mapper<REQ, CERT, ARTIFACTS, ERROR> 	mapper;
	private Slot sCertAuth;
	private CertManAPI certman;
	private final String voidResp;

	public FacadeImpl(CertManAPI certman,
					  CMService service, 
					  Mapper<REQ,CERT,ARTIFACTS,ERROR> mapper, 
					  Data.TYPE dataType) throws APIException {
		this.service = service;
		this.mapper = mapper;
		this.certman = certman;
		AuthzEnv env = certman.env;
		(errDF 				= env.newDataFactory(mapper.getClass(API.ERROR))).in(dataType).out(dataType);
		(certRequestDF 		= env.newDataFactory(mapper.getClass(API.CERT_REQ))).in(dataType).out(dataType);
		(certRenewDF 		= env.newDataFactory(mapper.getClass(API.CERT_RENEW))).in(dataType).out(dataType);
		(certDropDF 		= env.newDataFactory(mapper.getClass(API.CERT_DROP))).in(dataType).out(dataType);
		(certDF 			= env.newDataFactory(mapper.getClass(API.CERT))).in(dataType).out(dataType);
		(artiDF 			= env.newDataFactory(mapper.getClass(API.ARTIFACTS))).in(dataType).out(dataType);
		sCertAuth = env.slot(API_Cert.CERT_AUTH);
		if(artiDF.getOutType().name().contains("xml")) {
			voidResp = "application/Void+xml;charset=utf-8;version=1.0,application/xml;version=1.0,*/*";
		} else {
			voidResp = "application/Void+json;charset=utf-8;version=1.0,application/json;version=1.0,*/*";
		}
	}
	
	public Mapper<REQ,CERT,ARTIFACTS,ERROR> mapper() {
		return mapper;
	}
	
	/* (non-Javadoc)
	 * @see com.att.authz.facade.AuthzFacade#error(com.att.authz.env.AuthzTrans, javax.servlet.http.HttpServletResponse, int)
	 * 
	 * Note: Conforms to AT&T TSS RESTful Error Structure
	 */
	@Override
	public void error(AuthzTrans trans, HttpServletResponse response, Result<?> result) {
		error(trans, response, result.status,
				result.details==null?"":result.details.trim(),
				result.variables==null?new String[0]:result.variables);
	}
		
	@Override
	public void error(AuthzTrans trans, HttpServletResponse response, int status, final String _msg, final String ... _detail) {
		String msgId;
		String prefix;
		switch(status) {
			case 202:
			case ERR_ActionNotCompleted:
				msgId = "SVC1202";
				prefix = "Accepted, Action not complete";
				response.setStatus(/*httpstatus=*/202);
				break;

			case 403:
			case ERR_Policy:
			case ERR_Security:
			case ERR_Denied:
				msgId = "SVC1403";
				prefix = "Forbidden";
				response.setStatus(/*httpstatus=*/403);
				break;
				
			case 404:
			case ERR_NotFound:
				msgId = "SVC1404";
				prefix = "Not Found";
				response.setStatus(/*httpstatus=*/404);
				break;

			case 406:
			case ERR_BadData:
				msgId="SVC1406";
				prefix = "Not Acceptable";
				response.setStatus(/*httpstatus=*/406);
				break;
				
			case 409:
			case ERR_ConflictAlreadyExists:
				msgId = "SVC1409";
				prefix = "Conflict Already Exists";
				response.setStatus(/*httpstatus=*/409);
				break;
			
			case 501:
			case ERR_NotImplemented:
				msgId = "SVC1501";
				prefix = "Not Implemented"; 
				response.setStatus(/*httpstatus=*/501);
				break;
				

			default:
				msgId = "SVC1500";
				prefix = "General Service Error";
				response.setStatus(/*httpstatus=*/500);
				break;
		}

		try {
			StringBuilder holder = new StringBuilder();
			errDF.newData(trans).load(
				mapper().errorFromMessage(holder, msgId,prefix + ": " + _msg,_detail)).to(response.getOutputStream());
			
			holder.append(']');
			trans.checkpoint(
					"ErrResp [" + 
					holder,
					Env.ALWAYS);
		} catch (Exception e) {
			trans.error().log(e,"unable to send response for",_msg);
		}
	}

	@Override
	public Result<Void> check(AuthzTrans trans, HttpServletResponse resp, String perm) throws IOException {
		String[] p = Split.split('|',perm);
		if(p.length!=3) {
			return Result.err(Result.ERR_BadData,"Invalid Perm String");
		}
		AAFPermission ap = new AAFPermission(p[0],p[1],p[2]);
		if(certman.aafLurPerm.fish(trans.getUserPrincipal(), ap)) {
			resp.setContentType(voidResp);
			resp.getOutputStream().write(0);
			return Result.ok();
		} else {
			return Result.err(Result.ERR_Denied,"%s does not have %s",trans.user(),ap.getKey());
		}
	}

	/* (non-Javadoc)
	 * @see com.att.auth.certman.facade.Facade#requestCert(com.att.authz.env.AuthzTrans, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public Result<Void> requestCert(AuthzTrans trans, HttpServletRequest req, HttpServletResponse resp, boolean withTrust) {
		TimeTaken tt = trans.start(REQUEST_CERT, Env.SUB|Env.ALWAYS);
		try {
			REQ request;
			try {
				Data<REQ> rd = certRequestDF.newData().load(req.getInputStream());
				request = rd.asObject();
			} catch(APIException e) {
				trans.error().log("Invalid Input",IN,REQUEST_CERT);
				return Result.err(Result.ERR_BadData,"Invalid Input");
			}
			
			Result<CertResp> rcr = service.requestCert(trans,mapper.toReq(trans,request));
			if(rcr.notOK()) {
				return Result.err(rcr);
			}
			
			CA certAuth = trans.get(sCertAuth,null);
			Result<CERT> rc = mapper.toCert(trans, rcr, withTrust?certAuth.getTrustChain():null);
			switch(rc.status) {
			case OK: 
				RosettaData<CERT> data = certDF.newData(trans).load(rc.value);
				data.to(resp.getOutputStream());

				setContentType(resp,certDF.getOutType());
				return Result.ok();
			default:
				return Result.err(rc);
		}

		} catch (Exception e) {
			trans.error().log(e,IN,REQUEST_CERT);
			return Result.err(e);
		} finally {
			tt.done();
		}
	}
	
	@Override
	public Result<Void> renewCert(AuthzTrans trans, HttpServletRequest req, HttpServletResponse resp, boolean withTrust) {
		TimeTaken tt = trans.start(RENEW_CERT, Env.SUB|Env.ALWAYS);
		try {
			REQ request;
			try {
				Data<REQ> rd = certRenewDF.newData().load(req.getInputStream());
				request = rd.asObject();
			} catch(APIException e) {
				trans.error().log("Invalid Input",IN,RENEW_CERT);
				return Result.err(Result.ERR_BadData,"Invalid Input");
			}
			
			String certAuth = trans.get(sCertAuth,null);
			Result<CertResp> rcr = service.renewCert(trans,mapper.toRenew(trans,request));
			Result<CERT> rc = mapper.toCert(trans, rcr, certman.getTrustChain(certAuth));

			switch(rc.status) {
				case OK: 
					RosettaData<CERT> data = certDF.newData(trans).load(rc.value);
					data.to(resp.getOutputStream());

					setContentType(resp,certDF.getOutType());
					return Result.ok();
				default:
					return Result.err(rc);
			}
		} catch (Exception e) {
			trans.error().log(e,IN,RENEW_CERT);
			return Result.err(e);
		} finally {
			tt.done();
		}

	}

	@Override
	public Result<Void> dropCert(AuthzTrans trans, HttpServletRequest req, HttpServletResponse resp) {
		TimeTaken tt = trans.start(DROP_CERT, Env.SUB|Env.ALWAYS);
		try {
			REQ request;
			try {
				Data<REQ> rd = certDropDF.newData().load(req.getInputStream());
				request = rd.asObject();
			} catch(APIException e) {
				trans.error().log("Invalid Input",IN,DROP_CERT);
				return Result.err(Result.ERR_BadData,"Invalid Input");
			}
			
			Result<Void> rv = service.dropCert(trans,mapper.toDrop(trans, request));
			switch(rv.status) {
				case OK: 
					setContentType(resp,certRequestDF.getOutType());
					return Result.ok();
				default:
					return Result.err(rv);
			}
		} catch (Exception e) {
			trans.error().log(e,IN,DROP_CERT);
			return Result.err(e);
		} finally {
			tt.done();
		}
	}

	////////////////////////////
	// Artifacts
	////////////////////////////
	@Override
	public Result<Void> createArtifacts(AuthzTrans trans, HttpServletRequest req, HttpServletResponse resp) {
		TimeTaken tt = trans.start(CREATE_ARTIFACTS, Env.SUB);
		try {
			ARTIFACTS arti;
			try {
				Data<ARTIFACTS> rd = artiDF.newData().load(req.getInputStream());
				arti = rd.asObject();
			} catch(APIException e) {
				trans.error().log("Invalid Input",IN,CREATE_ARTIFACTS);
				return Result.err(Result.ERR_BadData,"Invalid Input");
			}
			
			return service.createArtifact(trans,mapper.toArtifact(trans,arti));
		} catch (Exception e) {

			trans.error().log(e,IN,CREATE_ARTIFACTS);
			return Result.err(e);
		} finally {
			tt.done();
		}
	}

	@Override
	public Result<Void> readArtifacts(AuthzTrans trans, HttpServletRequest req, HttpServletResponse resp) {
		TimeTaken tt = trans.start(READ_ARTIFACTS, Env.SUB);
		try {
			String mechid = req.getParameter("mechid");
			String machine = req.getParameter("machine");
			
			Result<ARTIFACTS> ra;
			if( machine !=null && mechid == null) {
				ra = mapper.fromArtifacts(service.readArtifactsByMachine(trans, machine));
			} else if(mechid!=null && machine==null) {
				ra = mapper.fromArtifacts(service.readArtifactsByMechID(trans, mechid));
			} else if(mechid!=null && machine!=null) {
				ArtiDAO.Data add = new ArtiDAO.Data();
				add.mechid = mechid;
				add.machine = machine;
				ra = mapper.fromArtifacts(service.readArtifacts(trans,add));
			} else {
				ra = Result.err(Status.ERR_BadData,"Invalid request inputs");
			}
			
			if(ra.isOK()) {
				RosettaData<ARTIFACTS> data = artiDF.newData(trans).load(ra.value);
				data.to(resp.getOutputStream());
				setContentType(resp,artiDF.getOutType());
				return Result.ok();
			} else {
				return Result.err(ra);
			}

		} catch (Exception e) {
			trans.error().log(e,IN,READ_ARTIFACTS);
			return Result.err(e);
		} finally {
			tt.done();
		}
	}

	@Override
	public Result<Void> readArtifacts(AuthzTrans trans, HttpServletResponse resp, String mechid, String machine) {
		TimeTaken tt = trans.start(READ_ARTIFACTS, Env.SUB);
		try {
			ArtiDAO.Data add = new ArtiDAO.Data();
			add.mechid = mechid;
			add.machine = machine;
			Result<ARTIFACTS> ra = mapper.fromArtifacts(service.readArtifacts(trans,add));
			if(ra.isOK()) {
				RosettaData<ARTIFACTS> data = artiDF.newData(trans).load(ra.value);
				data.to(resp.getOutputStream());
				setContentType(resp,artiDF.getOutType());
				return Result.ok();
			} else {
				return Result.err(ra);
			}
		} catch (Exception e) {
			trans.error().log(e,IN,READ_ARTIFACTS);
			return Result.err(e);
		} finally {
			tt.done();
		}
	}


	@Override
	public Result<Void> updateArtifacts(AuthzTrans trans, HttpServletRequest req, HttpServletResponse resp) {
		TimeTaken tt = trans.start(UPDATE_ARTIFACTS, Env.SUB);
		try {
			ARTIFACTS arti;
			try {
				Data<ARTIFACTS> rd = artiDF.newData().load(req.getInputStream());
				arti = rd.asObject();
			} catch(APIException e) {
				trans.error().log("Invalid Input",IN,UPDATE_ARTIFACTS);
				return Result.err(Result.ERR_BadData,"Invalid Input");
			}
			
			return service.updateArtifact(trans,mapper.toArtifact(trans,arti));
		} catch (Exception e) {
			trans.error().log(e,IN,UPDATE_ARTIFACTS);
			return Result.err(e);
		} finally {
			tt.done();
		}
	}

	@Override
	public Result<Void> deleteArtifacts(AuthzTrans trans, HttpServletRequest req, HttpServletResponse resp) {
		TimeTaken tt = trans.start(DELETE_ARTIFACTS, Env.SUB);
		try {
			ARTIFACTS arti;
			try {
				Data<ARTIFACTS> rd = artiDF.newData().load(req.getInputStream());
				arti = rd.asObject();
			} catch(APIException e) {
				trans.error().log("Invalid Input",IN,DELETE_ARTIFACTS);
				return Result.err(Result.ERR_BadData,"Invalid Input");
			}
			
			Result<Void> rv = service.deleteArtifact(trans,mapper.toArtifact(trans,arti));
			switch(rv.status) {
				case OK: 
					setContentType(resp,artiDF.getOutType());
			} 
			return rv;
		} catch (Exception e) {
			trans.error().log(e,IN,DELETE_ARTIFACTS);
			return Result.err(e);
		} finally {
			tt.done();
		}
	}

	@Override
	public Result<Void> deleteArtifacts(AuthzTrans trans, HttpServletResponse resp, String mechid, String machine) {
		TimeTaken tt = trans.start(DELETE_ARTIFACTS, Env.SUB);
		try {
			Result<Void> rv = service.deleteArtifact(trans, mechid, machine);
			switch(rv.status) {
				case OK: 
					setContentType(resp,artiDF.getOutType());
			} 
			return rv;
		} catch (Exception e) {
			trans.error().log(e,IN,DELETE_ARTIFACTS);
			return Result.err(e);
		} finally {
			tt.done();
		}
	}


}