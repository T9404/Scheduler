package com.example.demo;

import com.example.demo.controller.HomePageController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class JUnitControllerTest  {

	@Test
	public void testHomeController() {
		HomePageController homePageController = new HomePageController();
		String result = homePageController.home();
		assertEquals(result, "Server is work");
	}
}
