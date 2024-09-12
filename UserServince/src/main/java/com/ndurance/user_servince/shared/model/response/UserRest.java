package com.ndurance.user_servince.shared.model.response;

import java.util.Collection;
import java.util.List;

public class UserRest {
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private List<AddressesRest> addresses;
	private String profilePic;
	private Collection<String> roles;

	public Collection<String> getRoles() {
		return roles;
	}

	public void setRoles(Collection<String> roles) {
		this.roles = roles;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public int getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(int defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	private int defaultAddress;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<AddressesRest> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressesRest> addresses) {
		this.addresses = addresses;
	}

}
