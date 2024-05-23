package com.harry.userservice;

public class VehicleException extends Throwable {

    String errorString;

    VehicleException(String err) {
        errorString = err;
    }

}
