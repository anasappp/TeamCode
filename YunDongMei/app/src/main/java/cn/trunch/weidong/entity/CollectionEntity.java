package cn.trunch.weidong.entity;

public class CollectionEntity {

    /**
     * collectionId : 20210507223025097F7x90
     * cDataId : 20210507160644957k61Sf
     * cUserId : 20210506104618551L5p2D
     * cType : 1
     * videoInfoEntity : {"videoId":"20210507160644957k61Sf","vTitle":"标准健身教学：3个动作打造完美肩部肌肉，肌肉教练精彩演示！","vContent":"好身材从现在开始","vUrl":"https://vd3.bdstatic.com/mda-ifmstbhmfksy0gd8/sc/mda-ifmstbhmfksy0gd8.mp4?v_from_s=nj_haokan_4469&auth_key=1620375957-0-0-db932bad47d855cf46f74c319e85746b&bcevod_channel=searchbox_feed&pd=1&pt=3&abtest=3000165_2","vTime":"2021-05-07 16:06:44","vType":0}
     */

    private String collectionId;
    private String cDataId;
    private String cUserId;
    private Integer cType;
    private VideoInfoEntityBean videoInfoEntity;

    public String getCollectionId() {
        return collectionId == null ? "" : collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getcDataId() {
        return cDataId == null ? "" : cDataId;
    }

    public void setcDataId(String cDataId) {
        this.cDataId = cDataId;
    }

    public String getcUserId() {
        return cUserId == null ? "" : cUserId;
    }

    public void setcUserId(String cUserId) {
        this.cUserId = cUserId;
    }

    public Integer getcType() {
        return cType;
    }

    public void setcType(Integer cType) {
        this.cType = cType;
    }

    public VideoInfoEntityBean getVideoInfoEntity() {
        return videoInfoEntity;
    }

    public void setVideoInfoEntity(VideoInfoEntityBean videoInfoEntity) {
        this.videoInfoEntity = videoInfoEntity;
    }

    public static class VideoInfoEntityBean {
        /**
         * videoId : 20210507160644957k61Sf
         * vTitle : 标准健身教学：3个动作打造完美肩部肌肉，肌肉教练精彩演示！
         * vContent : 好身材从现在开始
         * vUrl : https://vd3.bdstatic.com/mda-ifmstbhmfksy0gd8/sc/mda-ifmstbhmfksy0gd8.mp4?v_from_s=nj_haokan_4469&auth_key=1620375957-0-0-db932bad47d855cf46f74c319e85746b&bcevod_channel=searchbox_feed&pd=1&pt=3&abtest=3000165_2
         * vTime : 2021-05-07 16:06:44
         * vType : 0
         */

        private String videoId;
        private String vTitle;
        private String vContent;
        private String vUrl;
        private String vTime;
        private Integer vType;

        public String getVideoId() {
            return videoId == null ? "" : videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getvTitle() {
            return vTitle == null ? "" : vTitle;
        }

        public void setvTitle(String vTitle) {
            this.vTitle = vTitle;
        }

        public String getvContent() {
            return vContent == null ? "" : vContent;
        }

        public void setvContent(String vContent) {
            this.vContent = vContent;
        }

        public String getvUrl() {
            return vUrl == null ? "" : vUrl;
        }

        public void setvUrl(String vUrl) {
            this.vUrl = vUrl;
        }

        public String getvTime() {
            return vTime == null ? "" : vTime;
        }

        public void setvTime(String vTime) {
            this.vTime = vTime;
        }

        public Integer getvType() {
            return vType;
        }

        public void setvType(Integer vType) {
            this.vType = vType;
        }
    }
}
