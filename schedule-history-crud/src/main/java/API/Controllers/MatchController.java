package API.Controllers;

import API.Models.Match;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MatchController {
    @GetMapping("/")
    public List<Match> matches(){

    }
}
