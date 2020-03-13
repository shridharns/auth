package com.shridhar.auth.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "CERTIFICATES")
@Data
public class CertificatesDAO {

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ID")
	private String id;

	@Column(name = "privateKey", length = 2000)
	private String privateKey;

	@Column(name = "publicKey", length = 10000)
	private String publicKey;

	@Column(name = "valid_until")
	private Date validUntil;

}
