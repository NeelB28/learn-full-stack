package com.dailycodework.dreamshops.service.image;

import com.dailycodework.dreamshops.dto.ImageDto;
import com.dailycodework.dreamshops.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    List<ImageDto> saveImages(List<MultipartFile> file, Long productId);
    Image getImageById(Long id);
    Image updateImage(MultipartFile file, Long imageId);
    void deleteImageById(Long id);
}
