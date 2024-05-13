package DomainLayer.Market;

import java.util.List;

public interface IRepository<K,T extends DataItem> {
    T findById(K id);
    T findByName(String name);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(K key);
}
