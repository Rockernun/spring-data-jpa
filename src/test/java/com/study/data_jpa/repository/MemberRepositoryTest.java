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
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

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
}
