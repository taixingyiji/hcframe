package com.taixingyiji.base.module.shiro.service;


import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.module.shiro.FtToken;

import java.util.Date;

/**
 * @author lhc
 */
public interface ShiroService {

    ResultVO createToken(String userId, String token, Date expireTime);

    ResultVO logout(String accessToken);

    FtToken findByToken(String accessToken);

    Object findByUserId(String userId);
}
