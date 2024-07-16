package DomainLayer.Repositories;

import DomainLayer.Market.Store.Discount.BaseDiscount;
import DomainLayer.Market.Store.Discount.IDiscount;
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
@Scope("prototype")
public class InMemoryDiscountRepository implements DiscountRepository {
    private final List<BaseDiscount> discounts = new ArrayList<>();

    @Override
    public void flush() {

    }

    @Override
    public <S extends BaseDiscount> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends BaseDiscount> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<BaseDiscount> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public BaseDiscount getOne(Long aLong) {
        return null;
    }

    @Override
    public BaseDiscount getById(Long id) {
        return discounts.stream()
                .filter(discount -> discount.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public BaseDiscount getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends BaseDiscount> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends BaseDiscount> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends BaseDiscount> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends BaseDiscount> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends BaseDiscount> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends BaseDiscount> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends BaseDiscount, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends BaseDiscount> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public List<BaseDiscount> findAll() {
        return discounts;
    }

    @Override
    public List<BaseDiscount> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public BaseDiscount save(BaseDiscount entity) {
        discounts.add(entity);
        return entity;
    }

    @Override
    public Optional<BaseDiscount> findById(Long id) {
        return discounts.stream()
                .filter(discount -> discount.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        return discounts.stream().anyMatch(discount -> discount.getId().equals(id));
    }

    @Override
    public void deleteById(Long id) {
        discounts.removeIf(discount -> discount.getId().equals(id));
    }

    @Override
    public void delete(BaseDiscount entity) {
        discounts.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends BaseDiscount> entities) {

    }

    @Override
    public void deleteAll() {
        discounts.clear();
    }

    @Override
    public List<BaseDiscount> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<BaseDiscount> findAll(Pageable pageable) {
        return null;
    }
}