package com.hit.dm;

public class AbstractSerializable {
    public String type;

    public AbstractSerializable() {
        // Add type name to allow deserialization by casting to inherited concrete classes
        type = this.getClass().getName();
    }
}
