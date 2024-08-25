package com.nibm.product_service.controller;

import com.nibm.product_service.service.ClothService;
import com.nibm.product_service.shared.dto.ClothDTO;
import com.nibm.product_service.shared.model.request.ClothRequestModel;
import com.nibm.product_service.shared.model.response.ClothRest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ClothController {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ClothService clothService;

    @PostMapping("/img")
    public void insertCloth(@RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("images") List<MultipartFile> files) throws Exception {

        ClothRequestModel clothRequestModel = new ClothRequestModel();
        clothRequestModel.setDescription(description);
        clothRequestModel.setName(name);

        clothService.saveCloth(clothRequestModel, files);

    }

    @PutMapping("/{publicId}")
    public void updateCloth(@PathVariable String publicId, @RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("images") List<MultipartFile> files) throws Exception {

        ClothRequestModel clothRequestModel = new ClothRequestModel();
        clothRequestModel.setDescription(description);
        clothRequestModel.setName(name);
        clothService.saveCloth(publicId,clothRequestModel, files);

    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Resource resource = clothService.loadImageAsResource(imageName);

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


    @GetMapping("/{publicId}")
    public ClothRest getCloth(@PathVariable String publicId){
        ClothDTO cloth = clothService.getCloth(publicId);
        return modelMapper.map(cloth, ClothRest.class);
    }

    @DeleteMapping("/{publicId}")
    public void deleteMapping(@PathVariable String publicId) throws Exception {
        clothService.deleteCloth(publicId);
    }

    @GetMapping
    public List<ClothDTO> getAllCloths(){
         return clothService.getCloths();
    }
}
