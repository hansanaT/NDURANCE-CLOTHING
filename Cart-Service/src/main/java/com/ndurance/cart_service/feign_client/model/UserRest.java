package com.ndurance.cart_service.feign_client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ndurance.cart_service.shared.AddressesModel;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRest {
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private List<AddressesModel> addresses;
	private int defaultAddress;

	private List<GrantedAuthority> authorities;

	public List<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public int getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(int defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

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

	public List<AddressesModel> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressesModel> addresses) {
		this.addresses = addresses;
	}

}
