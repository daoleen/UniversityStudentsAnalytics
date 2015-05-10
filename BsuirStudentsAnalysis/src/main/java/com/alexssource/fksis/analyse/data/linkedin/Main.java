package com.alexssource.fksis.analyse.data.linkedin;

import com.alexssource.fksis.analyse.data.linkedin.impl.LinkedinFileHandler;
import com.alexssource.fksis.analyse.data.linkedin.service.FileHandler;

/**
 * Its class isn't used by BigInsights application
 * @author alexander.kozlov
 *
 */
public class Main {
	public static void main(String[] args) {
		if(args == null || args.length < 2) {
			System.out.println("Args: <input file> <output path> [<proxy host> <proxy port>]");
			return;
		}
		
		if(args.length == 4) {
			ProxyConfiguration.proxyEnabled = true;
			ProxyConfiguration.proxyHost = args[2];
			ProxyConfiguration.proxyPort = Integer.parseInt(args[3]);
		}
		
		fileHandlerTest(args[0], args[1]);
	}
	
	private static void fileHandlerTest(String inputPath, String outputPath) {
		FileHandler handler = new LinkedinFileHandler();
		handler.readUrls(inputPath);
		
		// writing 2 files
		String file1 = outputPath.concat("/file1.html");
		String file2 = outputPath.concat("/file2.html");
		handler.saveProfile(file1, "<profile>my profile 1</pprofile>");
		handler.saveProfile(file2, "<profile>my profile 2</pprofile>");
	}
	
	
	private static void httpRequest() {
		
	}
}
