/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.authz.cm.api;

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

import com.att.authz.cm.service.CertManAPI;
import com.att.authz.env.AuthzTrans;
;

@RunWith(MockitoJUnitRunner.class)
public class API_CertTest {
	
	@Mock
	private static API_Cert api;
	
	@Mock
	private static CertManAPI certManApi;
	
	private static CertManAPI noMockAPI;
	private static API_Cert api_1;
	
	private static HttpServletRequest req;
	private static HttpServletResponse res;
	
	@BeforeClass
	public static void setUp() {
		AuthzTrans trans = mock(AuthzTrans.class);
		req = mock(HttpServletRequest.class);
		trans.setProperty("testTag", "UserValue");
		trans.set(req);
	}
	
	@Rule
    public ExpectedException thrown= ExpectedException.none();
	
	@Test
	public void init_bothValued() {
		try {
			api.init(certManApi);
		} catch (Exception e) {
			thrown.expect(NullPointerException.class);
			e.printStackTrace();
		}
	}
	
	@Test
	public void init_Null_() {
		try {
			api.init(null);
		} catch (Exception e) {
			//thrown.expect(Exception.class);
			e.printStackTrace();
		}
	}
	
	@Test
	public void init_NMC_Null() {
		try {
			api_1.init(null);
		} catch (Exception e) {
			//thrown.expect(NullPointerException.class);
			e.printStackTrace();
		}
	}
	
	@Test
	public void init_NMC() {
		try {
			api_1.init(noMockAPI);
		} catch (Exception e) {
			//thrown.expect(NullPointerException.class);
			e.printStackTrace();
		}
	}
}
