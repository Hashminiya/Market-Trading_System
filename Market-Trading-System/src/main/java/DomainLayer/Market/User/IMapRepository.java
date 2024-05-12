package DomainLayer.Market.User;

import java.util.List;
import java.util.Map;

public interface IMapRepository<T,K> {
    K getValue(T id);
    boolean containsKey(T key);
    List<K> values();
    Map<T,K> entries();
    void put(T key, K value);
    void remove(T key);
}
