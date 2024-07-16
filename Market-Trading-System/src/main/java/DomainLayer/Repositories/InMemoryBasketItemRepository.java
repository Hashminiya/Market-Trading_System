package DomainLayer.Repositories;

import DAL.BasketItem;
import DAL.BasketItemId;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Profile("in-memory")
public class InMemoryBasketItemRepository implements BasketItemRepository {

    private final Map<BasketItemId, BasketItem> basketItems = new HashMap<>();

    @Override
    public List<BasketItem> findAll() {
        return new ArrayList<>(basketItems.values());
    }

    @Override
    public List<BasketItem> findAllById(Iterable<BasketItemId> basketItemIds) {
        return List.of();
    }

    @Override
    public Optional<BasketItem> findById(BasketItemId id) {
        return Optional.ofNullable(basketItems.get(id));
    }

    @Override
    public boolean existsById(BasketItemId basketItemId) {
        return false;
    }

    @Override
    public BasketItem save(BasketItem basketItem) {
        basketItems.put(basketItem.getId(), basketItem);
        return basketItem;
    }

    @Override
    public void delete(BasketItem basketItem) {
        basketItems.remove(basketItem.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends BasketItemId> basketItemIds) {

    }

    @Override
    public void deleteAll(Iterable<? extends BasketItem> entities) {

    }

    @Override
    public long count() {
        return basketItems.size();
    }

    @Override
    public void deleteById(BasketItemId basketItemId) {

    }

    @Override
    public void deleteAll() {
        basketItems.clear();
    }

    // Additional methods from JpaRepository can be implemented as needed

    // These methods are not directly needed for the in-memory repository,
    // but they are required by the JpaRepository interface.
    @Override
    public <S extends BasketItem> List<S> saveAll(Iterable<S> entities) {
        return null;
//        for (S entity : entities) {
//            basketItems.put(entity.getId(), entity);
//        }
//        return (List<S>) basketItems.values().stream().filter(entities::iterator::hasNext).collect(Collectors.toList());
    }

    @Override
    public void flush() {
        // No-op for in-memory implementation
    }

    @Override
    public <S extends BasketItem> S saveAndFlush(S entity) {
        return (S) save(entity);
    }

    @Override
    public <S extends BasketItem> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch() {
        deleteAll();
    }

    @Override
    public void deleteAllInBatch(Iterable<BasketItem> entities) {
        for (BasketItem entity : entities) {
            basketItems.remove(entity.getId());
        }
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<BasketItemId> basketItemIds) {

    }

    @Override
    public BasketItem getOne(BasketItemId id) {
        return basketItems.get(id);
    }

    @Override
    public BasketItem getById(BasketItemId id) {
        return basketItems.get(id);
    }

    @Override
    public BasketItem getReferenceById(BasketItemId basketItemId) {
        return null;
    }

    @Override
    public <S extends BasketItem> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends BasketItem> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends BasketItem> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends BasketItem> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends BasketItem> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends BasketItem> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends BasketItem, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<BasketItem> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<BasketItem> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void deleteByBasketId(long basketId) {
        List<BasketItemId> keysToRemove = basketItems.keySet().stream()
                .filter(key -> key.getBasketId().equals(basketId))
                .toList();
        for (BasketItemId key : keysToRemove) {
            basketItems.remove(key);
        }
    }
}
