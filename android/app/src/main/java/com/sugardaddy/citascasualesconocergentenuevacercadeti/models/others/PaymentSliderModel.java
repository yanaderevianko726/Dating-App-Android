package com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;

public class PaymentSliderModel {

    public static final String SLIDER_YPE_PREMIUM = "PREMIUM";
    public static final String SLIDER_YPE_NORMAL = "NORMAL";

    private String type;
    private String title;
    private String credit;
    private int badgeImage;
    private int OtherImage;
    private User mUser;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public int getBadgeImage() {
        return badgeImage;
    }

    public void setBadgeImage(int badgeImage) {
        this.badgeImage = badgeImage;
    }

    public int getOtherImage() {
        return OtherImage;
    }

    public void setOtherImage(int otherImage) {
        OtherImage = otherImage;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }
}
