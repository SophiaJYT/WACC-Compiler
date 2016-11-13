package frontEnd;

import java.util.*;

public class SymbolTable<T> {

    SymbolTable<T> encSymbolTable;
    Dictionary<String, T> dictionary;
    Dictionary<String, T[]> funcParams;
    Map<String, SymbolTable<T>> funcTables;
    List<SymbolTable<T>> functions;

    private void initialiseCollections() {
        dictionary = new Hashtable<>();
        funcTables = new HashMap<>();
        funcParams = new Hashtable<>();
        functions = new ArrayList<>();
    }

    public SymbolTable() {
        encSymbolTable = null;
        initialiseCollections();
    }

    public SymbolTable(SymbolTable<T> st) {
        encSymbolTable = st;
        initialiseCollections();
    }

    public void addFunction(String name, T retType, T[] paramList) {
        add("func:" + name, retType);
        funcTables.put(name, new SymbolTable<>());
        funcParams.put(name, paramList);
        for (SymbolTable<T> symb : funcTables.values()) {
            symb.add("func:" + name, retType);
        }
    }

    public void add(String name, T obj) {
        dictionary.put(name, obj);
    }

    public SymbolTable<T> lookUpFunc(String name) {
        return funcTables.get(name);
    }

    public T[] lookUpParams(String name) {
        return funcParams.get(name);
    }

    public T lookUp(String name) {
        return dictionary.get(name);
    }

    public T lookUpAll(String name) {
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

    @Override
    public String toString() {
        return dictionary + "[" + encSymbolTable + "]";
    }
}
