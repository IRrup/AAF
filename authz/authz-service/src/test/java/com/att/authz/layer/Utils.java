package com.att.authz.layer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.att.dao.aaf.cass.CredDAO;
import com.att.dao.aaf.cass.Namespace;
import com.att.dao.aaf.cass.PermDAO;
import com.att.dao.aaf.cass.RoleDAO;
import com.att.dao.aaf.cass.PermDAO.Data;

public class Utils {
	public static Result<List<PermDAO.Data>> giveResult() {
		List<PermDAO.Data> lpdd = new ArrayList<PermDAO.Data>();
		PermDAO.Data dt = new Data();
		lpdd.add(dt);
		Result<List<Data>> pdr = Result.ok(lpdd);
		return pdr;
	}
	

	public static Result<Date> giveResult_userPass() {
		Date expires = new Date();
		Result<Date> pdr = Result.ok(expires);
		return pdr;
	}

	public static boolean ch(boolean b) {
		List<CredDAO.Data> lcred = new ArrayList<>();
		PermDAO.Data dt = new Data();
		if (dt!=null)
			return true;
		return false;
	}
	
	public static Result<PermDAO.Data> getPpd() {
		return Result.ok(new Data());
	}
	
	public static Result<RoleDAO.Data> GetRrd() {
		return Result.ok(new RoleDAO.Data());
	}
	
	public static Result<Namespace> getNsd () {
		return Result.ok(new Namespace());
	}
}