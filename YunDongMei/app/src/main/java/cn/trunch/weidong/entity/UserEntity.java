package cn.trunch.weidong.entity;

import android.text.TextUtils;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class UserEntity extends BmobObject implements Serializable {

  private static final long serialVersionUID = -5723025473217684554L;
  private String uId;
  private String uPhone;
  private String uPwd;
  private String uNickname;
  private String uSelfdes;
  private String uHeight;
  private String uWeight;
  private String uVialCap;
  private String uChangeTime;


  public String getuSelfdes() {
    return uSelfdes;
  }

  public void setuSelfdes(String uSelfdes) {
    this.uSelfdes = uSelfdes;
  }

  private String uBirthday;
  private int uGender;
  private String uAvatar;
  private int uExTime;
  private int uRank;
  private int uExAmount;
  private String uRegTime;
  private int uPostNum;






  public String getuId() {
    return uId;
  }

  public void setuId(String uId) {
    this.uId = uId;
  }

  public String getuPhone() {
    return uPhone;
  }

  public void setuPhone(String uPhone) {
    this.uPhone = uPhone;
  }

  public String getuPwd() {
    return uPwd;
  }


  public String getuBirthday() {
    return uBirthday;
  }

  public void setuBirthday(String uBirthday) {
    this.uBirthday = uBirthday;
  }

  public void setuPwd(String uPwd) {
    this.uPwd = uPwd;
  }

  public String getuNickname() {
    return uNickname;
  }

  public void setuNickname(String uNickname) {
    this.uNickname = uNickname;
  }

  public int getuGender() {
    return uGender;
  }

  public void setuGender(int uGender) {
    this.uGender = uGender;
  }

  public String getuAvatar() {
    return uAvatar;
  }

  public void setuAvatar(String uAvatar) {
    this.uAvatar = uAvatar;
  }

  public int getuExTime() {
    return uExTime;
  }

  public void setuExTime(int uExTime) {
    this.uExTime = uExTime;
  }

  public int getuRank() {
    return uRank;
  }

  public void setuRank(int uRank) {
    this.uRank = uRank;
  }

  public int getuExAmount() {
    return uExAmount;
  }

  public void setuExAmount(int uExAmount) {
    this.uExAmount = uExAmount;
  }

  public String getuRegTime() {
    return uRegTime;
  }

  public void setuRegTime(String uRegTime) {
    this.uRegTime = uRegTime;
  }

  public int getuPostNum() {
    return uPostNum;
  }

  public void setuPostNum(int uPostNum) {
    this.uPostNum = uPostNum;
  }

  public UserEntity() {
  }


  public String getuHeight() {
    if (TextUtils.isEmpty(uHeight))
      uHeight = "";
    return uHeight;
  }

  public void setuHeight(String uHeight) {
    this.uHeight = uHeight;
  }

  public String getuWeight() {
    if (TextUtils.isEmpty(uWeight))
      uWeight = "";
    return uWeight;
  }

  public void setuWeight(String uWeight) {
    this.uWeight = uWeight;
  }

  public String getuVialCap() {
    if (TextUtils.isEmpty(uVialCap))
      uVialCap = "";
    return uVialCap;
  }

  public void setuVialCap(String uVialCap) {
    this.uVialCap = uVialCap;
  }

  public String getuChangeTime() {
    return uChangeTime;
  }

  public void setuChangeTime(String uChangeTime) {
    this.uChangeTime = uChangeTime;
  }

  @Override
  public String toString() {
    return "UserEntity{" +
            "uId='" + uId + '\'' +
            ", uPhone='" + uPhone + '\'' +
            ", uPwd='" + uPwd + '\'' +
            ", uNickname='" + uNickname + '\'' +
            ", uSelfdes='" + uSelfdes + '\'' +
            ", uHeight=" + uHeight +
            ", uWeight=" + uWeight +
            ", uVialCap=" + uVialCap +
            ", uChangeTime='" + uChangeTime + '\'' +
            ", uBirthday='" + uBirthday + '\'' +
            ", uGender=" + uGender +
            ", uAvatar='" + uAvatar + '\'' +
            ", uExTime=" + uExTime +
            ", uRank=" + uRank +
            ", uExAmount=" + uExAmount +
            ", uRegTime='" + uRegTime + '\'' +
            ", uPostNum=" + uPostNum +
            '}';
  }
}
