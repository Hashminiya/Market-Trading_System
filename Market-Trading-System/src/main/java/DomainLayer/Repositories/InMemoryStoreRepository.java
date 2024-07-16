package DomainLayer.Repositories;

import DomainLayer.Market.Store.Store;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
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

@Repository
@Profile("in-memory")
@Scope("singleton")
public class InMemoryStoreRepository implements StoreRepository {

    private List<Store> stores = new ArrayList<>();

    @Override
    public Store getById(Long id) {
        return stores.stream()
                .filter(store -> store.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public <S extends Store> List<S> findAll(Example<S> example) {
        // Since it's in-memory, not implementing Example querying
        return List.of();
    }

    @Override
    public List<Store> findAll() {
        return stores;
    }

    @Override
    public <S extends Store> S save(S entity) {
        stores.add(entity);
        return entity;
    }

    @Override
    public <S extends Store> List<S> saveAll(Iterable<S> entities) {
        List<S> savedEntities = new ArrayList<>();
        entities.forEach(entity -> {
            stores.add(entity);
            savedEntities.add(entity);
        });
        return savedEntities;
    }

    @Override
    public Optional<Store> findById(Long id) {
        return stores.stream()
                .filter(store -> store.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        return stores.stream().anyMatch(store -> store.getId().equals(id));
    }

    @Override
    public void deleteById(Long id) {
        stores.removeIf(store -> store.getId().equals(id));
    }

    @Override
    public void delete(Store entity) {
        stores.remove(entity);
    }

    @Override
    public void deleteAll() {
        stores.clear();
    }

    // these down function don't unusable..

    @Override
    public void flush() {
        // not in use
    }

    @Override
    public <S extends Store> S saveAndFlush(S entity) {
        // not in use
        return null;
    }

    @Override
    public <S extends Store> List<S> saveAllAndFlush(Iterable<S> entities) {
        // not in use
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Store> entities) {
        // not in use

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {
        // not in use

    }

    @Override
    public void deleteAllInBatch() {
        // not in use

    }

    @Override
    public Store getOne(Long aLong) {
        // not in use
        return null;
    }

    @Override
    public Store getReferenceById(Long aLong) {
        // not in use
        return null;
    }

    @Override
    public <S extends Store> Optional<S> findOne(Example<S> example) {
        // not in use
        return Optional.empty();
    }

    @Override
    public <S extends Store> List<S> findAll(Example<S> example, Sort sort) {
        // not in use
        return List.of();
    }

    @Override
    public <S extends Store> Page<S> findAll(Example<S> example, Pageable pageable) {
        // not in use
        return null;
    }

    @Override
    public <S extends Store> long count(Example<S> example) {
        // not in use
        return 0;
    }

    @Override
    public <S extends Store> boolean exists(Example<S> example) {
        // not in use
        return false;
    }

    @Override
    public <S extends Store, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        // not in use
        return null;
    }

    @Override
    public List<Store> findAllById(Iterable<Long> longs) {
        // not in use
        return List.of();
    }

    @Override
    public long count() {
        // not in use
        return 0;
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        // not in use

    }
    @Override
    public void deleteAll(Iterable<? extends Store> entities) {
        // not in use

    }

    @Override
    public List<Store> findAll(Sort sort) {
        return List.of();
        // not in use
    }

    @Override
    public Page<Store> findAll(Pageable pageable) {
        return null;
        // not in use
    }
}