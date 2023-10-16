package com.app.datablog.service;

import java.util.Map;

public interface AuthUserService {

    Map<String, Object> authenticateUser(String email, String password);

}
