package com.miraclepat.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
public class FirebaseConfig {

    // firebase를 사용하기 위한 설정

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        //발급받은 firebase key 를 등록해 계정 설정
        Resource resource = new ClassPathResource("miracle-pat-firebase-adminsdk.json");
        InputStream serviceAccount = resource.getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp app = FirebaseApp.initializeApp(options);

        log.info("FirebaseApp initialized" + app.getName());
        return app;
    }

    @Bean
    public FirebaseAuth firebaseAuth() throws IOException {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp());
        return firebaseAuth;
    }
}
