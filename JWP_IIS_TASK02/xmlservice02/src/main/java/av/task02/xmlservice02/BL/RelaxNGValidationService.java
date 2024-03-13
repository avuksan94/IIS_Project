package av.task02.xmlservice02.BL;

import av.task02.xmlservice02.DAL.Entity.XMLWorkoutModel;

import java.io.File;

public interface RelaxNGValidationService {
    boolean validate(File xmlFile, String rngSchemaPath);
}
