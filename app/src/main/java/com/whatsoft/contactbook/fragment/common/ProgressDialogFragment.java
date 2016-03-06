package com.whatsoft.contactbook.fragment.common;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

public class ProgressDialogFragment extends RetainedDialogFragment {
    private CharSequence message;

    public static ProgressDialogFragment instantiate(String message, boolean cancelable, OnCancelListener cancelListener) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        fragment.message = message;
        fragment.setCancelable(cancelable);
        fragment.setOnCancelListener(cancelListener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(message);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(isCancelable());
        pDialog.setCanceledOnTouchOutside(false);
        return pDialog;
    }
}
