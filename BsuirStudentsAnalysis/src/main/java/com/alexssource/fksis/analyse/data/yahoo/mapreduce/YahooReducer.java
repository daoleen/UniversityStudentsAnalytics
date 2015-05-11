package com.alexssource.fksis.analyse.data.yahoo.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alexssource.fksis.analyse.data.yahoo.FileWriterHdfs;
import com.alexssource.fksis.analyse.data.yahoo.ProxyConfiguration;
import com.alexssource.fksis.analyse.data.yahoo.YahooFileWriter;
import com.alexssource.fksis.analyse.data.yahoo.YahooParserMain;

public class YahooReducer extends Reducer<IntWritable, Text, NullWritable, NullWritable> {
	private final static Logger logger = LoggerFactory.getLogger(YahooReducer.class);
	
	@Override
	protected void reduce(IntWritable key, Iterable<Text> values, Context ctx)
			throws IOException, InterruptedException 
	{
		Configuration conf = ctx.getConfiguration();
		String searchQueryTemplate = conf.get("searchQuery");
		logger.info("Reducer start");
		logger.info("Thread ID: {}", Thread.currentThread().getId());
		logger.info("Key: {}", key);
		
		// TODO:  Œ—“€À»Ÿ≈! ;)
		if(conf.getBoolean("proxyEnabled", false)) {
			ProxyConfiguration.proxyEnabled = true;
			ProxyConfiguration.proxyHost = conf.get("proxyHost");
			ProxyConfiguration.proxyPort = conf.getInt("proxyPort", 8080);
		}
		
		for(Text value : values) {
			String fileTemplate = String.format(conf.get("outputFileTemplate"), key.get(), "%d", value);
			String searchQuery = String.format(searchQueryTemplate, value.toString());
			logger.info("--- Value: {}", value);
			logger.info("File template: {}", fileTemplate);
			logger.info("SearchQuery: {}", searchQuery);
			
			YahooFileWriter filewriter = new FileWriterHdfs(fileTemplate);
			YahooParserMain yahooParser = new YahooParserMain(filewriter, searchQuery);
			yahooParser.parse();
		}
	}

}
