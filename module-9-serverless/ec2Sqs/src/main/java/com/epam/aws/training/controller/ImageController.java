package com.epam.aws.training.controller;

import com.epam.aws.training.service.ImageService;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public void uploadImage(@RequestParam("file") MultipartFile multipartFile) {
        imageService.upload(multipartFile);
    }

    @DeleteMapping("/{name}")
    public void delete(@PathVariable String name) {
        imageService.delete(name);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String name) {
        var data = imageService.download(name);
        return ResponseEntity.ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; fileName=\"" + name + "\"")
                .body(new ByteArrayResource(data));
    }

}
