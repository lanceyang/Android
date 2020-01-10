package com.lance.library.push;

/**
 * Created by fermo.fxd on 2017/11/3.
 * 登录返回阿里云使用信息部分
 */

public class AliyunPO {
    private String ticket;
    private String device;
    private String account;
    private String tag;
    private String phone;

    public AliyunPO() {
    }

    public AliyunPO(String ticket, String device, String account, String tag, String phone) {
        this.ticket = ticket;
        this.device = device;
        this.account = account;
        this.tag = tag;
        this.phone = phone;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getDevide() {
        return device;
    }

    public void setDevide(String devide) {
        this.device = devide;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
