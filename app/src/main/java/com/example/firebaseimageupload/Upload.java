package com.example.firebaseimageupload;

public class Upload {
    String mName;
    String mImageUrl;

    public Upload(){

    }

    public Upload(String name, String imageUrl) {
        if(name.trim().equals(""))
            name="No name";
        mName = name;
       mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
