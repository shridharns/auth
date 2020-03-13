package com.shridhar.auth.events;

import org.springframework.context.ApplicationEvent;

public class LoginEvent extends ApplicationEvent {
    
	private static final long serialVersionUID = 1L;

	private String email;
	
	private boolean status;
 
    public LoginEvent(Object source, String email, boolean status) {
        super(source);
        this.email = email;
        this.status = status;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public boolean getStatus() {
        return this.status;
    }
}