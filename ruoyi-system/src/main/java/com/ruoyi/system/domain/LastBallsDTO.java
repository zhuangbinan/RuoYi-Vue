package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

@TableName("last_balls")
public class LastBallsDTO extends BaseEntity {

    public LastBallsDTO() {}

    private Long id;

    private String userName;

    private int countNum;

    private String blueBalls;

    private String redBall;

    private Date createTime;

    private String other;

    @Override
    public String toString() {
        return "LastBallsDTO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", countNum=" + countNum +
                ", blueBalls='" + blueBalls + '\'' +
                ", redBall='" + redBall + '\'' +
                ", createTime=" + createTime +
                ", other='" + other + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCountNum() {
        return countNum;
    }

    public void setCountNum(int countNum) {
        this.countNum = countNum;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlueBalls() {
        return blueBalls;
    }

    public void setBlueBalls(String blueBalls) {
        this.blueBalls = blueBalls;
    }

    public String getRedBall() {
        return redBall;
    }

    public void setRedBall(String redBall) {
        this.redBall = redBall;
    }

}
