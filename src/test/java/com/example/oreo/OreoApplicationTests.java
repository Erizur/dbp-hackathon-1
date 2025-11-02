package com.example.oreo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@SpringBootTest
@Profile("test")
class OreoApplicationTests {

	@Test
	void contextLoads() {
	}

}
