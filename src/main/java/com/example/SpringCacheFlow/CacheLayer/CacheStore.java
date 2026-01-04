package com.example.SpringCacheFlow.CacheLayer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class CacheStore {
    int cacheLimit = 10;  // cache can only hold 10 entries (key → product) at a time.”
    private static final long TTL = 20000; // 20 SECOND

    // make a LRU(Doubly LL)
    public class Node{
        @Setter
        @Getter
        int key;
        @Getter
        @Setter
        CacheEntry cacheEntry;
        Node next;
        Node prev;

        // constructor
        public Node(int key,CacheEntry cacheEntry){
            this.key = key;
            this.cacheEntry = cacheEntry;
            next = prev = null;
        }

    }
    // create 2 dummy pointer node
    Node head = new Node(-1,null);
    Node tail = new Node(-1,null);
        // connect head with tail
     public  CacheStore(){
         head.next = tail;
         tail.prev = head;
     }

    // create a hashmap of key value pair to fast-lookup
    HashMap<Integer,Node> map = new HashMap<>();

     // function for addNode at starting point of DLL
    public void addNode(Node newCacheEntryNode){
        Node oldNext = head.next;
        head.next = newCacheEntryNode;
        oldNext.prev = newCacheEntryNode;
        newCacheEntryNode.next = oldNext;
        newCacheEntryNode.prev = head;
    }

    // function for deleteNode
    public void deleteNode(Node oldCacheEntryNode){
        Node oldPrev = oldCacheEntryNode.prev;
        Node oldNext = oldCacheEntryNode.next;
        oldPrev.next = oldNext;
        oldNext.prev = oldPrev;
    }

    // add node to Head(Always) to keet new entry MSU(most recent search)
    public void addCacheEntry(int key, CacheEntry cacheEntry){

        long expiryTime = System.currentTimeMillis()+TTL;   // calculate expiry time

        cacheEntry.setExpiryTime(expiryTime); // update CacheEntry with expiry time

        if(map.containsKey(key)){  // if key exists
            // then remove entry for this particular key
            Node  oldCacheEntryNode = map.get(key);
            deleteNode(oldCacheEntryNode);
            map.remove(key);
        }
        // if cache is full then remove from entry from tail(remove LRU)
        if(map.size() == cacheLimit ){
            map.remove(tail.prev.key);
            deleteNode(tail.prev);
        }
        // now add new Entry with updated CacheEntry
        Node newCacheEntryNode = new Node(key, cacheEntry);
        addNode(newCacheEntryNode);
        map.put(key,newCacheEntryNode);
    }


    // Implementing getCacheEntry by key
    public Node getEntry(int key){
        if(!map.containsKey(key)) return null;  // cache miss
        Node getCacheEntry = map.get(key);

        // TTL Check  // expired or not
        if(getCacheEntry.cacheEntry.isExpired()){ // Immediate eviction of expired entries if expired and return null
            deleteNode(getCacheEntry); // Remove node from DLL/ Remove key from HashMap /Return null
            map.remove(key);
            return null;
        }
        // if not expired --> then move it at MSU
        deleteNode(getCacheEntry);  // delete from current position
        addNode(getCacheEntry);   // and add at head of DLL

        // now return entry
        return getCacheEntry;
    }


    // Implementing Spring Scheduler
    @Scheduled(fixedRate = 10000) // scan every 10 seconds
    public synchronized void cleanUpExpiredEntries(){
        System.out.println("Scheduler running.... checking for expired entries ");

        // Approach : 1
//        // now scan over hashmap bcz map contains node and inside node there is cache-entry
//        for(Node node:map.values()){
//            if(node.getCacheEntry().isExpired()){
//                System.out.println("Evicting the expired entries with key" + node.getKey());
//
//                // now remove from DLL and from HashMap also
//                deleteNode(node);
//                map.remove(node.key);
//            }
//        }
        // in appraoch 1 , i'm iterating over map.values and (line 114) and at the same time i'm also doing modification
                    // in map(map.remove.... line 120) , so this will cause ConcurrentModificationException bcz ..
                 ///  --> Java collections do not allow structural modification while iterating using for-each.


        // Appproach-2 using Iterator
        Iterator<Map.Entry<Integer,Node>>iterator = map.entrySet().iterator();
        while(iterator.hasNext()){
         Map.Entry<Integer,Node>nodeEntry = iterator.next();
         Node node = nodeEntry.getValue();

              if(node.getCacheEntry().isExpired()){
                  System.out.println("Evicting the expired entries with key" + node.getKey());

                  // now delete from DLL & HashMap also
                  deleteNode(node);
                  iterator.remove();
              }
        }
    }
}