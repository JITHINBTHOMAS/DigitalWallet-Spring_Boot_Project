package com.ust.wallet.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ust.wallet.model.Members;
@Repository
public interface MemberRepo extends JpaRepository<Members, Long>{

}
