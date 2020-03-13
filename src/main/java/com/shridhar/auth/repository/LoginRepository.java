package com.shridhar.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shridhar.auth.dao.UserDAO;

@Repository
public interface LoginRepository extends JpaRepository<UserDAO,Long>, LoginRepositoryExtended {

}
