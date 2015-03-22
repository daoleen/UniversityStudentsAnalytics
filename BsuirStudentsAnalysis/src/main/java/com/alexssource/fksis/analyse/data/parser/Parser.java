package com.alexssource.fksis.analyse.data.parser;

import java.util.List;
import java.util.Map;

public interface Parser {
	public List<Map<String, Object>> parse(String text) throws ParserException;
}