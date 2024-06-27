package DomainLayer.Repositories;


import DomainLayer.Market.ShoppingBasket;
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
public class InMemoryBasketRepository implements BasketRepository {
    @Override
    public void flush() {

    }

    @Override
    public <S extends ShoppingBasket> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends ShoppingBasket> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<ShoppingBasket> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public ShoppingBasket getOne(Long aLong) {
        return null;
    }

    @Override
    public ShoppingBasket getById(Long aLong) {
        return null;
    }

    @Override
    public ShoppingBasket getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends ShoppingBasket> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ShoppingBasket> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends ShoppingBasket> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends ShoppingBasket> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ShoppingBasket> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ShoppingBasket> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends ShoppingBasket, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends ShoppingBasket> S save(S entity) {
        return null;
    }

    @Override
    public <S extends ShoppingBasket> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<ShoppingBasket> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<ShoppingBasket> findAll() {
        return List.of();
    }

    @Override
    public List<ShoppingBasket> findAllById(Iterable<Long> longs) {
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
    public void delete(ShoppingBasket entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends ShoppingBasket> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<ShoppingBasket> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<ShoppingBasket> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<ShoppingBasket> findByUserName(String userName) {
        return List.of();
    }
}
