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
}
