package av.iisproject.restapi.DAL.Repository;

import av.iisproject.restapi.DAL.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
}
