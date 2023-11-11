package com.miraclepat.proof.controller;

import com.miraclepat.proof.dto.ProofListDto;
import com.miraclepat.proof.entity.Proof;
import com.miraclepat.utils.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/test/members/pats/{pat-id}/proofs")
public class TestProofController {

    //인증하기
    @PostMapping
    public ResponseEntity proof(){
        return ResponseEntity.noContent().build();
    }

    //참여중인 팟에서 내 인증 보기
    @GetMapping
    public ResponseEntity getMyProofList(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "1") Integer size
            ){

        List<Proof> list = new ArrayList<>();

        for(int i = 0; i<5;i++){
            Proof proof = new Proof();

            proof.setId(Long.valueOf(i));
            proof.setProofImg(Constants.MY_PROOF);
            proof.setOriImgName("이미지 이름"+i);

            list.add(proof);

        }

        ProofListDto proofListDto = new ProofListDto(list, 1L, 1L);

        return ResponseEntity.ok().body(proofListDto);
    }

    //참여중인 팟에서 다른 사람들 인증 사진 보기
    @GetMapping("/another")
    public ResponseEntity getAnotherProofList(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "1") Integer size
    ){


        List<Proof> list = new ArrayList<>();

        for(int i = 0; i<5;i++){
            Proof proof = new Proof();

            proof.setId(Long.valueOf(i));
            proof.setProofImg(Constants.ANOTHER_PROOF);
            proof.setOriImgName("이미지 이름"+i);

            list.add(proof);

        }

        ProofListDto proofListDto = new ProofListDto(list, 5L, 50L);

        return ResponseEntity.ok().body(proofListDto);
    }
}
