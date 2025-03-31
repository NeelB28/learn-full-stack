package com.dailycodework.dreamshops.service.image;

import com.dailycodework.dreamshops.dto.ImageDto;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.ImageRepository;
import com.dailycodework.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    @Autowired
    private final ImageRepository imageRepository;
    @Autowired
    private final IProductService productService;

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> file, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        for(MultipartFile multipartFile : file){
            try {
                Image image = new Image();
                image.setFileName(multipartFile.getOriginalFilename());
                image.setFileType(multipartFile.getContentType());
                image.setImage(new SerialBlob(multipartFile.getBytes()));
                image.setProduct(product);
                
                // First save without download URL
                Image savedImage = imageRepository.save(image);
                
                // Set download URL after we have an ID
                String buildDownloadUrl = "/api/v1/images/image/download/";
                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);

            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }


    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Image with id: " + id + " not found")
        );
    }

    @Override
    public Image updateImage(MultipartFile file, Long imageId) {
        return imageRepository.findById(imageId).map(
                oldImage -> {
                    try {
                        oldImage.setFileName(file.getOriginalFilename());
                        oldImage.setFileType(file.getContentType());
                        oldImage.setImage(new SerialBlob(file.getBytes()));
                        return imageRepository.save(oldImage);
                    } catch (SQLException e) {
                        throw new RuntimeException(e.getMessage());
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
        ).orElseThrow(
                ()-> new ResourceNotFoundException("Image with id: " + imageId + " not found")
        );
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository :: delete,
                ()-> {
                    throw new ResourceNotFoundException("Image with id: " + id + " not found");
                }
        );
    }
}
