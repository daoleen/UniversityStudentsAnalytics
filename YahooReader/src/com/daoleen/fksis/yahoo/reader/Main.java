package com.daoleen.fksis.yahoo.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alexssource.fksis.analyse.data.yahoo.FileWriterHdfs;
import com.alexssource.fksis.analyse.data.yahoo.ProxyConfiguration;
import com.alexssource.fksis.analyse.data.yahoo.YahooFileWriter;
import com.alexssource.fksis.analyse.data.yahoo.YahooParserMain;

public class Main {
private final static Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		if(args.length < 2) {
			logger.error("Incorrect invocation of Main function");
			logger.error("You need to type 2 or 4 parameters: folder where save the yahoo content and yahoo pages count to parse; and proxy host and port");
			System.exit(-1);
		}
		
		String folder = args[0];
		int pages = Integer.parseInt(args[1]);
		String template = folder + "/yahoo-%d.html";
		
		if(args.length == 4) {
			ProxyConfiguration.proxyEnabled = true;
			ProxyConfiguration.proxyHost = args[2];
			ProxyConfiguration.proxyPort = Integer.parseInt(args[3]);
		}
		
		// TODO: replace to hdfs before deployment
		YahooFileWriter filewriter = new FileWriterHdfs(template);
		YahooParserMain yahooParser = new YahooParserMain(filewriter,
			"\"belarusian state university of informatics and radioelectronics\" site:linkedin.com/in/"
		);
		yahooParser.parse(pages);
	}
}
