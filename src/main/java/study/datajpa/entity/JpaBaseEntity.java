package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

// @MappedSuperclass 진짜 상속관계가 아닌, 데이터만 공유하는 어노테이션
@MappedSuperclass
@Getter
public class JpaBaseEntity {

    @Column(updatable = false) //수정 불가
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist //Persist 하기 전에 호출
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now; // null 이 아니라 값을 넣어두는게 Query를 짤때나 여로모로 편리함.
    }

    @PreUpdate //Update 하기 전에 호출
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }

}
