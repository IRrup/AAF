/*******************************************************************************
 * Copyright (c) 2017 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.cmd.perm;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.ConnectException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.aft.dme2.internal.jetty.http.HttpStatus;
import com.att.cadi.CadiException;
import com.att.cadi.LocatorException;
import com.att.cadi.client.Future;
import com.att.cadi.client.Rcli;
import com.att.cadi.client.Retryable;
import com.att.cmd.AAFcli;
import com.att.cmd.AAFcliTest;
import com.att.cmd.role.Role;
import com.att.inno.env.APIException;
import com.att.rosetta.env.RosettaDF;

import aaf.v2_0.RoleRequest;

@RunWith(MockitoJUnitRunner.class)
public class ListActivityTest {
	
	private static ListActivity lsActivity;
	private static ListActivity _lsActivity;
	private static Future<RoleRequest> rreq;
	
	@Before
	public void setUp () throws NoSuchFieldException, SecurityException, Exception, IllegalAccessException {
		AAFcli cli = AAFcliTest.getAAfCli();
		Role role = new Role(cli);
		Perm perm = new Perm(role);
		List ls = new List(perm);
		lsActivity = new ListActivity(ls);
		_lsActivity = mock(ListActivity.class);
		rreq = mock(Future.class);
		when(rreq.code()).thenReturn(HttpStatus.PARTIAL_CONTENT_206);
		when(_lsActivity._exec(Mockito.anyInt(), Mockito.anyString(),Mockito.anyString())).thenCallRealMethod();
		Retryable<Integer> ret= new Retryable<Integer>() {
			
			@Override
			public Integer code(Rcli<?> client) throws CadiException, ConnectException, APIException {
				client = mock(Rcli.class);
				rreq = new Future<RoleRequest>() {
					
					@Override
					public String header(String tag) {
						return "GET /_assets/common.eb9d6e8a62faabacac30.js HTTP/1.1";
					}
					
					@Override
					public boolean get(int timeout) throws CadiException {
						return false;
					}
					
					@Override
					public int code() {
						return 206;
					}
					
					@Override
					public String body() {
						return "HTTP/1.1 206";
					}
				};
				when(client.create(Mockito.anyString(), Mockito.any(RosettaDF.class), Mockito.any(Integer.class))).thenReturn(rreq);
				return rreq.code();
			}
			public Future<RoleRequest> get() {
				return rreq;
			}
		};
		when(_lsActivity.same(Mockito.any(Retryable.class))).thenReturn(206);
	}
	
	@Test
	public void exec() {
		try {
			assertEquals(lsActivity._exec(0, "add","del","reset","extend"),500);
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
	
	@Test
	public void exec_206() {
		try {
			assertEquals(_lsActivity._exec(0, "add","del"),206);
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
