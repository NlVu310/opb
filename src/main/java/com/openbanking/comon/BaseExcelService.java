package com.openbanking.comon;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
@Service
public interface BaseExcelService{
    public <T> ResponseEntity<InputStreamResource> exportToExcel(List<T> data, Class<T> clazz , InputStream inputStream);

    public <T> ResponseEntity<String> importFromExcel(MultipartFile file, Class<T> clazz) throws Exception;
}
