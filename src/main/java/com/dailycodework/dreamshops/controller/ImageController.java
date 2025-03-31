package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.dto.ImageDto;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {

    // Inject the image service
    // why autowired is not used here?
    // because we are using constructor injections
    @Autowired
    private final IImageService imageService;

    // Add image to the product
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
        try {
            List<ImageDto> imageDtoList = imageService.saveImages(files, productId);
            // in api response we are sending the message and the list of images where param object is list of images
            // here object is a generic type so it can be any type, imagine in js where we create an object and
            // we can add any type of value to it be it only one type or multiple types or one value or multiple values
            return ResponseEntity.ok(new ApiResponse("Images added successfully", imageDtoList));
            // we are using API response for errors too as it is a generic class and we can pass any type of object to it
            // and common mean to respond to our client from our server/application
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed", e.getMessage()));
        }
    }

    // Download image
    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        // Create a byte array resource from the image and used a constructor that takes a byte array
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                .body(resource);
    }

    // Update image
    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestParam MultipartFile file) {
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                Image updatedImage = imageService.updateImage(file, imageId);
                return ResponseEntity.ok(new ApiResponse("Image updated successfully", updatedImage));
            }
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update Failed", INTERNAL_SERVER_ERROR));
    }

    // Delete image
    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try{
            return Optional.ofNullable(imageService.getImageById(imageId))
                    .map(
                            image -> {
                                imageService.deleteImageById(imageId);
                                return ResponseEntity.ok(new ApiResponse("Image deleted successfully", null));
                            }
                    ).orElseGet(
                            ()-> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Image not found", NOT_FOUND)));

        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Deletion Failed", e.getMessage()));
        }
    }
}
