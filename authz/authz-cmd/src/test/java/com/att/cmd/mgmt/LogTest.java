/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.cmd.mgmt;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.cadi.CadiException;
import com.att.cadi.LocatorException;
import com.att.inno.env.APIException;

@RunWith(MockitoJUnitRunner.class)
public class LogTest {
	
	private static Log log;
	
	@BeforeClass
	public static void setUp() {
		log = mock(Log.class);
	}
	
	@Test
	public void exec() {
		try {
			assertEquals(log._exec(0, "session clear"), 0);
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
