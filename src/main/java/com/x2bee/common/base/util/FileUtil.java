package com.x2bee.common.base.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.multipart.MultipartFile;

import com.x2bee.common.base.Validator;
import com.x2bee.common.base.constant.X2Constants;
import com.x2bee.common.base.exception.UserDefinedException;

public class FileUtil {
	private static final int BUFFER_SIZE = 8192;
    private FileUtil() {
    	throw new UnsupportedOperationException();
    }

    /**
     * 파일 임시 저장
     * @name_ko 파일 임시 저장
     */
    public static File saveTempFile(MultipartFile filePart, String strTempUploadDir)  {
    	Validator.throwIfNull(filePart, "MultipartFile cannot be null");
    	Validator.throwIfNull(strTempUploadDir, "strTempUploadDir cannot be null");

        long randomNumber = RandomUtils.nextLong();
        byte[] uploadedBytes;
		try{
			uploadedBytes = filePart.getBytes();
		} catch (IOException e) {
			throw new UserDefinedException(e);
		}

        File uploadDir = new File(strTempUploadDir);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File targetFile = new File(uploadDir, String.format("%s%s", randomNumber, getExtension(filePart)));

        if (targetFile.exists()) {
            targetFile.delete();
        }

        try {
			FileUtils.writeByteArrayToFile(targetFile, uploadedBytes);
		} catch (IOException e) {
			throw new UserDefinedException(e);
		}

        return targetFile;
    }

    /**
     * 유저 템프 디렉터리 조회
     * @name_ko 유저 템프 디렉터리 조회
     */
    public static File getUserTempDirectory(String tempDir, String loginId) {
        File tempDirectory = new File(tempDir, loginId);
        if (!tempDirectory.exists()) {
            tempDirectory.mkdirs();
        }

        return tempDirectory;
    }

    public static void moveFile(File sourceFile, File targetFile) throws Exception {
        copyFile(sourceFile, targetFile);
        sourceFile.delete();
    }

    /**
     * 디렉토리 경로 확인 후 없으면 생성.
     * @param path
     */
    public static void checkDir(String path) {
    	File dir= new File(path);
        if (!dir.exists()) {
        	dir.mkdirs();
        }
    }

    /**
     * 파일 복사
     * @name_ko 파일 복사
     */
    public static void copyFile(File sourceFile, File targetFile)  {
    	Validator.throwIfNull(sourceFile, "sourceFile cannot be null");
    	Validator.throwIfNull(targetFile, "targetFile cannot be null");

        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
        		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(targetFile));){
        	transfer(inputStream, outputStream);
        } catch (Exception e) {
        	throw new UserDefinedException(e);
        }
    }

	public static void transfer(BufferedInputStream inputStream, BufferedOutputStream outputStream)
			throws IOException {
		int read;
		byte[] buffer = new byte[BUFFER_SIZE];
		while ((read = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, read);
		}
		outputStream.flush();
	}

    /**
     * 파일 확장자 반환
     * @name_ko 파일 확장자 반환
     */
    public static String getExtension(MultipartFile filePart) {
        return getExtension(filePart.getOriginalFilename());
    }

    /**
     * 파일 확장자 반환
     * @name_ko 파일 확장자 반환
     */
    public static String getExtension(File file) {
        return getExtension(file.getAbsolutePath());
    }

    /**
     * 파일 확장자 반환
     * @name_ko 파일 확장자 반환
     */
    public static String getExtension(String fullName) {
    	int lastIndexOfPeriod = fullName.lastIndexOf(X2Constants.PERIOD);

    	return lastIndexOfPeriod == -1
    			? X2Constants.EMPTY
    			: X2Constants.PERIOD + fullName.substring(fullName.lastIndexOf(X2Constants.PERIOD) + 1);
    }

    /**
	 * 파일 확장자 체크
	 * 위의 목록과 맞으면 true, 아니면 false;
	 * @param fileName
	 * @param allowExtension
	 *
	 * @name_ko 파일 확장자 체크
	 */
    public static boolean checkAllowExtension(String fileName, String allowExtension) {
    	String fileExtension = FileUtil.getExtension(fileName).toLowerCase().replace(".", "");
    	return allowExtension.indexOf(fileExtension) > -1;
    }

    /**
	 * 파일 확장자 체크
	 * 가능 확장자 PPTX, DOC, EXCEL, JPG, PDF, ZIP
	 * 위의 목록과 맞으면 true, 아니면 false;
	 * @param file
	 *
	 * @name_ko 파일 확장자 체크
	 */
    public static boolean checkAllowExtension(File file, String allowExtension) {
    	String fileExtension = FileUtil.getExtension(file).toLowerCase().replace(".", "");
    	return allowExtension.indexOf(fileExtension) > -1;
    }
}
