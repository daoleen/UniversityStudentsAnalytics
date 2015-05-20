package com.alexssource.fksis.analyse.data.yahoo.query;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryGenerator {
	private static final Logger logger = LoggerFactory.getLogger(QueryGenerator.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		if(args.length < 3) {
//			logger.error("Incorrect invocaion of QueryGenerator.main() method");
//			logger.error("Usage: <Path to Names file> <Path to QueryTemplate file> " +
//					"<Output folder path>");
//			return;
//		}
		
		/**
		 * INFO -> BSUIR: 	11304 queries
		 * INFO -> BSU:		5024 queries
		 */
		
		File namesFile = new File("src/main/resources/yahoo-query/RussianNames.txt");
		File queryTemplates = new File("src/main/resources/yahoo-query/QueryTemplates-BSUIR-UTF8.txt");
		File outputFile = new File("src/main/resources/yahoo-query/out/Query-BSUIR.txt");
		
		if(outputFile.exists()) {
			outputFile.delete();
		}
		
		try {
			List<String> names = readFileLn(namesFile);
			logger.info("Found {} names", names.size());
			List<String> templates = readFileLn(queryTemplates);
			logger.info("Found {} templates", templates.size());
			List<String> queries = new ArrayList<String>(names.size()*templates.size());
			
			for (String name : names) {
				if(!name.trim().equals("")) {
					for (String template : templates) {
						if(!template.trim().equals("")) {
							String query = String.format(template, name);
							//logger.debug("Query: {}", query);
							queries.add(query);
						}
					}
				}
			}
			
			logger.info("Created {} queries", queries.size());
			writeQueriesResult(outputFile, queries);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	public static List<String> readFileLn(File file) throws IOException {
		FileInputStream input = new FileInputStream(file);
		return IOUtils.readLines(input);
	}
	
	public static void writeQueriesResult(File outputFile, List<String> queries) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(outputFile, false);
			for (String query : queries) {
				writer.write(query + "\n");
			}
			logger.info("Queries wan sucessfully written to the output file!");
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			if(writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}
}
