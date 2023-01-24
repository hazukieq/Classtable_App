package com.hazukie.scheduleviews.iJBridges;

import androidx.annotation.NonNull;

public class JBridgeObject {
    String command;
    String params;

    public JBridgeObject(String command, String params){
        this.command=command;
        this.params=params;
    }

    @NonNull
    @Override
    public String toString() {
        return "JBridgeObject{" +
                "command='" + command + '\'' +
                ", params='" + params + '\'' +
                '}';
    }
}
