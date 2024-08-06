package com.openbanking.comon;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
public abstract class BaseController<T, ID> {

    private BaseService<T, ID> service;

    @PostMapping
    public ResponseBuilder<T> create(@RequestBody T dto) {
        T rs = service.create(dto);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @PutMapping("/{id}")
    public ResponseBuilder<T> update(@PathVariable ID id, @RequestBody T dto) {
        T rs = service.update(id, dto);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/{id}")
    public ResponseBuilder<T> getById(@PathVariable ID id) {
        T rs = service.getById(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping
    public ResponseBuilder<List<T>> getAll(@RequestBody(required = false) SearchCriteria rq) {
        List<T> rsLst = service.getAll(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rsLst);
    }

    @DeleteMapping("/{id}")
    public ResponseBuilder<Void> deleteById(@PathVariable ID id) {
        service.deleteById(id);
        return new ResponseBuilder<>(HttpStatus.NO_CONTENT.value(), "Success", null);
    }
}
