package com.nibm.product_service.service;

import com.nibm.product_service.shared.dto.ClothDTO;
import com.nibm.product_service.shared.model.request.ClothRequestModel;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

public interface ClothService {
    ClothDTO insertCloth(ClothRequestModel clothRequestModel);
    void saveCloth(ClothRequestModel clothRequestModel, List<MultipartFile> files) throws Exception;
    void saveCloth(String publicId, ClothRequestModel clothRequestModel,List<MultipartFile> files) throws Exception;

    void deleteCloth(String publicId) throws Exception;
    ClothDTO getCloth(String publicId);

    List<ClothDTO> getCloths();
    Resource loadImageAsResource(String imageName) throws MalformedURLException;
}
