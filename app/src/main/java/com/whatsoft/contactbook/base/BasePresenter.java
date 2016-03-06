package com.whatsoft.contactbook.base;

public class BasePresenter<T extends BaseView> implements Presenter<T> {

    private T v;

    @Override
    public void attachView(T view) {
        v = view;
    }

    @Override
    public void deAttachView() {
        v = null;
    }

    public T getBaseView() {
        return v;
    }
}
