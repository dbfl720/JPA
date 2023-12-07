package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;


@SpringBootTest // 스프링 컨테이너 안에서 돌리기, 이것이 없으면 autowired도 안됨
@Transactional // test에 있으면 기본적으로 rollback을 한다.
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @Rollback(false) //db에 들어가는지 확인하려면 rollback -false 해서 db 확인
    public void 회원가입() throws Exception {
        //given - 이런게 주어졌을 때
        Member member = new Member();
        member.setName("kim");


        //when - 이렇게 하면
        Long savedId = memberService.join(member);

        //then - 이렇게 된다.
        em.flush(); //db에 강제로 쿼리가 나가는 것.
        assertEquals(member, memberRepository.findOne(savedId));
    }



    @Test  // (expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");


        //when
        memberService.join(member1);
        try {
            memberService.join(member2); //예외발생
        } catch (IllegalStateException e) {
            return;
        }


        //then
        fail("예외가 발생해야 한다.");
    }
}