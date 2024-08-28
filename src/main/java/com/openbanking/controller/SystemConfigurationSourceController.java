package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.model.security.UserService;
import com.openbanking.model.system_configuration_source.CreateSystemConfigurationSource;
import com.openbanking.model.system_configuration_source.SystemConfigurationSource;
import com.openbanking.model.system_configuration_source.UpdateSystemConfigurationSource;
import com.openbanking.service.SystemConfigurationSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/source")
public class SystemConfigurationSourceController extends BaseController<SystemConfigurationSource, CreateSystemConfigurationSource, UpdateSystemConfigurationSource, Long> {

    @Autowired
    private SystemConfigurationSourceService systemConfigurationSourceService;

    @PostMapping("/create")
    public ResponseBuilder<?> create(@Valid @RequestBody CreateSystemConfigurationSource rq) {
        systemConfigurationSourceService.create(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }

    @PutMapping("/update")
    public ResponseBuilder<SystemConfigurationSource> update(@Valid @RequestBody UpdateSystemConfigurationSource rq, UserService userService) {
        systemConfigurationSourceService.update(rq, userService.getCurrentUser().getId());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }

    @DeleteMapping("/delete")
    public ResponseBuilder<Void> deleteByListId(@RequestParam List<Long> ids) {
        systemConfigurationSourceService.deleteListById(ids);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }

    @GetMapping("/get")
    public ResponseBuilder<SystemConfigurationSource> getById(@RequestParam("id") Long id) {
        var rs = systemConfigurationSourceService.getById(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @PostMapping("/list")
    public ResponseBuilder<PaginationRS<SystemConfigurationSource>> getAll(@RequestBody SearchCriteria searchCriteria) {
        var rs = systemConfigurationSourceService.getAll(searchCriteria);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/list/by-partner")
    public ResponseBuilder<?> getListSourceByPartnerId(@RequestParam ("id") Long id) {
        var rs = systemConfigurationSourceService.getListSourceByPartnerId(id);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
    @GetMapping("/list/code")
    public ResponseBuilder<?> getListSourceCode() {
        var rs = systemConfigurationSourceService.getListSourceCode();
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}


