package API.footballstats.client.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InadmissiblefieldsException extends RuntimeException {
    public InadmissiblefieldsException(String message){
        super (message);
    }
}
