package cn.trunch.weidong.util;

public class GPSUtils {
    private static final double EARTH_RADIUS = 6378137.0;

    /**
     * 判断是否在区域内
     *
     * @param latitue
     * @param longitude
     * @param areaLatitude1
     * @param areaLatitude2
     * @param areaLongitude1
     * @param areaLongitude2
     * @return
     */
    public static boolean isInArea(Double latitue,
                                   Double longitude,
                                   Double areaLatitude1,
                                   Double areaLatitude2,
                                   Double areaLongitude1,
                                   Double areaLongitude2) {
        if (isInRange(latitue, areaLatitude1, areaLatitude2)) {//如果在纬度的范围内
            if (areaLongitude1 * areaLongitude2 > 0) {//如果都在东半球或者都在西半球
                if (isInRange(longitude, areaLongitude1, areaLongitude2)) {
                    return true;
                } else {
                    return false;
                }
            } else {//如果一个在东半球，一个在西半球
                if (Math.abs(areaLongitude1) + Math.abs(areaLongitude2) < 180.0) {//如果跨越0度经线在半圆的范围内
                    if (isInRange(longitude, areaLongitude1, areaLongitude2)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {//如果跨越180度经线在半圆范围内
                    double left = Math.max(areaLongitude1, areaLongitude2);//东半球的经度范围left-180
                    double right = Math.min(areaLongitude1, areaLongitude2);//西半球的经度范围right-（-180）
                    if (isInRange(longitude, left, 180.0) || isInRange(longitude, right, -180.0)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
    }

    private static boolean isInRange(Double point, Double left, Double right) {
        if (point >= Math.min(left, right) && point <= Math.max(left, right)) {
            return true;
        } else {
            return false;
        }
    }

    /**

     * 根据两点间经纬度坐标(double值)，计算两点间距离，单位为米

     *

     * @param lng1

     * @param lat1

     * @param lng2

     * @param lat2

     * @return

     */

    public static double GetDistance(double lng1, double lat1, double lng2,
                                     double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;

        return s;

    }

    /**
     * 计算两点经纬度距离
     * @param longitude
     * @param latitue
     * @param longitude2
     * @param latitue2
     * @return
     */
    public static double getDistance(double longitude, double latitue, double longitude2, double latitue2) {
        double lat1 = rad(latitue);
        double lat2 = rad(latitue2);
        double a = lat1 - lat2;
        double b = rad(longitude) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    /**
     * @param lat1   纬度
     * @param lat2   纬度
     * @param lng1   经度
     * @param lng2   经度
     * @param radius 判断一个点是否在圆形区域内,比较坐标点与圆心的距离是否小于半径
     */
    public static boolean isInCircle(double lng1, double lat1, double lng2, double lat2, double radius) {
        double distance = getDistance(lat1, lng1, lat2, lng2);
        if (distance > radius) {
            return false;
        } else {
            //System.err.println("经度："+lng1+"维度："+lat1+"经度："+lng2+"维度："+lat2+"距离:"+distance);
            return true;
        }
    }



}
