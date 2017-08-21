package com.a2017hkt15.sortaddr;

import android.graphics.drawable.Drawable;

/**
 * Created by gwmail on 2017-08-21.
 */

public class ListViewWay {
    private Drawable deleteImage;
    private String titleStr;
    private String addrStr;

    public Drawable getDeleteImage() {
        return deleteImage;
    }

    public void setDeleteImage(Drawable deleteImage) {
        this.deleteImage = deleteImage;
    }

    public String getAddrStr() {
        return addrStr;
    }

    public void setAddrStr(String addrStr) {
        this.addrStr = addrStr;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }
}
