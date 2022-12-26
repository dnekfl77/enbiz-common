package com.x2bee.common.base.upload;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class UploaderTest {
	@Autowired
	private Uploader uploader;
	
	@Test
	void upload() {
		MockMultipartFile file = new MockMultipartFile(
	        "file", 
	        "hello.txt", 
	        MediaType.TEXT_PLAIN_VALUE, 
	        "Hello, World!".getBytes()
	    );
		
		UploadReqDto uploadReqDto = new UploadReqDto();
        uploadReqDto.setAttacheFileKind(AttacheFileKind.SYSTEM);
        uploadReqDto.setTempPathYn(false);
		Map<String, Object> retMap = uploader.upload(file, uploadReqDto);

		Assertions.assertEquals(retMap.get("cd"), "00");
		
		if ("00".equals(retMap.get("cd")) ) { // S3 정상 업로드인경우
			UploadResDto uploadResDto = (UploadResDto)((Map<String, Object>)retMap.get("data")).get("data");

			Map<String,String> fileInfo = new HashMap<>();
			fileInfo.put("I_FILE_TITLE", uploadResDto.getOrgFileName()); //사용
			fileInfo.put("I_FILE_NM", uploadResDto.getFileName()); //사용안함
			fileInfo.put("I_FILE_URL", uploadResDto.getFullPath());//사용
			fileInfo.put("I_FILE_TEMP_URL", uploadResDto.getTempFullPath());//사용
			fileInfo.put("I_FILE_PATH", uploadResDto.getPath());//사용안함
			fileInfo.put("I_FILE_SIZE", uploadResDto.getSize().toString());
			fileInfo.put("I_FILE_EXT", uploadResDto.getExtension());

			log.debug("[I_FILE_TITLE]"+fileInfo.get("I_FILE_TITLE"));	//원본 파일명
			log.debug("[I_FILE_NM]"+fileInfo.get("I_FILE_NM"));			//파일명
			log.debug("[I_FILE_URL]"+fileInfo.get("I_FILE_URL"));		//파일URL
			log.debug("[I_FILE_PATH]"+fileInfo.get("I_FILE_PATH"));		//파일경로
			log.debug("[I_FILE_SIZE]"+fileInfo.get("I_FILE_SIZE"));		//파일사이즈
			log.debug("[I_FILE_EXT]"+fileInfo.get("I_FILE_EXT"));		//파일확장자
			log.debug("[I_FILE_TEMP_URL]"+fileInfo.get("I_FILE_TEMP_URL"));		//임시파일URL
			
		}

	}
}
