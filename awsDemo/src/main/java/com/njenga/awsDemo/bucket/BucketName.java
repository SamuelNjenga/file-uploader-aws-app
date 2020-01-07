package com.njenga.awsDemo.bucket;

public enum BucketName {

	PROFILE_IMAGE("njengabucket");
	private final String bucketName;

	private BucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getBucketName() {
		return bucketName;
	}
	
}
