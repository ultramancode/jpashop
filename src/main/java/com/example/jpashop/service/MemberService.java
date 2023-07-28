package com.example.jpashop.service;

import com.example.jpashop.domain.Member;
import com.example.jpashop.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;

  /**
   * 회원가입 약식
   */
  @Transactional
  public Long join(Member member) {
    validateDuplicateMember(member); //중복 회원 검증
    memberRepository.save(member);
    return member.getId();

  }
//실무에서는 동시에 요청하는 경우 동시에 a라는 이름으로 가입하게될 수 있으니 최후의 방어막으로 name에다가 유니크 제약 조건 걸어줘야함!(멀티스레드 고려)
  public void validateDuplicateMember(Member member) {
    List<Member> findMembers = memberRepository.findByName(member.getName());
    if (!findMembers.isEmpty()) {
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
  }

  public List<Member> findMembers() {
    return memberRepository.findAll();
  }


  public Member findOne(Long memberId) {
    return memberRepository.findOne(memberId);
  }
}
