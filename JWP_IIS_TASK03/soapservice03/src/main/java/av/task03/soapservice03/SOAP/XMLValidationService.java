package av.task03.soapservice03.SOAP;

import org.xml.sax.SAXException;

import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public interface XMLValidationService {
    Validator initValidator(String xsdPath) throws SAXException, IOException;
    File getFile(String location);
    public boolean isValid(String xmlFilePath, String xsdPath) throws IOException, SAXException;
}
