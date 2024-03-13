package av.task01.xmlservice.BL;

import av.task01.xmlservice.DAL.Entity.XMLWorkoutModel;
import jakarta.xml.bind.JAXBException;
import org.xml.sax.SAXException;

import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public interface XMLService {
    Validator initValidator(String xsdPath) throws SAXException, IOException;
    File getFile(String location);
    boolean isValid(XMLWorkoutModel workout, String xsdPath) throws JAXBException, IOException, SAXException;
}
