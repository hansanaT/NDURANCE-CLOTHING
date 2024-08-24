package com.ndurance.UserService.Service;


import com.ndurance.UserService.shared.DTO.AddressDTO;

import java.util.List;

public interface AddressService {
	List<AddressDTO> getAddresses(String userId);
    AddressDTO getAddress(String addressId);
}
