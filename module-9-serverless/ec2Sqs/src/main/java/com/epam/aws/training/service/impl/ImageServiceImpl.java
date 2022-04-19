package com.epam.aws.training.service.impl;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.epam.aws.training.config.properties.ImageBucketProperties;
import com.epam.aws.training.controller.ImageController;
import com.epam.aws.training.dto.ImageMetadataDto;
import com.epam.aws.training.mapper.ImageMetadataMapper;
import com.epam.aws.training.repository.ImageMetadataRepository;
import com.epam.aws.training.repository.model.ImageMetadata;
import com.epam.aws.training.service.ImageService;
import com.epam.aws.training.service.NotificationService;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private static final String IMAGE_UPLOAD_ACTION = "Image was uploaded";

    private final TransferManager transferManager;
    private final ImageBucketProperties s3Properties;
    private final AmazonS3 s3Client;
    private final ImageMetadataMapper imageMetadataMapper;
    private final NotificationService notificationService;

    @Override
    public void upload(MultipartFile multipartFile) {
        try {
            var upload = transferManager.upload(s3Properties.getBucketName(),
                    multipartFile.getOriginalFilename(), multipartFile.getInputStream(),
                    new ObjectMetadata());
            upload.waitForUploadResult();
            var imageMetadata = ImageMetadata.builder()
                    .fileExtension(FilenameUtils.getExtension(multipartFile.getOriginalFilename()))
                    .name(multipartFile.getOriginalFilename())
                    .sizeInBytes(multipartFile.getSize())
                    .lastUpdateDate(Instant.now())
                    .build();
            notificationService.sendMessageToQueue(createMessage(imageMetadata));
        } catch (IOException | InterruptedException e) {
            delete(multipartFile.getOriginalFilename());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    private String createMessage(ImageMetadata imageMetadata) {
        var downloadLink = linkTo(methodOn(ImageController.class).download(imageMetadata.getName()));
        return StringUtils.joinWith(":::", IMAGE_UPLOAD_ACTION, imageMetadata.toString(),
                downloadLink);
    }

    @Override
    @Transactional
    public void delete(String name) {
        try {
            s3Client.deleteObject(s3Properties.getBucketName(), name);
        } catch (AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @Override
    public byte[] download(String name) {
        try {
            return s3Client.getObject(s3Properties.getBucketName(), name).getObjectContent()
                    .readAllBytes();
        } catch (IOException | AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
