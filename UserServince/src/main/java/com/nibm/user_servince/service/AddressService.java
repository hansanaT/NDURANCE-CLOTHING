package com.nibm.user_servince.service;


import com.nibm.user_servince.shared.dto.AddressDTO;

import java.util.List;

public interface AddressService {
	List<AddressDTO> getAddresses(String userId);
    AddressDTO getAddress(String addressId);
}
