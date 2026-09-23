package com.study.data_jpa.repository;

import com.study.data_jpa.entity.Member;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

//    @Test
//    public void testMember() {
//        Member member = new Member("test-member-1", 20);
//        Member savedMember = memberJpaRepository.save(member);
//
//        Member findMember = memberJpaRepository.findById(savedMember.getId());
//        Assertions.assertThat(findMember.getId()).isEqualTo(savedMember.getId());
//        Assertions.assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
//        Assertions.assertThat(findMember).isEqualTo(savedMember);
//    }
//
//    @Test
//    public void testMemberExtendsJpaRepository() {
//        Member member = new Member("test-member-1", 20);
//        Member savedMember = memberRepository.save(member);
//        Member findMember = memberRepository.findById(savedMember.getId()).orElseThrow();
//
//        Assertions.assertThat(findMember.getId()).isEqualTo(savedMember.getId());
//        Assertions.assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
//        Assertions.assertThat(findMember).isEqualTo(savedMember);
//    }

    @Test
    public void basicCrud() {
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 30);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        List<Member> allMembers = memberJpaRepository.findAll();
        Assertions.assertThat(allMembers.size()).isEqualTo(2);

        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        long count = memberJpaRepository.count();
        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member1", 30);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> findMember = memberJpaRepository.findByUsernameAndAgeGreaterThan("member1", 25);
        Assertions.assertThat(findMember.size()).isEqualTo(1);
    }

    @Test
    public void paging() {
        memberJpaRepository.save(new Member("member1", 20));
        memberJpaRepository.save(new Member("member2", 20));
        memberJpaRepository.save(new Member("member3", 20));
        memberJpaRepository.save(new Member("member4", 30));
        memberJpaRepository.save(new Member("member5", 30));
        memberJpaRepository.save(new Member("member6", 40));

        List<Member> findByPage = memberJpaRepository.findByPage(20, 0, 2);
        long totalCount = memberJpaRepository.totalCount(20);

        Assertions.assertThat(findByPage.size()).isEqualTo(2);
        Assertions.assertThat(totalCount).isEqualTo(3);
    }
}