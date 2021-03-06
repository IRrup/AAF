/*******************************************************************************
 * Copyright (c) 2016 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.cadi.client;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import com.att.aft.dme2.api.DME2Manager;
import com.att.cadi.Access;
import com.att.cadi.config.Config;
import com.att.cadi.config.SecurityInfo;
import com.att.cadi.dme2.DME2Locator;
import com.att.cadi.http.HBasicAuthSS;
import com.att.cadi.http.HMangr;
import com.att.inno.env.APIException;
import com.att.rosetta.env.RosettaDF;
import com.att.rosetta.env.RosettaEnv;

public class AAFClient {
	private RosettaEnv env;
	private Map<Class<?>,RosettaDF<?>> map = new HashMap<Class<?>,RosettaDF<?>>();
	HMangr hman;
	HBasicAuthSS ss;

	public AAFClient(RosettaEnv env) throws Exception {
		this.env = env;
		Access access = new EnvAccess(env);
		String user = access.getProperty(Config.AAF_MECHID,null);
		String password = access.decrypt(access.getProperty(Config.AAF_MECHPASS,null), true);
		
		SecurityInfo<HttpURLConnection> si = new SecurityInfo<HttpURLConnection>(access);
		DME2Manager dm = new DME2Manager("APIclient DME2Manager", System.getProperties());
		DME2Locator loc = new DME2Locator(access, dm, access.getProperty(Config.AAF_URL,null));

		int TIMEOUT = Integer.parseInt(access.getProperty(Config.AAF_CONN_TIMEOUT, "30000"));

		hman = new HMangr(access, loc).readTimeout(TIMEOUT).apiVersion("2.0");
		ss = new HBasicAuthSS(user, password, si);
	}

	public AAFClient(RosettaEnv env, DME2Manager dm) throws Exception {
		this.env = env;
		Access access = new EnvAccess(env);
		String user = access.getProperty(Config.AAF_MECHID,null);
		String password = access.decrypt(access.getProperty(Config.AAF_MECHPASS,null), true);
		
		SecurityInfo<HttpURLConnection> si = new SecurityInfo<HttpURLConnection>(access);
		DME2Locator loc = new DME2Locator(access, dm, access.getProperty(Config.AAF_URL,null));

		int TIMEOUT = Integer.parseInt(access.getProperty(Config.AAF_CONN_TIMEOUT, "30000"));

		hman = new HMangr(access, loc).readTimeout(TIMEOUT).apiVersion("2.0");
		ss = new HBasicAuthSS(user, password, si);
	}
	
	@SuppressWarnings("unchecked")
	private synchronized<T> RosettaDF<T> getDF(Class<T> cls) throws APIException {
		RosettaDF<?> rdf;
		synchronized (env) {
			rdf = map.get(cls);
			if(rdf==null) {
				rdf = env.newDataFactory(cls);
				map.put(cls, rdf);
			}
		}
		return (RosettaDF<T>)rdf;
	}

	// Package on purpose
	static class Call<T> {
		protected final static String VOID_CONTENT_TYPE="application/Void+json;version=2.0";
		
		protected RosettaDF<T> df;
		protected AAFClient client;

		public Call(AAFClient ac, RosettaDF<T> df) {
			this.client = ac;
			this.df = df;
		}
	}
	

	///////////  Calls /////////////////
	/**
	 * Returns a Get Object... same as "get"
	 * 
	 * @param cls
	 * @return
	 * @throws APIException
	 */
	public<T> Get<T> read(Class<T> cls) throws APIException {
		return new Get<T>(this,getDF(cls));
	}

	/**
	 * Returns a Get Object... same as "read"
	 * 
	 * @param cls
	 * @return
	 * @throws APIException
	 */
	public<T> Get<T> get(Class<T> cls) throws APIException {
		return new Get<T>(this,getDF(cls));
	}

	/**
	 * Returns a Post Object... same as "create"
	 * 
	 * @param cls
	 * @return
	 * @throws APIException
	 */
	public<T> Post<T> post(Class<T> cls) throws APIException {
		return new Post<T>(this,getDF(cls));
	}

	/**
	 * Returns a Post Object... same as "post"
	 * 
	 * @param cls
	 * @return
	 * @throws APIException
	 */
	public<T> Post<T> create(Class<T> cls) throws APIException {
		return new Post<T>(this,getDF(cls));
	}

	/**
	 * Returns a Put Object... same as "update"
	 * 
	 * @param cls
	 * @return
	 * @throws APIException
	 */
	public<T> Put<T> put(Class<T> cls) throws APIException {
		return new Put<T>(this,getDF(cls));
	}

	/**
	 * Returns a Put Object... same as "put"
	 * 
	 * @param cls
	 * @return
	 * @throws APIException
	 */
	public<T> Put<T> update(Class<T> cls) throws APIException {
		return new Put<T>(this,getDF(cls));
	}

	/**
	 * Returns a Delete Object
	 * 
	 * @param cls
	 * @return
	 * @throws APIException
	 */
	public<T> Delete<T> delete(Class<T> cls) throws APIException {
		return new Delete<T>(this,getDF(cls));
	}

	/**
	 * Returns a Delete Object
	 * 
	 * @param cls
	 * @return
	 * @throws APIException
	 */
	public Delete<Void> delete() throws APIException {
		return new Delete<Void>(this,null);
	}

	public Put<Void> put() {
		return new Put<Void>(this,null);	
	}
	

}
