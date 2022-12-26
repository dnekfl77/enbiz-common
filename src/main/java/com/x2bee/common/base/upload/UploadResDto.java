package com.x2bee.common.base.upload;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadResDto {
    private String statusCode;      //상태코드
    private String orgFileName;     //원본파일명
    private String fileName;        //파일명
    private String url;             //파일URL
    private String path;            //파일경로
    private Long size;            //파일사이즈
    private String extension;       //파일확장자
    private String fullPath;        //파일전체경로
    private String tempFullPath;    //임시파일전체경로
    private String typeCd;          //컨텐츠유형코드
	private MultipartFile multipartFile;
}
