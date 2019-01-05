package com.neighbor.app.group.dao;

import com.neighbor.app.group.entity.GroupApply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupApplyMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(GroupApply record);

    GroupApply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GroupApply record);

    Long selectPageTotalCount(GroupApply record);

    List<GroupApply> selectPageByObjectForList(GroupApply record);

    List<GroupApply> selectAll();

    List<GroupApply> selectFullInfoPageForList(GroupApply record);

    void clearGroupApplyNum(GroupApply groupApply);

    void deleteByGroupApply(GroupApply groupApply);
}