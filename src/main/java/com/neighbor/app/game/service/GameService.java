package com.neighbor.app.game.service;

import com.neighbor.app.game.entity.GameRule;
import com.neighbor.common.util.ResponseResult;

import java.util.List;

public interface GameService {

    int deleteRuleByPrimaryKey(Long id);

    int insertRuleSelective(GameRule record);

    GameRule selectRuleByPrimaryKey(Long id);

    int updateRuleByPrimaryKeySelective(GameRule record);

    Long selectRulePageTotalCount(GameRule record);

    List<GameRule> selectPageRuleByObjectForList(GameRule record);

    List<GameRule> selectRuleAll();

    ResponseResult ruleMatching(GameRule gameRule);

    ResponseResult ruleRecord(GameRule gameRule);
}
