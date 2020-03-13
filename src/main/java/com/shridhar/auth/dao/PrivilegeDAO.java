package com.shridhar.auth.dao;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRIVILEGE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegeDAO {
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
    private String name;
 
    @ManyToMany(mappedBy = "privileges")
    private Collection<RoleDAO> roles;
    
    public PrivilegeDAO(String name) {
    	this.name = name;
    }
}