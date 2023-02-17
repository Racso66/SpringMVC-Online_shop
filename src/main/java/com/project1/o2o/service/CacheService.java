package com.project1.o2o.service;

public interface CacheService {
	/**
	 * remove all key-value which matches keyPrefix ex. if keyPrefix = shopcategory, 
	 * then all values under key with prefix shopcategory will be emptied (shopcategory_allfirstlevel etc.)
	 * @param keyPrefix
	 */
	void removeFromCache(String keyPrefix);
}
