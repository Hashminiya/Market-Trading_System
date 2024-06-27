package DomainLayer.Converters;

import DomainLayer.Market.Util.StoreEnum;
import DomainLayer.Market.Util.StorePermission;
import DomainLayer.Market.Util.StoreRole;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StoreEnumConverter implements AttributeConverter<StoreEnum, String> {

    @Override
    public String convertToDatabaseColumn(StoreEnum attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getClass().getSimpleName() + "." + attribute.name();
    }

    @Override
    public StoreEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        String[] parts = dbData.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid StoreEnum value: " + dbData);
        }
        if ("StoreRole".equals(parts[0])) {
            return StoreRole.valueOf(parts[1]);
        } else if ("StorePermission".equals(parts[0])) {
            return StorePermission.valueOf(parts[1]);
        }
        throw new IllegalArgumentException("Unknown StoreEnum type: " + parts[0]);
    }
}