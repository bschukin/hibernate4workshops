package com.boris.repository;

import com.boris.model.oneToN.A;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterARepository extends JpaRepository<A, Integer>{
}
