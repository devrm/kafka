package br.com.alura.ecommerce;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

public class GsonSerializer<T> implements Serializer<T> {


    private static final Gson gson = new GsonBuilder().create();


    @Override
    public byte[] serialize(String s, T t) {

        final String s1 = gson.toJson(t);

        return s1.getBytes();
    }
}
