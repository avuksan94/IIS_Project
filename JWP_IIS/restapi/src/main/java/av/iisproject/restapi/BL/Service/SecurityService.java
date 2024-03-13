package av.iisproject.restapi.BL.Service;

public interface SecurityService {
    String doBCryptPassEncoding(String plainTextPassword);
}