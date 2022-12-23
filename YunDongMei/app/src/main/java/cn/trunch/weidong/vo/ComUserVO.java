package cn.trunch.weidong.vo;


import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.trunch.weidong.entity.CommentEntity;
import cn.trunch.weidong.entity.UserEntity;

/**

 */

public class ComUserVO extends BmobObject implements Serializable {
    private static final long serialVersionUID = 7303548370899898267L;
    private UserEntity user;
    private int repository; //0为点赞  一点咱
    private String comId;
    private String comItemId;
    private String comUid;
    private String comBUid;
    private int comType;
    private String comContent;
    private int comLikeNum;
    private String comTime;
    private List<String> imgs;

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public String getComItemId() {
        return comItemId;
    }

    public void setComItemId(String comItemId) {
        this.comItemId = comItemId;
    }

    public String getComUid() {
        return comUid;
    }

    public void setComUid(String comUid) {
        this.comUid = comUid;
    }

    public String getComBUid() {
        return comBUid;
    }

    public void setComBUid(String comBUid) {
        this.comBUid = comBUid;
    }

    public int getComType() {
        return comType;
    }

    public void setComType(int comType) {
        this.comType = comType;
    }

    public String getComContent() {
        return comContent;
    }

    public void setComContent(String comContent) {
        this.comContent = comContent;
    }

    public int getComLikeNum() {
        return comLikeNum;
    }

    public void setComLikeNum(int comLikeNum) {
        this.comLikeNum = comLikeNum;
    }

    public String getComTime() {
        return comTime;
    }

    public void setComTime(String comTime) {
        this.comTime = comTime;
    }
    public int getRepository() {
        return repository;
    }

    public void setRepository(int repository) {
        this.repository = repository;
    }



    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ComUserVO() {
        this.repository=0;
    }
}
