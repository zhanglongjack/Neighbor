package com.neighbor.app.game.dao;

import com.neighbor.app.game.entity.GameRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GameRuleMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(GameRule record);

    GameRule selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GameRule record);

    Long selectPageTotalCount(GameRule record);

    List<GameRule> selectPageByObjectForList(GameRule record);

    List<GameRule> selectAll();

	List<GameRule> selectBySelective(GameRule gameRule);
}