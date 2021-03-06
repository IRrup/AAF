/*******************************************************************************
 * Copyright (c) 2016 AT&T Intellectual Property. All rights reserved.
 *******************************************************************************/
package com.att.cssa.rserv;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.att.inno.env.Env;
import com.att.inno.env.TimeTaken;
import com.att.inno.env.Trans;

public class Route<TRANS extends Trans> {
	public final String auditText;
	public final HttpMethods meth;
	public final String path;
	
	private Match match;
	// package on purpose
	private final TypedCode<TRANS> content;
	private final boolean isGet;
	
	public Route(HttpMethods meth, String path) {
		this.path = path;
		auditText = meth.name() + ' ' + path;
		this.meth = meth; // Note: Using Spark def for now.
		isGet = meth.compareTo(HttpMethods.GET) == 0;
		match = new Match(path);
		content = new TypedCode<TRANS>();
	}
	
	public void add(HttpCode<TRANS,?> code, String ... others) {
		code.match = match;
		content.add(code, others);
	}
	
//	public void add(HttpCode<TRANS,?> code, Class<?> cls, String version, String ... others) {
//		code.match = match;
//		content.add(code, cls, version, others);
//	}
//
	public HttpCode<TRANS,?> getCode(TRANS trans, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		// Type is associated with Accept for GET (since it is what is being returned
		// We associate the rest with ContentType.
		// FYI, thought about this a long time before implementing this way.
		String compare;
//		String special[]; // todo, expose Charset (in special) to outside
		if(isGet) {
			compare = req.getHeader("Accept"); // Accept is used for read, as we want to agree on what caller is ready to handle
		} else {
			compare = req.getContentType(); // Content type used to declare what data is being created, updated or deleted (might be used for key)
		}

		Pair<String, Pair<HttpCode<TRANS, ?>, List<Pair<String, Object>>>> hl = content.prep(trans, compare);
		if(hl==null) {
			resp.setStatus(406); // NOT_ACCEPTABLE
		} else {
			if(isGet) { // Set Content Type to expected content
				if("*".equals(hl.x) || "*/*".equals(hl.x)) {// if wild-card, then choose first kind of type
					resp.setContentType(content.first());
				} else {
					resp.setContentType(hl.x);
				}
			}
			return hl.y.x;
		}
		return null;
	}
	
	public Route<TRANS> matches(String method, String path) {
		return meth.name().equalsIgnoreCase(method) && match.match(path)?this:null;
	}
	
	public TimeTaken start(Trans trans, String auditText, HttpCode<TRANS,?> code, String type) {
		StringBuilder sb = new StringBuilder(auditText);
		sb.append(", ");
		sb.append(code.desc());
		sb.append(", Content: ");
		sb.append(type);
		return trans.start(sb.toString(), Env.SUB);
	}

	// Package on purpose.. for "find/Create" routes only
	boolean resolvesTo(HttpMethods hm, String p) {
		return(path.equals(p) && hm.equals(meth));
	}
	
	public String toString() {
		return auditText + ' ' + content; 
	}

	public String report(HttpCode<TRANS, ?> code) {
		StringBuilder sb = new StringBuilder();
		sb.append(auditText);
		sb.append(' ');
		content.relatedTo(code, sb);
		return sb.toString();
	}

	public RouteReport api() {
		RouteReport tr = new RouteReport();
		tr.meth = meth;
		tr.path = path;
		content.api(tr);
		return tr;
	}


	/**
	 * contentRelatedTo (For reporting) list routes that will end up at a specific Code
	 * @return
	 */
	public String contentRelatedTo(HttpCode<TRANS, ?> code) {
		StringBuilder sb = new StringBuilder(path);
		sb.append(' ');
		content.relatedTo(code, sb);
		return sb.toString();
	}
}
