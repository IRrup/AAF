/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.authz.facade;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthzFacadeImplTest {
	
	@Mock
	private static AuthzFacadeImpl impl;
	
	@Before
	public static void setUp() {
		impl = mock(AuthzFacadeImpl.class, CALLS_REAL_METHODS);
		
	}
}
