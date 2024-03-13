package av.iisproject.restapi.Rest;

import av.iisproject.restapi.BL.Service.ImageService;
import av.iisproject.restapi.DTO.DTOImageRequest;
import av.iisproject.restapi.DAL.Entity.Image;
import av.iisproject.restapi.DTO.DTOImageResponse;
import av.iisproject.restapi.Mapper.ImageRequestMapper;
import av.iisproject.restapi.Mapper.ImageResponseMapper;
import av.iisproject.restapi.Utils.CustomNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("image")
public class ImageRestController {
    final ImageService imageService;
    final ImageRequestMapper imageRequestMapper;
    final ImageResponseMapper imageResponseMapper;

    public ImageRestController(ImageService imageService, ImageRequestMapper imageRequestMapper, ImageResponseMapper imageResponseMapper) {
        this.imageService = imageService;
        this.imageRequestMapper = imageRequestMapper;
        this.imageResponseMapper = imageResponseMapper;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/allImages")
    public List<DTOImageResponse> findAllImages(){
        return imageService.findAll()
                .stream()
                .map(imageResponseMapper::ImageToDTOImageRes)
                .collect(Collectors.toList());
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{imageId}")
    public DTOImageResponse findImageById(@PathVariable int imageId) {
        Image image = imageService.findById(imageId);
        if (image == null) {
            throw new CustomNotFoundException("Image not found for id: " + imageId);
        }
        return imageResponseMapper.ImageToDTOImageRes(image);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/image")
    public DTOImageResponse createImage(@Valid @RequestBody DTOImageRequest dtoImage) {
        Image newImage = imageRequestMapper.DTOImageReqToImage(dtoImage);
        Image savedImage = imageService.save(newImage);
        return imageResponseMapper.ImageToDTOImageRes(savedImage);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/image/{imageId}")
    public DTOImageResponse updateImage(@PathVariable int imageId, @Valid @RequestBody DTOImageRequest imageRequest){
        Image existingImage = imageService.findById(imageId);
        if (existingImage == null) {
            throw new CustomNotFoundException("Image not found for id: " + imageId);
        }
        existingImage.setContent(imageRequest.getContent());
        Image updatedImage = imageService.save(existingImage);
        return imageResponseMapper.ImageToDTOImageRes(updatedImage);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/image/{imageId}")
    public String deleteImage(@PathVariable int imageId){
        Image imageToDelete = imageService.findById(imageId);
        if (imageToDelete == null){
            throw new CustomNotFoundException("Image id not found - " + imageId);
        }
        imageService.deleteById(imageId);
        return "Deleted image with ID: " + imageId;
    }

}
