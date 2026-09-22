package com.study.data_jpa.repository;

import com.study.data_jpa.dto.MemberDto;
import com.study.data_jpa.entity.Member;
import com.study.data_jpa.entity.Team;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

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
}
