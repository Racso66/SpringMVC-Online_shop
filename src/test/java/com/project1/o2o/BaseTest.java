package com.project1.o2o;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

// configure spring and junit combination, load springIOC container when starting junit
@RunWith(SpringJUnit4ClassRunner.class)
//Tell Junit where spring configuration files are located
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class BaseTest {
	
}
