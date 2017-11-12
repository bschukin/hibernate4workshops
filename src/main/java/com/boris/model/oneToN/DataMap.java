package com.boris.model.oneToN;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Щукин on 27.10.2017.
 */
public class DataMap<T> {

    private final String name;
    private T id;
    private Map<String, Object> map = new HashMap<>() ;

    public DataMap(String name, T id) {
        this.name = name;
        this.id = id;
    }

    public DataMap(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public <TT>  List<DataMap<TT>> list(String prop)
    {
        if(!map.containsKey(prop))
                map.put(prop, new ArrayList<>());
        return (List<DataMap<TT>>) map.get(prop);
    }
    public Object get(String prop)
    {
        return map.get(prop);
    }
    public <T> DataMap<T> geto(String prop)
    {
        return (DataMap<T>) map.get(prop);
    }
    public DataMap<T> set(String prop, Object value)
    {
        map.put(prop,value);
        return this;
    }
}
