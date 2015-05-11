package com.daoleen.fksis.bigdata.yahoo;

import com.alexssource.fksis.analyse.data.yahoo.mapreduce.YahooJob;

public class Main {
	public static void main(String[] args) throws Exception {
		new YahooJob().run(args);
	}
}
