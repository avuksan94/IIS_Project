package av.task02.xmlservice02.BL;

import com.thaiopensource.validate.SchemaReader;
import com.thaiopensource.validate.ValidationDriver;
import com.thaiopensource.validate.auto.AutoSchemaReader;
import lombok.Getter;
import lombok.Setter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.thaiopensource.validate.ValidationDriver;
import jakarta.xml.bind.JAXBException;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;

@Service
public class RelaxNGValidationServiceImpl implements RelaxNGValidationService {

    private String errorMessage;

    @Override
    public boolean validate(File xmlFile, String rngSchemaPath) {
        try {
            SchemaReader reader = new AutoSchemaReader();
            ValidationDriver driver = new ValidationDriver(reader);

            InputSource rngInput = ValidationDriver.fileInputSource(new File(rngSchemaPath));
            if (!driver.loadSchema(rngInput)) {
                throw new Exception("Failed to load RelaxNG schema.");
            }

            InputSource xmlInput = ValidationDriver.fileInputSource(xmlFile);
            boolean fileValid = driver.validate(xmlInput);

            if (fileValid) {
                return true;
            } else {
                return false;
            }
        } catch (IOException | SAXException e) {
            setErrorMessage("Validation error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
