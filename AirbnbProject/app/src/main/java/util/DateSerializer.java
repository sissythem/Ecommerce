package util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sissy on 1/7/2017.
 */

public class DateSerializer extends JsonSerializer<Date>{
    static final SimpleDateFormat format = new SimpleDateFormat(Utils.DATABASE_DATE_FORMAT);


    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeString(format.format(value));
    }
}
