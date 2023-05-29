package com.example.demoCustomer.customer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StateConverter {

    private final Map<String, String> stateMapping;

    public StateConverter(@Value("${states.mapping}") String stateMapping) {
        this.stateMapping = new HashMap<>();
        String[] entries = stateMapping.split(",");
        System.out.println(entries.length);
        for(String entry : entries) {
            String[] state = entry.split(":");
            if(state.length == 2){
                this.stateMapping.put(state[0].trim(), state[1].trim());
            }
        }
    }

    public String convertToCode(String state) {
        if(stateMapping.containsKey(state)) {
            return stateMapping.get(state);
        }
        return state;
    }

    public boolean isValidState(String state) {
        return stateMapping.containsKey(state);
    }
}
