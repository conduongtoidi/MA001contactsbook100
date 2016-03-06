package com.whatsoft.contactbook.fragment.common;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.whatsoft.contactbook.utils.Log;
import com.whatsoft.contactbook.utils.PauseHandler;

public class RetainedDialogFragment extends DialogFragment {
    public interface OnCancelListener {
        void onCancel(RetainedDialogFragment fragment);
    }

    public interface OnDismissListener {
        void onDismiss(RetainedDialogFragment fragment);
    }

    private static class ConcreteTestHandler extends PauseHandler {
        /**
         * Activity instance
         */
        private RetainedDialogFragment fragment;

        /**
         * Set the activity associated with the handler
         */
        public void setFragment(RetainedDialogFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        protected boolean storeMessage(Message message) {
            // All messages are stored by default
            return true;
        }

        @Override
        protected void processMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT:
                    switch (msg.arg1) {
                        case MSG_SHOW_DIALOG:
                            fragment.superShow(fragment.getFragmentManager(), fragment.getTag());
                            break;
                        case MSG_DISMISS_DIALOG:
                            fragment.superDismiss();
                            break;
                    }
                    break;
            }
        }
    }

    /**
     * Used for "what" parameter to handler messages
     */
    private static final int MSG_WHAT = ('F' << 16) + ('T' << 8) + 'A';
    private static final int MSG_SHOW_DIALOG = 1;
    private static final int MSG_DISMISS_DIALOG = 2;

    /**
     * Handler for this activity
     */
    private ConcreteTestHandler handler = new ConcreteTestHandler();

    private OnCancelListener onCancelListener;
    private OnDismissListener onDismissListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.setFragment(this);
        handler.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.pause();
    }

    public void onDestroy() {
        super.onDestroy();
        handler.setFragment(null);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (onCancelListener != null) onCancelListener.onCancel(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) onDismissListener.onDismiss(this);
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    /**
     * A quick (and possibly dirty) workaround to the issue:
     * DialogFragment dismissed on orientation change when setRetainInstance(true) is set (compatibility library)
     * http://code.google.com/p/android/issues/detail?id=17423
     */
    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setOnDismissListener(null);
        super.onDestroyView();
    }

    /**
     * Removes old fragment with same tag first.
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        Log.d();
        if (manager.isDestroyed()) {
            return;
        }
        Fragment oldFragment = manager.findFragmentByTag(tag);
        FragmentTransaction ft = manager.beginTransaction();
        if (oldFragment != null) {
            ft.remove(oldFragment);
        }

        ft.add(this, tag);
        ft.commitAllowingStateLoss(); // TODO: find out what can cause the state to be lost, since the state doesn't seem to be lose-able
    }

    private void superShow(FragmentManager manager, String tag) {
        Log.d();
        super.show(manager, tag);
    }

    @Override
    public void dismiss() {
        handler.sendMessage(handler.obtainMessage(MSG_WHAT, MSG_DISMISS_DIALOG, 0));
    }

    private void superDismiss() {
        super.dismiss();
    }
}
