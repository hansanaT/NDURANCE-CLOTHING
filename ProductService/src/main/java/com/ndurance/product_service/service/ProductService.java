package com.ndurance.product_service.service;

import com.ndurance.product_service.entity.ProductEntity;
import com.ndurance.product_service.shared.dto.CommentDTO;
import com.ndurance.product_service.shared.model.request.ClothRequestModel;
import com.ndurance.product_service.shared.dto.ProductDTO;
import com.ndurance.product_service.shared.model.request.CommentRequestModel;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

public interface ProductService {
    ProductDTO insertProduct(ClothRequestModel clothRequestModel);
    void saveProduct(ClothRequestModel clothRequestModel, List<MultipartFile> files) throws Exception;
    void saveProduct(String productId, ClothRequestModel clothRequestModel, List<MultipartFile> files) throws Exception;
    void deleteProduct(String productId) throws Exception;
    ProductDTO getProducts(String productId);
    List<ProductDTO> getProducts();
    Page<ProductDTO> findAll(int page, int size);
    Page<ProductDTO> findByType(String type, int page, int size);
    Resource loadImageAsResource(String imageName) throws MalformedURLException;
    List<CommentDTO> getComments(String productId);
    void saveComment(CommentRequestModel requestModel, String userid, String auth);
}
