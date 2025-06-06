package com.example.fintech;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FintechApplication {

	public static void main(String[] args) {

		// .env 파일 로딩 (없어도 실행되게 ignoreIfMissing 추가)
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		// 환경 변수로 등록
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
		System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
		System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
		System.setProperty("CORS_ALLOWED_ORIGIN", dotenv.get("CORS_ALLOWED_ORIGIN"));
		System.setProperty("AI_SERVER_URL", dotenv.get("AI_SERVER_URL"));


		SpringApplication.run(FintechApplication.class, args);

	}
}
