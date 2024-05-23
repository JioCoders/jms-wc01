package com.harry.userservice.swagger;

import org.springframework.http.ResponseEntity;

import com.harry.userservice.model.req.ReqLogin;
import com.harry.userservice.model.res.ResLogin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "user-controller", description = "the user info Api")
public interface UserInfo {

        @Operation(summary = "Login ", description = "fetches Login and get user info by email and password")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "successful Login"),
                        @ApiResponse(responseCode = "400", description = "user not found")
        })
        ResponseEntity<ResLogin> loginUser(@RequestBody ReqLogin request);

}