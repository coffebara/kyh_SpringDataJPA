package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {
    @Value("#{target.username + ' ' + target.age}") // 오픈 프로젝션: 엔티티를 모두 가져와서 필요한 것을 뽑는 것
    String getUsername(); // 이것만 하면 클로즈 프로젝션: 처음부터 필요한것만 가져오는 것
}
// interface 기반의