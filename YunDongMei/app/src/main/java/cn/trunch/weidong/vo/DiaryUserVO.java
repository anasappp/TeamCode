package cn.trunch.weidong.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.trunch.weidong.entity.DiaryEntity;
import cn.trunch.weidong.entity.UserEntity;
import io.reactivex.disposables.Disposable;

/**
 *
 */

public class DiaryUserVO extends BmobObject implements Serializable {

    private static final long serialVersionUID = 8649420209386553539L;

    private UserEntity user;
    private UserEntity buser;
    private String userId;
    private String buserId;
    private List<String> img;
    private int isLike;
    private int isFollow;
    private String diaryId;
    private String diaryUid;
    private String diaryBUid;
    private String diaryTime;
    private String diaryLable;
    private int diaryType;
    private String diaryTitle;
    private String diaryContent;
    private String diaryContentPreview;
    private String diaryImgPreview;
    private int diaryAnonymous;
    private int diaryReadNum;
    private int diaryCommentNum;
    private int diaryLikeNum;
    private int diaryReward;
    private int diaryStatus;
    private List<String> diaryLikUserNids;



    public List<String> getDiaryLikUserNids() {
        if (diaryLikUserNids == null)
            diaryLikUserNids = new ArrayList<>();
        return diaryLikUserNids;
    }

    public void setDiaryLikUserNids(List<String> diaryLikUserNids) {
        this.diaryLikUserNids = diaryLikUserNids;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBuserId() {
        return buserId;
    }

    public void setBuserId(String buserId) {
        this.buserId = buserId;
    }

    public String getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(String diaryId) {
        this.diaryId = diaryId;
    }

    public String getDiaryUid() {
        return diaryUid;
    }

    public void setDiaryUid(String diaryUid) {
        this.diaryUid = diaryUid;
    }

    public String getDiaryBUid() {
        return diaryBUid;
    }

    public void setDiaryBUid(String diaryBUid) {
        this.diaryBUid = diaryBUid;
    }

    public String getDiaryTime() {
        return diaryTime;
    }

    public void setDiaryTime(String diaryTime) {
        this.diaryTime = diaryTime;
    }

    public String getDiaryLable() {
        return diaryLable;
    }

    public void setDiaryLable(String diaryLable) {
        this.diaryLable = diaryLable;
    }

    public int getDiaryType() {
        return diaryType;
    }

    public void setDiaryType(int diaryType) {
        this.diaryType = diaryType;
    }

    public String getDiaryTitle() {
        return diaryTitle;
    }

    public void setDiaryTitle(String diaryTitle) {
        this.diaryTitle = diaryTitle;
    }

    public String getDiaryContent() {
        return diaryContent;
    }

    public void setDiaryContent(String diaryContent) {
        this.diaryContent = diaryContent;
    }

    public String getDiaryContentPreview() {
        return diaryContentPreview;
    }

    public void setDiaryContentPreview(String diaryContentPreview) {
        this.diaryContentPreview = diaryContentPreview;
    }

    public String getDiaryImgPreview() {
        return diaryImgPreview;
    }

    public void setDiaryImgPreview(String diaryImgPreview) {
        this.diaryImgPreview = diaryImgPreview;
    }

    public int getDiaryAnonymous() {
        return diaryAnonymous;
    }

    public void setDiaryAnonymous(int diaryAnonymous) {
        this.diaryAnonymous = diaryAnonymous;
    }

    public int getDiaryReadNum() {
        return diaryReadNum;
    }

    public void setDiaryReadNum(int diaryReadNum) {
        this.diaryReadNum = diaryReadNum;
    }

    public int getDiaryCommentNum() {
        return diaryCommentNum;
    }

    public void setDiaryCommentNum(int diaryCommentNum) {
        this.diaryCommentNum = diaryCommentNum;
    }

    public int getDiaryLikeNum() {
        return diaryLikeNum;
    }

    public void setDiaryLikeNum(int diaryLikeNum) {
        this.diaryLikeNum = diaryLikeNum;
    }

    public int getDiaryReward() {
        return diaryReward;
    }

    public void setDiaryReward(int diaryReward) {
        this.diaryReward = diaryReward;
    }

    public int getDiaryStatus() {
        return diaryStatus;
    }

    public void setDiaryStatus(int diaryStatus) {
        this.diaryStatus = diaryStatus;
    }

    public int getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }

    public int getRepositity() {
        return repositity;
    }

    public void setRepositity(int repositity) {
        this.repositity = repositity;
    }

    private int repositity;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UserEntity getBuser() {
        return buser;
    }

    public void setBuser(UserEntity buser) {
        this.buser = buser;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    /*    public DiaryUserVO(DiaryEntity diary) {
            super(diary);
        }
        public DiaryUserVO(DiaryUserVO diary) {
            super((DiaryEntity)diary);
            this.user=diary.getUser();
            this.buser=diary.getBuser();
            this.img=diary.getImg();
            this.repositity=diary.getRepositity();
            this.isLike=diary.getIsLike();
        }*/
    public DiaryUserVO() {
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }
}
