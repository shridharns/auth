package com.shridhar.auth.dao;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CLIENT")
@Data
public class ClientDAO {

    @Id
    @GeneratedValue(generator = "gen")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "clientkey")
    private String clientkey;

    @Column(name = "secret")
    private String secret;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "valid_until")
    private Date validUntil;

}
