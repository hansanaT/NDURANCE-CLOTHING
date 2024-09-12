package com.ndurance.product_service.controller;

import com.ndurance.product_service.exceptions.ProductServiceException;
import com.ndurance.product_service.exceptions.ProductUnAuthorizedServiceException;
import com.ndurance.product_service.shared.ProductType;
import com.ndurance.product_service.shared.dto.CommentDTO;
import com.ndurance.product_service.shared.model.request.ClothRequestModel;
import com.ndurance.product_service.service.ProductService;
import com.ndurance.product_service.shared.dto.ProductDTO;
import com.ndurance.product_service.shared.model.request.CommentRequestModel;
import com.ndurance.product_service.shared.model.response.ProductRest;
import com.ndurance.product_service.shared.model.response.ErrorMessages;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ProductService productService;

    @PostMapping("/comments/{userid}")
    public void insertComment(@RequestBody CommentRequestModel requestModel, @PathVariable String userid, HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");

        if(requestModel == null)
            throw new ProductServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("userid " +userid);
        System.out.println("username " +username);
        if(!Objects.equals(username, userid))
            throw new ProductUnAuthorizedServiceException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());

        productService.saveComment(requestModel, userid, authorizationHeader);
    }

    @GetMapping("/comments/{productId}")
    public List<CommentDTO> commentDTOS(@PathVariable String productId){
        return productService.getComments(productId);
    }

    @PostMapping
    public void insertProduct(@RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("images") List<MultipartFile> files,
                            @RequestParam("type") String type, @RequestParam("price") String price) throws Exception {

        ClothRequestModel clothRequestModel = new ClothRequestModel();
        if(description == null || name == null || type == null || price == null || files == null)
            throw new ProductServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        clothRequestModel.setDescription(description);
        clothRequestModel.setType(ProductType.valueOf(type));
        clothRequestModel.setName(name);
        clothRequestModel.setPrice(Integer.parseInt(price));
        productService.saveProduct(clothRequestModel, files);

    }

    @PutMapping("/{productId}")
    public void updateProduct(@PathVariable String productId, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "description", required = false) String description,
                            @RequestParam(value = "images", required = false) List<MultipartFile> files, @RequestParam(value = "price", required = false) String price ,
                            @RequestParam(value = "type", required = false) String type) throws Exception {

        ClothRequestModel clothRequestModel = new ClothRequestModel();
        if(description != null)
            clothRequestModel.setDescription(description);

        if(name != null)
            clothRequestModel.setName(name);

        if(type != null)
            clothRequestModel.setType(ProductType.valueOf(type));

        if(price != null){
            clothRequestModel.setPrice(Integer.parseInt(price));
        }
        productService.saveProduct(productId,clothRequestModel, files);

    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Resource resource = productService.loadImageAsResource(imageName);

            String contentType = "image/jpeg"; // Default to JPEG
            try {
                contentType = Files.probeContentType(resource.getFile().toPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{productId}")
    public ProductDTO getProduct(@PathVariable String productId){
        return productService.getProduct(productId);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable String productId) throws Exception {
        productService.deleteProduct(productId);
    }

    @GetMapping
    public Page<ProductRest> getAllProducts(@RequestParam(name = "page", defaultValue="0") int page, @RequestParam(name="size", defaultValue = "20") int size){
        return productService.findAll(page,size).map(i-> modelMapper.map(i, ProductRest.class));
    }

    @GetMapping("/byType")
    public Page<ProductRest> getAllProducts(@RequestParam ProductType type, @RequestParam(name = "page", defaultValue="0") int page, @RequestParam(name="size", defaultValue = "20") int size){
        return productService.findByType(type, page,size).map(i-> modelMapper.map(i, ProductRest.class));
    }
}
