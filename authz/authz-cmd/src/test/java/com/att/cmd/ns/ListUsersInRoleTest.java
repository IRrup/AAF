/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.cmd.ns;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.cadi.CadiException;
import com.att.cadi.LocatorException;
import com.att.cmd.AAFcli;
import com.att.cmd.AAFcliTest;
import com.att.inno.env.APIException;

@RunWith(MockitoJUnitRunner.class)
public class ListUsersInRoleTest {
	
	private static ListUsersInRole lsUserinRole;
	
	@BeforeClass
	public static void setUp () throws NoSuchFieldException, SecurityException, Exception, IllegalAccessException {
		AAFcli cli = AAFcliTest.getAAfCli();
		NS ns = new NS(cli);
		List ls = new List(ns);
		ListUsers lsU = new ListUsers(ls);
		lsUserinRole = new ListUsersInRole(lsU);
	}
	
	@Test
	public void exec() {
		try {
			assertEquals(lsUserinRole._exec(0, "add","del","reset","extend"),500);
		} catch (CadiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LocatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
