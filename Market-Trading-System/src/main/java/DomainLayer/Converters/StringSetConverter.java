package DomainLayer.Converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class StringSetConverter implements AttributeConverter<Set<String>, String> {

    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(Set<String> stringSet) {
        if (stringSet == null || stringSet.isEmpty()) {
            return "";
        }
        return String.join(SPLIT_CHAR, stringSet);
    }

    @Override
    public Set<String> convertToEntityAttribute(String string) {
        if (string == null || string.trim().isEmpty()) {
            return new HashSet<>();
        }
        return Arrays.stream(string.split(SPLIT_CHAR))
                .collect(Collectors.toSet());
    }
}
