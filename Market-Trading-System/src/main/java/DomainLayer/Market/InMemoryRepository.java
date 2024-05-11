package DomainLayer.Market;

import java.util.HashMap;
import java.util.List;

public class InMemoryRepository<T extends DataItem> implements IRepository<T> {

    HashMap<Long,T> data = new HashMap<Long,T>();

    @Override
    public T findById(long id) {
        return data.getOrDefault(id, null);
    }

    @Override
    public T findByName(String name) {
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
    public void delete(T entity) {
        //TODO return an error if does not exist?
        data.remove(entity.getId());
    }
}
