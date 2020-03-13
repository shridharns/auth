package com.shridhar.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shridhar.auth.dao.ClientDAO;

@Repository
public interface ClientRepository extends CrudRepository<ClientDAO, Integer> {}