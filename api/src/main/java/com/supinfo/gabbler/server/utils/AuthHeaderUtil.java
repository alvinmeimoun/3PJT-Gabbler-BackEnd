package com.supinfo.gabbler.server.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthHeaderUtil {

    public static final String TOKEN_HEADER_NAME = "sessionAuthToken";

    public static String getToken(HttpServletRequest request) {
        String header = request.getHeader(TOKEN_HEADER_NAME);
        return StringUtils.isNotBlank(header) ? header : null;
    }

    public static String createAuthToken(String userName) {
        return userName + "|" + System.currentTimeMillis();
    }

}
