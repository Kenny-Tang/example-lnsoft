package com.example.client.service;

import lombok.Data;

@Data
public class BidDownloadParam {
	private Long syncRecordId;
	private String documentStoragePath;
	private String referencePath;
	private long fileSize;

	public String getReferencePath() {
		return referencePath;
	}

	public void setReferencePath(String referencePath) {
		this.referencePath = referencePath;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
}
