package com.epam.aws.training.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

  void upload(MultipartFile file);

  void delete(String name);

  byte[] download(String name);

}
