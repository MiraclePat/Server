package com.miraclepat.pat.service;

import com.miraclepat.pat.constant.ImgType;
import com.miraclepat.utils.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatImgService {

    private final FileService fileService;

    public List<List<String>> uploadPatImg(
            MultipartFile repImg,
            MultipartFile correctImg,
            MultipartFile incorrectImg,
            List<MultipartFile> bodyImg){
        //이미지 업로드
        List<List<String>> imgInfoList = new ArrayList<>();
        imgInfoList.add(savePatImg(repImg, ImgType.REPRESENTATIVE));
        imgInfoList.add(savePatImg(correctImg, ImgType.CORRECT));
        imgInfoList.add(savePatImg(incorrectImg, ImgType.INCORRECT));
        if (bodyImg != null){
            imgInfoList.addAll(savePatImg(bodyImg, ImgType.BODY));}

        return imgInfoList;
    }

    //팟 이미지 저장
    private List<String> savePatImg(MultipartFile image, ImgType imgType){
        List<String> result = new ArrayList<>();
        if (image.getOriginalFilename() == null) {
            throw new IllegalArgumentException("파일 원본 이름은 필수 값입니다.");}

        String fileName = fileService.updateFile(image);
        result.add(fileName);
        result.add(image.getOriginalFilename());
        result.add(imgType.toString());

        return result;
    }

    //팟 이미지 리스트 저장
    private List<List<String>> savePatImg(List<MultipartFile> images, ImgType imgType) {
        List<List<String>> result = new ArrayList<>();

        for (MultipartFile file : images) {
            if (file.getOriginalFilename() == null) {
                throw new IllegalArgumentException("파일 원본 이름은 필수 값입니다.");}

            String fileName = fileService.updateFile(file);

            List<String> name = new ArrayList<>();
            name.add(fileName);
            name.add(file.getOriginalFilename());
            name.add(imgType.toString());

            result.add(name);
        }

        return result;
    }
}
