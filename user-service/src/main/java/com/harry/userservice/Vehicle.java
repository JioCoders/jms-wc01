package com.harry.userservice;

enum state {
    start,
    gear_0, gear_1, gear_2, gear_3, gear_4, gear_5,
    stop,
}

/**
 * MyInterface
 */
abstract class Vehicle implements VehicleProps {

    int maxSpeed = 300;
    int maxGear = 5;
    int offSet = 20;

    @Override
    public void defaultHorn() {
        VehicleProps.super.defaultHorn();
        System.out.println("defaultHorning----------");
    }
    abstract void getState();

    abstract void automate(boolean autoMode) throws VehicleException;

    abstract void start() throws VehicleException;

    abstract void stop() throws VehicleException;

    abstract int gearUp() throws VehicleException;

    abstract int gearDown() throws VehicleException;

    abstract int accelerate() throws VehicleException;

    abstract int brek() throws VehicleException;

}