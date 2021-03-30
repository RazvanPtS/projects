package model.adt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MyDict<K,V> implements IDict<K,V> {
    private HashMap<K,V> symTable;

    public MyDict() {
        this.symTable = new HashMap<K, V>();
    }

    public void add(K id, V valType) {
        this.symTable.put(id,valType);
    }

    public V getValue(K key)
    {
        return this.symTable.get(key);
    }

    public HashMap<K,V> getADT(){
        return this.symTable;
    }
    public Set<K> getKeys()
    {
        return symTable.keySet();
    }

    public boolean isDefined(K id) {
        try {
            if (this.symTable.get(id) == null)
                return false;
            return true;
        }
        catch(NullPointerException ex) {
            return false;
        }
    }
    public void update(K id, V exp) {
        this.symTable.put(id,exp);
    }

    public V get(K id) {
       return this.symTable.get(id);
    }

    @Override
    public String toString() {
        return this.symTable.toString();
    }

    @Override
    public void remove(K toString) {
        this.symTable.remove(toString);
    }

    public Collection<V> values() {
        return symTable.values();
    }
}
