package model.adt;

import java.util.HashMap;
import java.util.Set;

public class MyHeap<K,V> implements IHeap<K,V> {
    private HashMap<K,V> heap;
    private Integer newFree;
    public MyHeap() {
        this.heap = new HashMap<K, V>();
        this.newFree=1;
    }

    public HashMap<K,V> getADT(){
        return this.heap;
    }
    public Integer getNewFree(){
        return newFree;
    }

    public void add(K Id,V val) {
        this.heap.put(Id,val);
        this.newFree++;
    }

    public boolean isKey(K key){
        return heap.containsKey(key);
    }

    public void update(K id, V exp) {
        this.heap.put(id,exp);
    }

    public V get(K id) {
        return this.heap.get(id);
    }

    @Override
    public String toString() {
        return this.heap.toString();
    }

    public void remove(K address) {
        this.heap.remove(address);
    }

    public Set<K> keySet() {
        return heap.keySet();
    }
}
