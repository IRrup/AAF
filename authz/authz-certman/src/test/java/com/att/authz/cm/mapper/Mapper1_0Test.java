/*******************************************************************************
 * Copyright (c) 2016 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.authz.cm.mapper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.authz.cm.mapper.Mapper.API;
import com.att.authz.layer.Result;
import com.att.dao.aaf.cass.ArtiDAO.Data;

@RunWith(MockitoJUnitRunner.class)
public class Mapper1_0Test {
	
	private static Mapper1_0 maps;
	
	@Before
	public void setUp() {
		maps = new Mapper1_0();
	}
	
	@Test
	public void getClassCERTREQ() {
		API api = API.CERT_REQ;
		assertNotNull(maps.getClass(api));
	}
	
	@Test
	public void getClassCERT_RENEW() {
		API api = API.CERT_RENEW;
		assertNotNull(maps.getClass(api));
	}
	
	@Test
	public void getClassCERT_DROP() {
		API api = API.CERT_DROP;
		assertNotNull(maps.getClass(api));
	}
	
	@Test
	public void getClassCERT() {
		API api = API.CERT;
		assertNotNull(maps.getClass(api));
	}
	
	@Test
	public void getClassARTIFACTS() {
		API api = API.ARTIFACTS;
		assertNotNull(maps.getClass(api));
	}
	
	@Test
	public void getClassERROR() {
		API api = API.ERROR;
		assertNotNull(maps.getClass(api));
	}
	
	@Test
	public void getClassVOID() {
		API api = API.VOID;
		assertNotNull(maps.getClass(api));
	}
	
	@Test
	public void getnewInstanceCERTREQ() {
		API api = API.CERT_REQ;
		assertNotNull(maps.newInstance(api));
	}
	
	@Test
	public void getnewInstanceCERT_RENEW() {
		API api = API.CERT_RENEW;
		assertNotNull(maps.newInstance(api));
	}
	
	@Test
	public void getnewInstanceCERT_DROP() {
		API api = API.CERT_DROP;
		assertNotNull(maps.newInstance(api));
	}
	
	@Test
	public void getnewInstanceCERT() {
		API api = API.CERT;
		assertNotNull(maps.newInstance(api));
	}
	
	@Test
	public void getnewInstanceARTIFACTS() {
		API api = API.ARTIFACTS;
		assertNotNull(maps.newInstance(api));
	}
	
	@Test
	public void getnewInstanceERROR() {
		API api = API.ERROR;
		assertNotNull(maps.newInstance(api));
	}
	
	@Test
	public void getnewInstanceVOID() {
		API api = API.VOID;
		assertNull(maps.newInstance(api));
	}
	
	@Test
	public void errorFromMessage() {
		StringBuilder srtb = new StringBuilder();
		String msgID = "123";
		String text = "error for testing";
		aaf.v2_0.Error err = maps.errorFromMessage(srtb, msgID, text, "str1","str3","str2");
		assertEquals(err.getText(), "error for testing");
	}
	
	@Test
	public void fromArtifacts() {
		//Result<List<Data>> lArtiDAO = 
	}
}
