package com.alexssource.fksis.biginsights;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

import com.alexssource.fksis.analyse.data.hbase.importer.ImporterJob;

public class ImporterRunner {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new ImporterJob(), args);
		System.exit(res);
	}
}