package com.neighbor.app.game.service;

import com.neighbor.app.game.constants.RuleTypeDesc;
import com.neighbor.app.game.entity.Game;
import com.neighbor.app.game.entity.GameRule;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.ResponseResult;

import java.util.List;

public interface GameService {

    int deleteRuleByPrimaryKey(Long id);

    int insertRuleSelective(GameRule record);

    Long selectPageTotalCount(Game game);

    GameRule selectRuleByPrimaryKey(Long id);

    int updateRuleByPrimaryKeySelective(GameRule record);

    Long selectRulePageTotalCount(GameRule record);

    List<GameRule> selectPageRuleByObjectForList(GameRule record);

    List<GameRule> selectRuleAll();

    GameRule ruleMatching(GameRule gameRule);

    ResponseResult ruleRecord(GameRule gameRule);

	GameRule ruleMatching(long gameId, RuleTypeDesc award, double value);

	List<GameRule> ruleCommissionRecord(Long gameId, int ruleType);

    List<Game> selectPageByObjectForList(Game game);

    int updateByGame(Game game);
    Game selectByPrimaryKey(Long id);
    int insertSelective(Game game);

    ResponseResult listRecord(UserInfo user, Game game);
}
