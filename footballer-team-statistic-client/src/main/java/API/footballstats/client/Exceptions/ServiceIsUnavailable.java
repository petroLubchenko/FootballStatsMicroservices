package API.footballstats.client.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceIsUnavailable extends RuntimeException {
    public ServiceIsUnavailable(String message){
        super(message);
    }
}
