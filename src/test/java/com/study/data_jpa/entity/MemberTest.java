package com.study.data_jpa.entity;

import com.study.data_jpa.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

//        Member member1 = new Member("member1", 25, teamA);
//        Member member2 = new Member("member2", 30, teamA);
//        Member member3 = new Member("member3", 31, teamB);
//        Member member4 = new Member("member4", 35, teamB);

//        em.persist(member1);
//        em.persist(member2);
//        em.persist(member3);
//        em.persist(member4);

        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : members) {
            System.out.println("Member: " + member);
            System.out.println("Member's Team -> " + member.getTeam());
        }
    }

    @Test
    public void jpaEventBaseEntity() throws Exception {
        Member member1 = new Member("member1", 20);
        memberRepository.save(member1);

        Thread.sleep(100);
        member1.setUsername("changedName");

        em.flush();
        em.clear();

        Member member = memberRepository.findById(member1.getId()).get();
        System.out.println("Member created Date: " + member.getCreatedDate());
        System.out.println("Member updated Date: " + member.getLastModifiedDate());
        System.out.println("Member created by: " + member.getCreatedBy());
        System.out.println("Member last modified by: " + member.getLastModifiedBy());

        /**
         * Member created Date: 2026-09-27T23:31:53.975033
         * Member updated Date: 2026-09-27T23:31:54.084407
         * Member created by: a6e233af-fca7-4367-803c-45b0d1a15b46
         * Member last modified by: 27c318fe-a678-4945-aa2e-ce9879698046
         */
    }
}
