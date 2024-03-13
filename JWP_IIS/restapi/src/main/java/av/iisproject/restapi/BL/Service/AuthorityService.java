package av.iisproject.restapi.BL.Service;

import av.iisproject.restapi.DAL.Entity.Authority;

import java.util.List;

public interface AuthorityService {
    List<Authority> findAll();
    Authority findById(long id);
    Authority save(Authority obj);
    void deleteById(long id);
}
