package DomainLayer.Market.Util;

import java.util.UUID;

public class IdGenerator {
    public static Long generateId() {
        UUID uuid = UUID.randomUUID();
        long mostSignificantBits = uuid.getMostSignificantBits();
        long leastSignificantBits = uuid.getLeastSignificantBits();

        long id = mostSignificantBits ^ leastSignificantBits;

        return Math.abs(id);
    }
}
