package com.nibm.product_service.service.impl;

import com.nibm.product_service.entity.ClothEntity;
import com.nibm.product_service.exceptions.ProductNotFoundServiceException;
import com.nibm.product_service.repository.ClothRepository;
import com.nibm.product_service.service.ClothService;
import com.nibm.product_service.shared.Utils;
import com.nibm.product_service.shared.dto.ClothDTO;
import com.nibm.product_service.shared.model.request.ClothRequestModel;
import com.nibm.product_service.shared.model.response.ErrorMessages;
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
public class ClothServiceImpl implements ClothService {

    @Autowired
    private ClothRepository clothRepository;

    @Autowired
    private Utils utils;

    private final ModelMapper modelMapper = new ModelMapper();

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/uploads/";
    private final Path fileStorageLocation = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();

    @Override
    public ClothDTO getCloth(String publicId) {
        ClothEntity clothEntity = clothRepository.findByPublicId(publicId);
        if(clothEntity == null)
            throw new ProductNotFoundServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        return modelMapper.map(clothEntity, ClothDTO.class);
    }

    @Override
    public List<ClothDTO> getCloths() {

        List<ClothDTO> clothDTOS = new ArrayList<>();

        clothRepository.findAll().forEach(cloth -> {
            ClothDTO clothDTO = modelMapper.map(cloth, ClothDTO.class);
            clothDTOS.add(clothDTO);
        });
        return clothDTOS;
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
    public ClothDTO insertCloth(ClothRequestModel clothRequestModel) {
        ClothEntity cloth = modelMapper.map(clothRequestModel, ClothEntity.class);
        ClothEntity saved = clothRepository.save(cloth);
        return modelMapper.map(saved, ClothDTO.class);
    }

    @Override
    public void saveCloth(ClothRequestModel clothRequestModel, List<MultipartFile> files) throws Exception {
        List<String> images = new ArrayList<>();

        ClothEntity cloth = modelMapper.map(clothRequestModel, ClothEntity.class);
        cloth.setPublicId(utils.generateAddressId(20));

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
            ClothEntity clothEntity = clothRepository.save(cloth);
        }
    }

    @Override
    public void saveCloth(String publicId, ClothRequestModel clothRequestModel, List<MultipartFile> files) throws Exception {
        ClothEntity existingCloth = clothRepository.findByPublicId(publicId);
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
                    ClothEntity clothEntity = clothRepository.save(existingCloth);
                }else{
                    existingCloth.setImages(newImages);
                    ClothEntity clothEntity = clothRepository.save(existingCloth);
                }
            }
        } else {
            return;
        }
    }

    @Override
    public void deleteCloth(String publicId) throws Exception {
        ClothEntity existingCloth = clothRepository.findByPublicId(publicId);
        if(existingCloth != null)
            clothRepository.delete(existingCloth);
        else
            throw new ProductNotFoundServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
    }
}

