package av.iisproject.restapi.Mapper;

import av.iisproject.restapi.DAL.Entity.Image;
import av.iisproject.restapi.DTO.DTOImageRequest;
import av.iisproject.restapi.DTO.DTOImageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageResponseMapper {
    @Mapping(target = "imageId", source = "imageId")
    @Mapping(target = "content", source = "content")
    DTOImageResponse ImageToDTOImageRes(Image source);
    @Mapping(target = "imageId", source = "imageId")
    @Mapping(target = "content", source = "content")
    Image DTOImageResToImage(DTOImageResponse destination);
}
