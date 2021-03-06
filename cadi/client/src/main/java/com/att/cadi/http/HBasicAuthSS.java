/*******************************************************************************
 * Copyright (c) 2016 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.cadi.http;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

import com.att.cadi.Access;
import com.att.cadi.client.AbsBasicAuth;
import com.att.cadi.config.Config;
import com.att.cadi.config.SecurityInfo;
import com.att.cadi.principal.BasicPrincipal;

public class HBasicAuthSS extends AbsBasicAuth<HttpURLConnection> {
	public HBasicAuthSS(Access access, SecurityInfo<HttpURLConnection> si) throws IOException {
		super(access.getProperty(Config.AAF_MECHID, null),
				access.decrypt(access.getProperty(Config.AAF_MECHPASS, null), false),
				si);
	}


	public HBasicAuthSS(String user, String pass, SecurityInfo<HttpURLConnection> si) throws IOException {
		super(user,pass,si);
	}

	public HBasicAuthSS(String user, String pass, SecurityInfo<HttpURLConnection> si, boolean asDefault) throws IOException {
		super(user,pass,si);
		if(asDefault) {
			si.set(this);
		}
	}
	
	public HBasicAuthSS(BasicPrincipal bp, SecurityInfo<HttpURLConnection> si) throws IOException {
		super(bp.getName(),new String(bp.getCred()),si);
	}
	
	public HBasicAuthSS(BasicPrincipal bp, SecurityInfo<HttpURLConnection> si, boolean asDefault) throws IOException {
		super(bp.getName(),new String(bp.getCred()),si);
		if(asDefault) {
			si.set(this);
		}
	}

	@Override
	public void setSecurity(HttpURLConnection huc) {
		huc.setRequestProperty("Authorization" , headValue);
		if(securityInfo!=null && huc instanceof HttpsURLConnection) {
			securityInfo.setSocketFactoryOn((HttpsURLConnection)huc);
		}
	}
}
