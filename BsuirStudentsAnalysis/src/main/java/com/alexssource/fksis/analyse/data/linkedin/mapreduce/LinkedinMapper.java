package com.alexssource.fksis.analyse.data.linkedin.mapreduce;

import java.io.IOException;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkedinMapper extends Mapper<LongWritable, Text, IntWritable, Text> {
	private final static Logger logger = LoggerFactory.getLogger(LinkedinMapper.class);
	private final static Random r = new Random();
	//private final JsonParser parser = new JsonParser();
	
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException 
	{
		Configuration conf = context.getConfiguration();
		
		logger.debug("Run LinkedinMapper map()");
		logger.debug("key (names file id): {}", key);
		logger.debug("value (urls): {}", value);
		logger.debug("Configuration parameters information:");
		logger.debug("outputFolder: {}", conf.get("outputFolder"));
		logger.debug("proxyEnabled: {}, proxyHost: {}, proxyPort: {}",
				conf.getBoolean("proxyEnabled", false), conf.get("proxyHost"),
				conf.getInt("proxyPort", 8080)
		);
		logger.debug("Thread ID: {}", Thread.currentThread().getId());
		
		
		//String url = parser.parse(value.toString()).getAsJsonObject().get("url").getAsString();
		logger.info("JSON url: {}", value);
		
		int datanode = r.nextInt(10) + 1;
		context.write(new IntWritable(datanode), new Text(value));
	}
}
