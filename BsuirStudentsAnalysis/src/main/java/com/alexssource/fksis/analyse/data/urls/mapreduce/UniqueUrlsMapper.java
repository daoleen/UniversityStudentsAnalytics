package com.alexssource.fksis.analyse.data.urls.mapreduce;

import java.io.IOException;
import java.net.URLDecoder;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniqueUrlsMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
	private final static Logger logger = LoggerFactory.getLogger(UniqueUrlsMapper.class);
	private final static String Encoding = "UTF-8";
	
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException 
	{
		logger.debug("Run UniqueUrlsMapper map()");
		logger.debug("key (names file id): {}", key);
		logger.debug("value (url): {}", value);
		String decodedUrl = URLDecoder.decode(value.toString(), UniqueUrlsMapper.Encoding);
		
		// if this url is public directory ignore it
		if(decodedUrl.contains("linkedin.com/pub/dir/")) {
			return;
		}
		
		context.write(new Text(decodedUrl), NullWritable.get());
	}
}
