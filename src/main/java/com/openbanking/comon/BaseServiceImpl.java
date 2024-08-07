package com.openbanking.comon;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class BaseServiceImpl<E extends BaseEntity, D, CD, UD, ID> implements BaseService<D, CD, UD, ID> {
    private final BaseRepository<E, ID> repository;
    private final BaseMapper<E, D, CD, UD> mapper;

    @Override
    public D create(CD dto) {
        E entity = mapper.toEntityFromCD(dto);
        E savedEntity = repository.save(entity);
        return mapper.toDTO(savedEntity);
    }

    @Override
    public D update(ID id, D dto) {
        E entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found"));
        mapper.updateEntityFromDTO(dto, entity);
        E updatedEntity = repository.save(entity);
        return mapper.toDTO(updatedEntity);
    }

    @Override
    public D getById(ID id) {
        E entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found"));
        return mapper.toDTO(entity);
    }

    @Override
    public List<D> getAll(SearchCriteria criteria) {
        if (criteria == null || (criteria.getTerms() == null && criteria.getPage() == null && criteria.getSize() == null)) {
            return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
        }

        Pageable pageable = PageRequest.of(
                criteria.getPage() != null ? criteria.getPage() : 0,
                criteria.getSize() != null ? criteria.getSize() : 10,
                Sort.Direction.fromString(criteria.getSortDirection() != null ? criteria.getSortDirection() : "ASC"),
                criteria.getSortBy() != null ? criteria.getSortBy() : "id"
        );

        Specification<E> spec = (root, query, builder) -> {
            Predicate finalPredicate = builder.conjunction();

            if (criteria.getTerms() != null && !criteria.getTerms().isEmpty()) {
                Predicate[] keywordPredicates = criteria.getTerms().stream()
                        .map(keyword -> builder.like(builder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"))
                        .toArray(Predicate[]::new);
                finalPredicate = builder.and(finalPredicate, builder.or(keywordPredicates));
            }

            if (criteria.getFromDate() != null) {
                finalPredicate = builder.and(finalPredicate, builder.greaterThanOrEqualTo(root.get("createdAt"), criteria.getFromDate()));
            }

            if (criteria.getToDate() != null) {
                finalPredicate = builder.and(finalPredicate, builder.lessThanOrEqualTo(root.get("createdAt"), criteria.getToDate()));
            }

            return finalPredicate;
        };

        return repository.findAll(spec, pageable).getContent().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteByListId(List<ID> ids) {
        List<E> entities = repository.findAllById(ids);
        entities.forEach(e -> e.setDeletedAt(OffsetDateTime.now()));
        repository.saveAll(entities);
    }
}