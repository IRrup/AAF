/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.cmd.user;

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
public class ListApprovalsTest {
	
	private static ListApprovals lsApprovals;
	
	@BeforeClass
	public static void setUp () throws NoSuchFieldException, SecurityException, Exception, IllegalAccessException {
		AAFcli cli = AAFcliTest.getAAfCli();
		User usr = new User(cli);
		List parent = new List(usr);
		lsApprovals = new ListApprovals(parent);
		
	}
	
	@Test
	public void exec() {
		try {
			assertEquals(lsApprovals._exec(0, "add","del","reset","extend","clear", "rename", "create"),500);
		} catch (CadiException e) {
			
			e.printStackTrace();
		} catch (APIException e) {
			
			e.printStackTrace();
		} catch (LocatorException e) {
			
			e.printStackTrace();
		}
	}
}
