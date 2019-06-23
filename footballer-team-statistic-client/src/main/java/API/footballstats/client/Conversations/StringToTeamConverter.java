package API.footballstats.client.Conversations;

import API.footballstats.client.Models.Team;
import org.springframework.core.convert.converter.Converter;

public class StringToTeamConverter implements Converter<String, Team> {


    @Override
    public Team convert(String source) {
        String[] data = source.split(", ");
        String lastnum = (data[4].split("="))[1];
        return new Team(
                Long.parseLong((data[0].split("="))[1]),
                (data[1].split("="))[1],
                (data[3].split("="))[1],
                lastnum.substring(0, lastnum.length() - 2),
                Integer.parseInt((data[2].split("="))[1]));
    }
}
