/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.authz.service.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.authz.env.AuthzTrans;
import com.att.authz.facade.AuthzFacade;
import com.att.authz.service.AuthAPI;
import com.att.authz.service.Code;

@RunWith(MockitoJUnitRunner.class)
public class API_ApprovalTest {
	
	@Mock
	private static API_Approval api;
	
	@Mock
	private static AuthAPI authApi;
	
	@Mock
	private static AuthzFacade fac;
	
	private static HttpServletRequest req;
	private static HttpServletResponse res;
	private static Code code;
	
	@BeforeClass
	public static void setUp() {
		AuthzTrans trans = mock(AuthzTrans.class);
		req = mock(HttpServletRequest.class);
		trans.setProperty("testTag", "UserValue");
		trans.set(req);
		code = new Code(fac, "Document API", false) {
			
			@Override
			public void handle(AuthzTrans trans, HttpServletRequest req, HttpServletResponse resp) throws Exception {
				
			}
		};
	}
	
	@Rule
    public ExpectedException thrown= ExpectedException.none();
	
	@Test
	public void init_bothValued() {
		try {
			api.init(authApi, fac);
		} catch (Exception e) {
			thrown.expect(NullPointerException.class);
			e.printStackTrace();
		}
	}
	
	@Test
	public void init_OneNull_valued() {
		try {
			api.init(null, fac);
		} catch (Exception e) {
			//thrown.expect(Exception.class);
			e.printStackTrace();
		}
	}
	
	@Test
	public void init_Onevalued_oneNull() {
		try {
			api.init(authApi, null);
		} catch (Exception e) {
			thrown.expect(Exception.class);
			e.printStackTrace();
		}
	}
	
	@Test
	public void init_BothNull() {
		try {
			api.init(null, null);
		} catch (Exception e) {
			//thrown.expect(Exception.class);
			e.printStackTrace();
		}
	}
}