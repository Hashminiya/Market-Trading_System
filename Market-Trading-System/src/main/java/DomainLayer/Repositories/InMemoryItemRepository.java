package DomainLayer.Repositories;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import DomainLayer.Market.Store.Item;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

@Repository
@Profile("in-memory")
public class InMemoryItemRepository implements ItemRepository {

    private ConcurrentHashMap<Long, Item> storage = new ConcurrentHashMap<>();
    private AtomicLong idCounter = new AtomicLong();


    @Override
    public List<String> findCategoriesByItemId(Long itemId) {
        return storage.values().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .map(Item::getCategories)
                .orElse(List.of());
    }

    @Override
    public List<String> findAllCategoriesByStoreId(long storeId) {
        return storage.values().stream()
                .filter(item -> item.getStoreId() == storeId)
                .flatMap(item -> item.getCategories().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findByStoreIdAndNameContaining(long storeId, String name) {
        return storage.values().stream()
                .filter(item -> item.getStoreId() == storeId && item.getName().contains(name))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    @Override
    public Item save(Item entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }

    //@Override
    public List<String> findAllCategories() {
        Set<String> categories = new HashSet<>();
        for (Item item : storage.values()) {
            categories.addAll(item.getCategories());
        }
        return List.copyOf(categories); // Assuming Java 10+
    }

    @Override
    public <S extends Item> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Item> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public void delete(Item entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Item> entities) {

    }

    @Override
    public void deleteAll() {

    }

    //@Override
    public List<Item> findByName(String name) {
        List<Item> result = new ArrayList<>();
        for (Item item : storage.values()) {
            if (item.getName().equals(name)) {
                result.add(item);
            }
        }
        return result;
    }

    //@Override
    public List<Item> findByPriceGreaterThan(double price) {
        return List.of();
    }

    //@Override
    public List<Item> findByNameContaining(String keyword) {
        return List.of();
    }

    //@Override
    public List<Item> findByCategoriesIn(List<String> categories) {
        return List.of();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Item> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Item> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Item> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Item getOne(Long aLong) {
        return null;
    }

    @Override
    public Item getById(Long aLong) {
        return null;
    }

    @Override
    public Item getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Item> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Item> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Item> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Item> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Item> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Item> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Item, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public Optional<Item> findOne(Specification<Item> spec) {
        return Optional.empty();
    }

    @Override
    public List<Item> findAll(Specification<Item> spec) {
        return List.of();
    }

    @Override
    public Page<Item> findAll(Specification<Item> spec, Pageable pageable) {
        return null;
    }

    @Override
    public List<Item> findAll(Specification<Item> spec, Sort sort) {
        return List.of();
    }

    @Override
    public long count(Specification<Item> spec) {
        return 0;
    }

    @Override
    public boolean exists(Specification<Item> spec) {
        return false;
    }

    @Override
    public long delete(Specification<Item> spec) {
        return 0;
    }

    @Override
    public <S extends Item, R> R findBy(Specification<Item> spec, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<Item> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Item> findAll(Pageable pageable) {
        return null;
    }

    //get list of items by store id
    // InMemoryItemRepository.java
    @Override
    public List<Item> findAllByStoreId(Long storeId) {
        List<Item> itemsByStoreId = new ArrayList<>();
        for (Item item : storage.values()) {
            if (Long.valueOf(item.getStoreId()).equals(storeId)) {
                itemsByStoreId.add(item);
            }
        }
        return itemsByStoreId;
    }


}