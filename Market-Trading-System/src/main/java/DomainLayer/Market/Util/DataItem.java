package DomainLayer.Market.Util;

import jakarta.persistence.Transient;

public interface DataItem<K> {
    K getId();
    String getName();
}
