package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

//Auditing: 감사하다, 감시하다
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
// BaseEntity처럼 시간 뿐만 아니라 작성자 정보까지 넣으면
// Table 마다 작성자는 필요 여부가 갈리기 때문에
// 별도로 BaseTimeEntity를 만들고 이를 상속받아 BaseEntity에 작성자 정보를 넣어
// Table 마다 필요한 Entity를 상속받으면 좋다.