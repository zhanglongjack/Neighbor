package com.neighbor.app.game.dao;

import com.neighbor.app.game.entity.Game;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GameMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Game record);

    int insertSelective(Game record);

    Game selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Game record);

    int updateByPrimaryKey(Game record);
}