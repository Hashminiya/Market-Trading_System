package DomainLayer.Market;

import java.util.List;

public interface IRepository<T> {
    T findById(long id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(T entity);
}
