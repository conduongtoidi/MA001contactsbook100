package com.whatsoft.contactbook.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.whatsoft.contactbook.model.Contact;

/**
 * Created by mb on 3/5/16
 */
public class Utils {
    public static void hideSoftKeyboard(Context context) {
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            view.clearFocus();
        }
    }

    public static void setUpHideSoftKeyboard(final Activity activity, final View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) { //If a layout container, iterate over children and seed recursion.
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setUpHideSoftKeyboard(activity, innerView);
            }
        }
    }

    public static String formatAddress(Contact contact) {
        try {
            int index = contact.getAddress().toLowerCase().indexOf("đường");
            String addr = contact.getAddress().toLowerCase().substring(index);
            // address number
//            String no = contact.getAddressNo().toLowerCase().replace("trọ", "");
//            no = no.replace("jan", "");
//            no = no.replace("feb", "");
//            no = no.replace("mar", "");
//            no = no.replace("apr", "");
//            no = no.replace("may", "");
//            no = no.replace("jun", "");
//            no = no.replace("jul", "");
//            no = no.replace("aug", "");
//            no = no.replace("sep", "");
//            no = no.replace("oct", "");
//            no = no.replace("nov", "");
//            no = no.replace("dec", "");
//            no = no.replace("-", "");
//
//            char[] cs = no.toCharArray();
//            no = "";
//            boolean isBreak = false;
//            for (char c : cs) {
//                if (c == '/' || (c >= '0' && c <= '9') || c == ' ') {
//                    no += c;
//                    if (!isBreak) {
//                        isBreak = true;
//                    }
//                } else if (isBreak) {
//                    break;
//                }
//            }
//            String[] nos = no.split("/");
//            if (nos.length > 0) {
//                no = nos[0];
//            }
            //=========

            String[] split = addr.split(",");
            addr = "";
            String subRoad = "";
            boolean isFirstSub = true;
            for (String s : split) {
                if (TextUtils.isEmpty(s)) {
                    continue;
                }
                if (!s.toLowerCase().contains("hẻm")) {
                    if (!TextUtils.isEmpty(s)) {
                        s += ",";
                    }
                    addr += s;
                } else if (isFirstSub) {
                    subRoad = s;
                    isFirstSub = false;
                }
            }

            subRoad = subRoad.toLowerCase().replace("hẻm", "").trim();
            addr = addr.toLowerCase().replace("phường", "").trim();
            String address = (subRoad + " " + addr.trim() + " thủ đức, hồ chí minh");
            Log.d(address);
            return address;
        } catch (Exception e) {
            Log.e(e);
        }
        return contact.getAddress();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
