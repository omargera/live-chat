package com.livechat.api;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

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

@Nonnull
public class AgentManagerImplTest {
	private static final Logger log = LoggerFactory.getLogger(AgentManagerImplTest.class);

	private AgentManager agentManager;

	@Before
	public void setUp() {
		agentManager = new AgentManagerImpl();
		
		checkNotNull(agentManager, "Expected not null app");
	}

	@Test
	public void testAgentManager() {
		log.debug(agentManager.test());
		Assert.assertEquals("Test function works", agentManager.test());
	}
}
