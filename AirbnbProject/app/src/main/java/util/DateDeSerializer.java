package util;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sissy on 1/7/2017.
 */

public class DateDeSerializer extends JsonDeserializer<Date> {

    static final SimpleDateFormat format = new SimpleDateFormat(Utils.DATABASE_DATE_FORMAT);

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);
        String strvalue = node.asText();

        Date d = null;
        try {
            d = format.parse(strvalue);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return d;
    }
}
