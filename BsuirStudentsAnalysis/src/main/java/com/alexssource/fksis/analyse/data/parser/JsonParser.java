package com.alexssource.fksis.analyse.data.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonParser implements Parser {
	private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);
	private final com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
	
	private List<Map<String, Object>> result = new ArrayList<Map<String,Object>>(1000);
	private Map<String, Object> user;
	
	
	@Override
	public List<Map<String, Object>> parse(String text) throws ParserException {
		JsonElement tree = parser.parse(text);
		parseTree(tree, null);
		logger.info("Result: {}", result);
		return result;
	}

	private Object parseTree(JsonElement element, String name) throws ParserException {
		if(!element.isJsonNull()) {
			if(element.isJsonArray()) {
				logger.debug("json array: {}", element.toString());
				JsonArray jsonArray = element.getAsJsonArray();
				if(name.equals("universities")) { 
					processJsonUniveristyArray(jsonArray, name);
				} if(name.equals("response")) {
					for(JsonElement user : jsonArray) {
						this.user = new HashMap<String, Object>();
						parseTree(user, "response");
						result.add(this.user);
					}
				} else {
					processJsonArray(jsonArray, name);
				}
			}
			if(element.isJsonObject()) {
				logger.debug("json key: {}, object: {}", name, element.toString());
				JsonObject jsonObject = element.getAsJsonObject();
				processJsonObject(jsonObject, name);
				return null;
			}
			if(element.isJsonPrimitive()) {
				logger.debug("json primitive: {}", element.toString());
				JsonPrimitive jsonPrimitive = element.getAsJsonPrimitive();
				return processJsonPrimitive(jsonPrimitive);
			}
		}
		return null;
	}
	
	
	private void processJsonObject(JsonObject object, String name) throws ParserException {
		for(Entry<String, JsonElement> element : object.entrySet()) {
			logger.debug("Json entry from object. key: {}, value: {}", element.getKey(), element.getValue());
			String key = element.getKey();
			Object value = parseTree(element.getValue(), key);
			if(value != null && ( value instanceof String) ) {
				value = value.toString().replace("\"", "");
			}
			logger.debug("Parsed {} object: key: {}, value: {}", object, key, value);
			if(!key.equals("response") && value != null) {
				if(name != null && !name.equals("response")) {
					user.put(String.format("%s.%s", name, key), value);
				} else {
					user.put(key, value);
				}
			}
		}
	}
	
	
	private void processJsonArray(JsonArray array, String name) throws ParserException {
		StringBuffer ids = new StringBuffer();
		for(JsonElement element : array) {
			if(element.isJsonObject()) {
				JsonObject obj = element.getAsJsonObject();
				JsonElement idElement = obj.get("id");
				if(idElement != null && !idElement.isJsonPrimitive()) {
					throw new ParserException("ID is not the JsonPrimitive type");
				}
				Object id = (idElement == null) ? 0 : processJsonPrimitive(idElement.getAsJsonPrimitive());
				ids.append(id.toString()).append(',');
			}
		}
		
		if(ids.length() > 0) {
			String resultIDs = ids.deleteCharAt(ids.length()-1).toString();
			user.put(name, resultIDs);
		}
	}
	
	
	/**
	 * One of the my best ugly code :)
	 * @param array
	 * @param name
	 * @throws ParserException
	 */
	private void processJsonUniveristyArray(JsonArray array, String name) throws ParserException {
		StringBuffer ids = new StringBuffer();
		StringBuffer faculties = new StringBuffer();
		StringBuffer chairs = new StringBuffer();
		StringBuffer graduationYears = new StringBuffer();
		
		for(JsonElement element : array) {
			if(element.isJsonObject()) {
				JsonObject obj = element.getAsJsonObject();
				JsonElement idElement = obj.get("id");
				JsonElement facultyElement = obj.get("faculty");
				JsonElement chairElement = obj.get("chair");
				JsonElement gradYear = obj.get("graduation");
				
				if(idElement != null && !idElement.isJsonPrimitive()) {
					throw new ParserException("ID is not the JsonPrimitive type");
				}
				if(facultyElement != null && !facultyElement.isJsonPrimitive()) {
					throw new ParserException("facultyElementID is not the JsonPrimitive type");
				}
				if(chairElement != null && !chairElement.isJsonPrimitive()) {
					throw new ParserException("chairElementID is not the JsonPrimitive type");
				}
				if(gradYear != null && !gradYear.isJsonPrimitive()) {
					throw new ParserException("gradYear is not the JsonPrimitive type");
				}
				
				Object id = (idElement == null) ? 0 : processJsonPrimitive(idElement.getAsJsonPrimitive());
				Object facId = (facultyElement == null) ? 0 : processJsonPrimitive(facultyElement.getAsJsonPrimitive());
				Object chairId = (chairElement == null) ? 0 : processJsonPrimitive(chairElement.getAsJsonPrimitive());
				Object year = (gradYear == null) ? 0 : processJsonPrimitive(gradYear.getAsJsonPrimitive());
				
				ids.append(id.toString()).append(',');
				faculties.append(facId.toString()).append(',');
				chairs.append(chairId.toString()).append(',');
				graduationYears.append(year.toString()).append(',');
			}
		}
		
		if(ids.length() > 0) {
			String resultIDs = ids.deleteCharAt(ids.length()-1).toString();
			user.put("universities", resultIDs);
		}
		if(faculties.length() > 0) {
			String resultIDs = faculties.deleteCharAt(faculties.length()-1).toString();
			user.put("faculties", resultIDs);
		}
		if(chairs.length() > 0) {
			String resultIDs = chairs.deleteCharAt(chairs.length()-1).toString();
			user.put("chairs", resultIDs);
		}
		if(graduationYears.length() > 0) {
			String resultIDs = graduationYears.deleteCharAt(graduationYears.length()-1).toString();
			user.put("graduation_years", resultIDs);
		}
	}
	
	
	private Object processJsonPrimitive(JsonPrimitive primitive) {
		return primitive.toString();
	}
}
