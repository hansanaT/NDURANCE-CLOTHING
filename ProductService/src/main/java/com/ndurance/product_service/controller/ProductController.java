package com.ndurance.product_service.controller;

import com.ndurance.product_service.exceptions.ProductServiceException;
import com.ndurance.product_service.shared.ProductType;
import com.ndurance.product_service.shared.dto.CommentDTO;
import com.ndurance.product_service.shared.model.request.ClothRequestModel;
import com.ndurance.product_service.service.ProductService;
import com.ndurance.product_service.shared.dto.ProductDTO;
import com.ndurance.product_service.shared.model.request.CommentRequestModel;
import com.ndurance.product_service.shared.model.response.ProductRest;
import com.ndurance.product_service.shared.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ProductService productService;

    @PostMapping("/comments")
    public void insertComment(@RequestBody CommentRequestModel requestModel){
        if(requestModel == null)
            throw new ProductServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        productService.saveComment(requestModel);
    }

    @GetMapping("/comments/{productId}")
    public List<CommentDTO> commentDTOS(@PathVariable String productId){
        return productService.getComments(productId);
    }

    @PostMapping
    public void insertCloth(@RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("images") List<MultipartFile> files,
                            @RequestParam("type") String type, @RequestParam("price") String price) throws Exception {

        ClothRequestModel clothRequestModel = new ClothRequestModel();
        if(description == null || name == null || type == null || price == null || files == null)
            throw new ProductServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        clothRequestModel.setDescription(description);
        clothRequestModel.setType(ProductType.valueOf(type));
        clothRequestModel.setName(name);
        clothRequestModel.setPrice(Integer.parseInt(price));
        productService.saveCloth(clothRequestModel, files);

    }

    @PutMapping("/{productId}")
    public void updateCloth(@PathVariable String productId, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "description", required = false) String description,
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
        productService.saveCloth(productId,clothRequestModel, files);

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
    public ProductRest getCloth(@PathVariable String productId){
        ProductDTO cloth = productService.getCloth(productId);
        return modelMapper.map(cloth, ProductRest.class);
    }

    @DeleteMapping("/{productId}")
    public void deleteMapping(@PathVariable String productId) throws Exception {
        productService.deleteCloth(productId);
    }

    @GetMapping
    public List<ProductRest> getAllCloths(){
        List<ProductRest> productRests = new ArrayList<>();
        productService.getCloths().forEach(i->{
            productRests.add(modelMapper.map(i, ProductRest.class));
        });
        return productRests;
    }
}
