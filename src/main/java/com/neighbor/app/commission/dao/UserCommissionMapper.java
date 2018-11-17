package com.neighbor.app.commission.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.commission.entity.UserCommission;

@Mapper
public interface UserCommissionMapper {

    int insertSelective(UserCommission commission);

    UserCommission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserCommission commission);
    
    Long selectPageTotalCount(UserCommission commission);
    
    List<UserCommission> selectPageByObjectForList(UserCommission commission);

}