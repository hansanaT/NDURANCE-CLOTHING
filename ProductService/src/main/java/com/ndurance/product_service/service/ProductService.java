package com.ndurance.product_service.service;

import com.ndurance.product_service.shared.ProductType;
import com.ndurance.product_service.shared.dto.CommentDTO;
import com.ndurance.product_service.shared.model.request.ClothRequestModel;
import com.ndurance.product_service.shared.dto.ProductDTO;
import com.ndurance.product_service.shared.model.request.CommentRequestModel;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

public interface ProductService {
    ProductDTO insertProduct(ClothRequestModel clothRequestModel);
    void saveProduct(ClothRequestModel clothRequestModel, List<MultipartFile> files) throws Exception;
    void saveProduct(String productId, ClothRequestModel clothRequestModel, List<MultipartFile> files) throws Exception;
    void deleteProduct(String productId) throws Exception;
    ProductDTO getProduct(String productId);
    List<ProductDTO> getCloths();
    Resource loadImageAsResource(String imageName) throws MalformedURLException;
    Page<ProductDTO> findAll(int page, int size);
    Page<ProductDTO> findByType(ProductType type, int page, int size);
    List<CommentDTO> getComments(String productId);
    void saveComment(CommentRequestModel requestModel, String userid, String auth);
}
