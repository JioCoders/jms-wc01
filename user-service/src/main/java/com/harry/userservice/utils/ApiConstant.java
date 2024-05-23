package com.harry.userservice.utils;

public class ApiConstant {

    /**
     * ----------------- ALL SERVICES -----------------
     */
    public static final String ADMIN_SERVICE = "adminService";
    public static final String LOGIN_SERVICE = "loginService";
    public static final String LOCATION_SERVICE = "locationService";

    /**
     * -----------------END POINTS------------------
     */
    // LOGIN CONTROLLER
    public static final String WELCOME = "/welcome";
    public static final String USER_LOGIN = "/user-login";

    // ADMIN CONTROLLER
    public static final String USER_ADD = "/user-add";
    public static final String USER_UPDATE = "/user-update";
    public static final String USER_LIST = "/user-list";
    public static final String USER_VALIDATE = "/user-validate";
    public static final String USER_ACTIVE = "/user-active";
    public static final String RESET_PASSWORD = "/reset-password";

    /**
     * ----------------- CONTROLLERS -----------------
     */

    /**
     * COMMON CONTROLLER
     */
    public static final String API = "/api/v1";

    /**
     * Common CONTROLLER
     */
    public static final String ADD = "/add";
    public static final String UPDATE = "/update";
    public static final String LIST = "/list";
    public static final String REMOVE = "/remove";
    public static final String LIST_DATE_RANGE = "/list-date-range";
    public static final String DETAIL = "/detail";

    /**
     * REQUEST CODE
     */
    public static final int INVALID_REQUEST_CODE = -1;
    // public static final int BAD_REQUEST_CODE = 400;
    // public static final int BAD_TOKEN_CODE = 401;
    // public static final int FORBIDDEN_CODE = 403;
    // public static final int SUCCESS_INSERT_CODE = 201;
    public static final int SUCCESS_CODE = 1;
    // public static final int SERVER_ERROR_CODE = 500;

    /**
     * OTHER
     */
    public static final String APPLICATION_JSON = "application/json";

}
