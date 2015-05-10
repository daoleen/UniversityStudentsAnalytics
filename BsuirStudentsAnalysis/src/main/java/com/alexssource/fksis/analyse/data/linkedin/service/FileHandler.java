package com.alexssource.fksis.analyse.data.linkedin.service;

import java.util.List;

public interface FileHandler {
	public List<String> readUrls(String path);
	public void saveProfile(String file, String html);
}