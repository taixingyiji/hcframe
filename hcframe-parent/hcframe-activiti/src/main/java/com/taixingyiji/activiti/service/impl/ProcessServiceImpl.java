package com.taixingyiji.activiti.service.impl;

import com.taixingyiji.activiti.activiti.ActivitiUtils;
import com.taixingyiji.activiti.activiti.CommonProcessDefinition;
import com.taixingyiji.activiti.service.ProcessService;
import com.taixingyiji.activiti.utils.FileUtil;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.ServiceException;
import com.hcframe.base.common.WebPageInfo;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProcessServiceImpl implements ProcessService {

    private final static Logger logger = LoggerFactory.getLogger(ProcessServiceImpl.class);

    /**
     * 获取编译后的文件路径
     */
    public static String CLASSPATH;

    static {
        try {
            CLASSPATH = ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX + "bpmn").getPath() + "/";
        } catch (FileNotFoundException e) {
            throw new ServiceException("获取bpmn文件上传路径出错！");
        }
    }

    @Resource
    private RepositoryService repositoryService;

    @Resource
    ActivitiUtils activitiUtils;

    @Override
    public ResultVO getProcessDefinitionList(CommonProcessDefinition commonProcessDefinition,
                                             WebPageInfo webPageInfo, Boolean isLatestVersion) {
        return ResultVO.getSuccess(activitiUtils.getDeployProcessList(commonProcessDefinition, webPageInfo, isLatestVersion));
    }

    @Override
    public ResultVO deleteProcessDefinitionList(String id, boolean state) {
        activitiUtils.deleteDeploymenet(id, true);
        return ResultVO.getSuccess("删除成功");
    }

    @Override
    public ResultVO addProccessDefinition(MultipartFile file, String name) {
        if (file == null || file.isEmpty()) {
            throw new ServiceException("bpmn文件为空");
        }
        String fileName = file.getOriginalFilename();
        assert fileName != null : "文件名不存在";
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        assert !suffixName.equals(CommonProcessDefinition.BPMN) : "文件类型错误";
        Map<String, Object> map = new HashMap<>(2);
        try {
            Deployment deployment = activitiUtils.deployProcessFormInputStream(name, file.getInputStream(), fileName);
            map.put("流程名称", deployment.getName());
            map.put("流程ID", deployment.getId());
        } catch (IOException e) {
            logger.error("bpmn文件持久化出错", e);
            throw new ServiceException("文件持久化出错");
        }
        return ResultVO.getSuccess(map);
    }

    @Override
    public ResultVO getBpmnFile(String deploymentId, String resourceName, HttpServletResponse response) {
        response.addHeader("Content-Disposition", "attachment;fileName=" + resourceName);
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        InputStream bpmnIs = null;
        try {
            bpmnIs = repositoryService.getResourceAsStream(deploymentId, resourceName);
            bis = new BufferedInputStream(bpmnIs);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            return null;
        } catch (Exception e) {
            throw new ServiceException("文件下载失败");
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bpmnIs != null) {
                try {
                    bpmnIs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public ResultVO addProccessDefinitionByXml(String file, String name, String fileName) {
        if (file == null || file.isEmpty()) {
            throw new ServiceException("bpmn文件为空");
        }
        String filePath = CLASSPATH + fileName;
        File dest = new File(filePath);
        if (dest.exists()) {
            dest.delete();
        }
        if (!activitiUtils.uniqueName(name)) {
            throw new ServiceException("name已存在，请重新命名！");
        }
        Map<String, Object> map = new HashMap<>(2);
        InputStream is = null;
        try {
            boolean flag = dest.createNewFile();
            if (!flag) {
                throw new ServiceException("创建文件失败！");
            }
            FileUtil.replaceContentToFile(file, filePath);
            is= new FileInputStream(filePath);
            Deployment deployment = activitiUtils.deployProcessFormInputStream(name, is, fileName);
            map.put("流程名称", deployment.getName());
            map.put("流程ID", deployment.getId());

        } catch (IOException e) {
            logger.error("bpmn文件持久化出错", e);
            throw new ServiceException("文件持久化出错");
        }finally {
            if (is != null) {
                try {
                    is.close();
                    dest.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ResultVO.getSuccess(map);
    }

    @Override
    public ResultVO updateProcessDefinitionByXml(String file, String name, String fileName, Integer version) {
        if (file == null || file.isEmpty()) {
            throw new ServiceException("bpmn文件为空");
        }
        String filePath = CLASSPATH + fileName;
        File dest = new File(filePath);
        if (dest.exists()) {
            dest.delete();
        }
        Map<String, Object> map = new HashMap<>(2);
        InputStream is = null;
        try {
            boolean flag = dest.createNewFile();
            if (!flag) {
                throw new ServiceException("创建文件失败！");
            }
            FileUtil.replaceContentToFile(file, filePath);
            is = new FileInputStream(filePath);
            Deployment deployment = activitiUtils.deployProcessFormInputStream(name, is, fileName);
            map.put("流程名称", deployment.getName());
            map.put("流程ID", deployment.getId());
        } catch (IOException e) {
            logger.error("bpmn文件持久化出错", e);
            throw new ServiceException("文件持久化出错");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ResultVO.getSuccess(map);
    }

    @Override
    public ResultVO uniqueRecord(String name, String key) {
        boolean flag = true;
        flag = activitiUtils.uniqueName(name);
        if (!flag) {
            return ResultVO.getFailed("流程名称已存在！");
        }
        flag = activitiUtils.uniqueKey(key);
        if (!flag) {
            return ResultVO.getFailed("流程标识已存在！");
        }
        return ResultVO.getSuccess();
    }


    public static void main(String[] args) throws FileNotFoundException {
    }

}
