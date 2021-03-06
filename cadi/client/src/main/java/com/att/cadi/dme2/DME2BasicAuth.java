/*******************************************************************************
 * Copyright (c) 2016 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.cadi.dme2;

import java.io.IOException;

import com.att.aft.dme2.api.DME2Client;
import com.att.cadi.Access;
import com.att.cadi.client.AbsBasicAuth;
import com.att.cadi.config.Config;
import com.att.cadi.config.SecurityInfo;
import com.att.cadi.principal.BasicPrincipal;

public class DME2BasicAuth extends AbsBasicAuth<DME2Client> {
	public DME2BasicAuth(String user, String pass, SecurityInfo<DME2Client> si) throws IOException {
		super(user,pass,si);
	}

	public DME2BasicAuth(Access access, SecurityInfo<DME2Client> si) throws IOException {
		super(access.getProperty(Config.AAF_MECHID, null),
				access.decrypt(access.getProperty(Config.AAF_MECHPASS, null), false),
				si);
	}

	public DME2BasicAuth(BasicPrincipal bp,SecurityInfo<DME2Client> si) throws IOException {
		super(bp.getName(),new String(bp.getCred()),null);
	}

	public void setSecurity(DME2Client client) {
		client.addHeader("Authorization", headValue);
	}
}
