package API.footballstats.client.Models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

public class Message {
    private String description;
    private HttpMethod httpMethod;
    int httpStatus;
    private String object;
    private String error;

    public Message(){

    }

    public Message(String description, HttpMethod httpMethod, HttpStatus httpStatus, Footballer footballer, String error){
        this.description = description;
        this.httpMethod = httpMethod;
        this.httpStatus = httpStatus.value();
        this.object = footballer == null ? "" : footballer.toString();
        this.error = error;
    }
    public Message(String description, HttpMethod httpMethod, HttpStatus httpStatus, Team team, String error){
        this.description = description;
        this.httpMethod = httpMethod;
        this.httpStatus = httpStatus.value();
        this.object = team == null ? "" : team.toString();
        this.error = error;
    }
    public Message(String description, HttpMethod httpMethod, HttpStatus httpStatus, Championship championship, String error){
        this.description = description;
        this.httpMethod = httpMethod;
        this.httpStatus = httpStatus.value();
        this.object = championship == null ? "" : championship.toString();
        this.error = error;
    }

    public String getObject() {
        return object;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getDescription() {
        return description;
    }

    public String getError() {
        return error;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setObject(String footballer) {
        this.object = footballer;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
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
