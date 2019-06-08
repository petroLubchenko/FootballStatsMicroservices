package API.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class testController {
    @Autowired
    private Environment env;

    @GetMapping("/inf")
    public String getProps(){
        Map<String, Object> props = new HashMap<>();
        try {
            List<String> names = new ArrayList<>();
            ((AbstractEnvironment) env).getPropertySources().iterator().forEachRemaining(p -> names.add(p.getName()));
            for (String s : names){
                CompositePropertySource lprops = (CompositePropertySource) ((AbstractEnvironment) env).getPropertySources().get(s);
                if (lprops != null)
                    for (String propertName : lprops.getPropertyNames())
                        props.put(propertName, lprops.getProperty(propertName));
            }
        }
        catch (Exception e){

        }
        try {
            CompositePropertySource appProps = (CompositePropertySource) ((AbstractEnvironment) env).getPropertySources().get("applicationProperties");
            for (String propertName : appProps.getPropertyNames())
                props.put(propertName, appProps.getProperty(propertName));
        }
        catch (Exception e){

        }


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            return mapper.writeValueAsString(props);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "Something gone wrong!!!";
    }


}
