package com.x2bee.common.base.upload;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.x2bee.common.base.util.DateTimeUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
//@Component
public class S3UploaderCommonImpl implements Uploader {

    private final Environment env;
	private final AmazonS3Client amazonS3Client;
    private final S3UploadCommonComponent s3UploadComponent;

    private boolean validateParameter (MultipartFile multipartFile, UploadReqDto uploadReqDto) {
        if ( multipartFile == null )
            return false;

        if ( uploadReqDto == null )
            return false;

        if ( uploadReqDto.getAttacheFileKind() == null )
            return false;

        return true;
    }

    private boolean validateAttacheFileEmpty(MultipartFile multipartFile) {
        return !multipartFile.isEmpty();
    }

    private boolean validateExtension(MultipartFile multipartFile, String[] extensions) {
        List<String> extensionList = Arrays.asList(extensions);

        String fileExtension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename()).toLowerCase();

        return extensionList.contains(fileExtension);
    }

    private boolean validateAttacheFileExtension(MultipartFile multipartFile, AttacheFileKind attacheKind) {
        String[] extensions = env.getProperty("upload." + attacheKind.getNm() + ".extension", String[].class);

        return validateExtension(multipartFile, extensions);
    }

    private boolean validateAttacheFileSize(MultipartFile multipartFile, AttacheFileKind attacheKind) {
        Long maxSizePerFile =
                env.getProperty("upload." + attacheKind.getNm() + ".maxUploadSizePerFile", Long.class);

        return maxSizePerFile > multipartFile.getSize();
    }

    private String putS3(String fileName, MultipartFile multipartFile) throws IOException {
    	//PutObjectRequest putObjectRequest = new PutObjectRequest(s3UploadComponent.getBucket(), fileName, uploadFile);
    	ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());
    	PutObjectRequest putObjectRequest = new PutObjectRequest(s3UploadComponent.getBucket(), fileName, multipartFile.getInputStream(), objectMetadata);

        amazonS3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.Private));

        return amazonS3Client.getUrl(s3UploadComponent.getBucket(), fileName).toString();
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(String fullPath, String originalFileName) throws IOException{
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        S3Object o = amazonS3Client.getObject(new GetObjectRequest(s3UploadComponent.getBucket(), fullPath));
        S3ObjectInputStream objectInputStream = o.getObjectContent();

        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        String contentType = request.getSession().getServletContext().getMimeType(originalFileName);
        MediaType mediaType = null;
        if(contentType == null) {
        	mediaType = MediaType.APPLICATION_OCTET_STREAM;
        } else {
        	mediaType = MediaType.valueOf(contentType);
        	if(mediaType == null) {
        		mediaType = MediaType.APPLICATION_OCTET_STREAM;
        	}
        }

        String fileName = URLEncoder.encode(originalFileName, "UTF-8");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(mediaType);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    private Map<String, Object> upload(UploadReqDto uploadReqDto) {
    	Map<String, Object> retMap = new HashMap<>();

        String rootPath = env.getProperty("upload.s3.path");

        if ( rootPath == null ) {
            retMap.put("cd", "10");
            retMap.put("msg", "config error");
            return retMap;
        }

        String uploadPath = "";

        uploadPath = uploadReqDto.getAttacheFileKind().getNm() + "/" + DateTimeUtil.getFormatString("yyyy/MM/dd")+"/";

        if ( uploadReqDto.getCustomPath() != "" ) {
            uploadPath = uploadPath + uploadReqDto.getCustomPath();
        }

        String extension = "";
        String originalFilename = "";
        String fileName = "";
        
        try {
        	MultipartFile multipart = uploadReqDto.getMultipartFile();
        	
        	originalFilename = multipart.getOriginalFilename();
        	
        	if(multipart.getOriginalFilename().contains(".")){
        		extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                if(extension.length()>1){
                	extension = extension.substring(1);
                }
        	}
        	UUID randomeUUID = UUID.randomUUID();
        	fileName = randomeUUID + "";
            if ( !org.apache.commons.lang3.StringUtils.equals("", uploadReqDto.getGoodsNo()) ) {
            	fileName += "_" + uploadReqDto.getGoodsNo();
            }
            if ( !org.apache.commons.lang3.StringUtils.equals("", uploadReqDto.getSerialNo()) ) {
            	fileName += "_" + uploadReqDto.getSerialNo();
            }
            if ( !org.apache.commons.lang3.StringUtils.equals("", uploadReqDto.getTypeCd()) ) {
                fileName += "_" + uploadReqDto.getTypeCd();
            }
        	

            retMap.put("cd", "00");
            retMap.put("msg", "success");

            UploadResDto uploadResDto = new UploadResDto();

            uploadResDto.setStatusCode("00");
            uploadResDto.setFileName(multipart.getOriginalFilename());
            uploadResDto.setSize(multipart.getSize());
            uploadResDto.setExtension(extension);
            uploadResDto.setUrl(putS3(rootPath + (uploadReqDto.getTempPathYn() ? "temp/" : "") +uploadPath
                    + fileName + "." + extension, multipart ));
            uploadResDto.setPath(rootPath + uploadPath);
            uploadResDto.setFullPath(rootPath + uploadPath + fileName + "." + extension);
            if (uploadReqDto.getTempPathYn()) {
            	uploadResDto.setTempFullPath(rootPath + (uploadReqDto.getTempPathYn() ? "temp/" : "") +uploadPath + fileName + "." + extension);
            }
            uploadResDto.setOrgFileName(originalFilename);

            retMap.put("data", uploadResDto);

            //임시저장이면 session에 저장한다.
            if (uploadReqDto.getTempPathYn()) {
            	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            	HttpSession session = request.getSession();

            	Object obj = session.getAttribute("S3_TEMP_LIST");
            	if (obj != null) {
            		List<UploadResDto> tempList = (List<UploadResDto>)obj;
            		tempList.add(uploadResDto);
            		session.setAttribute("S3_TEMP_LIST", tempList);
            	} else {
            		List<UploadResDto> tempList = new ArrayList<>();
            		tempList.add(uploadResDto);
            		session.setAttribute("S3_TEMP_LIST", tempList);
            	}
            }

        } catch (SdkClientException sce) {
            log.error(sce.fillInStackTrace().toString());
            log.error(String.format("AWS S3 Upload Error. File Name: %s", fileName));

            retMap.put("cd", "16");
            retMap.put("msg", "S3 Upload Error.");
            retMap.put("data", uploadReqDto);

            return retMap;
        } catch (NullPointerException nullPointerException) {
            log.error(nullPointerException.fillInStackTrace().toString());
            log.error(String.format("AWS S3 Upload Error. File Not Found."));

            retMap.put("cd", "16");
            retMap.put("msg", "S3 Upload Error.");
            retMap.put("data", uploadReqDto);

            return retMap;
        } catch (Exception e) {
        	log.error("AWS S3 ERROR {}", e.getMessage());
            retMap.put("cd", "16");
            retMap.put("msg", "S3 Upload Error.");
            retMap.put("data", uploadReqDto);
            return retMap;
        }

        return retMap;
    }

    private UploadResDto validation(MultipartFile multipartFile, UploadReqDto uploadReqDto) {

        UploadResDto uploadResDto = new UploadResDto();

        Map retMap = new HashMap();

        if ( !validateParameter(multipartFile, uploadReqDto) ) {
            uploadResDto.setStatusCode("10");

            return uploadResDto;
        }

        if ( !validateAttacheFileEmpty(multipartFile) ) {
            uploadResDto.setStatusCode("11");

            return uploadResDto;
        }

        if ( !validateAttacheFileExtension(multipartFile, uploadReqDto.getAttacheFileKind()) ) {
            uploadResDto.setStatusCode("12");

            return uploadResDto;
        }

        if ( !validateAttacheFileSize(multipartFile, uploadReqDto.getAttacheFileKind()) ) {
            uploadResDto.setStatusCode("13");

            return uploadResDto;
        }
        
        uploadResDto.setStatusCode("00");

        return uploadResDto;
    }

    @Override
    public Map<String, Object> upload(MultipartFile multipartFile, UploadReqDto uploadReqDto) {
        Map retMap = new HashMap();

        UploadResDto validationInfo = validation(multipartFile, uploadReqDto);

        if ( validationInfo == null ) {
            retMap.put("cd", "20");
            retMap.put("msg", "file convert error");

            return retMap;
        } else {
            if ( "00".equals(validationInfo.getStatusCode()) ) {
            	uploadReqDto.setMultipartFile(multipartFile);
                Map<String, Object> uploadedMap = upload(uploadReqDto);

                if ( uploadedMap == null ) {
                    retMap.put("cd", "21");
                    retMap.put("msg", "S3 upload error");

                    return retMap;
                } else {
                    if ( "00".equals(uploadedMap.get("cd")) ) {
                        retMap.put("cd", "00");
                        retMap.put("msg", "success");
                        retMap.put("data", uploadedMap);
                    } else {
                        retMap.put("cd", "22");
                        retMap.put("msg", "S3 upload error");

                        return retMap;
                    }
                }

            } else {
                retMap.put("cd", "23");
                retMap.put("msg", "file convert error");

                return retMap;
            }
        }

        return retMap;
    }

    @Override
    public Map<String, Object> upload(List<MultipartFile> multipartFiles, UploadReqDto uploadReqDto) {

        Map<String, Object> retMap = new HashMap<>();
        List<UploadResDto> listMap = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            Map<String, Object> calledMap = upload(multipartFile, uploadReqDto);

            if ( calledMap == null ) {
                retMap.put("cd", "30");
                retMap.put("msg", "S3 upload error");

                break;
            } else {
                if ( "00".equals(calledMap.get("cd")) ) {
                    retMap.put("cd", "00");
                    listMap.add((UploadResDto) (UploadResDto)((Map<String, Object>)calledMap.get("data")).get("data"));
                } else {
                    retMap.put("cd", "31");
                    retMap.put("msg", "S3 upload error");

                    break;
                }
            }
        }

        if ( "00".equals(retMap.get("cd")) ) {
            retMap.put("msg", "success");
            retMap.put("data", listMap);
        }

        return retMap;
    }

	//@Override
	public boolean confirmFile() {

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
    	Object tempList = session.getAttribute("S3_TEMP_LIST");

    	if (tempList != null) {
    		List<UploadResDto> uploadResDtoList = (List<UploadResDto>)tempList;
    		for (UploadResDto uploadResDto : uploadResDtoList) {
    			amazonS3Client.copyObject(s3UploadComponent.getBucket(), uploadResDto.getTempFullPath(), s3UploadComponent.getBucket(), uploadResDto.getFullPath());
    			amazonS3Client.deleteObject(s3UploadComponent.getBucket(), uploadResDto.getTempFullPath());
    		}
    		session.setAttribute("S3_TEMP_LIST", null);
    	}

		return true;
	}

	@Override
	public void deleteFile(List<String> fullPathList) {
		for (String fullPath : fullPathList) {
			amazonS3Client.deleteObject(s3UploadComponent.getBucket(), fullPath);
		}
	}
}
