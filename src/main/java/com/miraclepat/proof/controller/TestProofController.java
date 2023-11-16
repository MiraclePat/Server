package com.miraclepat.proof.controller;

import com.miraclepat.proof.dto.ProofListDto;
import com.miraclepat.proof.entity.Proof;
import com.miraclepat.utils.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/test/members/pats/{pat-id}/proofs")
public class TestProofController {

    //인증하기
    @PostMapping
    public ResponseEntity proof(
            @PathVariable("pat-id") Long patId,
            @NotNull(message = "proofImg는 필수입니다.")
            @RequestPart("proofImg")MultipartFile proofImg
            ){

        System.out.println("잘 들어옴");
        //이미지 받아서 저장
        if (proofImg.isEmpty()) {
            throw new IllegalArgumentException("proofImg 파일이 비어있습니다.");
        }

        return ResponseEntity.noContent().build();
    }

    //참여중인 팟에서 내 인증 보기
    @GetMapping
    public ResponseEntity getMyProofList(
            @RequestParam(name = "lastId", required = false)
            Long lastId,
            @RequestParam(name = "size", required = false, defaultValue = "10")
            int size
            ){

        List<Proof> list = new ArrayList<>();

        for(int i = 0; i<5;i++){
            Proof proof = new Proof();

            proof.setId(Long.valueOf(i));
            proof.setProofImg(Constants.MY_PROOF);
            proof.setOriImgName("이미지 이름"+i);

            list.add(proof);

        }

        ProofListDto proofListDto = new ProofListDto(list);

        return ResponseEntity.ok().body(proofListDto);
    }

    //참여중인 팟에서 다른 사람들 인증 사진 보기
    @GetMapping("/another")
    public ResponseEntity getAnotherProofList(
            @RequestParam(name = "lastId", required = false)
            Long lastId,
            @RequestParam(name = "size", required = false, defaultValue = "10")
            int size
    ){


        List<Proof> list = new ArrayList<>();

        for(int i = 0; i<5;i++){
            Proof proof = new Proof();

            proof.setId(Long.valueOf(i));
            proof.setProofImg(Constants.ANOTHER_PROOF);
            proof.setOriImgName("이미지 이름"+i);

            list.add(proof);

        }

        ProofListDto proofListDto = new ProofListDto(list);

        return ResponseEntity.ok().body(proofListDto);
    }
}
