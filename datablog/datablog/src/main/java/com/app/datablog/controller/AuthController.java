package com.app.datablog.controller;

import com.app.datablog.dto.AuthUserDTO;
import com.app.datablog.security.jwt.JwtUtils;
import com.app.datablog.service.AuthUserService;
import com.app.datablog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "1. Authentication", description = "Authentication management APIs")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthUserService authService;
    @Autowired
    private JwtUtils jwtUtils;


    @Operation(
            summary = "Retrieve Home page",
            description = "Get Main Page response.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/home")
    public String index(){
        return "Main Page";
    }



    @Operation(
            summary = "User login",
            description = "Log in a user if it was previously registered. The body require mandatory email and password.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = AuthUserDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthUserDTO authUserDTO) {
        String email = authUserDTO.getEmail();
        String password = authUserDTO.getPassword();

        Map<String, Object> response = authService.authenticateUser(email, password);

        return ResponseEntity.ok(response);
    }

}
