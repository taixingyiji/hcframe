package com.hcframe.base.module.shiro.service;


import com.hcframe.base.common.ResultVO;
import com.hcframe.base.module.shiro.FtToken;

import java.util.Date;

/**
 * @author lhc
 */
public interface ShiroService {

    ResultVO createToken(String userId,String token, Date expireTime);

    ResultVO logout(String accessToken);

    FtToken findByToken(String accessToken);

    Object findByUserId(String userId);
}
