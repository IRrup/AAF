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

import com.att.cadi.CadiException;
import com.att.cadi.LocatorException;
import com.att.inno.env.APIException;

@RunWith(MockitoJUnitRunner.class)
public class BaseCmdTest {
	
	private static AAFcli cli;
	private static BaseCmd bCmd;
	
	@BeforeClass
	public static void setUp() throws APIException, LocatorException, GeneralSecurityException, IOException {
		cli = AAFcliTest.getAAfCli();
		bCmd = new BaseCmd<>(cli, "testString");
	}
	
	@Test
	public void exec() throws CadiException, APIException, LocatorException {
		assertEquals(bCmd._exec(0, "add","del","reset","extend"), 0);
	}
}
