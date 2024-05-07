package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

@TableName("buy_record")
public class BuyRecordDTO extends BaseEntity {

    private Long id;

    private String userName;
    //期数
    private int countNum;

    private String blueBalls;

    private String redBall;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //注数
    private int buyCount;

    //是否中奖
    private String rightBallFlag;

    //是否兑奖
    private String checkScoreFlag;

    public BuyRecordDTO() {
    }

    @Override
    public String toString() {
        return "BuyRecordDTO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", countNum=" + countNum +
                ", blueBalls='" + blueBalls + '\'' +
                ", redBall='" + redBall + '\'' +
                ", createTime=" + createTime +
                ", buyCount=" + buyCount +
                ", rightBallFlag='" + rightBallFlag + '\'' +
                ", checkScoreFlag='" + checkScoreFlag + '\'' +
                '}';
    }

    public String getRightBallFlag() {
        return rightBallFlag;
    }

    public void setRightBallFlag(String rightBallFlag) {
        this.rightBallFlag = rightBallFlag;
    }

    public String getCheckScoreFlag() {
        return checkScoreFlag;
    }

    public void setCheckScoreFlag(String checkScoreFlag) {
        this.checkScoreFlag = checkScoreFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }
}
