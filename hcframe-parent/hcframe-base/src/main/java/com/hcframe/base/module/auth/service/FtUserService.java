package com.hcframe.base.module.auth.service;


import com.hcframe.base.common.ResultVO;

import javax.servlet.http.HttpServletRequest;

/**
 * (FtUser)表服务接口
 *
 * @author lhc
 * @since 2020-02-11 19:29:10
 */
public interface FtUserService {

    ResultVO login(HttpServletRequest request, String username, String password);

}
