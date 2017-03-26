/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.authz.service.validation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.authz.layer.Result;
import com.att.authz.layer.Utils;
import com.att.authz.org.Organization;
import com.att.dao.aaf.cass.CredDAO;
import com.att.dao.aaf.cass.DelegateDAO;
import com.att.dao.aaf.cass.PermDAO;
import com.att.dao.aaf.cass.RoleDAO;
import com.att.dao.aaf.cass.RoleDAO.Data;

@RunWith(MockitoJUnitRunner.class)
public class ValidatorTest {
	
	private static Validator valid;
	
	@Mock
	private static Result<PermDAO.Data> rpd;
	
	@Mock
	private static Validator mvalid;
	
	@BeforeClass
	public static void setUp() {
		valid = new Validator();
		rpd = Utils.getPpd();
	}
	
	@Test
	public void getPerm() {
		assertNotNull(valid.perm(rpd));
	}
	
	@Test
	public void getPermDao() {
		assertNotNull(valid.perm(new PermDAO.Data()));
	}
	
	@Test
	public void getPermDao_null() {
		PermDAO.Data dt = null;
		assertNotNull(valid.perm(dt));
	}
	
	@Test
	public void getRoleDAONull() {
		RoleDAO.Data pd = null;
		assertNotNull(valid.role(pd));
	}
	
	@Test
	public void getRoleDAO() {
		RoleDAO.Data pd = new Data();
		assertNotNull(valid.role(pd));
	}
	
	@Test
	public void getRole() {
		assertNotNull(valid.role(Utils.GetRrd()));
	}
	
	@Test
	public void getDelegate() {
		Organization org = mock(Organization.class);
		DelegateDAO.Data dd = new DelegateDAO.Data();
		assertNotNull(valid.delegate(org, dd));
	}
	
	@Test
	public void getDelegateNull() {
		Organization org = mock(Organization.class);
		DelegateDAO.Data dd = null;
		assertNotNull(valid.delegate(org, dd));
	}
	
	@Test
	public void getCrednullfalse() {
		Organization org = mock(Organization.class);
		CredDAO.Data dd = null;
		assertNotNull(valid.cred(org, dd, false));
	}
	
	@Test
	public void getCrednull_true() {
		Organization org = mock(Organization.class);
		CredDAO.Data dd = null;
		assertNotNull(valid.cred(org, dd, true));
	}
	
	@Test
	public void getCred_false() {
		Organization org = mock(Organization.class);
		CredDAO.Data dd = new CredDAO.Data();
		assertNull(mvalid.cred(org, dd, false));
	}
	
	@Test
	public void getCred_true() {
		Organization org = mock(Organization.class);
		CredDAO.Data dd = new CredDAO.Data();
		assertNull(mvalid.cred(org, dd, true));
	}
	
	@Test
	public void getUser() {
		Organization org = mock(Organization.class);
		assertNotNull(valid.user(org, "testUser"));
	}
	
	@Test
	public void getUser_NullString() {
		Organization org = mock(Organization.class);
		assertNotNull(valid.user(org, null));
	}
	
	@Test
	public void getNS() {
		Organization org = mock(Organization.class);
		assertNull(mvalid.ns(Utils.getNsd()));
	}
	
	@Test
	public void getNSString() {
		assertNotNull(valid.ns("Test"));
	}
	
	@Test
	public void getPermType() {
		assertNotNull(valid.permType("w-", "testString"));
	}
	
	@Test
	public void getPermInstance() {
		assertNotNull(valid.permInstance("instance"));
	}
	
	@Test
	public void getPermAction() {
		assertNotNull(valid.permInstance("action"));
	}
	
	@Test
	public void getRole_v() {
		assertNotNull(valid.role("action"));
	}
}