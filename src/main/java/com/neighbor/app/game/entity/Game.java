package com.neighbor.app.game.entity;

import java.util.Date;

public class Game {
    private Long id;

    private Date createTime;

    private Date updateTime;

    private String gameName;

    private Long gameType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Long getGameType() {
        return gameType;
    }

    public void setGameType(Long gameType) {
        this.gameType = gameType;
    }
}