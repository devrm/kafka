package br.com.alura.ecommerce;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class GsonDeserializer<T> implements Deserializer<T> {

    public static final String TYPE_CONFIG = "br.alura.typeconfig";
    private final Gson gson = new GsonBuilder().create();
    private Class<T> type;

    public void configure(Map<String, ?> configs, boolean isKey) {

        final String o = String.valueOf(configs.get(GsonDeserializer.TYPE_CONFIG));
        try {
             type = (Class<T>) Class.forName(o);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Type does not exist in classpath ", e);
        }
    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        return gson.fromJson(new String(bytes), type);
    }
}
