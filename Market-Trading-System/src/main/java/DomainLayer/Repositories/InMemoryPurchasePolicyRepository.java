package DomainLayer.Repositories;

import DomainLayer.Market.Store.StorePurchasePolicy.PurchasePolicy;
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
@Scope("prototype")
@Profile("in-memory")
public class InMemoryPurchasePolicyRepository implements PurchasePolicyRepository {

    @Override
    public void flush() {

    }

    @Override
    public <S extends PurchasePolicy> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends PurchasePolicy> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<PurchasePolicy> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public PurchasePolicy getOne(Long aLong) {
        return null;
    }

    @Override
    public PurchasePolicy getById(Long aLong) {
        return null;
    }

    @Override
    public PurchasePolicy getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends PurchasePolicy> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends PurchasePolicy> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends PurchasePolicy> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends PurchasePolicy> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends PurchasePolicy> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends PurchasePolicy> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends PurchasePolicy, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends PurchasePolicy> S save(S entity) {
        return null;
    }

    @Override
    public <S extends PurchasePolicy> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<PurchasePolicy> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<PurchasePolicy> findAll() {
        return List.of();
    }

    @Override
    public List<PurchasePolicy> findAllById(Iterable<Long> longs) {
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
    public void delete(PurchasePolicy entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends PurchasePolicy> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<PurchasePolicy> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<PurchasePolicy> findAll(Pageable pageable) {
        return null;
    }
}
