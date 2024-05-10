package Market.DomainLayer.User;

import Market.IRepository;

import java.util.HashMap;
import java.util.List;

public class UsersRepository implements IRepository<User> {

    HashMap<Long,User> users = new HashMap<Long,User>();

    @Override
    public User findById(long id) {
        return users.getOrDefault(id, null);
    }

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public void save(User entity) {
        if(!users.containsKey(entity.id))
            users.put(entity.id,entity);
        //TODO return an error if exist?
    }

    @Override
    public void update(User entity) {
        //TODO return an error if does not exist?
        if(users.containsKey(entity.id))
            users.put(entity.id,entity);
    }

    @Override
    public void delete(User entity) {
        //TODO return an error if does not exist?
        users.remove(entity.id);
    }
}
