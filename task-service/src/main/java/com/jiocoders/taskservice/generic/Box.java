package com.jiocoders.taskservice.generic;

public class Box<T> {
    /**
     * Object class is top most parent class of all java classes
     */
    T container;

    public Box(T container) {
        this.container = container;
    }

    public T getContainer() {
        return container;
    }

    public void setContainer(T container) {
        this.container = container;
    }
}
