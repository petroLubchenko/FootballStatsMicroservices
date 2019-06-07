package API.footballstats.client.Models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.persistence.*;
import java.util.List;

public class Championship {
    private long id;
    private String name;
    private String abbreviatedname;

    public Championship(){}
    public Championship(long id, String name, String abbreviatedname){
        this.id = id;
        this.name = name;
        this.abbreviatedname = abbreviatedname;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }


    public String getAbbreviatedname() {
        return abbreviatedname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAbbreviatedname(String abbreviatedname) {
        this.abbreviatedname = abbreviatedname;
    }


    public boolean isValid(boolean checkId){
        if (checkId && id == 0L)
            return false;
        return !(name == null || abbreviatedname == null);
    }

    @Override
    public String toString(){
        String jsonString = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonString = mapper.writeValueAsString(this);
        }
        catch (JsonProcessingException ex){
            ex.printStackTrace();
        }
        return jsonString;
    }

}
