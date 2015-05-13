package com.alexssource.fksis.biginsights;

import org.apache.avro.data.Json;

import com.alexssource.fksis.analyse.data.linkedin.ProxyConfiguration;
import com.alexssource.fksis.analyse.data.linkedin.impl.LinkedinFileHandler;
import com.alexssource.fksis.analyse.data.linkedin.impl.LinkedinUrlProfileCrawler;
import com.alexssource.fksis.analyse.data.linkedin.service.FileHandler;
import com.alexssource.fksis.analyse.data.linkedin.service.UrlProfileCrawler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class Main {
	public static void main(String[] args) {
		gsontest();
		
		if(args == null || args.length <= 2) {
			System.out.println("Args: <input file> <output path> [<proxy host> <proxy port>]");
			return;
		}
		
		if(args.length == 4) {
			ProxyConfiguration.proxyEnabled = true;
			ProxyConfiguration.proxyHost = args[2];
			ProxyConfiguration.proxyPort = Integer.parseInt(args[3]);
		}
		
		fileHandler(args[0], args[1]);
		//fileHandlerTest(args[0], args[1]);
	}
	
	private static void fileHandler(String inputPath, String outputPath) {
		UrlProfileCrawler crawler = new LinkedinUrlProfileCrawler();
		crawler.crawl(inputPath, outputPath);
	}
	
	
	@SuppressWarnings(value = "unused")
	private static void fileHandlerTest(String inputPath, String outputPath) {
		FileHandler handler = new LinkedinFileHandler();
		handler.readUrls(inputPath);
		
		// writing 2 files
		String file1 = outputPath.concat("/file1.html");
		String file2 = outputPath.concat("/file2.html");
		handler.saveProfile(file1, "<profile>my profile 1</pprofile>");
		handler.saveProfile(file2, "<profile>my profile 2</pprofile>");
	}
	
	
	private static void gsontest() {
		String json = "{  \"url\": \"https://www.linkedin.com/in/igortucha\"}";
		JsonParser parser = new JsonParser();
		JsonElement root = parser.parse(json);
		JsonObject obj = root.getAsJsonObject();
		String url = obj.get("url").getAsString();
		System.out.println(url);
	}
}
