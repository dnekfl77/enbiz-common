package com.x2bee.common.base.upload;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Uploader {

    Map<String, Object> upload(MultipartFile multipartFile, UploadReqDto uploadReqDto);

    Map<String, Object> upload(List<MultipartFile> multipartFiles, UploadReqDto uploadReqDto);

    // boolean confirmFile();
    
    void deleteFile(List<String> fullPathList);

    ResponseEntity<byte[]> downloadFile(String fullPath, String originalFileName) throws IOException;

}
