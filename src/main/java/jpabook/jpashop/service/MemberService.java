package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 성능 최적화 해줌, 읽기에는 이것을 쓰는 것이 좋음
@RequiredArgsConstructor //final이 있는 필드만 생성자 만들어줌.
public class MemberService {


    private final MemberRepository memberRepository; // final을 넣는 것을 추천. 캄파일 시점에 에러 체크 가능하기 때문.



    /**
     * 회원가입
     * @param member
     * @return
     */
    @Transactional
    public Long join(Member member) {

        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }


    }

    //회원 전체 조회

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }


    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }



    // 커멘드와 쿼리를 철저하게 분리한다. update하면서 member를 쿼리해버린다.(만약 void대신에 member가 들어갈 경우)
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);


    }
}
