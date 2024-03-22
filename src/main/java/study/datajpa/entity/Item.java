package study.datajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {

    @Id @GeneratedValue
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
        // id 값만으로는 판단하기 어렵기 때문에 createdDate를 통해
    }

    // id 값은 기본적으로 @GeneratedValue를 통해서 생성해야 SimpleRepository의 로직에서 isNew()로 pk의 null을 기준으로
    // 객체를 판단한다. 하지만 본인이 직접 id값을 넣어야 하는 상황이 온다면 Entity에 Persistable<>을 직접 imple하고 isNew()를
    // 로직에 맞게 오버라이드 해야한다.

}
