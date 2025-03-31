package com.taixingyiji.base.module.shiro.service;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.ServiceException;
import com.taixingyiji.base.common.config.FrameConfig;
import com.taixingyiji.base.module.data.exception.SqlException;
import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.Condition;
import com.taixingyiji.base.module.shiro.dao.FtTokenDao;
import com.taixingyiji.base.module.shiro.FtToken;
import com.taixingyiji.base.common.utils.TokenProccessor;
import com.taixingyiji.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author lhc
 */
@Service
public class ShiroServiceImpl implements ShiroService {

    /**
     * 用户失效时间
     */
    private static int EXPIRE;

    private static Boolean isRedisLogin;

    @Autowired
    public void setHost(FrameConfig frameConfig) {
        ShiroServiceImpl.EXPIRE = frameConfig.getLoginTimeout() * 3600 * 1000;
        isRedisLogin = frameConfig.getIsRedisLogin();
    }

    @Resource
    FtTokenDao tokenMapper;
    @Resource
    BaseMapper baseMapper;
    @Resource
    RedisUtil redisUtil;

    @Resource
    SystemRealm systemRealm;

    @Override
    /** 重点！！
     * 生成一个token
     *@param  [userId]
     *@return Result
     */
    public ResultVO createToken(String userId, String token, Date expireTime) {
        Map<String, Object> result = new HashMap<>();

        FtToken tokenEntity = new FtToken();
        Map<String, Object> resultEntity = new HashMap<>();
        Date now = new Date();
        // 是否使用redis存入token
        if (isRedisLogin) {
            boolean flag = redisUtil.set("session:" + userId, token, EXPIRE / 1000);
            if (flag) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("userId", userId);
                map.put("expireTime", expireTime);
                flag = redisUtil.hset("tokenSession:" + token, "userId", userId, EXPIRE / 1000);
                if (!flag) {
                    throw new ServiceException("登陆失败");
                }
                flag = redisUtil.hset("tokenSession:" + token, "expireTime", String.valueOf(expireTime.getTime()), EXPIRE / 1000);
                if (!flag) {
                    throw new ServiceException("登陆失败");
                }
            } else {
                throw new ServiceException("登陆失败");
            }
        } else {
//            tokenEntity.setUserId(userId);
            //判断是否生成过token
            Condition condition = Condition.creatCriteria().andEqual("USER_ID", userId).build();
            resultEntity = baseMapper.selectOneByCondition("FT_TOKEN", condition);
            if (resultEntity == null || resultEntity.isEmpty()) {
                resultEntity = new HashMap<>();
                resultEntity.put("TOKEN_ID", UUID.randomUUID().toString() + System.currentTimeMillis());
                resultEntity.put("USER_ID", userId);
                resultEntity.put("TOKEN", token);
                resultEntity.put("UPDATE_TIME", now);
                resultEntity.put("EXPIRE_TIME", expireTime);
                baseMapper.save("FT_TOKEN", "TOKEN_ID", resultEntity);
            } else {
                resultEntity.put("TOKEN", token);
                resultEntity.put("UPDATE_TIME", now);
                resultEntity.put("EXPIRE_TIME", expireTime);
                int i = baseMapper.updateByPk("FT_TOKEN", "TOKEN_ID", resultEntity);
                SqlException.base(i, "登录失败");
            }
//            tokenEntity = tokenMapper.selectOne(tokenEntity);
//            if (tokenEntity == null) {
//                tokenEntity = new FtToken();
//                tokenEntity.setTokenId(UUID.randomUUID().toString() + System.currentTimeMillis());
//                tokenEntity.setUserId(userId);
//                tokenEntity.setToken(token);
//                tokenEntity.setUpdateTime(now);
//                tokenEntity.setExpireTime(expireTime);
//                //保存token
//                tokenMapper.insertSelective(tokenEntity);
//            } else {
//                tokenEntity.setToken(token);
//                tokenEntity.setUpdateTime(now);
//                tokenEntity.setExpireTime(expireTime);
//                //更新token
//                int i = tokenMapper.updateByPrimaryKey(tokenEntity);
//            }
        }
        //返回token给前端
        result.put("token", token);
        result.put("expire", expireTime);
        return ResultVO.getSuccess(result);
    }

    @Override
    public ResultVO logout(String accessToken) {
        if (isRedisLogin) {
//            Map<Object, Object> map = (Map<Object, Object>) redisUtil.get("tokenSession:"+accessToken);
            String userId = (String) redisUtil.hget("tokenSession:" + accessToken, "userId");
//            String userId = (String) map.get("userId");
            redisUtil.del("tokenSession:" + accessToken);
            redisUtil.del("session:" + userId);
            return ResultVO.getSuccess();
        } else {
            //生成一个token
            TokenProccessor tokenProccessor = TokenProccessor.getInstance();
            //生成一个token
            String token = tokenProccessor.makeToken();
            //修改token
//            FtToken tokenEntity = new FtToken();
//            tokenEntity.setToken(token);
//            Example example = new Example(FtToken.class);
//            example.createCriteria().andEqualTo("token", accessToken);
//            int i = tokenMapper.updateByExampleSelective(tokenEntity, example);
            Map<String, Object> resultEntity = new HashMap<>();
            resultEntity.put("TOKEN", token);
            Condition condition = Condition.creatCriteria().andEqual("TOKEN", accessToken).build();
            int i = baseMapper.updateByCondition("FT_TOKEN", resultEntity, condition);
            if (i == 1) {
                return ResultVO.getSuccess();
            } else {
                throw new ServiceException("退出失败");
            }
        }
    }

    @Override
    public FtToken findByToken(String accessToken) {
        FtToken osToken = new FtToken();
//        osToken.setToken(accessToken);
        Condition condition = Condition.creatCriteria().andEqual("TOKEN", accessToken).build();
        Map<String, Object> map = baseMapper.selectOneByCondition("FT_TOKEN", condition);
        osToken.setToken((String) map.get("TOKEN"));
        osToken.setTokenId((String) map.get("TOKEN_ID"));
        osToken.setUserId((Integer) map.get("USER_ID"));
        osToken.setExpireTime((Date) map.get("EXPIRE_TIME"));
        osToken.setUpdateTime((Date) map.get("UPDATE_TIME"));
        return osToken;
    }

    @Override
    public Object findByUserId(String userId) {
        return systemRealm.findByUserId(userId);
    }

    public static void main(String[] args) {
        ServiceLoader<ShiroService> loader = ServiceLoader.load(ShiroService.class);
        Iterator<ShiroService> iterator = loader.iterator();
        iterator.next();
    }
}


