package com.shridhar.auth;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
       TokenServiceTest.class,
       UserServiceTest.class
})
public class ApplicationSuite {
}
