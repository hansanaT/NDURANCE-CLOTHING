package com.ndurance.user_servince.shared.model.request;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsRequestModel {
	
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private List<AddressRequestModel> addresses = new ArrayList<>();

	private int defaultAddress = 0;

	public int getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(int defaultAddress) {
		this.defaultAddress = defaultAddress;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<AddressRequestModel> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<AddressRequestModel> addresses) {
		this.addresses = addresses;
	}

}
