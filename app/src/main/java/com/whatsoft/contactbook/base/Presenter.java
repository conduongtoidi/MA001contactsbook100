package com.whatsoft.contactbook.base;

public interface Presenter<V extends BaseView> {
    public void attachView(V view);
    public void deAttachView();
}
