package com.neighbor.app.commission.service;

import java.util.List;
import com.neighbor.app.commission.entity.UserCommission;

public interface CommissionService {

    int insertSelective(UserCommission commission);

    UserCommission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserCommission commission);
    
    Long selectPageTotalCount(UserCommission commission);

	List<UserCommission> selectPageByObjectForList(UserCommission commission);

	UserCommission selectAmountBy(Long ownUserId);
}