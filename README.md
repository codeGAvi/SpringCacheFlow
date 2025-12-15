
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