package com.taixingyiji.base.common.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SwaggerEntity {

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public String path;

    public String enableAuth = FALSE;

    public String username = "admin";

    public String password = "123456";
}
