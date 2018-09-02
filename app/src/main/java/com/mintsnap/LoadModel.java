package com.mintsnap;


public class LoadModel
{
    private String mImageUrl;
    private String mCreator;
    private String  mLikes;

    public LoadModel(String mImageUrl, String mCreator, String mLikes)
    {
        this.mImageUrl = mImageUrl;
        this.mCreator = mCreator;
        this.mLikes = mLikes;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmCreator() {
        return mCreator;
    }

    public String getmLikes() {
        return mLikes;
    }
}