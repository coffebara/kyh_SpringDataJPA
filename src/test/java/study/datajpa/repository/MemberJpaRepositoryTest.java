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
@Rollback(value = false) // default: false -> flush를 안함 -> db 반영 X
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @DisplayName("테스트 멤버")
    @Test
    public void testMember() throws Exception {
        //given
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        //when
        Member findMember = memberJpaRepository.find(savedMember.getId());

        //then
        assertEquals(savedMember.getId(), findMember.getId());
        assertEquals(savedMember.getUsername(), findMember.getUsername());

        assertEquals(savedMember, findMember);
    }

    @DisplayName("기본 CRUD 테스트")
    @Test
    public void basicCRUD() throws Exception {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        findMember1.setUsername("member!!!!!!!!");

//        //then
//        assertEquals(findMember1, member1);
//        assertEquals(findMember2, member2);
//
//        //리스트 조회 검증
//        List<Member> all = memberJpaRepository.findAll();
//
//        //then
//        assertEquals(all.size(), 2);
//
//        //카운트 검증
//        Long count = memberJpaRepository.count();
//        assertEquals(count, 2);
//
//        //삭제 검증
//        memberJpaRepository.delete(member1);
//        memberJpaRepository.delete(member2);
//        Long deleteCount = memberJpaRepository.count();
//        assertEquals(deleteCount, 0);



    }

}