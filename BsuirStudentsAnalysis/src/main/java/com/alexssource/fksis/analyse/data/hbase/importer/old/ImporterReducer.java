package com.alexssource.fksis.analyse.data.hbase.importer.old;
/*
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alexssource.fksis.analyse.data.parser.JsonParser;
import com.alexssource.fksis.analyse.data.parser.Parser;
import com.alexssource.fksis.analyse.data.parser.ParserException;

@Deprecated
public class ImporterReducer  extends TableReducer <LongWritable, Text, Text> {
	private final static Logger logger = LoggerFactory.getLogger(ImporterReducer.class);
	private final Parser parser = new JsonParser();
	
	@Override
	protected void reduce(LongWritable key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException 
	{
		for(Text document : values) {
			try {
				List<Map<String, Object>> mapList = parser.parse(document.toString());
				for(Map<String, Object> map : mapList) {
					Put put = new Put(Bytes.toBytes(map.get("id").toString()));
					for(Entry<String, Object> field : map.entrySet()) {
						if(!field.getKey().equals("id")) {
							put.add(ImporterJob.USERS_CF, Bytes.toBytes(field.getKey()), Bytes.toBytes(field.getValue().toString()));
						}
					}
					context.write(null, put);
				}
			} catch(ParserException exc) {
				logger.error(ExceptionUtils.getStackTrace(exc));
			}
		}
	}
}
*/