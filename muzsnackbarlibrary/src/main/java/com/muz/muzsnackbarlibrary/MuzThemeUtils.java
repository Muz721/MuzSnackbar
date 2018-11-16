package com.muz.muzsnackbarlibrary;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * Created by Administrator on 2018/11/15.
 */

 class MuzThemeUtils {

        private static final int[] APPCOMPAT_CHECK_ATTRS = {
                android.support.v7.appcompat.R.attr.colorPrimary
        };

        static void checkAppCompatTheme(Context context) {
            TypedArray a = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS);
            final boolean failed = !a.hasValue(0);
            a.recycle();
            if (failed) {
                throw new IllegalArgumentException("You need to use a Theme.AppCompat theme "
                        + "(or descendant) with the design library.");
            }
        }
}
