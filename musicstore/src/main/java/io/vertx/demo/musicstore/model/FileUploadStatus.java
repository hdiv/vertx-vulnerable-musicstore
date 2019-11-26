package io.vertx.demo.musicstore.model;

public class FileUploadStatus {
	private String fileName;

	private String status;

	public FileUploadStatus(final String fileName, final String status) {
		super();
		this.fileName = fileName;
		this.status = status;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

}