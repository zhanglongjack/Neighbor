package com.neighbor.app.game.entity;

import com.neighbor.app.common.entity.PageEntity;
import com.neighbor.common.util.DateFormateType;
import com.neighbor.common.util.DateUtils;

import java.util.Date;

public class Game  extends PageEntity {
    private Long id;

    private Date createTime;
    private String createTimeStr;

    private Date updateTime;

    private String gameName;

    private Long gameType;

    private String gameTypeStr;

    public String getGameTypeStr() {
        if(gameType!=null){
            if(gameType==1){
                return "红包游戏";
            }else if(gameType==2){
                return "猜猜乐";
            }else if(gameType==3){
                return "休闲游戏";
            }else if(gameType==4){
                return "福利群";
            }
        }
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


}