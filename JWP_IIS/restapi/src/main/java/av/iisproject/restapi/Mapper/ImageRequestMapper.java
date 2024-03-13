package av.iisproject.restapi.Mapper;

import av.iisproject.restapi.DTO.DTOImageRequest;
import av.iisproject.restapi.DAL.Entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageRequestMapper {
    @Mapping(target = "content", source = "content")
    DTOImageRequest ImageToDTOImageReq(Image source);
    @Mapping(target = "content", source = "content")
    Image DTOImageReqToImage(DTOImageRequest destination);
}
