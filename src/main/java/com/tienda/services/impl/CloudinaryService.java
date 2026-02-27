package com.tienda.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String subirImagen(MultipartFile img){
        try{
            Map params = ObjectUtils.asMap(
                    "folder","tienda_productos",
                    "transformation", new Transformation()
                            .width(800).height(800).crop("limit")
                            .quality("auto")
                            .fetchFormat("auto")
            );

            Map uploadResult = cloudinary.uploader().upload(img.getBytes(),params);

            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Error subiendo la imagen a Cloudinary", e);
        }
    }
}
