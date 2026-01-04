
🏗️ Architecture
Client → Controller → Service → CacheStore + Repository → MySQL
↓
(Cache HIT or MISS)


Final Flow Diagram
Client → Controller → Service → CacheStore → (if miss) → Repository → MySQL
↑
Cache hit → Return fast 🚀

💡 Implemented a cache-aside design pattern in a Spring Boot e-commerce service. 
   Built a custom in-memory caching system (using HashMap + LRU + TTL) to reduce DB calls and improve performance.


Flow:

Request product by ID

If found in cache → return immediately (⚡fast)

If not found → fetch from MySQL → store in cache → return response

Expired items (based on TTL) are auto-removed  // will do it later 

// add docker
I created a Dockerfile to containerize my Spring Boot application,
allowing it to run consistently across different systems without installing Java or dependencies manually.


// #Spring Scheduler 
A background job that periodically scan and  removes expired cache entries, even if no API call happens.
   ....> Scheduler’s responsibility = cache cleanup
    ....> TTL = 20 seconds
        Scheduler = every 10 seconds
        Why this is good: Expired entries get cleaned quickly, Memory doesn’t hold stale data
Rule of thumb;
Scheduler interval ≤ TTL

🧠 Final Architecture:
cache now supports:
Feature	                      Status
 LRU eviction	                ✅
 TTL expiry	                    ✅
 Lazy eviction (on get)	        ✅
 Proactive eviction (scheduler)	✅    // AUTOMATICALLY Scan and remove expired entries using spring scheduler
 Single JVM in-memory cache	    ✅
 Spring-managed background job	✅



