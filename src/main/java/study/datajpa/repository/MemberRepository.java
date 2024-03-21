package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

//핵심 비지니스 로직과 화면에 맞춘 복잡한 로직은 분리하는 것이 좋다. (라이프 사이클이 다르다!)
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    // jpql에 :필드가 있으면 @Param을 넣어줘야함
    // 해당 엔티티에 nameQuery가 있으면 찾고, 없으면 query를 만듦 따라서 @Query 생략 가능! 하지만 실무에서는 거의 안씀(불편)
//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
    // @Query 장점
    // NamedQuery처럼 애플리케이션 로딩 시점에 문법 오류를 발견할 수 있음!

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); //컬렉션
    Member findMemberByUsername(String username); //단건
    Optional<Member> findOptionalByUsername(String username); //단건 Optional

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
    //totalCount는 모든 데이터를 가져오는데 query가 복잡해질수록 성능이 기하급수적으로 안좋아진다.
    //하지만 totalCount는 left join을 하지 않아도 그 수가 같기 때문에 @Query를 통해 별도로 분리한다.
    // 참고: sort도 조건이 복잡하면 잘 안풀리기 때문에 그 경우엔 @Qeury 안에 넣는 것도 좋다.


    // bulk성 업데이트나 삭제는 @Modifying을 붙이지 않으면 에러가 터진다.
    // bulk 연산은 영속성 컨텍스트를 무시하고 DB에 Query를 날리기 때문에, bulk성 query 후 추가 로직이 남아있다면 강제로 clear()를 해줘야한다.
    // 이를 @Modifying에서 clearAutomatically를 true로 해주면 자동으로 clear를 해준다!
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    // @EntityGraph를 사용하면 위 메서드처럼 jpql을 사용하지 않고도 fetch join을 사용할 수 있다.
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member>findAll();

    // jpql과 같이 사용 가능
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // 메소드 이름 쿼리와도 사용 가능
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);
    // 간단할 때 @EntityGraph를 쓰고 복잡해지면 jpql로 fetch join을 쓴다.


    // Query Hint
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReaOnlyByUsername(String username);
    // JPA는 더티체킹을 위해 스냅샷을 만들어 두는데 @QueryHint를 통해 readOnly를 true로 주면,
    // 스냅샷을 만들지 않기 때문에 더티체킹에 의한 변경이 이뤄지지 않는다.
    // QueryHint를 통해 최적화를 할 수 있지만, 특정 복잡한 API만 유의미한 최적화가 이뤄지기 때문에 모든 조회에 넣을 필요가 없다.
    // 또한 대규모 트래픽이 유발되는 API라면 캐시가 더 필요하기 때문에 성능 테스트를 해보고 적용하자.

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

}
