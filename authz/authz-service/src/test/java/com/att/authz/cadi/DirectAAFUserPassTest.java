/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.authz.cadi;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.Principal;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.authz.env.AuthzEnv;
import com.att.authz.env.AuthzTrans;
import com.att.authz.layer.Result;
import com.att.authz.layer.Utils;
import com.att.cadi.Permission;
import com.att.dao.DAOException;
import com.att.dao.aaf.hl.Question;

@RunWith(MockitoJUnitRunner.class)
public class DirectAAFUserPassTest {
	
	@Mock
	private static AuthzEnv env;
	@Mock
	private static Question ques;
	private static AuthzTrans trans;
	private static Principal bait;
	private static Permission pond;
	private static Result<Date> pdr;
	@InjectMocks 
	private static DirectAAFUserPass dfluserPass;
	
	@BeforeClass
	public static void setUp() throws Exception {
		env = mock(AuthzEnv.class);
		ques = mock(Question.class);
		trans = mock(AuthzTrans.class);
		bait = mock(Principal.class);
		pond = mock(Permission.class);
		pdr =  Utils.giveResult_userPass();
				
		dfluserPass = mock(DirectAAFUserPass.class);
		
	}
	
	@Test
	public void fish () throws DAOException {
		
		when(env.newTransNoAvg()).thenReturn(trans);
		when(ques.doesUserCredMatch(trans, "TestUser", null)).thenReturn(pdr);		
		assertFalse(dfluserPass.validate("testUser", null, null));
	}
	
	@Test
	public void fish_positive() throws DAOException {
		when(env.newTransNoAvg()).thenReturn(trans);
		when(ques.doesUserCredMatch(trans, "TestUser", null)).thenReturn(pdr);
		byte [] pass = {7,6,5,4};
		assertFalse(dfluserPass.validate("testUser", null, pass));
	}
	
	
	static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);
        // remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        
    }
}
