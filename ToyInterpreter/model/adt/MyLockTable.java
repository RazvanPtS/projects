package model.adt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class MyLockTable<K,V> implements ILock<K,V> {
    private HashMap<K,V> lockTable;
    private int freeL;

    public MyLockTable() {
        this.lockTable = new HashMap<K, V>();
        this.freeL=0;
    }

    public synchronized void add(K id, V valType) {
        this.lockTable.put(id,valType);
        this.freeL++;
    }

    public V getValue(K key)
    {
        return this.lockTable.get(key);
    }

    public HashMap<K,V> getADT(){
        return this.lockTable;
    }
    public Set<K> getKeys()
    {
        return lockTable.keySet();
    }

    public boolean isDefined(K id) {
        try {
            if (this.lockTable.get(id) == null)
                return false;
            return true;
        }
        catch(NullPointerException ex) {
            return false;
        }
    }
    public synchronized void update(K id, V exp) {
        this.lockTable.put(id,exp);
    }

    public V get(K id) {
        return this.lockTable.get(id);
    }

    @Override
    public String toString() {
        return this.lockTable.toString();
    }

    public synchronized void remove(K toString) {
        this.lockTable.remove(toString);
    }

    public Collection<V> values() {
        return lockTable.values();
    }
    public int getFreeL(){return this.freeL;}
}

