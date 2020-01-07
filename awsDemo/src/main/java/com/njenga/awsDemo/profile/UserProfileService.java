
package com.njenga.awsDemo.profile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.client.builder.AdvancedConfig.Key;
import com.njenga.awsDemo.bucket.BucketName;
import com.njenga.awsDemo.filestore.FileStore;
@Service
public class UserProfileService {

	private final UserProfileDataAccessService userProfileDataAccessService;
	private final FileStore fileStore;

	@Autowired
	public UserProfileService(UserProfileDataAccessService userProfileDataAccessService,FileStore fileStore) {
		super();
		this.userProfileDataAccessService = userProfileDataAccessService;
		this.fileStore = fileStore;
	}
	
	List<UserProfile> getUserProfiles(){
		return userProfileDataAccessService.getUserProfiles();
	}

	public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
		//1.Check if image is not empty
		if(file.isEmpty()) {
			throw new  IllegalStateException("Cannot upload empty file [" + file.getSize() +"]");
		}
		//2.If file is an image
		if(!Arrays.asList(ContentType.IMAGE_JPEG.getMimeType(),ContentType.IMAGE_PNG.getMimeType(),ContentType.IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
			throw new  IllegalStateException("File must be an image [" +file.getContentType());
		}
		//3.The user exists in our database
		
		UserProfile user = getUserProfileOrThrow(userProfileId);
		// 4.Grab some metadata from file if any
		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("Content-Type", file.getContentType());
		metadata.put("Content-Lenght", String.valueOf(file.getSize()));
		//5.Store the image in s3 and update database (userProfileImageLink) with s3 image link
		
		String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(),user.getUserProfileId());
		String fileName = String.format("%s-%s", file.getOriginalFilename(),UUID.randomUUID());
		try {
			fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
			user.setUserProfileImageLink(fileName);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
			
	}

	private UserProfile getUserProfileOrThrow(UUID userProfileId) {
		return userProfileDataAccessService
		.getUserProfiles()
		.stream()
		.filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
		.findFirst()
		.orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", userProfileId)));
	}

	public byte[] downloadUserProfileImage(UUID userProfileId) {
		UserProfile user = getUserProfileOrThrow(userProfileId);
		String path = String.format("%s/%s",
				BucketName.PROFILE_IMAGE.getBucketName(),
				user.getUserProfileId());
			return	user.getUserProfileImageLink()
					.map(key -> fileStore.download(path, key))
					.orElse(new byte[0]);
		
	}
	
}
