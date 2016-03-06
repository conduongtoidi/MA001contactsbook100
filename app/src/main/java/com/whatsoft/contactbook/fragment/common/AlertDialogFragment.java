package com.whatsoft.contactbook.fragment.common;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.whatsoft.contactbook.R;

public class AlertDialogFragment extends RetainedDialogFragment {

    public enum Type {
        ERROR,
        WARNING,
        INFO
    }

    public static AlertDialogFragment instantiate(String title, String message, Type type, OnDismissListener dismissListener) {
        AlertDialogFragment frag = new AlertDialogFragment();
        frag.message = message;
        frag.title = title;
        frag.setOnDismissListener(dismissListener);
        frag.type = type;
        return frag;
    }

    private String message;
    private String title;
    private Type type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_connection_error);

        if (!TextUtils.isEmpty(title)) {
            TextView tvTitle = ((TextView) dialog.findViewById(R.id.title));
            tvTitle.setText(Html.fromHtml(title));
        }

        if (!TextUtils.isEmpty(message)) {
            TextView tvMessage = ((TextView) dialog.findViewById(R.id.tvMessage));
            tvMessage.setText(Html.fromHtml(message));
        }

//        ImageView ivIcon = (ImageView) dialog.findViewById(R.id.iv_icon);
//        if(type != Type.ERROR){
//            ivIcon.setVisibility(View.GONE);
//        }
        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}