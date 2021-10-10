package com.santander.games.challenges.dekorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.dekorate.openshift.annotation.OpenshiftApplication;

@OpenshiftApplication(name = "challenge-2")
@SpringBootApplication
public class DekorateChallengeApplication {

        public static void main(String[] args) {
		SpringApplication.run(DekorateChallengeApplication.class, args);
	}

}
