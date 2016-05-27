package com.livechat.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.livechat.adapter.AgentManager;
import com.livechat.adapter.liveperson.LPAgentManagerImpl;

public class AgentManagerImplTest {
	private static final Logger log = LoggerFactory.getLogger(AgentManagerImplTest.class);

	private AgentManager agentManager;

	@Before
	public void setUp() {
		agentManager = new LPAgentManagerImpl();
		
		
	}

	@Test
	public void testAgentManager() {
		log.debug(agentManager.test());
		Assert.assertEquals("Test function works", agentManager.test());
	}
}
