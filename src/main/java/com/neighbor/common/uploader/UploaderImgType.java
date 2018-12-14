package com.neighbor.common.uploader;

import com.neighbor.app.bankcard.constants.CardTypeDesc;

public enum UploaderImgType {
    avatar("avatar"),chat("chat"),common("common");
    private String des;

    private UploaderImgType(String des) {
        this.des = des;
    }
    public String getDes() {
        return des;
    }
    public static String getDesByValue(String value){
        return UploaderImgType.valueOf(value).getDes();
    }

    public static boolean contains(String _name){
        UploaderImgType[] season = values();
        for(UploaderImgType s:season){
            if(s.name().equals(_name)){
                return true;
            }
        }
        return false;

    }
}