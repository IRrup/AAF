/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.authz.cm.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.authz.cm.ca.CA;
import com.att.authz.cm.cert.BCFactory;
import com.att.authz.cm.cert.CSRMeta;
import com.att.authz.cm.cert.StandardFields;
import com.att.cadi.cm.CertException;
import com.att.inno.env.Trans;

@RunWith(MockitoJUnitRunner.class)
public class CertReqTest {
	
	private static BCFactory bcFact;
	
	private static CSRMeta value;
	
	private static CertReq req;
	
	@BeforeClass
	public static void setUp() {
		bcFact = mock(BCFactory.class);
		value = mock(CSRMeta.class);
		req = mock(CertReq.class);
		
	}
	
	@Test
	public void getCSRMeta() throws CertException {
		//req = new CertReq();
		req.mechid = "1213";
		List<String> fqdnsas = new ArrayList<String>();
		fqdnsas.add("String1");
		List<String> emails = new ArrayList<String>();
		emails.add("pupleti@hotmail.com");
		req.emails = emails;
		req.fqdns = fqdnsas;
		StandardFields sf = mock(StandardFields.class);
		req.certAuthority = new CA("testName", sf, "ALL") {
			
			@Override
			public X509Certificate sign(Trans trans, CSRMeta csrmeta) throws IOException, CertException {
	
				return null;
			}
		};
		req.sponsor = "asa@df.co";
		assertNull(req.getCSRMeta());
	}
}
