package study.datajpa.repository;

import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}

// 핵심 비지니스 로직과 화면에 맞춘 복잡한 로직은 분리하는 것이 좋다.
// 1. 레파지토리가 너무 복잡해지면 핵심 비지니스 로직을 구분하여 쓰기 어렵다.
// 2. 핵심 로직과 화면에 맞춘 복잡한 로직은 라이프 사이클이 다르다!