package DomainLayer.Market.Util;

import DomainLayer.Market.Util.DataItem;

import java.util.List;

public interface IRepository<K,T extends DataItem> {
    T findById(K id);
    List<T> search(String query);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(K key);
}
