package com.miraclepat.utils.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //S3에 이미지 업로드
    public void uploadFile(MultipartFile multipartFile, String keyName){
        //폴더 경로는 년/월/일/uuid.확장자
        //s3에 올리려면 MultipartFile -> file로 변경해야함.
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        try {
            InputStream inputStream = multipartFile.getInputStream();
            amazonS3.putObject(new PutObjectRequest(bucket, keyName, inputStream, objectMetadata));
        }catch (Exception e){
            log.info("이미지 업로드 중 오류 발생: "+e);
        }
    }

    //파일이 존재한다면, 파일 삭제
    public void deleteFile(String fileName){
        boolean isObjectExist = amazonS3.doesObjectExist(bucket, fileName);
        if(isObjectExist){
            log.info("파일을 삭제합니다.");
            amazonS3.deleteObject(bucket, fileName);
        }
        log.info("파일이 없습니다.");
    }

    public String findUrl(String keyName){
        return amazonS3.getUrl(bucket, keyName).toString();
    }
}
