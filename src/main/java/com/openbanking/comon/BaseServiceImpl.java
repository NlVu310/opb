package com.openbanking.comon;

import com.openbanking.exception.base_exception.EntityNotFoundException;
import com.openbanking.exception.base_exception.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.Attribute;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
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
        E entity = repository.findById((ID) dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + dto.getId() + " not found"));
        entity.setCreatedBy(accountId);
        mapper.updateEntityFromUDTO(dto, entity);
        E updatedEntity = repository.save(entity);
        return mapper.toDTO(updatedEntity);
    }

    @Override
    public D getById(ID id) {
        E entity = repository.findByIdAndDeletedAtNull(id);
        if (entity == null) {
            throw new EntityNotFoundException("Entity with ID " + id + " not found");
        }
        return mapper.toDTO(entity);
    }

    @Override
    public PaginationRS<D> getAll(SearchCriteria criteria) {
        if (criteria == null || (criteria.getTerm() == null && criteria.getPage() == null && criteria.getSize() == null)) {
            Specification<E> spec = (root, query, builder) ->
                    builder.isNull(root.get("deletedAt"));

            List<D> allEntities = repository.findAll(spec).stream()
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
                Sort.Direction.fromString(criteria.getSortDirection() != null ? criteria.getSortDirection() : "DESC"),
                criteria.getSortBy() != null ? criteria.getSortBy() : "id"
        );

        Specification<E> spec = (root, query, builder) -> {
            Predicate finalPredicate = builder.conjunction();

            finalPredicate = builder.and(finalPredicate, builder.isNull(root.get("deletedAt")));

            if (criteria.getTerm() != null && !criteria.getTerm().isEmpty()) {
                String[] keywords = criteria.getTerm().split("\\s+");
                for (String keyword : keywords) {
                    Predicate keywordPredicate = builder.disjunction();

                    for (Attribute<? super E, ?> attribute : root.getModel().getAttributes()) {
                        String fieldName = attribute.getName();
                        if (attribute.getJavaType().equals(String.class)) {
                            keywordPredicate = builder.or(
                                    keywordPredicate,
                                    builder.like(builder.lower(root.get(fieldName)), "%" + keyword.toLowerCase() + "%")
                            );
                        } else if (attribute.getJavaType().equals(LocalDate.class) || attribute.getJavaType().equals(LocalDateTime.class)) {
                            String[] dateRange = keyword.split("-");
                            if (dateRange.length == 2) {
                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    Date startDate = formatter.parse(dateRange[0]);
                                    Date endDate = formatter.parse(dateRange[1]);
                                    keywordPredicate = builder.or(
                                            keywordPredicate,
                                            builder.between(root.get(fieldName).as(java.sql.Date.class), new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()))
                                    );
                                } catch (ParseException e) {
                                    throw new InvalidInputException("Invalid date range: " + keyword);
                                }
                            }
                        }
                    }

                    finalPredicate = builder.and(finalPredicate, keywordPredicate);
                }
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
        if (entities.isEmpty()) {
            throw new EntityNotFoundException("No entities found with the provided IDs");
        }
        entities.forEach(e -> e.setDeletedAt(OffsetDateTime.now()));
        repository.saveAll(entities);
    }
}
