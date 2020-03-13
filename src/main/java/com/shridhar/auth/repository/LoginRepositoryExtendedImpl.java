package com.shridhar.auth.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.shridhar.auth.dao.RoleDAO;
import com.shridhar.auth.dao.UserDAO;

@Repository
@Transactional
public class LoginRepositoryExtendedImpl implements LoginRepositoryExtended {

    @PersistenceContext
    EntityManager entityManager;

    public UserDAO login(String email) {

        Query query = entityManager.createNativeQuery("SELECT u.* FROM user as u "
                + "WHERE u.email = ?", UserDAO.class);

        query.setParameter(1, email);
        
        if (query.getResultList().size() > 0) {
            return (UserDAO) query.getSingleResult();
        } else {
            return null;
        }
    }
    
    public RoleDAO getRole(Integer id) {
    	
    	Query query = entityManager.createQuery("SELECT r FROM roles r, users_roles ur "
    			+ "where ur.user_id= ? AND ur.role_id = r.id", RoleDAO.class);

        query.setParameter(1, id);
        
        Object result = query.getSingleResult();
        
        System.out.println(result.toString());
        
        if (result != null) {
            return (RoleDAO) result;
        } 
        
        return null;
    }
}
