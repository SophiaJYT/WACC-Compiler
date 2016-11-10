package frontEnd;

import java.util.Dictionary;
import java.util.Hashtable;

public class SymbolTable<T> {

    SymbolTable<T> encSymbolTable;
    Dictionary<String, T> dictionary;

    public SymbolTable() {
        encSymbolTable = null;
        dictionary = new Hashtable<>();
    }

    public SymbolTable(SymbolTable<T> st) {
        encSymbolTable = st;
        dictionary = new Hashtable<>();
    }

    public void add(String name, T obj) {
        dictionary.put(name, obj);
    }

    public T lookUp(String name) {
        return dictionary.get(name);
    }

    public T lookupAll(String name) {
        SymbolTable<T> s = this;
        while (s != null) {
            T obj = s.lookUp(name);
            if (obj != null) {
                return obj;
            }
            s = s.encSymbolTable;
        }
        return null;
    }

}
