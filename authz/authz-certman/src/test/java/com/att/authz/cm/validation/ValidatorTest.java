package com.att.authz.cm.validation;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.att.dao.aaf.cass.ArtiDAO;

@RunWith(MockitoJUnitRunner.class)
public class ValidatorTest {
	
	private static Validator validator;
	
	@BeforeClass
	public static void setUp() {
		validator = new Validator();
	}
	
	@Test
	public void nullCheck() {
		assertNotNull(validator.nullOrBlank("TestName", null).errs());
	}
	
	@Test
	public void blankCheck() {
		assertNotNull(validator.nullOrBlank("TestName", "").err());
	}
	
	@Test
	public void notOK_null() {
		assertNotNull(validator.notOK(null));
	}
	
	@Test
	public void isNullCheck() {
		assertNotNull(validator.isNull("TestName", null).errs());
	}
	
	@Test
	public void nullBlankMin() {
		assertNotNull(validator.nullBlankMin("TestName", null, 0));
	}
	
	@Test
	public void artistsRequired() {
		assertNotNull(validator.artisRequired(null, 0));
	}
	
	@Test
	public void artistRequired() {
		assertNotNull(validator.artisRequired(new ArrayList<ArtiDAO.Data>(), -1));
	}
	
	@Test
	public void artistRequired_Null() {
		assertNotNull(validator.artisRequired(null, -1));
	}
	
	@Test
	public void artistkeys() {
		assertNotNull(validator.artisKeys(new ArrayList<ArtiDAO.Data>(), -1));
	}
	
	@Test
	public void artistKeys_Null() {
		assertNotNull(validator.artisKeys(null, -1));
	}
	
	@Test
	public void keys() {
		assertNotNull(validator.keys(new ArtiDAO.Data()));
	}
}
