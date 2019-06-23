package web.API.consumer;

import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;

import javax.naming.Name;
import javax.persistence.*;

@Entity
@Table(name = "Entities")
public class ConsumedMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    @Column(name = "DESCRIPTION", length = 100)
    private String description;
    @Column(name = "method")
    private HttpMethod httpMethod;
    @Column(name = "status")
    int httpStatus;
    @Column(name = "object", length = 2000)
    private String object;
    @Column(name = "error")
    private String error;

    public ConsumedMessage(){

    }
    public ConsumedMessage(String description, HttpMethod method, int httpStatus, String object, String error){
        this.description = description;
        this.httpMethod = method;
        this.httpStatus = httpStatus;
        this.object = object;
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getObject() {
        return object;
    }

    public String getError() {
        return error;
    }
}
