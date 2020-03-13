package com.shridhar.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shridhar.auth.dao.CertificatesDAO;

@Repository
public interface CertificateRepository extends CrudRepository<CertificatesDAO, Integer> {}