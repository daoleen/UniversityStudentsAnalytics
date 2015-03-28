package com.alexssource.fksis.analyse.data.hbase.importer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alexssource.fksis.analyse.data.hbase.importer.ImporterJob;
import com.alexssource.fksis.analyse.data.parser.JsonParser;
import com.alexssource.fksis.analyse.data.parser.Parser;
import com.alexssource.fksis.analyse.data.parser.ParserException;

public class ImporterMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {
	private final static Logger logger = LoggerFactory.getLogger(ImporterMapper.class);
	private final Parser parser = new JsonParser();

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException 
	{
		logger.debug("Key is: {}", key);

		try {
			List<Map<String, Object>> mapList = parser.parse(value.toString());
			for (Map<String, Object> map : mapList) {
				byte[] rowkey = Bytes.toBytes(map.get("id").toString());
				Put put = new Put(rowkey);
				for (Entry<String, Object> field : map.entrySet()) {
					if (!field.getKey().equals("id")) {
						put.add(ImporterJob.USERS_CF,
								Bytes.toBytes(field.getKey()),
								Bytes.toBytes(field.getValue().toString()));
					}
				}
				context.write(new ImmutableBytesWritable(rowkey), put);
				// context.getCounter . increment
			}
		} catch (ParserException exc) {
			logger.error(ExceptionUtils.getStackTrace(exc));
		}

		logger.info("Mapper and Reducer for key {} has completed successfully!", key);
		// context.write(key, value);
	}
}
