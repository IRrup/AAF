/*******************************************************************************
 * Copyright (c) 2016 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.inno.env.jaxb;

import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import com.att.inno.env.APIException;
import com.att.inno.env.Env;
import com.att.inno.env.TimeTaken;
import com.att.inno.env.old.IOStringifier;

public class JAXBStringifier<T> implements IOStringifier<T> {
	private JAXBmar jmar;

	public JAXBStringifier(Class<?>... classes) throws APIException {
		try {
			jmar = new JAXBmar(classes);
		} catch (JAXBException e) {
			throw new APIException(e);
		}
	}

	public JAXBStringifier(QName qname, Class<?>... classes)
			throws APIException {
		try {
			jmar = new JAXBmar(qname, classes);
		} catch (JAXBException e) {
			throw new APIException(e);
		}
	}
	
	// package on purpose
	JAXBStringifier(JAXBmar jmar) {
		this.jmar = jmar;
	}

	// // @Override
	public void stringify(Env env, T input, Writer writer, boolean ... options)
			throws APIException {
		TimeTaken tt = env.start("JAXB Marshal", Env.XML);
		try {
			jmar.marshal(env.debug(), input, writer, options);
		} catch (JAXBException e) {
			throw new APIException(e);
		} finally {
			tt.done();
		}
	}

	// @Override
	public void stringify(Env env, T input, OutputStream os, boolean ... options)
			throws APIException {
		// TODO create an OutputStream that Counts?
		TimeTaken tt = env.start("JAXB Marshal", Env.XML);
		try {
			jmar.marshal(env.debug(), input, os, options);
		} catch (JAXBException e) {
			throw new APIException(e);
		} finally {
			tt.done();
		}
	}

	// @Override
	public String stringify(Env env, T input, boolean ... options) throws APIException {
		TimeTaken tt = env.start("JAXB Marshal", Env.XML);
		StringWriter sw = new StringWriter();
		try {
			jmar.marshal(env.debug(), input, sw, options);
			String rv = sw.toString();
			tt.size(rv.length());
			return rv;
		} catch (JAXBException e) {
			tt.size(0);
			throw new APIException(e);
		} finally {
			tt.done();
		}
	}

	// // @Override
	public void servicePrestart(Env env) throws APIException {
	}

	// // @Override
	public void threadPrestart(Env env) throws APIException {
	}

	// // @Override
	public void refresh(Env env) throws APIException {
	}

	// // @Override
	public void threadDestroy(Env env) throws APIException {
	}

	// // @Override
	public void serviceDestroy(Env env) throws APIException {
	}

	// @Override
	public JAXBStringifier<T> pretty(boolean pretty) {
		jmar.pretty(pretty);
		return this;
	}

	// @Override
	public JAXBStringifier<T> asFragment(boolean fragment) {
		jmar.asFragment(fragment);
		return this;
	}

}
