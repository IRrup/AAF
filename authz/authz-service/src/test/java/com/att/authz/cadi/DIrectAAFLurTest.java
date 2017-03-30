/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.authz.cadi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.Principal;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.authz.env.AuthzEnv;
import com.att.authz.env.AuthzTrans;
import com.att.authz.layer.Result;
import com.att.authz.layer.Utils;
import com.att.cadi.Permission;
import com.att.dao.aaf.cass.PermDAO.Data;
import com.att.dao.aaf.hl.Question;

@RunWith(MockitoJUnitRunner.class)
public class DIrectAAFLurTest {
	
	private static AuthzEnv env;
	private static Question ques;
	private static AuthzTrans trans;
	private static Principal bait;
	private static Permission pond;
	private static Result<List<Data>> pdr;
	private static DirectAAFLur dflur;
	
	@BeforeClass
	public static void setUp() throws Exception {
		env = mock(AuthzEnv.class);
		ques = mock(Question.class);
		trans = mock(AuthzTrans.class);
		bait = mock(Principal.class);
		pond = mock(Permission.class);
		pdr =  Utils.giveResult();
				
		dflur = mock(DirectAAFLur.class);
		
	}
	
	@Test
	public void fish () {
		
		when(env.newTransNoAvg()).thenReturn(trans);
		when(ques.getPermsByUser(trans, "TestUser", false)).thenReturn(pdr);		
		assertFalse(dflur.fish(bait, pond));
	}
	
	@Test
	public void fish_positive() {
		when(env.newTransNoAvg()).thenReturn(trans);
		when(ques.getPermsByUser(trans, "TestUser", true)).thenReturn(pdr);
		assertFalse(dflur.fish(trans, bait, pond));
	}
	
	static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);
        // remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        
    }
}
