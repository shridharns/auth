package com.shridhar.auth.constants;

public interface Constants {

	public static final String DEBUG = "debug";

    public static final String X_DEBUG = "x-debug";
    
    public static final String READ_PRIVILEGE = "READ_PRIVILEGE";
    
    public static final String WRITE_PRIVILEGE = "WRITE_PRIVILEGE";
    
    public static final String ROLE_USER = "ROLE_USER";
    
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    
    public static final String[] REQUIRED_CLAIMS = { "sub", "aud", "iat", "exp" };

	public static final String GIVEN_NAME = "given_name";

	public static final String FAMILY_NAME = "family_name";
	
	public static final String EMAIL = "email";

	public static final String ID_TOKEN = "id";
	
	public static final String IDP = "https://shridhar.com";

	public static final String SCOPE = "scope";

	public static final String ROLE = "role";

	
}
