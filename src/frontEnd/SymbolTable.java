package frontEnd;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class SymbolTable<T> {

    SymbolTable<T> encSymbolTable;
    Dictionary<String, T> dictionary;
    Dictionary<String, T[]> funcParams;
    Dictionary<String, SymbolTable<T>> funcTables;

    private void initialiseDictionaries() {
        dictionary = new Hashtable<>();
        funcTables = new Hashtable<>();
        funcParams = new Hashtable<>();
    }

    public SymbolTable() {
        encSymbolTable = null;
        initialiseDictionaries();
    }

    public SymbolTable(SymbolTable<T> st) {
        encSymbolTable = st;
        initialiseDictionaries();
    }

    public void addFunction(String name, T retType, T[] paramList) {
        add("func:" + name, retType);
        funcTables.put(name, new SymbolTable<>());
        funcParams.put(name, paramList);
    }

    public void add(String name, T obj) {
        dictionary.put(name, obj);
    }

    public SymbolTable<T> lookUpFunc(String name) {
        return funcTables.get(name);
    }

    public T[] lookUpParam(String name) {
        return funcParams.get(name);
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

    @Override
    public String toString() {
        return dictionary + "[" + encSymbolTable + "]";
    }
}
