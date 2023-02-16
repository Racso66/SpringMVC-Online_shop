package com.project1.o2o.cache;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.util.SafeEncoder;

public class JedisUtil {
	private JedisPool pool;
	//Basic redis data storage type
	public Strings STRINGS;
	// check if key exists, decide operation on values in the key
	public Keys KEYS;
	
	public JedisPool getPool() {
		return pool;
	}
	public void setPool(JedisPoolWrapper poolWrapper) {
		this.pool = poolWrapper.getPool();
	}
	/*
	 * Get Jedis target from connection pool
	 */
	public Jedis getJedis() {
		return pool.getResource();
	}
	
	/************************** KEY ***********************/
	public class Keys {
		/*
		 * flush keys
		 */
		public String flushAll() {
			Jedis jedis = getJedis();
			// Data on all keys flushed
			String keyStringData = jedis.flushAll();
			jedis.close();
			return keyStringData;
		}
		
		/*
		 * Delete specified key(s)
		 */
		public long del(String... keys) {
			Jedis jedis = getJedis();
			// amount of keys deleted
			long count = jedis.del(keys);
			jedis.close();
			return count;
		}
		
		/*
		 * Check existence of key
		 */
		public boolean exists(String key) {
			Jedis jedis = getJedis();
			boolean existence = jedis.exists(key);
			jedis.close();
			return existence;
		}
		
		/*
		 * lookup all keys with specified pattern (* for all, ? for one. similar to natural language
		 */
		public Set<String> keys(String pattern){
			Jedis jedis = getJedis();
			Set<String> set = jedis.keys(pattern);
			jedis.close();
			return set;
		}
	}
	
	/************************** Strings ***********************/
	
	public class Strings {
		/*
		 * get value through key
		 */
		public String get(String key) {
			Jedis jedis = getJedis();
			String value = jedis.get(key);
			jedis.close();
			return value;
		}
		
		/*
		 * Set key value, overwrites original value
		 */
		public String set(String key, String value) {
			return set(SafeEncoder.encode(key), SafeEncoder.encode(value));
		}
		
		public String set(byte[] key, byte[] value) {
			Jedis jedis = getJedis();
			String status = jedis.set(key, value);
			jedis.close();
			return status;
		}
	}
}
