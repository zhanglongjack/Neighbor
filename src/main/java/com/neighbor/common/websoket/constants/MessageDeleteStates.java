package com.neighbor.common.websoket.constants;

import com.neighbor.app.chatlist.constants.ChatHistorySetDesc;

public enum MessageDeleteStates {
    normal("0"), delete("1");
    private String des;
    private MessageDeleteStates(String des) {
        this.des = des;
    }
    public String getDes() {
        return des;
    }
    public static String getDesByValue(String value) {
        return ChatHistorySetDesc.valueOf(value).getDes();
    }
}
