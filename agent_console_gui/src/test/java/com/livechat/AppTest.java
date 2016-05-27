package com.livechat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class AppTest {
	private static final Logger log = LoggerFactory.getLogger(AppTest.class);


	private Application application;

	@Test
	public void testApp() {
		application = new Application();
		Assert.assertNotNull(application);
	}
}
