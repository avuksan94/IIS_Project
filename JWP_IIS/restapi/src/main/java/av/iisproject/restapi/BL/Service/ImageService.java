package av.iisproject.restapi.BL.Service;

import av.iisproject.restapi.DAL.Entity.Image;

import java.util.List;

public interface ImageService {
    List<Image> findAll();
    Image findById(long id);
    Image save(Image obj);
    void deleteById(long id);
}
