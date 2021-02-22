package com.hcframe.base.common.config;

import lombok.Data;

@Data
public class SwaggerEntity {

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public String path;

    public String enableAuth = FALSE;

    public String username = "admin";

    public String password = "123456";
}
