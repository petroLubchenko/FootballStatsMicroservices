package API.Models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity(name = "championship")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Championship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String abbreviatedname;

    public Championship(){}
    public Championship(long id, String name, String abbreviatedname){
        this.id = id;
        this.name = name;
        this.abbreviatedname = abbreviatedname;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Championship)
            return id == ((Championship) obj).id &&
                    Objects.equals(name, ((Championship) obj).name) &&
                    Objects.equals(abbreviatedname, ((Championship) obj).abbreviatedname);
        return false;
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
}
