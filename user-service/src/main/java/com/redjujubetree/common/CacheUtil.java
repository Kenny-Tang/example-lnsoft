package com.redjujubetree.common;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CacheUtil {
	private static Boolean cacheEnabled = false;
	static class Entry {
		Object value;
		long expireAt;
		Entry(Object v, long e) { value = v; expireAt = e; }
	}

	private static final ConcurrentHashMap<String, Entry> cache = new ConcurrentHashMap<>();

	public static void put(String key, Object value, long ttl) {
		put(key, value, ttl, TimeUnit.HOURS);
	}

	public static void put(String key, Object value, long ttl, TimeUnit unit) {
		TimeUnit timeUnit = Optional.ofNullable(unit).orElse(TimeUnit.MILLISECONDS);
		if (timeUnit == TimeUnit.SECONDS) {
			ttl *= 1000; // Convert seconds to milliseconds
		} else if (timeUnit == TimeUnit.MINUTES) {
			ttl *= 60 * 1000; // Convert minutes to milliseconds
		} else if (timeUnit == TimeUnit.HOURS) {
			ttl *= 60 * 60 * 1000; // Convert hours to milliseconds
		} else if (timeUnit == TimeUnit.DAYS) {
			ttl *= 24 * 60 * 60 * 1000; // Convert days to milliseconds
		}
		cache.put(key, new Entry(value, System.currentTimeMillis() + ttl));
	}

	public static  <T> T get(String key) {
		if (!cacheEnabled) {
			return null; // Cache is disabled
		}
		Entry entry = cache.get(key);
		if (entry == null || System.currentTimeMillis() > entry.expireAt) {
			cache.remove(key);
			return null;
		}
		return (T) entry.value;
	}

	public static void removeKeyPrefix(String keyPattern) {
		cache.keySet().removeIf(key -> key.startsWith(keyPattern));
	}
}
