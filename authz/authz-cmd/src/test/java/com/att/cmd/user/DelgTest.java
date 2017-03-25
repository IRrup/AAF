/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.cmd.user;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.cadi.CadiException;
import com.att.cadi.LocatorException;
import com.att.inno.env.APIException;

@RunWith(MockitoJUnitRunner.class)
public class DelgTest {
	
	private static User testUser;
	private static Delg delg;
	
	@BeforeClass
	public static void setUp() throws APIException {
		testUser = mock(User.class);
		delg = mock(Delg.class);
	}
	
	@Test
	public void exec_add() {
		try {
			assertEquals(delg._exec(0, "zero","add","upd","del"), 0);
		} catch (CadiException | APIException | LocatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void exec_upd() {
		try {
			assertEquals(delg._exec(1, "zero","add","upd","del"), 0);
		} catch (CadiException | APIException | LocatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void exec_del() {
		try {
			assertEquals(delg._exec(2, "zero","add","upd","del"), 0);
		} catch (CadiException | APIException | LocatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
