package com.lance.library.custom;

/**
 * Created by Tjcx on 2017/9/20.
 */

public abstract class CustomBean {

    private static final int DEFAULT_HOLDER_TYPE = -1;

    protected int holderType;

    public CustomBean(int holderType) {
        this.holderType = holderType;
    }

    protected CustomBean() {
        this(DEFAULT_HOLDER_TYPE);
    }

    protected void setHolderType(int holderType) {
        this.holderType = holderType;
    }

    public int getHolderType() {
        return holderType;
    }
}
