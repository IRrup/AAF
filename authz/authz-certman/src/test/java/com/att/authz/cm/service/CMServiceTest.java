/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.authz.cm.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.x509.X509AttributeCertificate;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.authz.cm.ca.CA;
import com.att.authz.cm.cert.CSRMeta;
import com.att.authz.cm.data.CertDrop;
import com.att.authz.cm.data.CertRenew;
import com.att.authz.cm.data.CertReq;
import com.att.authz.cm.data.CertResp;
import com.att.authz.env.AuthzTrans;
import com.att.authz.layer.Result;
import com.att.authz.org.Organization;
import com.att.authz.org.OrganizationException;
import com.att.cadi.Permission;
import com.att.cadi.aaf.AAFPermission;
import com.att.cadi.cm.CertException;
import com.att.dao.aaf.cass.ArtiDAO;
import com.att.dao.aaf.cass.ArtiDAO.Data;
import com.att.inno.env.Trans;

@RunWith(MockitoJUnitRunner.class)
public class CMServiceTest {
	
	
	private static AuthzTrans trans;
	
	private static CertManAPI certman;
	private static CMService cmService;
	
	private static Result<CertReq> req;
	private static Result<CertRenew> renewReq;
	private static Result<CertDrop> drop;
	private static List<ArtiDAO.Data> list;
	private static ArtiDAO.Data artiDao;
	private static String mechid = "998";
	private static String machine = null;
	
	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUp() {
		trans = mock(AuthzTrans.class);
		certman = mock(CertManAPI.class);
		Organization org = mock(Organization.class);
		when(trans.org()).thenReturn(org);
		cmService = mock(CMService.class);
		renewReq = mock(Result.class);
		drop = mock(Result.class);
		artiDao = new ArtiDAO.Data();
		when(trans.fish((Permission) Mockito.any(AAFPermission.class))).thenReturn(false);
		CA ca = new CA("testName",null,"root") {
			
			@Override
			public X509Certificate sign(Trans trans, CSRMeta csrmeta) throws IOException, CertException {
				X509Certificate cert = mock(X509Certificate.class);
				return cert;
			}
		};
		req = mock(Result.class);
		list = new ArrayList<>();
		list.add(new ArtiDAO.Data());
		list.add(new ArtiDAO.Data());
		when(req.isOK()).thenReturn(true);
		when(renewReq.isOK()).thenReturn(true);
		when(drop.isOK()).thenReturn(true);
		try {
			when(cmService.readArtifacts(trans, artiDao)).thenCallRealMethod();
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		when(cmService.createArtifact(trans, list)).thenCallRealMethod();
		when(cmService.dropCert(trans, drop)).thenCallRealMethod();
		when(cmService.requestCert(trans, req)).thenCallRealMethod();
		try {
			when(cmService.deleteArtifact(trans, mechid, machine)).thenCallRealMethod();
		} catch (OrganizationException e1) {
			e1.printStackTrace();
		}
		when(cmService.renewCert(trans, renewReq)).thenCallRealMethod();
		try {
			when(cmService.updateArtifact(trans, list)).thenCallRealMethod();
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void requestCert() {
		Result<CertResp> resp = cmService.requestCert(trans, req);
		assertEquals(resp.status,4);
	}
	
	@Test
	public void renewCert() {
		Result<CertResp> resp = cmService.renewCert(trans, renewReq);
		assertEquals(resp.status,5);
	}
	
	@Test
	public void dropCert() {
		Result<Void> resp = cmService.dropCert(trans, drop);
		assertEquals(resp.status,5);
	}
	
	@Test
	public void createArtifact() {
		Result<Void> resp = cmService.createArtifact(trans, list);
		assertEquals(resp.status, 4);
	}
	
	@Test
	public void readArtifact() {
		Result<List<ArtiDAO.Data>> resp = null;
		try {
			resp = cmService.readArtifacts(trans, artiDao);
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		assertNotNull(resp);
		assertEquals(resp.status, 4);
	}
	
	@Test
	public void readArtifactsByMechID() {
		Result<List<ArtiDAO.Data>> resp = null;
		try {
			resp = cmService.readArtifactsByMechID(trans, mechid);
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		assertNull(resp);
	}
	
	@Test
	public void updateArtifacts() {
		try {
			Result<Void> resp = cmService.updateArtifact(trans, list);
			assertEquals(resp.status, 4);
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void delArtifacts() {
		try {
			Result<Void> resp = cmService.deleteArtifact(trans, mechid, machine);
			assertEquals(resp.status, 4);
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		
	}
}
