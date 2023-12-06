package com.taixingyiji.es.service;


import com.taixingyiji.base.common.ResultVO;

import javax.servlet.http.HttpServletRequest;

public interface SearchService {


    ResultVO getFullText(HttpServletRequest request, String text, Integer pageNum, Integer pageSize, String indexType);


    ResultVO expertSearch(HttpServletRequest request, String data, Integer pageNum, Integer pageSize, String indexType);
}
