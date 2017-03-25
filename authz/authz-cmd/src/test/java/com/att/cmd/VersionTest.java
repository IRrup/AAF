/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.cmd;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.aft.dme2.internal.jetty.http.HttpStatus;
import com.att.cadi.CadiException;
import com.att.cadi.LocatorException;
import com.att.inno.env.APIException;

@RunWith(MockitoJUnitRunner.class)
public class VersionTest {
	
	private static AAFcli cli;
	private static Version version;
	
	@BeforeClass
	public static void setUp() throws APIException, LocatorException, GeneralSecurityException, IOException {
		cli = AAFcliTest.getAAfCli();
		version = new Version(cli);
	}
	
	@Test
	public void exec_HTTP_200() throws CadiException, APIException, LocatorException {
		assertEquals(version._exec(0, "Version"), HttpStatus.OK_200);
	}
}
