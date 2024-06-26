
//package DomainLayer.Market.Util;
//
//import com.fasterxml.jackson.annotation.JsonTypeInfo;
//import org.springframework.context.annotation.Scope;
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.repository.query.FluentQuery;
//import org.springframework.stereotype.Repository;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Optional;
//import java.util.function.Function;
//
//
//@Repository("InMemoryRepository")
//@Scope("prototype")
//public class InMemoryRepository<K,T extends DataItem<K>> implements IRepository<K,T> {
//
//    protected HashMap<K,T> data = new HashMap<K,T>();
//
//
////    @Override
////    public T findById(K id) {
////        return data.getOrDefault(id, null);
////    }
//
//    @Override
//    public List<T> search(String query) {
//        //TODO implement search by name
//        throw new UnsupportedOperationException("findByName method is not implemented yet");
//    }
//
//    @Override
//    public <S extends T> S save(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends T> List<S> saveAll(Iterable<S> entities) {
//        return List.of();
//    }
//
//    @Override
//    public Optional<T> findById(K k) {
//        return Optional.empty();
//    }
//
//    @Override
//    public boolean existsById(K k) {
//        return false;
//    }
//
//    @Override
//    public List<T> findAll() {
//        return data.values().stream().toList();
//    }
//
//    @Override
//    public List<T> findAllById(Iterable<K> ks) {
//        return List.of();
//    }
//
//    @Override
//    public long count() {
//        return 0;
//    }
//
//    @Override
//    public void deleteById(K k) {
//
//    }
//
//    @Override
//    public void delete(T entity) {
//
//    }
//
//    @Override
//    public void deleteAllById(Iterable<? extends K> ks) {
//
//    }
//
//    @Override
//    public void deleteAll(Iterable<? extends T> entities) {
//
//    }
//
//    @Override
//    public void deleteAll() {
//
//    }
//
//    @Override
//    public void flush() {
//
//    }
//
//    @Override
//    public <S extends T> S saveAndFlush(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends T> List<S> saveAllAndFlush(Iterable<S> entities) {
//        return List.of();
//    }
//
//    @Override
//    public void deleteAllInBatch(Iterable<T> entities) {
//
//    }
//
//    @Override
//    public void deleteAllByIdInBatch(Iterable<K> ks) {
//
//    }
//
//    @Override
//    public void deleteAllInBatch() {
//
//    }
//
//    @Override
//    public T getOne(K k) {
//        return null;
//    }
//
//    @Override
//    public T getById(K k) {
//        return null;
//    }
//
//    @Override
//    public T getReferenceById(K k) {
//        return null;
//    }
//
//    @Override
//    public <S extends T> Optional<S> findOne(Example<S> example) {
//        return Optional.empty();
//    }
//
//    @Override
//    public <S extends T> List<S> findAll(Example<S> example) {
//        return List.of();
//    }
//
//    @Override
//    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
//        return List.of();
//    }
//
//    @Override
//    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public <S extends T> long count(Example<S> example) {
//        return 0;
//    }
//
//    @Override
//    public <S extends T> boolean exists(Example<S> example) {
//        return false;
//    }
//
//    @Override
//    public <S extends T, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
//        return null;
//    }
//
//    @Override
//    public List<T> findAll(Sort sort) {
//        return List.of();
//    }
//
//    @Override
//    public Page<T> findAll(Pageable pageable) {
//        return null;
//    }
//
////    @Override
////    public void save(T entity) {
////        if(!data.containsKey(entity.getId()))
////            data.put(entity.getId(),entity);
////        //TODO return an error if exist?
////    }
//
////    @Override
////    public void update(T entity) {
////        //TODO return an error if does not exist?
////        if(data.containsKey(entity.getId()))
////            data.put(entity.getId(),entity);
////    }
//
////    @Override
////    public void delete(K key) {
////        //TODO return an error if does not exist?
////        data.remove(key);
////    }
//}
