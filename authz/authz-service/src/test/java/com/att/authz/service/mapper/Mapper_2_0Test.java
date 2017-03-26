/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.authz.service.mapper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.authz.env.AuthzTrans;
import com.att.authz.layer.Result;
import com.att.authz.layer.Utils;
import com.att.dao.aaf.cass.Namespace;
import com.att.dao.aaf.cass.NsDAO;
import com.att.dao.aaf.cass.NsSplit;
import com.att.dao.aaf.cass.PermDAO;
import com.att.dao.aaf.cass.NsDAO.Data;
import com.att.dao.aaf.hl.Question;

import aaf.v2_0.NsRequest;
import aaf.v2_0.Nss;
import aaf.v2_0.Perm;
import aaf.v2_0.PermRequest;
import aaf.v2_0.Perms;
import aaf.v2_0.Pkey;
import aaf.v2_0.Request;
import aaf.v2_0.RolePermRequest;

@RunWith(MockitoJUnitRunner.class)
public class Mapper_2_0Test {
	
	private static Mapper_2_0 maps;
	
	@Mock
	private static Request base;
	@Mock
	private static AuthzTrans trans;
	@Mock
	private static Question q;
	
	static NsRequest nsReq;
	static Mapper_2_0 maps_2;
	static RolePermRequest rpreq;
	
	@BeforeClass
	public static void setUp() {
		nsReq = new NsRequest();
		nsReq.setName("name");
		nsReq.setDescription("This is very  good description");
		nsReq.setScope(2);
		nsReq.setType("ROOT");
		q = mock(Question.class);
		maps = new Mapper_2_0(q);
		Result<NsSplit> nss = Utils.getnss();
		maps_2 = mock(Mapper_2_0.class);
		Perms perms = mock(Perms.class);
		when(maps_2.permFromRPRequest(Mockito.any(AuthzTrans.class), Mockito.any(Request.class))).thenReturn(Utils.getPpd());
		Result<List<PermDAO.Data>> re = mock(Result.class);
		when(q.deriveNsSplit(Mockito.any(), Mockito.anyString())).thenReturn(nss);
		when(maps_2.roleFromRPRequest(Mockito.any(AuthzTrans.class), Mockito.any(Request.class))).thenReturn(Utils.GetRrd());
		when(q.permFrom(Mockito.any(AuthzTrans.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(Utils.getPpd());
		when(maps_2.perms(Mockito.any(AuthzTrans.class), Mockito.any(Perms.class))).thenReturn(Utils.giveResult());
		when(maps_2.perm(Mockito.any(AuthzTrans.class), Mockito.any(PermRequest.class))).thenReturn(Utils.getPpd());
	}
	
	@Test
	public void ns() {
		Result<Namespace> asl = maps.ns(trans, nsReq);
		assertEquals(asl.status,0);
	}
	
	@Test
	public void nsCompany() {
		nsReq.setType("COMPANY");
		Result<Namespace> asl = maps.ns(trans, nsReq);
		assertEquals(asl.status,0);
	}
	
	@Test
	public void nsAPP() {
		nsReq.setType("APP");
		Result<Namespace> asl = maps.ns(trans, nsReq);
		assertEquals(asl.status,0);
	}
	
	@Test
	public void nss() {
		NsDAO.Data ndd = new Data();
		ndd.name = "testUser";
		ndd.type = 2;
		ndd.description = "This is desc";
		ndd.parent = "parent";
		ndd.attrib = new HashMap<>();
		Namespace from = new Namespace(ndd);
		Nss nss = new Nss();
		Result<Nss> nsp = maps.nss(trans, from,nss);
		assertEquals(nsp.status, 0);
	}
	
	@Test
	public void nss_Coll() {
		NsDAO.Data ndd = new Data();
		ndd.name = "testUser";
		ndd.type = 2;
		ndd.description = "This is desc";
		ndd.parent = "parent";
		ndd.attrib = new HashMap<>();
		Namespace from = new Namespace(ndd, new ArrayList<>(), new ArrayList<>());
		Nss nss = new Nss();
		Collection<Namespace> collFrom = new ArrayList<>();
		collFrom.add(from);
		Result<Nss> nsp = maps.nss(trans, collFrom, nss);
		assertEquals(nsp.status, 0);
	}
	
	@Test
	public void getPerms() {
		Perm perm = new Perm();
		perm.setAction("del");
		perm.setInstance("app");
		perm.setType("ROOT");
		Perms perms = new Perms();
		perms.getPerm().add(perm);
		Result<List<PermDAO.Data>> permsRes = maps_2.perms(trans, perms);
		assertNotNull(permsRes);
	}
	
	@Test
	public void getPermkey() {
		Pkey from = new Pkey();
		assertNotNull(maps.permkey(trans, from));
	}
	
	@Test
	public void perFromRPRequest() {
		rpreq = new RolePermRequest();
		Pkey pkey = new Pkey();
		rpreq.setPerm(new Pkey());
		rpreq.setRole("Test");
		assertNotNull(maps_2.permFromRPRequest(trans, rpreq));
		
	}
	
	@Test
	public void roleFromRPRequest() {
		rpreq = new RolePermRequest();
		Pkey pkey = new Pkey();
		rpreq.setPerm(new Pkey());
		rpreq.setRole("Test");
		assertNotNull(maps_2.roleFromRPRequest(trans, rpreq));
		
	}
	
	@Test
	public void getPerm() {
		PermRequest preq = new PermRequest();
		preq.setAction("String");
		preq.setDescription("Test description");
		preq.setInstance("instance");
		assertNotNull(maps_2.perm(trans, preq));
	}
	
}
