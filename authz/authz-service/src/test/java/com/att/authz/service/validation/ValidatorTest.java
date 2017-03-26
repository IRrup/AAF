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
import com.att.dao.aaf.cass.PermDAO;
import com.att.dao.aaf.cass.RoleDAO;
import com.att.dao.aaf.cass.RoleDAO.Data;

@RunWith(MockitoJUnitRunner.class)
public class ValidatorTest {
	
	private static Validator valid;
	
	@Mock
	private static Result<PermDAO.Data> rpd;
	
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
		
	}
}