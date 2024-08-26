package com.ndurance.product_service.service;

import com.ndurance.product_service.shared.dto.CommentDTO;
import com.ndurance.product_service.shared.model.request.ClothRequestModel;
import com.ndurance.product_service.shared.dto.ClothDTO;
import com.ndurance.product_service.shared.model.request.CommentRequestModel;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

public interface ProductService {
    ClothDTO insertCloth(ClothRequestModel clothRequestModel);
    void saveCloth(ClothRequestModel clothRequestModel, List<MultipartFile> files) throws Exception;
    void saveCloth(String publicId, ClothRequestModel clothRequestModel,List<MultipartFile> files) throws Exception;
    void deleteCloth(String publicId) throws Exception;
    ClothDTO getCloth(String publicId);
    List<ClothDTO> getCloths();
    Resource loadImageAsResource(String imageName) throws MalformedURLException;

    List<CommentDTO> getComments(String clothPublicId);
    void saveComment(CommentRequestModel requestModel);
}
