package com.miraclepat.security;

import com.miraclepat.member.entity.Member;
import com.miraclepat.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@RequiredArgsConstructor
@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUserCode(username);

        if (member == null) { //만약 찾을 수 없으면 예외처리
            throw new UsernameNotFoundException(username);
        }

        return new User(member.getId().toString(), "", Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString())));
    }
}
