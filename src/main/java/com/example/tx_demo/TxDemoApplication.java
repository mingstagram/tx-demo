package com.example.tx_demo;

import com.example.tx_demo.service.TxService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TxDemoApplication implements CommandLineRunner {

	private final TxService txService;

	public TxDemoApplication(TxService txService) {
		this.txService = txService;
	}

	public static void main(String[] args) {
		SpringApplication.run(TxDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			txService.externalCall(); // 정상적으로 롤백
		} catch (Exception e) {
			System.out.println("[1] 외부 호출 예외 처리됨");
		}

		try {
			txService.internalCall(); // ❌ 트랜잭션 적용되지 않음
		} catch (Exception e) {
			System.out.println("[2] 내부 호출 예외 처리됨");
		}

		try {
			txService.checkedExceptionCall(); // ❌ 기본 설정에서 롤백 안 됨
		} catch (Exception e) {
			System.out.println("[3] Checked 예외 발생");
		}

		try {
			txService.forceRollbackCall(); // ✅ 명시적으로 롤백됨
		} catch (Exception e) {
			System.out.println("[4] 강제 롤백 예외 처리됨");
		}
	}
}
