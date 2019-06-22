package com.neighbor.app.game.entity;

import com.neighbor.app.common.entity.PageEntity;
import com.neighbor.common.util.DateFormateType;
import com.neighbor.common.util.DateUtils;

import java.util.Date;

public class Game  extends PageEntity {
    private Long id;

    private Date createTime;
    private String createTimeStr;
    private String url;
    private String headUrl;

    private Date updateTime;

    private String gameName;

    private Long gameType;

    private String gameTypeStr;

    public String getGameTypeStr() {
        return gameTypeStr;
    }

    public void setGameTypeStr(String gameTypeStr) {
        this.gameTypeStr = gameTypeStr;
    }

    public String getCreateTimeStr() {
        if(createTime!=null){
            return DateUtils.formatDateStr(createTime, DateFormateType.LANG_FORMAT);
        }
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", createTimeStr='" + createTimeStr + '\'' +
                ", url='" + url + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", updateTime=" + updateTime +
                ", gameName='" + gameName + '\'' +
                ", gameType=" + gameType +
                ", gameTypeStr='" + gameTypeStr + '\'' +
                '}';
    }
}