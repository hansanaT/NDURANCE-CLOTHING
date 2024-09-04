package com.ndurance.user_service.service;


import com.ndurance.user_service.shared.dto.AddressDTO;

import java.util.List;

public interface AddressService {
	List<AddressDTO> getAddresses(String userId);
    AddressDTO getAddress(String addressId);
}
