package com.jiocoders.taskservice.service;

import com.jiocoders.taskservice.base.ResponseBase;
import com.jiocoders.taskservice.entity.Project;
import com.jiocoders.taskservice.entity.UserX;
import com.jiocoders.taskservice.networkmodel.request.RequestProjectAdd;
import com.jiocoders.taskservice.networkmodel.request.RequestProjectUpdate;
import com.jiocoders.taskservice.networkmodel.response.ResponseProjectList;
import com.jiocoders.taskservice.repository.ProjectRepository;
import com.jiocoders.taskservice.repository.UserRepository;
import com.jiocoders.taskservice.security.AuthScope;
import com.jiocoders.taskservice.security.JwtTokenProvider;
import com.jiocoders.taskservice.utils.ApiConstant;
import com.jiocoders.taskservice.utils.StrConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.jiocoders.taskservice.utils.ApiConstant.BAD_TOKEN_CODE;
import static com.jiocoders.taskservice.utils.ApiConstant.PROJECT_SERVICE;
import static com.jiocoders.taskservice.utils.Common.checkNotNull;

@Service(PROJECT_SERVICE)
// @CacheConfig(cacheNames = {"Project"})
public class ProjectServiceImpl implements ProjectService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
    // private static final String KEY = "PROJECT";

    // @Autowired
    // private RedisTemplate redisTemplate;
    // private HashOperations hashOperations;

    // public OptionServiceImpl(RedisTemplate<String, Option> redisTemplate) {
    // this.redisTemplate = redisTemplate;
    // hashOperations = redisTemplate.opsForHash();
    // }

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    private UserRepository adminRepository;

    /**
     * Method to return the list of all the options in the system.This is an
     * implementation but use pagination in the real world.
     *
     * @param token
     * @return list of Options
     */
    @Override
    // @Cacheable(value = "projects", key = "#id")
    @Cacheable(value = "projects")
    public ResponseEntity<ResponseProjectList> getProjectList(String token) {
        ResponseProjectList response = new ResponseProjectList();
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
        List<Project> projects = projectRepository.findAll();
        response.projectList.addAll(projects);
        response.setMessage(StrConstant.SUCCESS);
        response.setStatus(ApiConstant.SUCCESS_CODE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create an option based on the data sent to the service class.
     *
     * @param projectRequest
     * @param token
     * @return DTO representation of the option
     */
    @Override
    @CacheEvict(value = "projects", allEntries = true)
    public ResponseEntity<ResponseBase> addProject(RequestProjectAdd projectRequest, String token) {
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
        int loginUserId = Integer.parseInt(jwtTokenProvider.extractUserId(bearer));
        if (!checkNotNull(projectRequest.getProjectName()) || projectRequest.getProjectName().length() < 2) {
            responseBase.setMessage(StrConstant.PLEASE_PROVIDE_MANDATORY_FIELD + ": Project name");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }
        List<Project> projectResult = projectRepository.findAll();
        boolean isExist = false;
        for (Project o : projectResult) {
            if (o.getProjectName().equalsIgnoreCase(projectRequest.getProjectName())) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            responseBase.setMessage(projectRequest.getProjectName() + " - project is already exists!");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }

        if (!checkNotNull(projectRequest.getProjectLeadEmpId())) {
            responseBase.setMessage(projectRequest.getProjectName() + " - not valid project lead employee id!");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }
        if (!checkNotNull(projectRequest.getProjectManagerEmpId())) {
            responseBase.setMessage(projectRequest.getProjectName() + " - not valid project manager employee id!");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }
        Project project = new Project();
        project.setProjectName(projectRequest.getProjectName());
        project.setProjectDescription(projectRequest.getProjectDescription());
        project.setCreatedAt(System.currentTimeMillis());
        project.setCreatedBy(loginUserId);
        project.setProjectStatus("Not allocated");
        project.setIsActive(true);
        projectRepository.save(project);
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
    public ResponseEntity<ResponseBase> updateProject(RequestProjectUpdate projectRequest, String token) {
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
        int userId = Integer.parseInt(jwtTokenProvider.extractUserId(bearer));
        if (!checkNotNull(projectRequest.getProjectName()) || projectRequest.getProjectName().length() < 2) {
            responseBase.setMessage(StrConstant.PLEASE_PROVIDE_MANDATORY_FIELD + ": Project name");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }
        List<Project> projectResult = projectRepository.findAll();
        boolean isExist = false;
        for (Project o : projectResult) {
            if (o.getProjectName().equalsIgnoreCase(projectRequest.getProjectName())) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            responseBase.setMessage(projectRequest.getProjectName() + " - project is already exists!");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }

        // Pattern pattern = Pattern.compile(regexEmail);
        // Matcher pLeadMatcher = pattern.matcher(projectRequest.getProjectLeadEmpId());
        // Matcher pManagerMatcher =
        // pattern.matcher(projectRequest.getProjectManagerEmpId());
        // if (!checkNotNull(projectRequest.getProjectLeadEmpId()) ||
        // !pLeadMatcher.matches()) {
        if (!checkNotNull(projectRequest.getProjectLeadEmpId())) {
            responseBase.setMessage(projectRequest.getProjectName() + " - not valid project lead employee id!");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }
        if (!checkNotNull(projectRequest.getProjectManagerEmpId())) {
            responseBase.setMessage(projectRequest.getProjectName() + " - not valid project manager employee id!");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }
        Project project = new Project();
        project.setProjectName(projectRequest.getProjectName());
        project.setProjectDescription(projectRequest.getProjectDescription());
        project.setCreatedAt(System.currentTimeMillis());
        project.setCreatedBy(userId);
        project.setProjectStatus("Not allocated");
        project.setIsActive(true);
        projectRepository.save(project);
        // try {
        // redisTemplate.opsForHash().put(KEY, option.getOptionId(), option);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        responseBase.setMessage(StrConstant.INSERT_SUCCESS);
        responseBase.setStatus(ApiConstant.SUCCESS_INSERT_CODE);

        return new ResponseEntity<>(responseBase, HttpStatus.OK);
    }

    /**
     * Delete project based on the option ID. We can also use other option to delete
     * project
     * based on the entity (passing JPA entity class as method parameter)
     *
     * @param projectId
     * @param token
     * @return boolean flag showing the request status
     */
    @Override
    @CacheEvict(value = "projects", allEntries = true)
    public ResponseEntity<ResponseBase> removeProject(String projectId, String token) {
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
        if (!checkNotNull(projectId) || projectId.length() < 10) {
            responseBase.setMessage(StrConstant.PLEASE_PROVIDE_MANDATORY_FIELD + ": Project id");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }

        projectRepository.removeProject(projectId);
        responseBase.setMessage(StrConstant.INSERT_SUCCESS);
        responseBase.setStatus(ApiConstant.SUCCESS_INSERT_CODE);

        return new ResponseEntity<>(responseBase, HttpStatus.OK);
    }
}
