package cn.trunch.weidong.entity;


import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;

public class DiaryEntity extends BmobObject implements Serializable {

    private static final long serialVersionUID = 9175569770353339936L;
    private String diaryId;
    private String diaryUid;
    private String diaryUName;
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
    private int isLike;
    private List<String> diaryLikUserNids;

    public List<String> getDiaryLikUserNids() {
        return diaryLikUserNids;
    }

    public void setDiaryLikUserNids(List<String> diaryLikUserNids) {
        this.diaryLikUserNids = diaryLikUserNids;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public String getDiaryUName() {
        return diaryUName;
    }

    public void setDiaryUName(String diaryUName) {
        this.diaryUName = diaryUName;
    }

    public String getDiaryId() {
        return diaryId;
    }

    public String getDiaryLable() {
        return diaryLable;
    }

    public void setDiaryLable(String diaryLable) {
        this.diaryLable = diaryLable;
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

    public String getDiaryTime() {
        return diaryTime;
    }

    public void setDiaryTime(String diaryTime) {
        this.diaryTime = diaryTime;
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

    public String getDiaryBUid() {
        return diaryBUid;
    }

    public void setDiaryBUid(String diaryBUid) {
        this.diaryBUid = diaryBUid;
    }

    public DiaryEntity() {
    }

    public DiaryEntity(DiaryEntity diary) {
        this.diaryId = diary.getDiaryId();
        this.diaryUid = diary.getDiaryUid();
        this.diaryBUid = diary.getDiaryBUid();
        this.diaryTime = diary.getDiaryTime();
        this.diaryLable = diary.getDiaryLable();
        this.diaryType = diary.getDiaryType();
        this.diaryTitle = diary.getDiaryTitle();
        this.diaryContent = diary.getDiaryContent();
        this.diaryContentPreview = diary.getDiaryContentPreview();
        this.diaryImgPreview = diary.getDiaryImgPreview();
        this.diaryAnonymous = diary.getDiaryAnonymous();
        this.diaryReadNum = diary.getDiaryReadNum();
        this.diaryCommentNum = diary.getDiaryCommentNum();
        this.diaryLikeNum = diary.getDiaryLikeNum();
        this.diaryReward = diary.getDiaryReward();
        this.diaryStatus = diary.getDiaryStatus();
    }

    @Override
    public String toString() {
        return "DiaryEntity{" +
                "diaryId='" + diaryId + '\'' +
                ", diaryUid='" + diaryUid + '\'' +
                ", diaryUName='" + diaryUName + '\'' +
                ", diaryBUid='" + diaryBUid + '\'' +
                ", diaryTime='" + diaryTime + '\'' +
                ", diaryLable='" + diaryLable + '\'' +
                ", diaryType=" + diaryType +
                ", diaryTitle='" + diaryTitle + '\'' +
                ", diaryContent='" + diaryContent + '\'' +
                ", diaryContentPreview='" + diaryContentPreview + '\'' +
                ", diaryImgPreview='" + diaryImgPreview + '\'' +
                ", diaryAnonymous=" + diaryAnonymous +
                ", diaryReadNum=" + diaryReadNum +
                ", diaryCommentNum=" + diaryCommentNum +
                ", diaryLikeNum=" + diaryLikeNum +
                ", diaryReward=" + diaryReward +
                ", diaryStatus=" + diaryStatus +
                '}';
    }
}
