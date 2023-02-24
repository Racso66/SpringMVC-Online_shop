package com.project1.o2o.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolWrapper {
	// Redis connection pool target
	private JedisPool pool;
	
	public JedisPoolWrapper(final JedisPoolConfig poolConfig, final String host, final int port) {
		try {
			pool = new JedisPool(poolConfig, host, port);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public JedisPool getPool() {
		return pool;
	}

	public void setPool(JedisPool pool) {
		this.pool = pool;
	}
	
	
}
