package com.taixingyiji.base.module.log.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lhc
 */
@Data
public class RequestErrorInfo implements Serializable {
    private static final long serialVersionUID = 2671491007419047891L;
    private String ip;
    private String url;
    private String httpMethod;
    private String classMethod;
    private Object requestParams;
    private RuntimeException exception;
}
