package com.yKul.privateclub;

import com.yKul.privateclub.controller.GuestController;
import com.yKul.privateclub.service.GuestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PrivateClubApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private GuestController guestController;

	@Autowired
	private GuestService guestService;

	@Test
	void contextLoads() {
		assertThat(applicationContext).isNotNull();
		assertThat(guestController).isNotNull();
		assertThat(guestService).isNotNull();
	}
}