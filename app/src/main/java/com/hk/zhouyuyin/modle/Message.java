package com.hk.zhouyuyin.modle;

public class Message {
    private String strMessage;
    private String imgUrl;
    private String voideUrl;

    private boolean isLeft;

    public Message(String strMessage, String imgUrl, String voideUrl,
                   boolean isLeft) {
        super();
        this.strMessage = strMessage;
        this.imgUrl = imgUrl;
        this.voideUrl = voideUrl;
        this.isLeft = isLeft;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean isLeft) {
        this.isLeft = isLeft;
    }

    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVoideUrl() {
        return voideUrl;
    }

    public void setVoideUrl(String voideUrl) {
        this.voideUrl = voideUrl;
    }
}
