package com.study.data_jpa.repository;

import com.study.data_jpa.entity.Member;
import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> findMemberCustom();
}
