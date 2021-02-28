import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class TestUtil {

    public static String writeObjectAsJsonString(Object object) {
        ObjectWriter objectWriter = new ObjectMapper().writerFor(object.getClass());
        try {
            return objectWriter.writeValueAsString(object);
        } catch (
                JsonProcessingException e) {
            throw new RuntimeException("Could not write object as JSON String.");
        }
    }
}
