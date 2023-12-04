package com.taixingyiji.base.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class HttpClient {


    //连接超时时间
    private static final int CONNECTION_TIME_OUT = 30000;

    //读取超时时间
    private static final int READ_TIME_OUT = 30000;

    /**
     * post方法接口
     * @param targetUrl---访问的接口网址
     * @param map----携带的参数
     * @return
     */
    public static Map<String,Object> postMethod(String targetUrl, Map<String,Object> map){
        //兑换带ticket的url
        String urlPath = targetUrl;
        // 发送请求
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
        JSONArray jsonArray1 = new JSONArray();
        Map<String, Object> map2 = HttpClient.getCasUtils(jsonObject, jsonArray1, "");
        RestTemplate restTemplate = (RestTemplate) map2.get("restTemplate");
        HttpEntity<String> httprequest = (HttpEntity<String>) map2.get("httprequest");
        String response = restTemplate.postForObject(urlPath, httprequest, String.class);
        JSONObject jsonObject1 = JSON.parseObject(response);
        Map<String, Object> resultMap = JSONObject.toJavaObject(jsonObject1, Map.class);
        return resultMap;
    }

    /**
     * get方法
     * @param targetUrl---目标地址
     * @param itemMap----传入参数
     * @return
     */
    public static Map<String,Object> getMethod(String targetUrl,Map<String,Object> itemMap){
        String url = targetUrl;
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(itemMap));
        JSONArray jsonArray = new JSONArray();
        Map<String, Object> map = HttpClient.getCasUtils(jsonObject,jsonArray,"");
        RestTemplate restTemplate = (RestTemplate) map.get("restTemplate");
        String response=restTemplate.getForEntity(url,String.class).getBody();
        JSONObject result = JSON.parseObject(response);
        Map<String, Object> resultMap = JSONObject.toJavaObject(result, Map.class);
        return resultMap;
    }

    public static Map<String,Object> getCasUtils(JSONObject jsonObject, JSONArray jsonArray, String str){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECTION_TIME_OUT);
        requestFactory.setReadTimeout(READ_TIME_OUT);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        // 参数
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> httprequest= null;
        if (jsonObject.isEmpty()&&!jsonArray.isEmpty()) {
            httprequest = new HttpEntity<String>(jsonArray.toJSONString(), headers);
        } else if (jsonArray.isEmpty() && !jsonObject.isEmpty()) {
            httprequest = new HttpEntity<String>(jsonObject.toJSONString(), headers);
        } else {
            httprequest = new HttpEntity<String>(str, headers);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("restTemplate", restTemplate);
        map.put("httprequest", httprequest);
        return map;
    }
}
