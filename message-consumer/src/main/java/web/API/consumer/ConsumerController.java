package web.API.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ConsumerController {
    @Autowired
    private ConsumedMessageRep rep;
    @GetMapping("/")
    public List<Message> getAll(){
        List<ConsumedMessage> consmesss = (List<ConsumedMessage>)rep.findAll();
        List<Message> messages = new ArrayList<>();
        for (ConsumedMessage cm : consmesss){
            messages.add(new Message(cm.getDescription(), cm.getHttpMethod(), cm.getHttpStatus(), cm.getObject(), cm.getError()));
        }
        return messages;
    }
}
