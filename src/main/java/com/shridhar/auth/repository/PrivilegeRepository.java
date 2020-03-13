package com.shridhar.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shridhar.auth.dao.PrivilegeDAO;

public interface PrivilegeRepository extends JpaRepository<PrivilegeDAO, Long> {

    PrivilegeDAO findByName(String name);

    @Override
    void delete(PrivilegeDAO privilege);

}