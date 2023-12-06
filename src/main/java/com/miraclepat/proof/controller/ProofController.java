package com.miraclepat.proof.controller;

import com.miraclepat.proof.dto.ProofListDto;
import com.miraclepat.proof.service.ProofService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members/pats/{pat-id}/proofs")
public class ProofController {

    private final ProofService proofService;

    //인증하기
    @PostMapping
    public ResponseEntity proof(
            @PathVariable("pat-id") Long patId,
            @RequestPart("proofImg") MultipartFile proofImg,
            Principal principal
    ) {
        if (proofImg.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }
        proofService.proof(patId, 1L, proofImg);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //참여중인 팟에서 내 인증 보기
    @GetMapping
    public ResponseEntity getMyProofList(
            @PathVariable("pat-id")
            Long patId,
            @RequestParam(name = "lastId", required = false)
            Long lastId,
            @RequestParam(name = "size", required = false, defaultValue = "10")
            int size,
            Principal principal
    ) {
        ProofListDto proofListDto = proofService.getMyProof(lastId, size, patId, 1L);
        return ResponseEntity.ok(proofListDto);
    }

    //참여중인 팟에서 다른 사람들 인증 사진 보기
    @GetMapping("/another")
    public ResponseEntity getAnotherProofList(
            @PathVariable("pat-id")
            Long patId,
            @RequestParam(name = "lastId", required = false)
            Long lastId,
            @RequestParam(name = "size", required = false, defaultValue = "10")
            int size,
            Principal principal
    ) {
        ProofListDto proofListDto = proofService.getAnotherProof(lastId, size, patId, 1L);
        return ResponseEntity.ok(proofListDto);
    }
}
