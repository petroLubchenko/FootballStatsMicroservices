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

import java.util.HashMap;
import java.util.Map;

@RestController
public class testController {
    @Autowired
    private Environment env;

    @GetMapping("/inf")
    public String getProps(){
        Map<String, Object> props = new HashMap<>();
        CompositePropertySource bootstrapProps = (CompositePropertySource) ((AbstractEnvironment) env).getPropertySources().get("bootstrapProperties");

        for (String propertName : bootstrapProps.getPropertyNames())
            props.put(propertName, bootstrapProps.getProperty(propertName));

        CompositePropertySource appProps = (CompositePropertySource) ((AbstractEnvironment) env).getPropertySources().get("applicationProperties");

        if (appProps != null)
            for (String propertName : appProps.getPropertyNames())
                props.put(propertName, appProps.getProperty(propertName));

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
