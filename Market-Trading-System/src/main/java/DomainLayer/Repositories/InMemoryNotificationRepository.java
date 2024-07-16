package DomainLayer.Repositories;

import DomainLayer.Market.Notifications.Notification;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Profile("in-memory")
public class InMemoryNotificationRepository implements NotificationRepository {

    private final List<Notification> notifications = new ArrayList<>();

    @Override
    public List<Notification> findByRecipient(String userName) {
        return notifications.stream()
                .filter(notification -> notification.getRecipient().equals(userName))
                .collect(Collectors.toList());
    }

    @Override
    public <S extends Notification> S save(S entity) {
        notifications.add(entity);
        return entity;
    }

    @Override
    public <S extends Notification> List<S> saveAll(Iterable<S> entities) {
        entities.forEach(notifications::add);
        return (List<S>) entities;
    }

    @Override
    public Optional<Notification> findById(Long aLong) {
        return notifications.stream()
                .filter(notification -> notification.getId().equals(aLong))
                .findFirst();
    }

    @Override
    public boolean existsById(Long aLong) {
        return notifications.stream().anyMatch(notification -> notification.getId().equals(aLong));
    }

    @Override
    public List<Notification> findAll() {
        return new ArrayList<>(notifications);
    }

    @Override
    public List<Notification> findAllById(Iterable<Long> longs) {
        return notifications.stream()
                .filter(notification -> containsId(longs, notification.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return notifications.size();
    }

    @Override
    public void deleteById(Long aLong) {
        notifications.removeIf(notification -> notification.getId().equals(aLong));
    }

    @Override
    public void delete(Notification entity) {
        notifications.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        longs.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends Notification> entities) {
        entities.forEach(notifications::remove);
    }

    @Override
    public void deleteAll() {
        notifications.clear();
    }

    private boolean containsId(Iterable<Long> iterable, Long id) {
        for (Long itemId : iterable) {
            if (itemId.equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Notification> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Notification> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Notification> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Notification getOne(Long aLong) {
        return null;
    }

    @Override
    public Notification getById(Long aLong) {
        return null;
    }

    @Override
    public Notification getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Notification> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Notification> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Notification> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Notification> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Notification> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Notification> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Notification, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<Notification> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Notification> findAll(Pageable pageable) {
        return null;
    }
}
