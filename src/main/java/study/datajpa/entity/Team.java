package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA는 기본생성자가 필수이다. JPA는 Proxy를 쓰는데 private로 하면 가져다 쓸수 없기 때문에 최소 protected로 열어놔야한다.
@ToString(of = {"id", "name"})
public class Team {

    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team") // foreign key가 없는 쪽에 mappedBy 지정
    private List<Member> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
