package DomainLayer.Market.Util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryRepository<K,T extends DataItem<K>> implements IRepository<K,T> {

    protected HashMap<K,T> data = new HashMap<K,T>();


    @Override
    public T findById(K id) {
        return data.getOrDefault(id, null);
    }

    @Override
    public List<T> search(String query) {
        //TODO implement search by name
        throw new UnsupportedOperationException("findByName method is not implemented yet");
    }

    @Override
    public List<T> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void save(T entity) {
        if(!data.containsKey(entity.getId()))
            data.put(entity.getId(),entity);
        //TODO return an error if exist?
    }

    @Override
    public void update(T entity) {
        //TODO return an error if does not exist?
        if(data.containsKey(entity.getId()))
            data.put(entity.getId(),entity);
    }

    @Override
    public void delete(K key) {
        //TODO return an error if does not exist?
        data.remove(key);
    }
}
