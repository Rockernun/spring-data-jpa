package com.study.data_jpa.repository;

import com.study.data_jpa.dto.MemberDto;
import com.study.data_jpa.entity.Member;
import com.study.data_jpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void basicCrud() {
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 30);
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        List<Member> allMembers = memberRepository.findAll();
        Assertions.assertThat(allMembers.size()).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member1", 30);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMember = memberRepository.findByUsernameAndAgeGreaterThan("member1", 25);
        Assertions.assertThat(findMember.size()).isEqualTo(1);
    }

    @Test
    public void testQuery() {
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 30);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMember = memberRepository.findUser("member1", 20);
        Assertions.assertThat(findMember.get(0)).isEqualTo(member1);
    }

    @Test
    public void findUsernameList() {
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 30);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernameList = memberRepository.findUsernameList();

        for (String s : usernameList) {
            System.out.println("유저명: " + s);
        }
    }

    @Test
    public void findMemberDto() {
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        Member member1 = new Member("member1", 20);
        memberRepository.save(member1);

        member1.changeTeam(teamA);

        List<MemberDto> memberDto = memberRepository.findMemberDto();

        for (MemberDto dto : memberDto) {
            System.out.println("멤버 DTO: " + dto.toString());
        }
    }

    @Test
    public void returnType() {
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 30);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMemberList = memberRepository.findListByUsername("member1");
        Member findMember = memberRepository.findMemberByUsername("member2");
        Optional<Member> optionalByUsername = memberRepository.findOptionalByUsername("ghost-member");

        Assertions.assertThat(findMemberList.get(0)).isEqualTo(member1);
        Assertions.assertThat(findMember).isEqualTo(member2);
        Assertions.assertThat(optionalByUsername).isEmpty();
    }

    @Test
    public void paging1() {
        memberRepository.save(new Member("member1", 20));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 30));
        memberRepository.save(new Member("member5", 30));
        memberRepository.save(new Member("member6", 40));

        int age = 20;
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Direction.DESC, "username"));
        Page<Member> pageByAge = memberRepository.findByAge(age, pageRequest);
        Page<MemberDto> toMap = pageByAge.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        List<Member> content = pageByAge.getContent();
        long totalCount = pageByAge.getTotalElements();

        Assertions.assertThat(content.size()).isEqualTo(2);
        Assertions.assertThat(totalCount).isEqualTo(6);
        Assertions.assertThat(pageByAge.getNumber()).isEqualTo(0);
        Assertions.assertThat(pageByAge.getTotalPages()).isEqualTo(3);
        Assertions.assertThat(pageByAge.isFirst()).isTrue();
        Assertions.assertThat(pageByAge.hasNext()).isTrue();
    }

    @Test
    public void paging2() {
        memberRepository.save(new Member("member1", 20));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 30));
        memberRepository.save(new Member("member5", 30));
        memberRepository.save(new Member("member6", 40));

//        int age = 20;
//        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Direction.DESC, "username"));
//        Slice<Member> pageByAge = memberRepository.findByAge(age, pageRequest);
//
//        List<Member> content = pageByAge.getContent();
//
//        Assertions.assertThat(content.size()).isEqualTo(2);
//        Assertions.assertThat(pageByAge.getNumber()).isEqualTo(0);
//        Assertions.assertThat(pageByAge.isFirst()).isTrue();
//        Assertions.assertThat(pageByAge.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 18));
        memberRepository.save(new Member("member3", 19));
        memberRepository.save(new Member("member4", 20));
        memberRepository.save(new Member("member5", 25));
        memberRepository.save(new Member("member6", 30));

        int resultCount = memberRepository.bulkAgePlus(20);
//        em.flush();
//        em.clear();

        Member member5 = memberRepository.findMemberByUsername("member5");
        System.out.println("member5의 나이는 " + member5.getAge() + "세입니다.");  // 영속성 컨텍스트를 날리지 않은 경우: "member5의 나이는 25세입니다." 출력

        Assertions.assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 11, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> allMembers = memberRepository.findAll();

        for (Member member : allMembers) {
            System.out.println("멤버명: " + member.getUsername());
            System.out.println("멤버의 클래스 정보: " + member.getClass());
            System.out.println("팀의 클래스 정보: " + member.getTeam().getClass());
            System.out.println("멤버의 팀명: " + member.getTeam().getName());
        }

        /**
         * (N + 1) 문제
         *
         * select
         *     m1_0.member_id,
         *     m1_0.age,
         *     m1_0.team_id,
         *     m1_0.username
         * from
         *     member m1_0
         *
         * select
         *     t1_0.team_id,
         *     t1_0.name
         * from
         *     team t1_0
         * where
         *     t1_0.team_id=?
         *
         * select
         *     t1_0.team_id,
         *     t1_0.name
         * from
         *     team t1_0
         * where
         *     t1_0.team_id=?
         *
         * 멤버명: member1
         * 멤버의 클래스 정보: class com.study.data_jpa.entity.Member
         * 팀의 클래스 정보: class com.study.data_jpa.entity.Team$HibernateProxy
         * 멤버의 팀명: teamA
         *
         * 멤버명: member2
         * 멤버의 클래스 정보: class com.study.data_jpa.entity.Member
         * 팀의 클래스 정보: class com.study.data_jpa.entity.Team$HibernateProxy
         * 멤버의 팀명: teamB
         */
    }

    @Test
    public void findMemberWithFetchJoin() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 11, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> allMembers = memberRepository.findMemberFetchJoin();

        for (Member member : allMembers) {
            System.out.println("멤버명: " + member.getUsername());
            System.out.println("멤버의 클래스 정보: " + member.getClass());
            System.out.println("팀의 클래스 정보: " + member.getTeam().getClass());
            System.out.println("멤버의 팀명: " + member.getTeam().getName());
        }

        /**
         * select
         *     m1_0.member_id,
         *     m1_0.age,
         *     t1_0.team_id,
         *     t1_0.name,
         *     m1_0.username
         * from
         *     member m1_0
         * left join
         *     team t1_0
         *         on t1_0.team_id=m1_0.team_id
         *
         * 멤버명: member1
         * 멤버의 클래스 정보: class com.study.data_jpa.entity.Member
         * 팀의 클래스 정보: class com.study.data_jpa.entity.Team
         * 멤버의 팀명: teamA
         *
         * 멤버명: member2
         * 멤버의 클래스 정보: class com.study.data_jpa.entity.Member
         * 팀의 클래스 정보: class com.study.data_jpa.entity.Team
         * 멤버의 팀명: teamB
         */
    }

    @Test
    public void findMemberWithEntityGraph() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 11, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> allMembers = memberRepository.findAll();

        for (Member member : allMembers) {
            System.out.println("멤버명: " + member.getUsername());
            System.out.println("멤버의 클래스 정보: " + member.getClass());
            System.out.println("팀의 클래스 정보: " + member.getTeam().getClass());
            System.out.println("멤버의 팀명: " + member.getTeam().getName());
        }

        /**
         * select
         *     m1_0.member_id,
         *     m1_0.age,
         *     t1_0.team_id,
         *     t1_0.name,
         *     m1_0.username
         * from
         *     member m1_0
         * left join
         *     team t1_0
         *         on t1_0.team_id=m1_0.team_id
         *
         * 멤버명: member1
         * 멤버의 클래스 정보: class com.study.data_jpa.entity.Member
         * 팀의 클래스 정보: class com.study.data_jpa.entity.Team
         * 멤버의 팀명: teamA
         *
         * 멤버명: member2
         * 멤버의 클래스 정보: class com.study.data_jpa.entity.Member
         * 팀의 클래스 정보: class com.study.data_jpa.entity.Team
         * 멤버의 팀명: teamB
         */
    }

    @Test
    public void queryHint() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void lock() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        List<Member> findMembers = memberRepository.findLockByUsername("member1");

        /**
         * select
         *     m1_0.member_id,
         *     m1_0.age,
         *     m1_0.team_id,
         *     m1_0.username
         * from
         *     member m1_0
         * where
         *     m1_0.username=?
         * for
         *     update
         */
    }
}
