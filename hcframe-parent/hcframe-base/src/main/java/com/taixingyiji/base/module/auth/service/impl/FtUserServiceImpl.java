package com.taixingyiji.base.module.auth.service.impl;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.ServiceException;
import com.taixingyiji.base.common.config.FrameConfig;
import com.taixingyiji.base.common.utils.MD5Util;
import com.taixingyiji.base.common.utils.TokenProccessor;
import com.taixingyiji.base.module.auth.dao.FtUserDao;
import com.taixingyiji.base.module.auth.entity.FtUser;
import com.taixingyiji.base.module.auth.service.FtUserService;
import com.taixingyiji.base.module.shiro.dao.FtTokenDao;
import com.taixingyiji.base.module.shiro.service.ShiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * (FtUser)表服务实现类
 *
 * @author lhc
 * @since 2020-02-11 19:29:10
 */
@Service("ftUserService")
public class FtUserServiceImpl implements FtUserService {


    /**
     * 用户失效时间
     */
    private static int EXPIRE;

    private final FtTokenDao ftTokenDao;
    private final FtUserDao ftUserDao;


    final
    ShiroService shiroService;



    public FtUserServiceImpl(FtUserDao ftUserDao, ShiroService shiroService, FtTokenDao ftTokenDao) {
        this.ftUserDao = ftUserDao;
        this.shiroService = shiroService;
        this.ftTokenDao = ftTokenDao;
    }

    @Autowired
    public void setHost(FrameConfig config) {
        FtUserServiceImpl.EXPIRE = config.getLoginTimeout() * 3600  * 1000;
    }



    @Override
    @Transactional
    public ResultVO login(HttpServletRequest request, String username, String password) {
        FtUser ftUser = FtUser.builder().username(username).build();
        ftUser = ftUserDao.selectOne(ftUser);
        if (ftUser == null) {
            throw new ServiceException("用户名不存在");
        }
        if (ftUser.getEnabled() != 2) {
            if (MD5Util.isEqual(password, ftUser.getPassword())) {
                TokenProccessor tokenProccessor = TokenProccessor.getInstance();
                //生成一个token
                String token = tokenProccessor.makeToken();
                //过期时间
                Date now = new Date();
                Date expireTime = new Date(now.getTime() + EXPIRE);
                ResultVO resultVO = shiroService.createToken(String.valueOf(ftUser.getUserId()),token,expireTime);
                Map<String, Object> map = (Map<String, Object>) resultVO.getData();
                return ResultVO.getSuccess(map);
            } else {
                throw new ServiceException("用户名或密码错误！");
            }
        } else {
            throw new ServiceException("用户已被禁用，请联系管理员");
        }
    }

}
