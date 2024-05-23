package com.jiocoders.taskservice.utils;

public class ApiConstant {

    /**
     * ----------------- ALL SERVICES -----------------
     */
    public static final String PROJECT_SERVICE = "projectService";
    public static final String TASK_SERVICE = "taskService";
    public static final String TASK_STATUS_SERVICE = "taskStatusService";

    /**
     * -----------------END POINTS------------------
     */
    // LOGIN CONTROLLER
    public static final String PROJECT_CONTROLLER = "/project";
    public static final String TASK_CONTROLLER = "/task";
    public static final String STATUS_CONTROLLER = "/status";

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
    public static final int BAD_REQUEST_CODE = 400;
    public static final int BAD_TOKEN_CODE = 401;
    public static final int FORBIDDEN_CODE = 403;
    public static final int SUCCESS_INSERT_CODE = 201;
    public static final int SUCCESS_CODE = 1;
    public static final int SERVER_ERROR_CODE = 500;

    /**
     * OTHER
     */
    public static final String APPLICATION_JSON = "application/json";

}
