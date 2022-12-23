package cn.trunch.weidong.entity;

import java.io.Serializable;

public class FollowInfoEntity implements Serializable {
    private static final long serialVersionUID = 3462213479004817845L;

    /**
     * followId : 20210508110005570Cw171
     * fUserId : 20210508090451976t1PW7
     * fHeUserId : 20210506104618551L5p2D
     * fTime : 2021-05-08 11:00:05
     * fType : 0
     * userEntity : {"uId":"20210506104618551L5p2D","uPhone":"15879104925","uNickname":"远方的过客","uGender":1,"uAvatar":"http://192.168.0.114:8080/back/user/getImg?imgPathEncode=/Users/liuyanping/Desktop/javaproject/images/7688cf7013358db.jpg","uExTime":0,"uRank":1,"uExAmount":0,"uRegTime":"2021-05-06 10:46:18","uPostNum":0}
     */

    private String followId;
    private String fUserId;
    private String fHeUserId;
    private String fTime;
    private Integer fType;
    private UserEntityBean userEntity;
    private DiaryEntityBean diaryEntity;

    public DiaryEntityBean getDiaryEntity() {
        return diaryEntity;
    }

    public void setDiaryEntity(DiaryEntityBean diaryEntity) {
        this.diaryEntity = diaryEntity;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFollowId() {
        return followId == null ? "" : followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }

    public String getfUserId() {
        return fUserId == null ? "" : fUserId;
    }

    public void setfUserId(String fUserId) {
        this.fUserId = fUserId;
    }

    public String getfHeUserId() {
        return fHeUserId == null ? "" : fHeUserId;
    }

    public void setfHeUserId(String fHeUserId) {
        this.fHeUserId = fHeUserId;
    }

    public String getfTime() {
        return fTime == null ? "" : fTime;
    }

    public void setfTime(String fTime) {
        this.fTime = fTime;
    }

    public Integer getfType() {
        return fType;
    }

    public void setfType(Integer fType) {
        this.fType = fType;
    }

    public UserEntityBean getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntityBean userEntity) {
        this.userEntity = userEntity;
    }

    public  static  class DiaryEntityBean{
        private static final long serialVersionUID = 9175569770353339936L;
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

    }
    public static class UserEntityBean {
        /**
         * uId : 20210506104618551L5p2D
         * uPhone : 15879104925
         * uNickname : 远方的过客
         * uGender : 1
         * uAvatar : http://192.168.0.114:8080/back/user/getImg?imgPathEncode=/Users/liuyanping/Desktop/javaproject/images/7688cf7013358db.jpg
         * uExTime : 0
         * uRank : 1
         * uExAmount : 0
         * uRegTime : 2021-05-06 10:46:18
         * uPostNum : 0
         */

        private String uId;
        private String uPhone;
        private String uNickname;
        private Integer uGender;
        private String uAvatar;
        private Integer uExTime;
        private Integer uRank;
        private Integer uExAmount;
        private String uRegTime;
        private Integer uPostNum;

        public String getuId() {
            return uId == null ? "" : uId;
        }

        public void setuId(String uId) {
            this.uId = uId;
        }

        public String getuPhone() {
            return uPhone == null ? "" : uPhone;
        }

        public void setuPhone(String uPhone) {
            this.uPhone = uPhone;
        }

        public String getuNickname() {
            return uNickname == null ? "" : uNickname;
        }

        public void setuNickname(String uNickname) {
            this.uNickname = uNickname;
        }

        public Integer getuGender() {
            return uGender;
        }

        public void setuGender(Integer uGender) {
            this.uGender = uGender;
        }

        public String getuAvatar() {
            return uAvatar == null ? "" : uAvatar;
        }

        public void setuAvatar(String uAvatar) {
            this.uAvatar = uAvatar;
        }

        public Integer getuExTime() {
            return uExTime;
        }

        public void setuExTime(Integer uExTime) {
            this.uExTime = uExTime;
        }

        public Integer getuRank() {
            return uRank;
        }

        public void setuRank(Integer uRank) {
            this.uRank = uRank;
        }

        public Integer getuExAmount() {
            return uExAmount;
        }

        public void setuExAmount(Integer uExAmount) {
            this.uExAmount = uExAmount;
        }

        public String getuRegTime() {
            return uRegTime == null ? "" : uRegTime;
        }

        public void setuRegTime(String uRegTime) {
            this.uRegTime = uRegTime;
        }

        public Integer getuPostNum() {
            return uPostNum;
        }

        public void setuPostNum(Integer uPostNum) {
            this.uPostNum = uPostNum;
        }
    }
}