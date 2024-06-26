package DomainLayer.Repositories;

import DomainLayer.Market.Purchase.Purchase;
import org.springframework.context.annotation.Profile;
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
public class InMemoryPurchaseRepository implements PurchaseRepository {
    @Override
    public void flush() {

    }

    @Override
    public <S extends Purchase> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Purchase> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Purchase> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Purchase getOne(Long aLong) {
        return null;
    }

    @Override
    public Purchase getById(Long aLong) {
        return null;
    }

    @Override
    public Purchase getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Purchase> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Purchase> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Purchase> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Purchase> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Purchase> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Purchase> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Purchase, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Purchase> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Purchase> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Purchase> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Purchase> findAll() {
        return List.of();
    }

    @Override
    public List<Purchase> findAllById(Iterable<Long> longs) {
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
    public void delete(Purchase entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Purchase> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Purchase> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Purchase> findAll(Pageable pageable) {
        return null;
    }
}
