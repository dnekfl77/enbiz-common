package com.x2bee.common.base.upload;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class UploadReqDto {
    private String customPath = "";      //임의패스(사용자가 임의로 지정하고 싶을 때)
    private String goodsNo = "";         //상품번호
    private String typeCd = "";          //컨텐츠유형코드
    private String serialNo = "";        //컨텐츠일렵번호
    private Boolean tempPathYn = false;     //임시경로유무. 사용안함.
    private AttacheFileKind attacheFileKind = AttacheFileKind.SYSTEM; //첨부파일 종류
    private MultipartFile multipartFile;
}
