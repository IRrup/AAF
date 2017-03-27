/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/

package com.att.authz.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.authz.env.AuthzTrans;
import com.att.authz.layer.Result;
import com.att.authz.layer.Utils;
import com.att.authz.service.mapper.Mapper;
import com.att.authz.service.mapper.Mapper.API;
import com.att.dao.aaf.cass.NsType;
import com.att.dao.aaf.hl.Function;
import com.att.dao.aaf.hl.Question;
import com.att.inno.env.APIException;
import com.datastax.driver.core.Cluster;



@RunWith(MockitoJUnitRunner.class)
public class AuthzCassServiceImplTest {

	private AuthzTrans trans;
	private AuthzCassServiceImpl authzimpl;
	@SuppressWarnings("rawtypes")
	private Mapper mapper;
	private Question ques;
	private Function func;
	private String namespaceName;
	
	@SuppressWarnings("rawtypes")
	AuthzCassServiceImpl authzImpl;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws APIException, IOException {
		Cluster cluster = mock(Cluster.class);
		mapper = mock(Mapper.class);
		trans = mock(AuthzTrans.class);
		ques = mock(Question.class);
		authzimpl = mock(AuthzCassServiceImpl.class);
		namespaceName = "auth";
		when(mapper.ns(trans, API.NS_REQ)).thenReturn(Utils.getNsdImpl());
		when(authzimpl.delAdminNS(Mockito.any(AuthzTrans.class), Mockito.anyString(), Mockito.anyString())).thenReturn(Utils.getPpd());
		when(ques.deriveNs(trans, namespaceName)).thenReturn(Utils.getNsdAO());
		when(authzimpl.delResponsibleNS(Mockito.any(AuthzTrans.class), Mockito.anyString(), Mockito.anyString())).thenReturn(Utils.getPpd());
		when(authzimpl.addAdminNS(Mockito.any(AuthzTrans.class), Mockito.anyString(), Mockito.anyString())).thenReturn(Utils.getPpd());
		authzImpl = new AuthzCassServiceImpl<>(trans, mapper, ques);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createNS() {
		Result<Void> cs = authzImpl.createNS(trans, API.NS_REQ, NsType.fromType(-1));
		assertEquals(cs.status, 4);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createNSNsTypeDOT() {
		Result<Void> cs = authzImpl.createNS(trans, API.NS_REQ, NsType.fromType(0));
		assertEquals(cs.status, 4);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createNSRoot() {
		Result<Void> cs = authzImpl.createNS(trans, API.NS_REQ, NsType.fromType(1));
		assertEquals(cs.status, 4);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createNSCompany() {
		Result<Void> cs = authzImpl.createNS(trans, API.NS_REQ, NsType.fromType(2));
		assertEquals(cs.status, 4);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createNSApp() {
		Result<Void> cs = authzImpl.createNS(trans, API.NS_REQ, NsType.fromType(3));
		assertEquals(cs.status, 4);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createNSStacked_APP() {
		Result<Void> cs = authzImpl.createNS(trans, API.NS_REQ, NsType.fromType(10));
		assertEquals(cs.status, 4);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createNSStack() {
		Result<Void> cs = authzImpl.createNS(trans, API.NS_REQ, NsType.fromType(11));
		assertEquals(cs.status, 4);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void addAdminNSS() {
		String ns = "testNamespace";
		String id = "testName";
		Result<Void> cs = authzimpl.addAdminNS(trans, ns, id);
		assertEquals(cs.status, 0);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void delAdminNSS() {
		String ns = "testNamespace";
		String id = "testName";
		Result<Void> cs = authzimpl.delAdminNS(trans, ns, id);
		assertEquals(cs.status, 0);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void delResponsibleNS() {
		String ns = "testNamespace";
		String id = "testName";
		Result<Void> cs = authzimpl.delResponsibleNS(trans, ns, id);
		assertEquals(cs.status, 0);
	}
}
