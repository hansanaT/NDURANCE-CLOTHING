package com.nibm.UserServince.service;


import com.nibm.UserServince.shared.dto.AddressDTO;

import java.util.List;

public interface AddressService {
	List<AddressDTO> getAddresses(String userId);
    AddressDTO getAddress(String addressId);
}
