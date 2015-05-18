package com.alexssource.fksis.analyse.data.linkedin.mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alexssource.fksis.analyse.data.linkedin.ProxyConfiguration;
import com.alexssource.fksis.analyse.data.linkedin.impl.LinkedinUrlProfileCrawler;
import com.alexssource.fksis.analyse.data.linkedin.service.UrlProfileCrawler;

public class LinkedinReducer extends Reducer<IntWritable, Text, NullWritable, NullWritable> {
	private final static Logger logger = LoggerFactory.getLogger(LinkedinReducer.class);

	@Override
	protected void reduce(IntWritable key, Iterable<Text> values, Context ctx)
			throws IOException, InterruptedException 
	{
		List<String> urls = new ArrayList<String>();
		Configuration conf = ctx.getConfiguration();
		logger.debug("Reducer start");
		logger.debug("Thread ID: {}", Thread.currentThread().getId());
		logger.info("Key: {}", key);
		
		// TODO:  Œ—“€À»Ÿ≈! ;)
		if(conf.getBoolean("proxyEnabled", false)) {
			ProxyConfiguration.proxyEnabled = true;
			ProxyConfiguration.proxyHost = conf.get("proxyHost");
			ProxyConfiguration.proxyPort = conf.getInt("proxyPort", 8080);
		}
		
		String outputFolder = conf.get("outputFolder");
		logger.error("--- Output folder: {}", outputFolder);
		
		for(Text value : values) {
			logger.debug("--- Value: {}", value);
			urls.add(value.toString());
		}
		
		UrlProfileCrawler crawler = new LinkedinUrlProfileCrawler();
		crawler.crawl(urls, outputFolder, key.get());
	}
	
}
