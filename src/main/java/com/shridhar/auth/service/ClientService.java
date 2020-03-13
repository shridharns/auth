package com.shridhar.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.shridhar.auth.dao.ClientDAO;
import com.shridhar.auth.repository.ClientRepository;

@Service
public class ClientService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

	@Autowired
	ClientRepository clientRepository;

	@Cacheable("clients")
	public List<ClientDAO> getAllClients() {
		List<ClientDAO> clientList = new ArrayList<>();
		clientRepository.findAll().forEach(client -> clientList.add(client));
		LOGGER.info("Returning from database");
		return clientList;
	}

	public ClientDAO getClientById(int id) {
		return clientRepository.findById(id).get();
	}

	@CacheEvict(value = "clients", allEntries = true)
	public void saveOrUpdate(ClientDAO client) {
		clientRepository.save(client);
	}

	@CacheEvict(value = "clients", key = "#id")
	public void delete(int id) {
		clientRepository.deleteById(id);
	}

}
