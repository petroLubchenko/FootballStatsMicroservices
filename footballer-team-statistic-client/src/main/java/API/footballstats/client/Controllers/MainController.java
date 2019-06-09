package API.footballstats.client.Controllers;

import API.footballstats.client.Models.Authorities;
import API.footballstats.client.Models.Message;
import API.footballstats.client.Models.User;
import API.footballstats.client.Security.AuthService;
import API.footballstats.client.Security.RoleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    AuthService authService;

    @Autowired
    RoleService roleService;

    private RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String Index(Model model, String error, String logout){
        return "Index";
    }

    @GetMapping(path = "/login")
    public String login(Model model, String error, String logout){
        if (error != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");
        if (logout != null)
            model.addAttribute("msg", "You have been logged out successfully.");

        return "login";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration_post(@ModelAttribute User user, String role) {
        user.setEnabled(true);
        authService.save(user);
        Authorities a = new Authorities(user.getUsername(),"ROLE_"+role.toUpperCase());
        roleService.save(a);
        return "redirect:/login";
    }

    @GetMapping("/main")
    public String main(){
        return "mainpage";
    }

    @GetMapping("/admin")
    public String userlist(Model model){
        List<Authorities> authorities = roleService.findAll();
        model.addAttribute("users", authorities);
        return "users";
    }

    @GetMapping("/admin/logs")
    public String logs(Model model) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messages = mapper.readValue(mapper.writeValueAsString(restTemplate.getForObject("http://ps:9001/", List.class)), (new ArrayList<Message>()).getClass());
        model.addAttribute("messages", messages);

        return "messages";
    }

}
