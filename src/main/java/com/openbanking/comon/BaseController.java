package com.openbanking.comon;

import com.openbanking.model.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
public abstract class BaseController<T, C, U, ID> {
    private BaseService<T, C, U, ID> service;

    @PostMapping
    public ResponseBuilder<T> create(@RequestBody C dto, UserService userService) {
        T rs = service.create(dto, userService.getCurrentUser().getId());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @PutMapping()
    public ResponseBuilder<T> update(@RequestBody U dto, UserService userService) {
        T rs = service.update(dto, userService.getCurrentUser().getId());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/{id}")
    public ResponseBuilder<T> getById(@PathVariable ID id) {
        T rs = service.getById(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping
    public ResponseBuilder<PaginationRS<T>> getAll(@RequestBody(required = false) SearchCriteria rq) {
        PaginationRS<T> rsLst = service.getAll(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rsLst);
    }

    @DeleteMapping()
    public ResponseBuilder<Void> deleteByListId(@RequestParam List<ID> ids) {
        service.deleteByListId(ids);
        return new ResponseBuilder<>(HttpStatus.NO_CONTENT.value(), "Success", null);
    }
}
