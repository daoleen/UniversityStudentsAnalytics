package com.alexssource.fksis.analyse.data.yahoo.mapreduce;

import java.io.IOException;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YahooMapper extends Mapper<LongWritable, Text, IntWritable, Text> {
	private final static Logger logger = LoggerFactory.getLogger(YahooMapper.class);
	private final static Random r = new Random();
	
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException 
	{
		Configuration conf = context.getConfiguration();
		
		logger.debug("Run YahooMapper map()");
		logger.debug("key (names file id): {}", key);
		logger.debug("value (names): {}", value);
		logger.debug("Configuration parameters information:");
		logger.debug("outputFileTemplate: {}", conf.get("outputFileTemplate"));
		logger.debug("outputFolder: {}", conf.get("outputFolder"));
		logger.debug("proxyEnabled: {}, proxyHost: {}, proxyPort: {}",
				conf.getBoolean("proxyEnabled", false), conf.get("proxyHost"),
				conf.getInt("proxyPort", 8080)
		);
		logger.debug("Search query: {}", conf.get("searchQuery"));
		logger.debug("Thread ID: {}", Thread.currentThread().getId());
		
		int datanode = r.nextInt(10) + 1;
		context.write(new IntWritable(datanode), new Text(value));
	}
	
	
}
