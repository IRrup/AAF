/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.cmd.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.cadi.CadiException;
import com.att.cadi.LocatorException;
import com.att.inno.env.APIException;

@RunWith(MockitoJUnitRunner.class)
public class CredTest {

	private static Cred testCred;
	private static User testUser;


	@BeforeClass
	public static void setUp() {
		testCred = mock(Cred.class);
		testUser = mock(User.class);
		try {
			when(testCred._exec(4, "String1","String2","String3","String4")).thenReturn(10);
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

	@Test
	public void exec() throws CadiException, APIException, LocatorException {
		assertEquals(testCred._exec(4, "String1","String2","String3","String4"), 10);
	}


	@Test
	public void exec_add() {		
		try {
			assertNotNull(testCred._exec(0, "zeroed","add","del","reset","extend"));
		} catch (CadiException | APIException | LocatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void exec_del() {		
		try {
			assertNotNull(testCred._exec(1, "zeroed","add","del","reset","extend"));
		} catch (CadiException | APIException | LocatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void exec_reset() {		
		try {
			assertNotNull(testCred._exec(2, "zeroed","add","del","reset","extend"));
		} catch (CadiException | APIException | LocatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void exec_extend() {		
		try {
			assertNotNull(testCred._exec(3, "zeroed","add","del","reset","extend"));
		} catch (CadiException | APIException | LocatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
