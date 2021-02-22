package com.sinoparasoft.storage.repository;

import com.sinoparasoft.storage.domain.HistoryFile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 历史文件管理接口
 *
 * @author 袁涛
 * @date 2017.10.26
 */
public interface HistoryFileRepository extends JpaRepository<HistoryFile, Long> {

}
