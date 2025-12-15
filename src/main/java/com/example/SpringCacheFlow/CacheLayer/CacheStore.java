package com.example.SpringCacheFlow.CacheLayer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
@Component
public class CacheStore {
    int cacheLimit = 100;  // cache can only hold 100 entries (key → product) at a time.”
    private static final long TTL = 30000; // 30 SECOND

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

}