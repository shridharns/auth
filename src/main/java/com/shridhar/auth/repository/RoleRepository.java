package com.shridhar.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shridhar.auth.dao.RoleDAO;

public interface RoleRepository extends JpaRepository<RoleDAO, Long> {

    RoleDAO findByName(String name);

    @Override
    void delete(RoleDAO role);

}