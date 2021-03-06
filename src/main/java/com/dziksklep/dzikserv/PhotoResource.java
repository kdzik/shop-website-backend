package com.dziksklep.dzikserv;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

@RestController
@RequestMapping("/rest")
@CrossOrigin(origins="http://localhost:4200", maxAge = 3600)
public class PhotoResource {

    private String imageName;
    @Value("${images.resource}")
    private String imagesDir;

    @Autowired
    private ImageRepository imageRespository;

    @PostMapping("/photo/upload")
    public String upload(@RequestParam("myFile") MultipartFile file){
        String fileName = file.getOriginalFilename();

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        String randomName = generatedString+fileName;

        String path = new File(imagesDir)+"/"+randomName;
        imageName = randomName;
        try {
            file.transferTo(new File(path));
            System.out.println(path);
        } catch (IOException e){
            e.printStackTrace();
        }
        return "Upload successful";
    }

    @PostMapping("/photo/add")
    public ImageModel addPhoto(@RequestBody ImageModel photo){
        photo.setPhotoName(imageName);
        return imageRespository.save(photo);
    }

    @PutMapping("/photo/update")
    public ImageModel updatePhoto(@RequestBody ImageModel photo){
        return imageRespository.save(photo);
    }

    @GetMapping("/photo/product")
    public ImageModel getPhoto(@RequestBody Product product){
        return imageRespository.findByProduct(product);
    }
}
