package com.abit;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
public class BaseServiceTest {
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("spring.cloud.service-registry.auto-registration.enabled", "false");
	}
}
