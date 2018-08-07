package com.operatornew.superhero.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@JsonComponent
public class SuperheroSerializer  extends JsonSerializer<Superhero> {

    final private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Override
    public void serialize(Superhero superhero, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", superhero.getName());
        jsonGenerator.writeStringField("pseudonym", superhero.getPseudonym());
        if (superhero.getPublisher() != null) {
            jsonGenerator.writeStringField("publisher", superhero.getPublisher());
        }
        if (superhero.getFirstPublished() != null) {
            jsonGenerator.writeStringField("firstPublished", superhero.getFirstPublished().format(dateFormatter));
        }
        serializerProvider.defaultSerializeField("skills", superhero.getSkills(), jsonGenerator);
//        if (superhero.getSkills() != null) {
//            jsonGenerator.writeFieldName("skills");
//            jsonGenerator.writeStartArray();
//            for (Skill skill : superhero.getSkills()) {
//                jsonGenerator.writeEmbeddedObject(skill);
//            }
//            jsonGenerator.writeEndArray();
//        }
        if (superhero.getAllies() != null) {
            jsonGenerator.writeFieldName("allies");
            jsonGenerator.writeStartArray();
            for (Superhero ally : superhero.getAllies()) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("pseudonym", ally.getPseudonym());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();
        }
        jsonGenerator.writeEndObject();
    }
}
