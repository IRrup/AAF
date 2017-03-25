/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.cmd;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.security.GeneralSecurityException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.authz.env.AuthzEnv;
import com.att.cadi.Locator;
import com.att.cadi.LocatorException;
import com.att.cadi.client.PropertyLocator;
import com.att.cadi.config.Config;
import com.att.cadi.config.SecurityInfo;
import com.att.cadi.http.HBasicAuthSS;
import com.att.cadi.http.HMangr;
import com.att.inno.env.APIException;

@RunWith(MockitoJUnitRunner.class)
public class AAFcliTest {
	
	private static AAFcli cli;
	private static int TIMEOUT = Integer.parseInt(Config.AAF_CONN_TIMEOUT_DEF);
	
	@BeforeClass
	public static void setUp() throws Exception, Exception {
		cli = getAAfCli();
	}
	
	@Test
	public void eval() throws Exception {
		assertTrue(cli.eval("#startswith"));
	}
	
	@Test
	public void eval_empty() throws Exception{
		assertTrue(cli.eval(""));
	}
	
	@Test
	public void eval_randomString() throws Exception {
		assertTrue(cli.eval("Some random string @#&*& to check complete 100 coverage"));
	}
	
	public static AAFcli getAAfCli() throws APIException, LocatorException, GeneralSecurityException, IOException {
		final AuthzEnv env = new AuthzEnv(System.getProperties());
		String aafUrl = "https://DME2RESOLVE";
		SecurityInfo<HttpURLConnection> si = new SecurityInfo<HttpURLConnection>(env);
		env.loadToSystemPropsStartsWith("AAF", "DME2");
		Locator loc;
		loc = new PropertyLocator(aafUrl);						
		TIMEOUT = Integer.parseInt(env.getProperty(Config.AAF_CONN_TIMEOUT, Config.AAF_CONN_TIMEOUT_DEF));
		HMangr hman = new HMangr(env, loc).readTimeout(TIMEOUT).apiVersion("2.0");
		
		//TODO: Consider requiring a default in properties
		env.setProperty(Config.AAF_DEFAULT_REALM, System.getProperty(Config.AAF_DEFAULT_REALM,Config.getDefaultRealm()));
		HBasicAuthSS ss = mock(HBasicAuthSS.class);
		return new AAFcli(env, new OutputStreamWriter(System.out), hman, si, ss);
	}
}
