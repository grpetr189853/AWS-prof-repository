package com.epam.aws.training.mapper;

import com.epam.aws.training.dto.ImageMetadataDto;
import com.epam.aws.training.repository.model.ImageMetadata;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ImageMetadataMapper {

  @Mapping(target = "size", source = "sizeInBytes")
  @Named("mapToMetadataDto")
  ImageMetadataDto convertTo(ImageMetadata metadata);

  @IterableMapping(qualifiedByName = "mapToMetadataDto")
  List<ImageMetadataDto> convertTo(List<ImageMetadata> metadataList);
}
