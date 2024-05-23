package com.harry.userservice;

public interface VehicleProps {
    
    default void defaultHorn() {
    }

    public static void staticTurn() {
        System.out.println("----------staticMethod(turning)-->");
    }
}
