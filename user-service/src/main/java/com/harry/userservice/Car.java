package com.harry.userservice;

public class Car extends Vehicle {

    state engine = state.stop;
    int gear = 0;
    int speed = 0;
    boolean autoMode = false;
    boolean brekApplied = false;

    @Override
    public void start() throws VehicleException {
        if (engine == state.stop) {
            engine = state.start;
        } else {
            throw new VehicleException("engine already started");
        }
        getState();
    }

    @Override
    public void stop() throws VehicleException {
        if (engine == state.start) {
            engine = state.stop;
        } else {
            throw new VehicleException("engine already stopped");
        }
        getState();
    }

    @Override
    public int gearUp() {
        if (gear < maxGear) {
            gear++;
            getState();
        }
        return gear;
    }

    @Override
    public int gearDown() {
        if (gear > 0) {
            gear--;
            getState();
        }
        return gear;
    }

    int validateSpeed() {
        int kSpeed = 0;
        switch (gear) {
            case 1:
                kSpeed = 20;
                break;
            case 2:
                kSpeed = 40;
                break;
            case 3:
                kSpeed = 60;
                break;
            case 4:
                kSpeed = 80;
                break;
            case 5:
                kSpeed = maxSpeed;
                break;

            default:
                break;
        }
        return kSpeed;
    }

    @Override
    public int accelerate() {
        if (speed < maxSpeed) {
            speed = speed + offSet;
            int kSpeed = validateSpeed();
            if (speed > kSpeed) {
                speed = speed - offSet;
            }
            getState();
        }
        return speed;
    }

    @Override
    public int brek() {
        if (speed > offSet) {
            speed = speed - offSet;
            getState();
            brekApplied = true;
        }
        return speed;
    }

    @Override
    void getState() {
        System.out.println("Car state: " + engine);
        System.out.println("Car speed: " + speed);
        System.out.println("Car gear: " + gear);
        defaultHorn();
        VehicleProps.staticTurn();
    }

    int checkGrearLimit() {
        switch (speed) {
            case 20:
                gear = 2;
                break;
            case 40:
                gear = 3;
                break;
            case 60:
                gear = 4;
                break;
            case 80:
                gear = 5;
                break;

            default:
                break;
        }
        return gear;
    }

    @Override
    public void automate(boolean autoMode) throws VehicleException {
        if (!autoMode) {
            return;
        }
        if (speed >= maxSpeed) {
            return;
        }
        for (int j = 0; j < maxSpeed; j = +1) {
            if (brekApplied) {
                continue;
            }
            gearUp();
            accelerate();
            checkGrearLimit();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new VehicleException("interupt thread exception");
            }
        }
    }
}