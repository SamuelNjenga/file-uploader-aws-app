package com.njenga.awsDemo.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.njenga.awsDemo.profile.UserProfile;

@Repository
public class FakeUserProfileDataStore {

	private static final List<UserProfile> USER_PROFILES = new ArrayList<>();
	static {
		USER_PROFILES.add(new UserProfile(UUID.randomUUID(),"cate wangui",null));
		USER_PROFILES.add(new UserProfile(UUID.randomUUID(),"frank kimani",null));
	}
	public List<UserProfile> getUserProfiles(){
		return USER_PROFILES;
	}
}
