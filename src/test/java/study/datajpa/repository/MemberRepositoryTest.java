package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    //spring data jpa가 구현체를 만들어서 자동으로 인젝션을 해줌.

    @DisplayName("테스트 멤버")
    @Test
    public void testMember() throws Exception {
        //given
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        //then
        assertEquals(findMember.getId(), savedMember.getId());
        assertEquals(findMember.getUsername(), savedMember.getUsername());
        assertEquals(findMember, savedMember);

    }

    @DisplayName("기본 CRUD 테스트")
    @Test
    public void basicCRUD() throws Exception {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        findMember1.setUsername("member!!!!!!!!");

        assertEquals(findMember1, member1);
        assertEquals(findMember2, member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertEquals(all.size(), 2);

        //카운트 검증
        Long count = memberRepository.count();
        assertEquals(count, 2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        Long deleteCount = memberRepository.count();
        assertEquals(deleteCount, 0);

    }
}