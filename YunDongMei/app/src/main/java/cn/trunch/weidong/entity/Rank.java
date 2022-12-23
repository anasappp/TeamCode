package cn.trunch.weidong.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 *
 */

public class Rank extends BmobObject implements Serializable {
    private static final long serialVersionUID = 2420214918726751716L;
    private int worldRank;      //世界排名
    private int schoolRank;     //同校排名
    private int myRank;          //我的段位
    private int rank;
    private String userId;
    private String time;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getWorldRank() {
        return worldRank;
    }

    public void setWorldRank(int worldRank) {
        this.worldRank = worldRank;
    }

    public int getSchoolRank() {
        return schoolRank;
    }

    public void setSchoolRank(int schoolRank) {
        this.schoolRank = schoolRank;
    }

    public int getMyRank() {
        return myRank;
    }

    public void setMyRank(int myRank) {
        this.myRank = myRank;
    }

    public Rank() {
    }

    public Rank(int worldRank, int schoolRank, int myRank) {
        this.worldRank = worldRank;
        this.schoolRank = schoolRank;
        this.myRank = myRank;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "worldRank=" + worldRank +
                ", schoolRank=" + schoolRank +
                ", myRank=" + myRank +
                ", rank=" + rank +
                ", userId='" + userId + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
