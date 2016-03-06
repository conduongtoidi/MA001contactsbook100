package com.whatsoft.contactbook.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.nineoldandroids.animation.AnimatorSet;
import com.whatsoft.contactbook.R;
import com.whatsoft.contactbook.fragment.common.AlertDialogFragment;
import com.whatsoft.contactbook.fragment.common.ConfirmDialogFragment;
import com.whatsoft.contactbook.fragment.common.ProgressDialogFragment;
import com.whatsoft.contactbook.fragment.common.RetainedDialogFragment;

/**
 * Base class to extent from for all menu_dashboard activities
 */
public class DialogUtils {

    private static AnimatorSet firstAnimationSet = null;
    private static AnimatorSet secondAnimationSet = null;

    public interface FacebookDialogCallback {
        void onConnectClick();
    }

    public static AnimatorSet getFirstAnimation() {
        if (firstAnimationSet == null) {
            firstAnimationSet = new AnimatorSet();
        }
        return firstAnimationSet;
    }

    public static AnimatorSet getSecondAnimation() {
        if (secondAnimationSet == null) {
            secondAnimationSet = new AnimatorSet();
        }
        return secondAnimationSet;
    }

	public static void hideProgressDialog(final FragmentActivity activity) {
		if (activity.isFinishing()) return;
    	Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(DialogUtils.class.getName()+":progress");
    	if (fragment != null && fragment instanceof ProgressDialogFragment) {
            ((ProgressDialogFragment)fragment).dismiss();
    	}
//        Utils.index = 0;
        getFirstAnimation().end();
        getSecondAnimation().end();
    }

    public static void showProgressDialog(final FragmentActivity activity) {
        showProgressDialog(activity, activity.getString(R.string.loading));
    }

    public static void showProgressDialog(final FragmentActivity activity, final String message) {
		if (activity.isFinishing()) return;
        Fragment fragment =
                activity.getSupportFragmentManager().findFragmentByTag(DialogUtils.class.getName() + ":progress");
        if(fragment == null) {
            ProgressDialogFragment
                    .instantiate(message, false, null)
                    .show(activity.getSupportFragmentManager(), DialogUtils.class.getName() + ":progress");
        }
    }

    public static void showProgressDialog(final FragmentActivity activity, final String message,
                                          final RetainedDialogFragment.OnCancelListener onCancelListener) {
		if (activity.isFinishing()) return;
		ProgressDialogFragment
		.instantiate(message, true, onCancelListener)
		.show(activity.getSupportFragmentManager(), DialogUtils.class.getName() + ":progress");
    }

    public static void showGeneralErrorAlert(final FragmentActivity activity) {
		if (activity.isFinishing()) return;
		AlertDialogFragment
		.instantiate(activity.getString(R.string.general_error_title), activity.getString(R.string.general_error_message), AlertDialogFragment.Type.ERROR, null)
		.show(activity.getSupportFragmentManager(), DialogUtils.class.getName() + ":alert");
    }

    public static void showGeneralErrorAlert(final FragmentActivity activity, String message) {
        if (activity.isFinishing()) return;
        AlertDialogFragment
                .instantiate(activity.getString(R.string.general_error_title), message, AlertDialogFragment.Type.ERROR, null)
                .show(activity.getSupportFragmentManager(), DialogUtils.class.getName() + ":alert");
    }

    public static void showGeneralAlert(final FragmentActivity activity, String title, String message) {
        if (activity.isFinishing()) return;
        AlertDialogFragment
                .instantiate(title, message, AlertDialogFragment.Type.WARNING, null)
                .show(activity.getSupportFragmentManager(), DialogUtils.class.getName() + ":alert");
    }

    public static void showGeneralAlert(final FragmentActivity activity, final String title, final String message,
                                        final RetainedDialogFragment.OnDismissListener onDismissListener) {
        if (activity.isFinishing()) return;
        AlertDialogFragment
                .instantiate(title, message, AlertDialogFragment.Type.WARNING, onDismissListener)
                .show(activity.getSupportFragmentManager(), DialogUtils.class.getName()+":alert");
    }

    public static void showValidateFormatErrorAlert(final FragmentActivity activity) {
		if (activity == null || (activity.isFinishing())) return;
		AlertDialogFragment
		.instantiate(activity.getString(R.string.format_error_title), activity.getString(R.string.format_error_message), AlertDialogFragment.Type.ERROR, null)
		.show(activity.getSupportFragmentManager(), DialogUtils.class.getName() + ":alert");
    }

    public static void showUnexpectedErrorAlert(final FragmentActivity activity) {
        if (activity.isFinishing()) return;
        AlertDialogFragment
                .instantiate(activity.getString(R.string.general_error_title), activity.getString(R.string.unexpected_error), AlertDialogFragment.Type.ERROR, null)
                .show(activity.getSupportFragmentManager(), DialogUtils.class.getName() + ":alert");
    }

    public static void showConfirmDialog(final FragmentActivity activity, String title, String message,
                                         String lblOk, String lblCancel,
                                         ConfirmDialogFragment.OnConfirmListener confirmListener,
                                         RetainedDialogFragment.OnCancelListener cancelListener) {
        if (activity.isFinishing()) return;
        ConfirmDialogFragment
                .instantiate(title, message, lblOk, lblCancel, confirmListener, cancelListener)
                .show(activity.getSupportFragmentManager(), DialogUtils.class.getName() + ":confirm");
    }
}