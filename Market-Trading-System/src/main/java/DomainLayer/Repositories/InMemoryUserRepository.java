package DomainLayer.Repositories;

import DomainLayer.Market.User.User;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Profile("in-memory")
@Scope("singleton")
public class InMemoryUserRepository implements UserRepository {

    protected Map<String, User> data = new HashMap<>();

    @Override
    public <S extends User> S save(S entity) {
        data.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            data.put(entity.getId(), entity);
            result.add(entity);
        }
        return result;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public boolean existsById(String id) {
        return data.containsKey(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<User> findAllById(Iterable<String> ids) {
        List<User> result = new ArrayList<>();
        for (String id : ids) {
            User user = data.get(id);
            if (user != null) {
                result.add(user);
            }
        }
        return result;
    }

    @Override
    public long count() {
        return data.size();
    }

    @Override
    public void deleteById(String id) {
        data.remove(id);
    }

    @Override
    public void delete(User entity) {
        data.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends String> ids) {
        for (String id : ids) {
            data.remove(id);
        }
    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {
        for (User entity : entities) {
            data.remove(entity.getId());
        }
    }

    @Override
    public void deleteAll() {
        data.clear();
    }

    @Override
    public List<User> findAll(Sort sort) {
        return data.values().stream()
                .sorted((u1, u2) -> {
                    for (Sort.Order order : sort) {
                        int comparison = order.isAscending() ?
                                u1.getUserName().compareTo(u2.getUserName()) :
                                u2.getUserName().compareTo(u1.getUserName());
                        if (comparison != 0) {
                            return comparison;
                        }
                    }
                    return 0;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        List<User> users = findAll();
        int start = Math.min((int) pageable.getOffset(), users.size());
        int end = Math.min(start + pageable.getPageSize(), users.size());
        return new PageImpl<>(users.subList(start, end), pageable, users.size());
    }

    @Override
    public void flush() {
        // Not applicable for in-memory
    }

    @Override
    public <S extends User> S saveAndFlush(S entity) {
        return save(entity);
    }

    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        return saveAll(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<User> entities) {
        deleteAll(entities);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> ids) {
        deleteAllById(ids);
    }

    @Override
    public void deleteAllInBatch() {
        deleteAll();
    }

    @Override
    public User getOne(String id) {
        return data.get(id);
    }

    @Override
    public User getById(String id) {
        return data.get(id);
    }

    @Override
    public User getReferenceById(String id) {
        return data.get(id);
    }

    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        return data.values().stream()
                .filter(user -> matches(example.getProbe(), user, example.getMatcher()))
                .map(user -> (S) user)
                .findFirst();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        return data.values().stream()
                .filter(user -> matches(example.getProbe(), user, example.getMatcher()))
                .map(user -> (S) user)
                .collect(Collectors.toList());
    }


    private <S extends User> boolean matches(S probe, User user, ExampleMatcher matcher) {
        // Implement matching logic here based on ExampleMatcher
        // For simplicity, we can assume all fields must match exactly
        if (!matcher.isIgnoreCaseEnabled()) {
            return probe.getUserName().equals(user.getUserName()) &&
                    probe.getPassword().equals(user.getPassword()) &&
                    probe.getUserAge() == user.getUserAge() &&
                    probe.getUserState().equals(user.getUserState());
        } else {
            // Implement more complex matching logic if needed
            return false;
        }
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll();  // ExampleMatcher provided by Spring

        // Extract the probe (example entity) and configure matcher if needed
        S probe = example.getProbe();
        matcher = matcher.withIgnoreCase(example.getMatcher().isIgnoreCaseEnabled());

        // Implement filtering based on example and sorting based on 'sort'
        ExampleMatcher finalMatcher = matcher;
        return data.values().stream()
                .filter(user -> finalMatcher.getIgnoredPaths().stream().allMatch(path -> {
                    // Implement filtering based on ignored paths in matcher
                    // Example: return user.getName().equals(probe.getName());
                    return true;  // Modify according to your needs
                }))
                .sorted((u1, u2) -> {
                    for (Sort.Order order : sort) {
                        int comparison = order.isAscending() ?
                                u1.getUserName().compareTo(u2.getUserName()) :
                                u2.getUserName().compareTo(u1.getUserName());
                        if (comparison != 0) {
                            return comparison;
                        }
                    }
                    return 0;
                })
                .map(user -> (S) user)
                .collect(Collectors.toList());
    }

    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        List<S> users = findAll(example);
        int start = Math.min((int) pageable.getOffset(), users.size());
        int end = Math.min(start + pageable.getPageSize(), users.size());
        return new PageImpl<>(users.subList(start, end), pageable, users.size());
    }

    @Override
    public <S extends User> long count(Example<S> example) {
        return findAll(example).size();
    }

    @Override
    public <S extends User> boolean exists(Example<S> example) {
        return findOne(example).isPresent();
    }

    @Override
    public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
