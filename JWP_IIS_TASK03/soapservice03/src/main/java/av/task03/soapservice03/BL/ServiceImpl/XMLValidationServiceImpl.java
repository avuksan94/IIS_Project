package av.task03.soapservice03.BL.ServiceImpl;

import av.task03.soapservice03.SOAP.XMLValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.net.URL;

@Service
public class XMLValidationServiceImpl implements XMLValidationService {
    private static final Logger log = LoggerFactory.getLogger(WorkoutServiceImpl.class);
    public XMLValidationServiceImpl() {
    }

    @Override
    public Validator initValidator(String xsdFileName) throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        InputStream xsdInputStream = getClass().getClassLoader().getResourceAsStream(xsdFileName);
        if (xsdInputStream == null) {
            throw new IOException("Could not find the XSD file in the classpath: " + xsdFileName);
        }
        Source schemaFile = new StreamSource(xsdInputStream);
        Schema schema = factory.newSchema(schemaFile);
        return schema.newValidator();
    }


    @Override
    public File getFile(String location) {
        URL resourceUrl = getClass().getClassLoader().getResource(location);
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Could not find the file: " + location);
        }
        return new File(resourceUrl.getFile());
    }

    @Override
    public boolean isValid(String xmlFilePath, String xsdPath) throws IOException, SAXException {
        File xmlFile = new File(xmlFilePath);
        Validator validator = initValidator(xsdPath);

        try {
            validator.validate(new StreamSource(xmlFile));
            return true;
        } catch (SAXException e) {
            log.error("Validation error", e);
            return false;
        }
    }


}
