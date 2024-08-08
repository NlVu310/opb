package com.openbanking.comon;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class BaseServiceImpl<E extends BaseEntity, D, CD, UD extends BaseDTO, ID> implements BaseService<D, CD, UD, ID> {
    private final BaseRepository<E, ID> repository;
    private final BaseMapper<E, D, CD, UD> mapper;

    @Override
    public D create(CD dto, Long accountId) {
        E entity = mapper.toEntityFromCD(dto);
        entity.setCreatedBy(accountId);
        E savedEntity = repository.save(entity);
        return mapper.toDTO(savedEntity);
    }

    @Override
    public D update(UD dto, Long accountId) {
        E entity = repository.findById((ID) dto.getId()).orElseThrow(() -> new RuntimeException("Entity not found"));
        entity.setCreatedBy(accountId);
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
    public PaginationRS<D> getAll(SearchCriteria criteria) {
        if (criteria == null || (criteria.getTerm() == null && criteria.getPage() == null && criteria.getSize() == null)) {
            List<D> allEntities = repository.findAll().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());

            PaginationRS<D> response = new PaginationRS<>();
            response.setContent(allEntities);
            response.setPageNumber(0);
            response.setPageSize(allEntities.size());
            response.setTotalElements(allEntities.size());
            response.setTotalPages(1);

            return response;
        }

        Pageable pageable = PageRequest.of(
                criteria.getPage() != null ? criteria.getPage() : 0,
                criteria.getSize() != null ? criteria.getSize() : 10,
                Sort.Direction.fromString(criteria.getSortDirection() != null ? criteria.getSortDirection() : "ASC"),
                criteria.getSortBy() != null ? criteria.getSortBy() : "id"
        );

        Specification<E> spec = (root, query, builder) -> {
            Predicate finalPredicate = builder.conjunction();

            if (criteria.getTerm() != null && !criteria.getTerm().isEmpty()) {
                String[] keywords = criteria.getTerm().split("\\s+");
                Predicate[] keywordPredicates = Arrays.stream(keywords)
                        .map(keyword -> builder.like(builder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"))
                        .toArray(Predicate[]::new);
                finalPredicate = builder.and(finalPredicate, builder.or(keywordPredicates));
            }

            return finalPredicate;
        };

        Page<E> page = repository.findAll(spec, pageable);
        List<D> content = page.getContent().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        PaginationRS<D> response = new PaginationRS<>();
        response.setContent(content);
        response.setPageNumber(page.getNumber() + 1);
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());

        return response;
    }


    @Override
    public void deleteByListId(List<ID> ids) {
        List<E> entities = repository.findAllById(ids);
        entities.forEach(e -> e.setDeletedAt(OffsetDateTime.now()));
        repository.saveAll(entities);
    }
}