package com.jiocoders.taskservice.service;

import static com.jiocoders.taskservice.utils.ApiConstant.BAD_TOKEN_CODE;
import static com.jiocoders.taskservice.utils.ApiConstant.TASK_SERVICE;
import static com.jiocoders.taskservice.utils.Common.checkNotNull;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jiocoders.taskservice.base.ResponseBase;
import com.jiocoders.taskservice.entity.Task;
import com.jiocoders.taskservice.entity.UserX;
import com.jiocoders.taskservice.networkmodel.request.RequestTaskAdd;
import com.jiocoders.taskservice.networkmodel.request.RequestTaskList;
import com.jiocoders.taskservice.networkmodel.request.RequestTaskUpdate;
import com.jiocoders.taskservice.networkmodel.response.ResponseTaskList;
import com.jiocoders.taskservice.repository.TaskRepository;
import com.jiocoders.taskservice.repository.UserRepository;
import com.jiocoders.taskservice.security.AuthScope;
import com.jiocoders.taskservice.security.JwtTokenProvider;
import com.jiocoders.taskservice.utils.ApiConstant;
import com.jiocoders.taskservice.utils.StrConstant;

@Service(TASK_SERVICE)
// @CacheConfig(cacheNames = {"Task"})
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    // private static final String KEY = "TASK";

    // @Autowired
    // private RedisTemplate redisTemplate;
    // private HashOperations hashOperations;

    // public TaskServiceImpl(RedisTemplate<String, Task> redisTemplate) {
    // this.redisTemplate = redisTemplate;
    // hashOperations = redisTemplate.opsForHash();
    // }

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    private UserRepository adminRepository;

    /**
     * Method to return the list of all the tasks in the system. This is an
     * implementation but use pagination in the real world.
     *
     * @param token
     * @return list of Tasks
     */
    @Override
    // @Cacheable(value = "tasks", key = "#id")
    @Cacheable(value = "tasks")
    public ResponseEntity<ResponseTaskList> getTaskList(RequestTaskList request, String token) {
        ResponseTaskList response = new ResponseTaskList();
        if (!checkNotNull(token)) {
            response.setMessage(StrConstant.TOKEN_NOT_FOUND);
            response.setStatus(BAD_TOKEN_CODE);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (!jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                && !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            response.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            response.setStatus(ApiConstant.FORBIDDEN_CODE);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        // CHECK VALID USER ID
        int loginUserId = Integer.parseInt(jwtTokenProvider.extractUserId(bearer));
        logger.info("UserId={}", loginUserId);
        UserX employee = adminRepository.findById(loginUserId).get();
        if (employee == null) {
            response.setMessage("User not found!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (!employee.getIsActive()) {
            response.setMessage("User is not active to update!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // List<Option> optionRedis = redisTemplate.opsForHash().values(KEY);
        // logger.info("+++++++++++++Executing query to fetch option list+++++++++++++"
        // + optionRedis.size());
        List<Task> tasks = taskRepository.findAllByProjectId(request.getProjectId());
        response.taskList.addAll(tasks);
        response.setMessage(StrConstant.SUCCESS);
        response.setStatus(ApiConstant.SUCCESS_CODE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create a task based on the data sent to the service class.
     *
     * @param taskRequest
     * @param token
     * @return DTO representation of the option
     */
    @Override
    @CacheEvict(value = "tasks", allEntries = true)
    public ResponseEntity<ResponseBase> addTask(RequestTaskAdd taskRequest, String token) {
        ResponseBase responseBase = new ResponseBase();
        if (!checkNotNull(token)) {
            responseBase.setMessage(StrConstant.TOKEN_NOT_FOUND);
            responseBase.setStatus(BAD_TOKEN_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.UNAUTHORIZED);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                || !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            responseBase.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            responseBase.setStatus(ApiConstant.FORBIDDEN_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.FORBIDDEN);
        }

        // CHECK Project uuid
        if (!checkNotNull(String.valueOf(taskRequest.getProjectId()))) {
            responseBase.setMessage(StrConstant.PLEASE_PROVIDE_MANDATORY_FIELD + ": Project id");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }
        // CHECK VALIDATION
        int loginUserId = Integer.parseInt(jwtTokenProvider.extractUserId(bearer));
        if (!checkNotNull(taskRequest.getTaskTitle()) || taskRequest.getTaskTitle().length() < 2) {
            responseBase.setMessage(StrConstant.PLEASE_PROVIDE_MANDATORY_FIELD + ": Task name");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }
        List<Task> taskResult = taskRepository.findAll();
        boolean isExist = false;
        for (Task t : taskResult) {
            if (t.getTaskTitle().equalsIgnoreCase(taskRequest.getTaskTitle())) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            responseBase.setMessage(taskRequest.getTaskTitle() + " - task is already exists!");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }
        Task task = new Task();
        task.setTaskTitle(taskRequest.getTaskTitle());
        task.setTaskDescription(taskRequest.getTaskDescription());
        task.setTaskDate(taskRequest.getTaskDate());
        task.setStatusId(taskRequest.getStatusId());
        task.setIsActive(true);
        task.setCreatedBy(loginUserId);
        task.setCreatedAt(System.currentTimeMillis());
        taskRepository.save(task);
        // try {
        // redisTemplate.opsForHash().put(KEY, option.getOptionId(), option);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        responseBase.setMessage(StrConstant.INSERT_SUCCESS);
        responseBase.setStatus(ApiConstant.SUCCESS_INSERT_CODE);

        return new ResponseEntity<>(responseBase, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseBase> updateTask(RequestTaskUpdate request, String token) {
        ResponseBase responseBase = new ResponseBase();
        if (!checkNotNull(token)) {
            responseBase.setMessage(StrConstant.TOKEN_NOT_FOUND);
            responseBase.setStatus(BAD_TOKEN_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.UNAUTHORIZED);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                || !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            responseBase.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            responseBase.setStatus(ApiConstant.FORBIDDEN_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.FORBIDDEN);
        }

        // CHECK VALIDATION
        // UUID userId = UUID.fromString(jwtTokenProvider.getUsername(bearer));

        if (request.getTaskId() == 0) {
            responseBase.setMessage(StrConstant.ITEM_NOT_FOUND + " : task id");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }
        Task task = taskRepository.findByTaskId(request.getTaskId());
        if (task == null) {
            responseBase.setMessage(StrConstant.ITEM_NOT_FOUND);
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }

        if (checkNotNull(request.getTaskTitle())) {

            task.setTaskTitle(request.getTaskTitle());
        }
        if (checkNotNull(request.getTaskDescription())) {
            task.setTaskDescription(request.getTaskDescription());
        }

        // Order details
        // order landing
        // notification list
        // HOme screen
        // Offer screen
        // package details(promotion)
        // video call
        // rate doctor
        //
        // push_notifi_new

        if (request.getTaskDate() != 0) {
            task.setTaskDate(request.getTaskDate());
        }
        if (request.getStatusId() != 0) {
            task.setStatusId(request.getStatusId());
        }
        taskRepository.save(task);

        responseBase.setMessage(StrConstant.UPDATE_SUCCESS);
        responseBase.setStatus(ApiConstant.SUCCESS_CODE);

        return new ResponseEntity<>(responseBase, HttpStatus.OK);
    }

    boolean isValidUser() {
        return false;
    }

    /**
     * Delete task based on the task ID. We can also use other option to delete task
     * based on the entity (passing JPA entity class as method parameter)
     *
     * @param taskId
     * @param token
     * @return boolean flag showing the request status
     */
    @Override
    @CacheEvict(value = "tasks", allEntries = true)
    public ResponseEntity<ResponseBase> removeTask(String taskId, String token) {
        ResponseBase responseBase = new ResponseBase();
        if (!checkNotNull(token)) {
            responseBase.setMessage(StrConstant.TOKEN_NOT_FOUND);
            responseBase.setStatus(BAD_TOKEN_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.UNAUTHORIZED);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                || !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            responseBase.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            responseBase.setStatus(ApiConstant.FORBIDDEN_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.FORBIDDEN);
        }

        // CHECK VALIDATION
        // UUID userId = UUID.fromString(jwtTokenProvider.getUsername(bearer));
        if (!checkNotNull(taskId) || taskId.length() < 10) {
            responseBase.setMessage(StrConstant.PLEASE_PROVIDE_MANDATORY_FIELD + ": Task id");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }

        taskRepository.removeTask(taskId);
        responseBase.setMessage(StrConstant.INSERT_SUCCESS);
        responseBase.setStatus(ApiConstant.SUCCESS_INSERT_CODE);

        return new ResponseEntity<>(responseBase, HttpStatus.OK);
    }
}
