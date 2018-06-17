package com.symbo.assignment.model.bo;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "COUNTER")
public class CounterBO {
    String key;
    Long value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
