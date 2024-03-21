package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;

// @Setter보다 아래와 같이 필요한 메서드를 구현하는 게 좋은 방법
//    public void changeUsername(String username) {
//        this.username = username;
//    }
@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA는 기본생성자가 필수이다. JPA는 Proxy를 쓰는데 private로 하면 가져다 쓸수 없기 때문에 최소 protected로 열어놔야한다.
@ToString(of = {"id", "username", "age"}) //연관관계 필드를 toString하면 무한루프를 돌 수 있기 때문에 지양하는게 좋음
@NamedQuery(
        name="Member.findByUsername",
        query="select m from Member m where m.username = :username")
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY) //default: FetchType.EAGER 이므로 지연로딩으로 바꿔줘야한다.
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null) {
            changeTeam(team);
        }
    }

    // 연관관계 메서드
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
