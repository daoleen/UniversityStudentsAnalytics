package com.alexssource.fksis.analyse.data.hbase.importer.old;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class ImporterJob extends Configured implements Tool {
	private final static Logger logger = LoggerFactory.getLogger(ImporterJob.class);
	//public final static String INPUT_DB = "VkOldStudents";
	//public final static String OUTPUT_TABLE = "VkOldUsers"; // has been moved to program argument
	private String outputTable;
	public final static byte[] USERS_CF = Bytes.toBytes("u");
	public final static byte[] UNIVERSITIES_COLUMN = Bytes.toBytes("universities");
	
	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = HBaseConfiguration.create(getConf());
		//Configuration conf = getConf();
		String[] remainingArgs = new GenericOptionsParser(args).getRemainingArgs();
		
		if(remainingArgs.length != 2) {
			System.out.print("Usage: <in_path> <output_table_name>");
			System.exit(0);
		}
		
		outputTable = remainingArgs[1];
		
		/*
		FileSystem fs = FileSystem.get(conf);
		Path outputDir = new Path(remainingArgs[1]);
		logger.info("Output dir is: {}", outputDir);
		if(fs.exists(outputDir)) {
			logger.info("EXIST OUTPUT DIR");
			fs.delete(outputDir, true);	// true - recursive
		}
		*/
		
		Scan scan = new Scan();
		scan.addColumn(USERS_CF, UNIVERSITIES_COLUMN);	// I can use scan to find some rows by specified column
		
		Job job = new Job(conf, "HBase Importer");
		job.setJarByClass(ImporterJob.class);
		job.setMapperClass(ImporterMapper.class);
		job.setReducerClass(ImporterReducer.class);
		//job.setInputFormatClass(FileInputFormat.class);
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Text.class);
		
		FileInputFormat.addInputPath(job, new Path(remainingArgs[0]));
		//FileOutputFormat.setOutputPath(job, outputDir);
		TableMapReduceUtil.initTableReducerJob(outputTable, ImporterReducer.class, job);
		job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE, outputTable);
		logger.info("Job configuration was successfully set. Running job...");
		
		return job.waitForCompletion(true) ? 0 : 1;
	}
}
