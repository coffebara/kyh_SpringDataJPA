package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    //spring data jpa가 구현체를 만들어서 자동으로 인젝션을 해줌.
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

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

    @Test
    public void findByUsernameAndAgeGreaterThan() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        //then
        assertEquals(result.get(0).getUsername(), "AAA");
        assertEquals(result.get(0).getAge(), 20);
        assertEquals(result.size(), 1);
    }

    @Test
    public void findHelloBy() throws Exception {
        List<Member> helloBy = memberRepository.findTop3HelloBy();
    }

    @Test
    public void testNamedQuery() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);

        //then
        assertEquals(findMember, m1);
    }

    @Test
    public void testFindUser() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findUser("AAA", 10);

        //then
        assertEquals(result.get(0).getUsername(), "AAA");
        assertEquals(result.get(0).getAge(), 10);
        assertEquals(result.get(0), m1);
    }
    
    @Test
    public void testFindUsernameList() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void testFindMemberDto() {
        //given
        Team t1 = new Team("teamA");
        teamRepository.save(t1);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(t1);
        memberRepository.save(m1);

        //when
        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void testFindByNames() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void testReturnType() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findListByUsername("asdasf");
        System.out.println("result = " + result.size()); // null 이 아니라 빈 List를 반환함 !!

        Member findMember = memberRepository.findMemberByUsername("asdasda");
        System.out.println("findMember = " + findMember); // 단건 조회일 때 없으면 null

        Optional<Member> findOptionalMember = memberRepository.findOptionalByUsername("asdasfasf");
        System.out.println("findOptionalMember = " + findOptionalMember.orElseThrow());  // null를 포함할 수있으므로 Optional 쓰면 됨.

        Optional<Member> findOptionalMemberList = memberRepository.findOptionalByUsername("AAA");
        System.out.println("findOptionalMemberList = " + findOptionalMemberList);
    }


    @Test
    public void testPaging() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest); //Pageable이면 자동으로 count를 해준다.

        // 실무에서 Entity를 바로 반납하면 여러 문제(API 스펙변화 등)가 생기기 때문에 아래와 같이 Dto로 변환해서 반환하는 게 좋다.
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //then
        List<Member> content = page.getContent(); //조회된 데이터
        long totalElements = page.getTotalElements();


        assertEquals(content.size(), 3); //조회덴 데이터 수
        assertEquals(page.getTotalElements(), 5);  //전체 데이터 수
        assertEquals(page.getNumber(), 0);  //페이지 번호
        assertEquals(page.getTotalPages(), 2);  //전체 페이지 번호
        assertTrue(page.isFirst()); //첫번째 항목인가?
        assertTrue(page.hasNext()); // 다음 항목이 있는가?

        // Pageable 은 주로 pageRequest 로 받고, pageRequest는 자동으로 count까지 해준다.
        // Slice는 count는 안해주고 content를 +1만큼 가져온다.
        // List는 conntent만 가져온다.
    }

    @Test
    public void testBulkUpdate() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);
        em.clear();

        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);

        // bulk 연산은 영속성 컨텍스트를 무시하고 DB에 Query를 날리기 때문에,
        // bulk성 query 후 추가 로직이 남아있다면 강제로 clear()를 해줘서 영속성 컨텍스트를 초기화 시켜주는 것이 좋다..

        //then
        assertEquals(resultCount, 3);
    }
    
    @Test
    public void findMemberLazy() {
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        
        em.flush();
        em.clear();
        
        //when N +1
        //select Member 1
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");
        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass()); // 지연로딩 시  Proxy 객체를 가져옴
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName()); // 실제 Data가 필요해지면 DB에서 값을 가져옴(Proxy 초기화)
        }
    }

    @Test
    public void testQueryHint() {
        //given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush(); // 1차 캐시를 DB에 전송
        em.clear(); // 남아있는 1차 캐시를 모두 제거

        //when
        Member findMember = memberRepository.findReaOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void testLock() {
        //given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush(); // 1차 캐시를 DB에 전송
        em.clear(); // 남아있는 1차 캐시를 모두 제거

        //when
        List<Member> member11 = memberRepository.findLockByUsername("member1");
    }

    @Test
    public void testCallCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }




}