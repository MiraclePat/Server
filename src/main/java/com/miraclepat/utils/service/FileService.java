package com.miraclepat.utils.service;

import com.miraclepat.global.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final StorageService storageService;

    //저장 서비스 호출
    public String updateFile(MultipartFile multipartFile) {

        String keyName = makeDir() + makeFileName()
                + getFileFormat(multipartFile.getOriginalFilename());
        storageService.uploadFile(multipartFile, keyName);

        return keyName;
    }

    //삭제 서비스 호출
    public void deleteFile(String filename) {
        storageService.deleteFile(filename);
    }

    //이미지 url 가져오기
    public String getUrl(String keyName) {
        return storageService.findUrl(keyName);
    }

    //년/월/일로 폴더 계층 관리
    private String makeDir() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        return "image/" + year + "/" + month + "/" + day + "/";
    }

    //중복을 피하기 위해 저장용 파일 이름 생성
    private String makeFileName() {
        return UUID.randomUUID().toString();
    }

    //파일의 확장자 추출
    private String getFileFormat(String originalFileName) {
        if (originalFileName == null) {
            throw new IllegalArgumentException(ErrorMessage.NO_FILE_NAME);
        }
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));

        return ext;
    }
}
