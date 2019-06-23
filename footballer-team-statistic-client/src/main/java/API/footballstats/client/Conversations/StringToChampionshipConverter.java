package API.footballstats.client.Conversations;

import API.footballstats.client.Models.Championship;
import org.springframework.core.convert.converter.Converter;

public class StringToChampionshipConverter implements Converter<String, Championship> {
    @Override
    public Championship convert(String source) {
        String[] data = source.split(", ");
        String lasttext = (data[2].split("="))[1];
        return new Championship(
                Long.parseLong((data[0].split("="))[1]),
                (data[1].split("="))[1],
                lasttext
                );
    }
}
