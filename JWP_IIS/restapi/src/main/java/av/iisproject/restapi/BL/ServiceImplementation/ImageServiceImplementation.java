package av.iisproject.restapi.BL.ServiceImplementation;

import av.iisproject.restapi.BL.Service.ImageService;
import av.iisproject.restapi.DAL.Entity.Image;
import av.iisproject.restapi.DAL.Entity.Workout;
import av.iisproject.restapi.DAL.Repository.ImageRepository;
import av.iisproject.restapi.Utils.CustomNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImplementation implements ImageService {
    private final ImageRepository imageRepository;

    public ImageServiceImplementation(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    @Override
    public Image findById(long id) {
        Optional<Image> imageOptional = imageRepository.findById(id);

        if (imageOptional.isEmpty()){
            throw new CustomNotFoundException("Image id not found - " + id);
        }
        return imageOptional.get();
    }

    @Override
    @Transactional
    public Image save(Image obj) {
        return imageRepository.save(obj);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Optional<Image> checkIfExists = imageRepository.findById(id);
        if (checkIfExists.isEmpty()){
            throw new CustomNotFoundException("Image with that ID was not found: " + id);
        }
        imageRepository.deleteById(id);
    }
}
