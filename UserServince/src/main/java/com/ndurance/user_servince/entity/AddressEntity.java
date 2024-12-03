package com.ndurance.user_servince.entity;

import jakarta.persistence.*;

import java.io.Serializable;
 
@Entity(name="addresses")
public class AddressEntity implements Serializable {

	private static final long serialVersionUID = 7809200551672852690L;
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(length=30, nullable=false)
	private String addressId;
	
	@Column(length=15, nullable=false)
	private String city;
	
	@Column(length=15, nullable=false)
	private String country;
	
	@Column(length=100, nullable=false)
	private String streetName;
	
	@Column(length=7, nullable=false)
	private String postalCode;

	@ManyToOne
	@JoinColumn(name="users_id")
	private UserEntity userDetails;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public UserEntity getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserEntity userDetails) {
		this.userDetails = userDetails;
	}


	@Override
	public String toString() {
		return "AddressEntity{" +
				"id=" + id +
				", addressId='" + addressId + '\'' +
				", city='" + city + '\'' +
				", country='" + country + '\'' +
				", streetName='" + streetName + '\'' +
				", postalCode='" + postalCode + '\'' +
				", userDetails=" + userDetails +
				'}';
	}
}
