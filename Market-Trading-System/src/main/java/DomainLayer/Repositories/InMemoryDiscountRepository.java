package DomainLayer.Repositories;

import DomainLayer.Market.Store.Discount.IDiscount;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
@Profile("in-memory")
@Scope("prototype")
public class InMemoryDiscountRepository implements DiscountRepository {
    @Override
    public void flush() {

    }

    @Override
    public <S extends IDiscount> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends IDiscount> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<IDiscount> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public IDiscount getOne(Long aLong) {
        return null;
    }

    @Override
    public IDiscount getById(Long aLong) {
        return null;
    }

    @Override
    public IDiscount getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends IDiscount> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends IDiscount> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends IDiscount> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends IDiscount> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends IDiscount> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends IDiscount> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends IDiscount, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends IDiscount> S save(S entity) {
        return null;
    }

    @Override
    public <S extends IDiscount> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<IDiscount> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<IDiscount> findAll() {
        return List.of();
    }

    @Override
    public List<IDiscount> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(IDiscount entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends IDiscount> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<IDiscount> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<IDiscount> findAll(Pageable pageable) {
        return null;
    }
}
