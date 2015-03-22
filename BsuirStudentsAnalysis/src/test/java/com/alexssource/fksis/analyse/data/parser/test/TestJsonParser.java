package com.alexssource.fksis.analyse.data.parser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.alexssource.fksis.analyse.data.parser.JsonParser;
import com.alexssource.fksis.analyse.data.parser.Parser;
import com.alexssource.fksis.analyse.data.parser.ParserException;

public class TestJsonParser {
	private Parser parser = new JsonParser();
	
	@Test
	public void testNotNull() throws ParserException {
		String json = "{ \"response\":[{ \"name\": \"myName\"} ]}";
		List<Map<String, Object>> result = parser.parse(json);
		assertNotNull(result);
		assertTrue("Result is empty", result.size() > 0);
	}
	
	@Test
	public void testSimpleObject() throws ParserException {
		String json = "{ 'response': [{ 'name': 'myName', 'age': 11 } ]}";
		Map<String, Object> expectedResult = new HashMap<String, Object>(1);
		expectedResult.put("name", "myName");
		expectedResult.put("age", 11);

		List<Map<String, Object>> result = parser.parse(json);
		assertNotNull(result);
		assertTrue("Result is empty", result.size() > 0);
		for(Map<String, Object> r : result) {
			assertThatMapsEquals(expectedResult, r);
		}
	}
	
	@Test
	public void testComplexObject() throws ParserException {
		String json = "{ 'response':[{ 'name': 'myName', 'age': 11, 'data': { 'name': 'bigdata', 'value': 'big-big' } }]}";
		Map<String, Object> expectedResult = new HashMap<String, Object>(1);
		expectedResult.put("age", 11);
		expectedResult.put("name", "myName");
		expectedResult.put("data.name", "bigdata");
		expectedResult.put("data.value", "big-big");

		List<Map<String, Object>> result = parser.parse(json);
		assertNotNull(result);
		assertTrue("Result is empty", result.size() > 0);
		for(Map<String, Object> r : result) {
			assertThatMapsEquals(expectedResult, r);
		}
	}
	
	@Test
	public void testAnotherComplexObject() throws ParserException {
		String json = "{'response' : [{ 'name': 'myName', 'age': 11, 'data': { 'name': 'bigdata', 'value': 'big-big' } }]}";
		Map<String, Object> expectedResult = new HashMap<String, Object>(1);
		expectedResult.put("age", 11);
		expectedResult.put("name", "myName");
		expectedResult.put("data.name", "bigdata");
		expectedResult.put("data.value", "big-big");

		List<Map<String, Object>> result = parser.parse(json);
		assertNotNull(result);
		assertTrue("Result is empty", result.size() > 0);
		for(Map<String, Object> r : result) {
			assertThatMapsEquals(expectedResult, r);
		}
	}
	
	@Test
	public void testObjectWithArray() throws ParserException {
		String json = "{'response':[{ 'name': 'myName', 'universities': [{'id':1, 'name':'BSUIR'},{'id':2, 'name':'BSU'}] }]}";
		Map<String, Object> expected = new HashMap<String, Object>(1);
		expected.put("name", "myName");
		expected.put("universities", "1,2");
		expected.put("faculties", "0,0");
		expected.put("chairs", "0,0");
		expected.put("graduation_years", "0,0");

		List<Map<String, Object>> result = parser.parse(json);
		assertNotNull(result);
		assertTrue("Result is empty", result.size() > 0);
		for(Map<String, Object> r : result) {
			assertThatMapsEquals(expected, r);
		}
	}
	
	@Test
	public void testObjectWithArrayWithoutIds() throws ParserException {
		String json = "{'response':[{ 'name': 'myName', 'universities': [{'id':1, 'name':'BSUIR'},{'name':'BSU'}] }]}";
		Map<String, Object> expected = new HashMap<String, Object>(1);
		expected.put("name", "myName");
		expected.put("universities", "1,0");
		expected.put("faculties", "0,0");
		expected.put("chairs", "0,0");
		expected.put("graduation_years", "0,0");
		
		List<Map<String, Object>> result = parser.parse(json);
		assertNotNull(result);
		assertTrue("Result is empty", result.size() > 0);
		for(Map<String, Object> r : result) {
			assertThatMapsEquals(expected, r);
		}
	}
	
	@Test(expected = ParserException.class)
	public void testExceptionForComplexIdsInArray() throws ParserException {
		String json = "{'response':[{ 'name': 'myName', 'universities': [{'id':1, 'name':'BSUIR'},{'id': { 'name': 'fail' }, 'name':'BSU'}] }]}";
		Map<String, Object> expected = new HashMap<String, Object>(1);
		expected.put("name", "myName");
		expected.put("universities", "1,0");

		List<Map<String, Object>> result = parser.parse(json);
		assertNotNull(result);
		assertTrue("Result is empty", result.size() > 0);
		for(Map<String, Object> r : result) {
			assertThatMapsEquals(expected, r);
		}
	}
	
	@Test
	public void testUniversityParser() throws ParserException {
		String json = "{'response':[{ 'name': 'myName', 'universities': [" +
				"{'id':1, 'name':'BSUIR', 'country':3,'city':282, 'faculty':9132,'chair':175701, 'graduation':2015}," +
				"{'id':5, 'name':'BSU', 'faculty': 12, 'chair': 11, 'graduation': 1992}" +
			"] }]}";
		
		Map<String, Object> expected = new HashMap<String, Object>(1);
		expected.put("name", "myName");
		expected.put("universities", "1,5");
		expected.put("faculties", "9132,12");
		expected.put("chairs", "175701,11");
		expected.put("graduation_years", "2015,1992");
		
		List<Map<String, Object>> result = parser.parse(json);
		assertNotNull(result);
		assertTrue("Result is empty", result.size() > 0);
		for(Map<String, Object> r : result) {
			assertThatMapsEquals(expected, r);
		}
	}
	
	
	@Test
	public void testComplexiestObject() throws ParserException {
		//
		String json = 
		"{\"response\":[{\"id\":153003036,\"first_name\":\"Виталий\",\"last_name\":\"Мазур\",\"sex\":2,\"domain\":\"id153003036\"," +
				"\"city\":{\"id\":1158,\"title\":\"Житомир\"},\"country\":{\"id\":2,\"title\":\"Украина\"}," +
				"\"photo_100\":\"http://Kx0V9cxZMAE.jpg\"," +
				"\"photo_max\":\"http://Kx0V9cxZMAE.jpg\",\"photo_200_orig\":\"http://X5yv4OlMFE.jpg\"," +
				"\"photo_400_orig\":\"http://cg9NV5kMzBU.jpg\",\"photo_max_orig\":\"http://cg9NV5kMzBU.jpg\"," +
				"\"has_mobile\":1,\"online\":0,\"can_post\":0,\"can_see_all_posts\":1,\"can_see_audio\":1," +
				"\"can_write_private_message\":1,\"mobile_phone\":\"\",\"home_phone\":\"\",\"skype\":\"null\",\"site\":\"\"," +
				"\"status\":\"\",\"last_seen\":{\"time\":1392019431,\"platform\":7},\"common_count\":0,\"university\":24903," +
				"\"university_name\":\"ЕУ (ЖФ)\",\"faculty\":59273,\"faculty_name\":\"Экономики и менеджмента\",\"graduation\":2012," +
				"\"education_form\":\"Заочное отделение\",\"education_status\":\"Выпускник (специалист)\",\"relation\":4," +
				"\"universities\":[{\"id\":24903,\"country\":2,\"city\":1158,\"name\":\"ЕУ (ЖФ)\",\"faculty\":59273," +
				"\"faculty_name\":\"Экономики и менеджмента\",\"chair\":243789,\"chair_name\":\"Финансов\",\"graduation\":2012," +
				"\"education_form\":\"Заочное отделение\",\"education_status\":\"Выпускник (специалист)\"}],\"schools\":[]," +
				"\"relatives\":[]}]}";
		
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("id", 153003036);
		expected.put("first_name","Виталий");
		expected.put("last_name","Мазур");
		expected.put("sex",2);
		expected.put("domain","id153003036");
		expected.put("city.id",1158);
		expected.put("city.title","Житомир");
		expected.put("country.id",2);
		expected.put("country.title","Украина");
		expected.put("photo_100","http://Kx0V9cxZMAE.jpg");
		expected.put("photo_max","http://Kx0V9cxZMAE.jpg");
		expected.put("photo_200_orig","http://X5yv4OlMFE.jpg");
		expected.put("photo_400_orig","http://cg9NV5kMzBU.jpg");
		expected.put("photo_max_orig","http://cg9NV5kMzBU.jpg");
		expected.put("has_mobile",1);
		expected.put("online",0);
		expected.put("can_post",0);
		expected.put("can_see_all_posts",1);
		expected.put("can_see_audio",1);
		expected.put("can_write_private_message",1);
		expected.put("mobile_phone","");
		expected.put("home_phone", "");
		expected.put("skype", "null");
		expected.put("site", "");
		expected.put("status", "");
		expected.put("last_seen.time", "1392019431");
		expected.put("last_seen.platform", "7");
		expected.put("common_count", "0");
		expected.put("university", "24903");
		expected.put("university_name", "ЕУ (ЖФ)");
		expected.put("faculty", "59273");
		expected.put("faculty_name", "Экономики и менеджмента");
		expected.put("graduation", "2012");
		expected.put("education_form", "Заочное отделение");
		expected.put("education_status", "Выпускник (специалист)");
		expected.put("relation", "4");
		expected.put("universities", "24903");
		expected.put("chairs", "243789");
		expected.put("faculties", "59273");
		expected.put("graduation_years", "2012");
		
		List<Map<String, Object>> result = parser.parse(json);
		assertNotNull(result);
		assertTrue("Result is empty", result.size() > 0);
		for(Map<String, Object> r : result) {
			assertTrue(isMapsEquals(expected, r));
			assertThatMapsEquals(expected, r);
		}
	}
	
	
	
	@Test
	public void testComplexiestObjects() throws ParserException {
		String json = "{\"response\":" +
				"[{\"id\":1,\"first_name\":\"Виталий\",\"last_name\":\"Мазур\",\"sex\":2," +
					"\"city\":{\"id\":1158,\"title\":\"Житомир\"}," +
					"\"universities\":[{\"id\":1,\"faculty\":2,\"chair\":3,\"graduation\":2012}]}," + 
		"{\"id\":2,\"first_name\":\"Вит\",\"last_name\":\"Маз\",\"sex\":2," +
		"\"city\":{\"id\":1158,\"title\":\"Житомир\"},\"universities\":[{\"id\":2,\"faculty\":3, \"chair\":4,\"graduation\":2012}]}," + 
		"{\"id\":3,\"first_name\":\"Виталий\",\"last_name\":\"Мазур\",\"sex\":2," +
		"\"city\":{\"id\":1158,\"title\":\"Житомир\"},\"universities\":[{\"id\":3,\"faculty\":4, \"chair\":5,\"graduation\":2012}]}" + 
		"]}";
		
		List<Map<String, Object>> expectedList = new ArrayList<Map<String,Object>>(3);
		Map<String, Object> expected;
		
		expected = new HashMap<String, Object>();
		expected.put("id", 1);
		expected.put("first_name","Виталий");
		expected.put("last_name","Мазур");
		expected.put("sex",2);
		expected.put("city.id",1158);
		expected.put("city.title","Житомир");
		expected.put("universities", "1");
		expected.put("chairs", "3");
		expected.put("faculties", "2");
		expected.put("graduation_years", "2012");
		expectedList.add(expected);
		
		expected = new HashMap<String, Object>();
		expected.put("id", 2);
		expected.put("first_name","Вит");
		expected.put("last_name","Маз");
		expected.put("sex",2);
		expected.put("city.id",1158);
		expected.put("city.title","Житомир");
		expected.put("universities", "2");
		expected.put("chairs", "4");
		expected.put("faculties", "3");
		expected.put("graduation_years", "2012");
		expectedList.add(expected);
		
		expected = new HashMap<String, Object>();
		expected.put("id", 3);
		expected.put("first_name","Виталий");
		expected.put("last_name","Мазур");
		expected.put("sex",2);
		expected.put("city.id",1158);
		expected.put("city.title","Житомир");
		expected.put("universities", "3");
		expected.put("chairs", "5");
		expected.put("faculties", "4");
		expected.put("graduation_years", "2012");
		expectedList.add(expected);
		
		
		List<Map<String, Object>> result = parser.parse(json);
		assertNotNull(result);
		assertTrue("Result is empty", result.size() > 0);
		
		assertThatArraysOfMapsIsEquals(expectedList, result);
	}
	
	
	private void assertThatMapsEquals(Map<String, Object> expected, Map<String, Object> actual) {
		assertEquals(expected.size(), actual.size());
		for(Entry<String, Object> es : expected.entrySet()) {
			assertTrue(actual.containsKey(es.getKey()));
			assertEquals(expected.get(es.getKey()).toString(), actual.get(es.getKey()).toString());
		}
	}
	
	private boolean isMapsEquals(Map<String, Object> expected, Map<String, Object> actual) {
		if(expected.size() != actual.size()) return false;
		for(Entry<String, Object> es : expected.entrySet()) {
			if(!actual.containsKey(es.getKey())) return false;
			if(!expected.get(es.getKey()).toString().equals(actual.get(es.getKey()).toString())) return false;
		}
		return true;
	}
	
	
	private void assertThatArraysOfMapsIsEquals(List<Map<String, Object>> expected, List<Map<String, Object>> actual) {
		boolean eqls;
		for(Map<String, Object> expc : expected) {
			eqls = false;
			for(Map<String, Object> act : actual) {
				System.out.println(String.format("Maps [%s and %s]", expc.get("id"), act.get("id")));
				if(isMapsEquals(expc, act)) {
					System.out.println("Maps is equals");
					eqls = true;
					break;
				}
			}
			if(!eqls) {
				System.out.println("Maps is not equals:");
				System.out.println("expc: " + expc.toString());
				//System.out.println("act : " + act.toString());
				throw new AssertionError("Unexpected result. Peoples aren't equals!");
			}
		}
	}
}
