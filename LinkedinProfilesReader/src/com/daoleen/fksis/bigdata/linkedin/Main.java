package com.daoleen.fksis.bigdata.linkedin;

import com.alexssource.fksis.analyse.data.linkedin.mapreduce.LinkedinJob;

public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		new LinkedinJob().run(args);
	}

}
