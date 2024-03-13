package av.task01.xmlservice.BL;

import av.task01.xmlservice.DAL.Entity.XMLWorkoutModel;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;

//https://www.baeldung.com/java-validate-xml-xsd
//https://stackoverflow.com/questions/2396903/java-xml-validation-against-xsd-schema

@Service
public class XMLServiceImplementation implements XMLService {

    public XMLServiceImplementation() {
    }

    @Override
    public Validator initValidator(String xsdPath) throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        InputStream xsdInputStream = getClass().getClassLoader().getResourceAsStream(xsdPath);
        if (xsdInputStream == null) {
            throw new IOException("Could not find the XSD file: " + xsdPath);
        }
        Source schemaFile = new StreamSource(xsdInputStream);
        Schema schema = factory.newSchema(schemaFile);
        return schema.newValidator();
    }
    @Override
    public File getFile(String location) {
        return new File(getClass().getClassLoader().getResource(location).getFile());
    }

    @Override
    public boolean isValid(XMLWorkoutModel workout, String xsdPath) throws JAXBException, IOException, SAXException {
        JAXBContext context = JAXBContext.newInstance(XMLWorkoutModel.class);
        Marshaller marshaller = context.createMarshaller();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(workout, baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        Validator validator = initValidator(xsdPath);

        try {
            validator.validate(new StreamSource(bais));
            return true;
        } catch (SAXException e) {
            return false;
        } finally {
            baos.close();
            bais.close();
        }
    }
}
