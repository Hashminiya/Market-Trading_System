package DomainLayer.Repositories;

import DAL.ItemDTO;
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
public class InMemoryItemDTORepository implements ItemDTORepository {

    private final List<ItemDTO> items = new ArrayList<>();

    @Override
    public <S extends ItemDTO> S save(S entity) {
        items.add(entity);
        return entity;
    }


    @Override
    public <S extends ItemDTO> List<S> saveAll(Iterable<S> entities) {
        entities.forEach(items::add);
        return (List<S>) entities;
    }

    @Override
    public Optional<ItemDTO> findById(Long aLong) {
        return items.stream()
                .filter(itemDTO -> itemDTO.getId().equals(aLong))
                .findFirst();
    }

    @Override
    public boolean existsById(Long aLong) {
        return items.stream().anyMatch(itemDTO -> itemDTO.getId().equals(aLong));
    }

    @Override
    public List<ItemDTO> findAll() {
        return new ArrayList<>(items);
    }

    @Override
    public List<ItemDTO> findAllById(Iterable<Long> longs) {
        return items.stream()
                .filter(itemDTO -> containsId(longs, itemDTO.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return items.size();
    }

    @Override
    public void deleteById(Long aLong) {
        items.removeIf(itemDTO -> itemDTO.getId().equals(aLong));
    }

    @Override
    public void delete(ItemDTO entity) {
        items.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        longs.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends ItemDTO> entities) {
        entities.forEach(items::remove);
    }

    @Override
    public void deleteAll() {
        items.clear();
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
    public <S extends ItemDTO> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends ItemDTO> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<ItemDTO> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public ItemDTO getOne(Long aLong) {
        return null;
    }

    @Override
    public ItemDTO getById(Long aLong) {
        return null;
    }

    @Override
    public ItemDTO getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends ItemDTO> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ItemDTO> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends ItemDTO> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends ItemDTO> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ItemDTO> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ItemDTO> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends ItemDTO, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<ItemDTO> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<ItemDTO> findAll(Pageable pageable) {
        return null;
    }
}
