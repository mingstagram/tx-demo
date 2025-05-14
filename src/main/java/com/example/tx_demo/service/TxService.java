package com.example.tx_demo.service;

import com.example.tx_demo.domain.Member;
import com.example.tx_demo.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class TxService {

    private final MemberRepository memberRepository;

    public TxService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void externalCall() {
        System.out.println(">> 외부에서 호출된 메서드");
        memberRepository.save(new Member("외부"));
        throw new RuntimeException("런타임 예외 (롤백됨)");
    }

    public void internalCall() {
        System.out.println(">> 내부 메서드 호출");
        this.innerTransactionalMethod(); // 프록시 무시 -> 트랜잭셩 미적용
    }

    @Transactional
    public void innerTransactionalMethod() {
        System.out.println("▶ 내부 메서드 진입");
        memberRepository.save(new Member("내부"));
        throw new RuntimeException("내부 예외 (롤백 기대)");
    }

    @Transactional
    public void checkedExceptionCall() throws Exception {
        System.out.println(">> Checked 예외 테스트");
        memberRepository.save(new Member("체크드"));
        throw new Exception("체크드 예외 (롤백 안 됨)");
    }

    @Transactional(rollbackOn = Exception.class)
    public void forceRollbackCall() throws Exception {
        System.out.println(">> rollbackOn 명시");
        memberRepository.save(new Member("롤백포"));
        throw new Exception("강제 롤백");
    }

}
