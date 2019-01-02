package com.neighbor.app.cms.dao;

import com.neighbor.app.cms.entity.Cms;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CmsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Cms record);

    int insertSelective(Cms record);

    Cms selectByPrimaryKey(Long id);

    List<Cms> selectByParams(Cms record);

    int updateByPrimaryKeySelective(Cms record);

    int updateByPrimaryKey(Cms record);

    Long selectPageTotalCount(Cms record);
}