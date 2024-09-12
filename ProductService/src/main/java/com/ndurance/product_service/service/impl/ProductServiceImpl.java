package com.ndurance.product_service.service.impl;

import com.ndurance.product_service.entity.CommentEntity;
import com.ndurance.product_service.exceptions.ProductNotFoundServiceException;
import com.ndurance.product_service.repository.CommentRepository;
import com.ndurance.product_service.shared.Utils;
import com.ndurance.product_service.shared.dto.CommentDTO;
import com.ndurance.product_service.shared.model.request.ClothRequestModel;
import com.ndurance.product_service.shared.model.request.CommentRequestModel;
import com.ndurance.product_service.shared.model.response.ErrorMessages;
import com.ndurance.product_service.entity.ProductEntity;
import com.ndurance.product_service.repository.ProductRepository;
import com.ndurance.product_service.service.ProductService;
import com.ndurance.product_service.shared.dto.ProductDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private Utils utils;

    private final ModelMapper modelMapper = new ModelMapper();

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/uploads/";
    private final Path fileStorageLocation = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();

    @Override
    public ProductDTO getCloth(String productId) {
        ProductEntity productEntity = productRepository.findByProductId(productId);
        if(productEntity == null)
            throw new ProductNotFoundServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        return modelMapper.map(productEntity, ProductDTO.class);
    }

    @Override
    public List<ProductDTO> getCloths() {

        List<ProductDTO> productDTOS = new ArrayList<>();

        productRepository.findAll().forEach(cloth -> {
            ProductDTO productDTO = modelMapper.map(cloth, ProductDTO.class);
            productDTOS.add(productDTO);
        });
        return productDTOS;
    }

    @Override
    public List<ProductDTO> searchCloths(String name) {
        return null;
    }

    @Override
    public List<ProductDTO> getClothsByType(String type) {
        return null;
    }

    @Override
    public Resource loadImageAsResource(String imageName) throws MalformedURLException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(imageName).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return resource;
        } else {
            throw new ProductNotFoundServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
    }

    @Override
    public List<CommentDTO> getComments(String productId) {
        ProductEntity cloth = productRepository.findByProductId(productId);

        if(cloth == null)
            throw new ProductNotFoundServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        List<CommentDTO> commentDTOS = new ArrayList<>();

        cloth.getComments().forEach(i->{
            commentDTOS.add(modelMapper.map(i, CommentDTO.class));
        });

        return commentDTOS;
    }

    @Override
    public void saveComment(CommentRequestModel requestModel) {

        CommentEntity comment = new CommentEntity();
        comment.setUserPublicId(requestModel.getUserPublicId());
        comment.setComment(requestModel.getComment());

        ProductEntity cloth = productRepository.findByProductId(requestModel.getClothPublicId());
        if(cloth == null)
            throw new ProductNotFoundServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        comment.setCloth(cloth);
        commentRepository.save(comment);

        List<CommentEntity> comments = cloth.getComments();
        if(comments == null){
            comments = new ArrayList<>();
            comments.add(comment);
        }
        else{
            comments.add(comment);
        }
        cloth.setComments(comments);
        productRepository.save(cloth);
    }

    @Override
    public ProductDTO insertCloth(ClothRequestModel clothRequestModel) {
        ProductEntity cloth = modelMapper.map(clothRequestModel, ProductEntity.class);
        ProductEntity saved = productRepository.save(cloth);
        return modelMapper.map(saved, ProductDTO.class);
    }

    @Override
    public void saveCloth(ClothRequestModel clothRequestModel, List<MultipartFile> files) throws Exception {
        List<String> images = new ArrayList<>();

        ProductEntity cloth = modelMapper.map(clothRequestModel, ProductEntity.class);
        cloth.setProductId(utils.generateAddressId(20));

        cloth.setComments(List.of());

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    // Save file to the upload directory
                    Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
                    Files.createDirectories(uploadPath);

                    String fileName = utils.generateUserId(20) + file.getOriginalFilename();
                    images.add(fileName);

                    Path filePath = uploadPath.resolve(Objects.requireNonNull(fileName));
                    file.transferTo(filePath.toFile());


                } catch (IOException e) {
                    throw new Exception(e.getMessage());
                }
            }


            cloth.setImages(images);
        }
        ProductEntity productEntity = productRepository.save(cloth);
    }

    @Override
    public void saveCloth(String productId, ClothRequestModel clothRequestModel, List<MultipartFile> files) throws Exception {
        ProductEntity existingCloth = productRepository.findByProductId(productId);

        if(clothRequestModel.getDescription() != null)
            existingCloth.setDescription(clothRequestModel.getDescription());

        if(clothRequestModel.getName() != null)
            existingCloth.setName(clothRequestModel.getName());

        if(clothRequestModel.getType() != null)
            existingCloth.setType(clothRequestModel.getType());

        if(clothRequestModel.getPrice() != 0){
            existingCloth.setPrice(clothRequestModel.getPrice());
        }

        if (existingCloth != null) {

            List<String> oldImages = existingCloth.getImages();
            List<String> newImages = new ArrayList<>();

            if (oldImages != null) {
                for (String imageMetadata : oldImages) {
                    Path oldFilePath = Paths.get(imageMetadata);
                    try {
                        Files.deleteIfExists(oldFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (files != null && !files.isEmpty()) {
                    for (MultipartFile file : files) {
                        try {
                            // Save file to the upload directory
                            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
                            Files.createDirectories(uploadPath);

                            String fileName = utils.generateUserId(20) + file.getOriginalFilename();
                            newImages.add(fileName);

                            Path filePath = uploadPath.resolve(Objects.requireNonNull(fileName));
                            file.transferTo(filePath.toFile());


                        } catch (IOException e) {
                            throw new Exception(e.getMessage());
                        }
                    }


                    existingCloth.setImages(newImages);
                    ProductEntity productEntity = productRepository.save(existingCloth);
                }else{
                    ProductEntity productEntity = productRepository.save(existingCloth);
                }
            }
        } else {
            throw new ProductNotFoundServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
    }

    @Override
    public void deleteCloth(String productId) throws Exception {
        ProductEntity existingCloth = productRepository.findByProductId(productId);
        if(existingCloth != null)
            productRepository.delete(existingCloth);
        else
            throw new ProductNotFoundServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
    }
}

