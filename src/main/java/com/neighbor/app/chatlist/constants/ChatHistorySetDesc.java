package com.neighbor.app.chatlist.constants;

public enum ChatHistorySetDesc {
    save("保留聊天记录"), oneHour("一小时"), twelveHours("十二小时"), oneDay("一天"), threeDay("三天"),sevenDay("七天");
    private String des;
    private ChatHistorySetDesc(String des) {
        this.des = des;
    }
    public String getDes() {
        return des;
    }
    public static String getDesByValue(String value) {
        return ChatHistorySetDesc.valueOf(value).getDes();
    }
}
