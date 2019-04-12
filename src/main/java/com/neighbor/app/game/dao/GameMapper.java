package com.neighbor.app.game.dao;

import com.neighbor.app.game.entity.Game;
import com.neighbor.app.game.entity.GameRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GameMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(Game record);

    Game selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Game record);


    Long selectPageTotalCount(Game record);

    List<Game> selectPageByObjectForList(Game record);

    List<Game> selectAll();

    List<Game> selectBySelective(Game game);
}