package com.taixingyiji.base.module.log.model;

import lombok.Data;

import java.io.Serializable;


/**
 * @author lhc
 */
@Data
public class RequestInfo implements Serializable {
    private static final long serialVersionUID = 386274939008099288L;
    private String ip;
    private String url;
    private String httpMethod;
    private String classMethod;
    private Object requestParams;
    private Object result;
    private Long timeCost;

}
