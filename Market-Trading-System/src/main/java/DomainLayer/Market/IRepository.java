package DomainLayer.Market;

import java.util.List;

public interface IRepository<T extends DataItem> {
    T findById(long id);
    T findByName(String name);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(T entity);
}
