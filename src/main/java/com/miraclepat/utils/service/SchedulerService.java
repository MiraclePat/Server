package com.miraclepat.utils.service;

import com.miraclepat.pat.repository.PatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final PatRepository patRepository;

    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void run(){
        //pat state update
        //진행중 -> 진행완료
        patRepository.updatePatStateComplete();
        //진행예정 -> 진행중
        patRepository.updatePatStateInProgress();
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *") //매일 자정
    public void midnight(){
        //pat state update
        //진행중 -> 진행완료 endTime 이 00:00 인 경우를 관리
        patRepository.updatePatStateCompleteMidnight();
    }
}
