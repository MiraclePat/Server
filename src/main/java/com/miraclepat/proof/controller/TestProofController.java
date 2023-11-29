package com.miraclepat.proof.controller;

import com.miraclepat.proof.dto.ProofDto;
import com.miraclepat.proof.dto.ProofListDto;
import com.miraclepat.utils.Constants;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //참여중인 팟에서 내 인증 보기
    @GetMapping
    public ResponseEntity getMyProofList(
            @RequestParam(name = "lastId", required = false)
            Long lastId,
            @RequestParam(name = "size", required = false, defaultValue = "10")
            int size
            ){

        List<ProofDto> list = new ArrayList<>();

        for(int i = 0; i<5;i++){
            ProofDto proof = new ProofDto(Long.valueOf(i), Constants.MY_PROOF);
            list.add(proof);
        }

        ProofListDto proofListDto = new ProofListDto(list, true);

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


        List<ProofDto> list = new ArrayList<>();

        for(int i = 0; i<5;i++){
            ProofDto proof = new ProofDto(Long.valueOf(i), Constants.ANOTHER_PROOF);
            list.add(proof);
        }

        ProofListDto proofListDto = new ProofListDto(list, true);

        return ResponseEntity.ok().body(proofListDto);
    }
}
