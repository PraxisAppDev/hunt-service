package com.afterhoursdev.scavengerhunt.hunt.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

/*
 * The CacheConfig class enables Caffeine caching. Caffeine is a 
 * high-performance, memory-efficient open source library used to
 * cache method results, reduce database calls, and improve performance.
 *   
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

@Configuration
@EnableCaching
public class CacheConfig { 
	
	@Value("${hunt.cache.name}") 
	private String huntCacheName;
	
	@Value("${hunt.cache.maxSize}") 
	private int huntCacheMaxSize;
	
	@Value("${hunt.cache.expiryHours}") 
	private int huntCacheExpiryHours;
	
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(huntCacheName);
        cacheManager.setCaffeine(Caffeine.newBuilder()
             .maximumSize(huntCacheMaxSize)     
             .expireAfterWrite(huntCacheExpiryHours, TimeUnit.HOURS)  
             .softValues()     // Allow GC to remove cached Hunt objects when memory pressured
             .recordStats());  // Enable cache statistics
        return cacheManager;
    }
}

/*
 have a scheduled task to send these stats to event bus metrics topic
 make sure to enable scheduling in the App class @EnableScheduling
 If you are using Prometheus, this tool can collect stats without a scheduled task.

@Component
public class CacheMonitor {
    private final Cache<String, String> cache;

    public CacheMonitor(Cache<String, String> cache) {
        this.cache = cache;
    }

    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void logCacheStats() {
        CacheStats stats = cache.stats();
        System.out.println("Cache Stats - Hit Rate: " + stats.hitRate() +
                           ", Miss Count: " + stats.missCount() +
                           ", Eviction Count: " + stats.evictionCount());
    }
}
*/
