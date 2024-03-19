package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Team;

// JpaRepository를 상속받으면 @Repository 생략 가능
//어노테이션의 기능: 1. 컴포넌트 스캔 2. 예외를 공통적으로 처리할 수 있는 예외로 변환
public interface TeamRepository extends JpaRepository<Team, Long> {
}
