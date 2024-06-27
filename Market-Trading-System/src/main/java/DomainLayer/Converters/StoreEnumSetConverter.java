package DomainLayer.Converters;

import DomainLayer.Market.Util.StoreEnum;
import DomainLayer.Market.Util.StorePermission;
import DomainLayer.Market.Util.StoreRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.*;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class StoreEnumSetConverter implements AttributeConverter<Set<StoreEnum>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<StoreEnum> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        return attribute.stream()
                .map(StoreEnum::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public Set<StoreEnum> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new HashSet<>();
        }
        return Arrays.stream(dbData.split(DELIMITER))
                .map(this::convertToEnum)
                .collect(Collectors.toSet());
    }

    private StoreEnum convertToEnum(String value) {
        try {
            return StoreRole.valueOf(value);
        } catch (IllegalArgumentException e) {
            return StorePermission.valueOf(value);
        }
    }
}