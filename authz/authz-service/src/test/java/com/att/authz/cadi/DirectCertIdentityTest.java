/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.authz.cadi;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Set;

import javax.security.auth.x500.X500Principal;
import javax.servlet.http.HttpServletRequest;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.aft.dme2.api.http.HttpResponse;
import com.att.aft.dme2.request.HttpRequest;
import com.att.authz.layer.Utils;
import com.att.dao.aaf.cached.CachedCertDAO;
import com.att.dao.aaf.cass.CertDAO;


@RunWith(MockitoJUnitRunner.class)
public class DirectCertIdentityTest {
	
	@Mock
	private static CachedCertDAO certDAO;
	
	@Mock
	private static HttpServletRequest req;
	
	static X509Certificate cert;
	static byte [] name = {1,23,4,54,6,56};
	
	@InjectMocks
	private static DirectCertIdentity directCertIdty;
	
	@BeforeClass
	public static void setUp() throws CertificateException {
		String str = "core java api";
        byte[] b = str.getBytes();
		Principal prc = new X500Principal("CN=Duke, OU=JavaSoft, O=Sun Microsystems, C=US");
		req = mock(HttpServletRequest.class);
		X509Certificate cert = new X509Certificate() {
			
			@Override
			public boolean hasUnsupportedCriticalExtension() {
				return false;
			}
			
			@Override
			public Set<String> getNonCriticalExtensionOIDs() {
				 
				return null;
			}
			
			@Override
			public byte[] getExtensionValue(String oid) {
				 
				return null;
			}
			
			@Override
			public Set<String> getCriticalExtensionOIDs() {
				 
				return null;
			}
			
			@Override
			public void verify(PublicKey key, String sigProvider) throws CertificateException, NoSuchAlgorithmException,
					InvalidKeyException, NoSuchProviderException, SignatureException {
				 
				
			}
			
			@Override
			public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException,
					NoSuchProviderException, SignatureException {
				 
				
			}
			
			@Override
			public String toString() {
				 
				return null;
			}
			
			@Override
			public PublicKey getPublicKey() {
				 
				return null;
			}
			
			@Override
			public byte[] getEncoded() throws CertificateEncodingException {
				 
				return null;
			}
			
			@Override
			public int getVersion() {
				 
				return 0;
			}
			
			@Override
			public byte[] getTBSCertificate() throws CertificateEncodingException {
				 
				return null;
			}
			
			@Override
			public boolean[] getSubjectUniqueID() {
				 
				return null;
			}
			
			@Override
			public Principal getSubjectDN() {
				 
				return null;
			}
			
			@Override
			public byte[] getSignature() {
				 
				return null;
			}
			
			@Override
			public byte[] getSigAlgParams() {
				 
				return null;
			}
			
			@Override
			public String getSigAlgOID() {
				 
				return null;
			}
			
			@Override
			public String getSigAlgName() {
				 
				return null;
			}
			
			@Override
			public BigInteger getSerialNumber() {
				 
				return null;
			}
			
			@Override
			public Date getNotBefore() {
				 
				return null;
			}
			
			@Override
			public Date getNotAfter() {
				 
				return null;
			}
			
			@Override
			public boolean[] getKeyUsage() {
				 
				return null;
			}
			
			@Override
			public boolean[] getIssuerUniqueID() {
				 
				return null;
			}
			
			@Override
			public Principal getIssuerDN() {
				 
				return null;
			}
			
			@Override
			public int getBasicConstraints() {
				 
				return 0;
			}
			
			@Override
			public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {
				 
				
			}
			
			@Override
			public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
				
			}
		};
		certDAO = mock(CachedCertDAO.class, CALLS_REAL_METHODS);
		directCertIdty = mock(DirectCertIdentity.class);
		
		when(directCertIdty.identity(req, cert, name)).thenReturn(prc);
	}
	
	@Test
	public void identity_True() throws CertificateException {
		assertTrue(Utils.ch(directCertIdty.identity(req, cert, name)==null));
	}
	
	
	@Test
	public void identityNull() throws CertificateException {
		assertNull(directCertIdty.identity(req, cert, name));
	}

}
