package com.njenga.awsDemo.profile;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

	
	private UUID userProfileId;
	private String username;
	private String userProfileImageLink; // S3 key
	
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserProfile other = (UserProfile) obj;
		return Objects.equals(userProfileId, other.userProfileId) &&
				Objects.equals(username, other.username) &&
				Objects.equals(userProfileImageLink,other.userProfileImageLink);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userProfileId == null) ? 0 : userProfileId.hashCode());
		result = prime * result + ((userProfileImageLink == null) ? 0 : userProfileImageLink.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}
	public Optional<String> getUserProfileImageLink() {
		return Optional.ofNullable(userProfileImageLink);
	}
	public void setUserProfileImageLink(String userProfileImageLink) {
		this.userProfileImageLink = userProfileImageLink;
	}
	
	
}
