package av.iisproject.restapi.BL.ServiceImplementation;

import av.iisproject.restapi.BL.Service.AuthorityService;
import av.iisproject.restapi.DAL.Entity.Authority;
import av.iisproject.restapi.DAL.Repository.AuthorityRepository;
import av.iisproject.restapi.Utils.CustomNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authRepo;

    public AuthorityServiceImpl(AuthorityRepository authRepo) {
        this.authRepo = authRepo;
    }

    @Override
    public List<Authority> findAll() {
        return authRepo.findAll();
    }

    @Override
    public Authority findById(long id) {
        Optional<Authority> authorityOptional = authRepo.findById(id);

        if (authorityOptional.isEmpty()){
            throw new CustomNotFoundException("Authority id not found - " + id);
        }

        return authorityOptional.get();
    }

    @Override
    @Transactional
    public Authority save(Authority authority) {
        return authRepo.save(authority);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Optional<Authority> checkIfExists = authRepo.findById(id);
        if (checkIfExists.isEmpty()){
            throw new CustomNotFoundException("Authority with that ID was not found: " + id);
        }
        authRepo.deleteById(id);
    }
}
