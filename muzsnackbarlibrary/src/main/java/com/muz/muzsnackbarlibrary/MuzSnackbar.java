package com.muz.muzsnackbarlibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;


/**
 * Created by Administrator on 2018/11/15.
 */

public final class MuzSnackbar extends MuzBaseTransientBottomBar<MuzSnackbar> {
    /**
     * Callback class for {@link MuzSnackbar} instances.
     * <p>
     * Note: this class is here to provide backwards-compatible way for apps written before
     * the existence of the base {@link MuzBaseTransientBottomBar} class.
     *
     * @see MuzBaseTransientBottomBar#addCallback(BaseCallback)
     */
    public abstract static class Callback extends BaseCallback<MuzSnackbar> {


//        @Override
//        public void onShown(MuzSnackbar sb) {
//            // Stub implementation to make API check happy.
//        }
//
//        @Override
//        public void onDismissed(MuzSnackbar transientBottomBar, @DismissEvent int event) {
//            // Stub implementation to make API check happy.
//        }
    }

    @Nullable
    private BaseCallback<MuzSnackbar> mCallback;

    /**
     * Constructor for the transient bottom bar.
     *
     * @param parent              The parent for this transient bottom bar.
     * @param content             The content view for this transient bottom bar.
     * @param contentViewCallback The content view callback for this transient bottom bar.
     */
    private MuzSnackbar(@NonNull ViewGroup parent, @NonNull View content, @NonNull ContentViewCallback contentViewCallback) {
        super(parent, content, contentViewCallback);
    }

    /**
     * Make a MuzSnackbar to display a message
     * <p>
     * <p>MuzSnackbar will try and find a parent view to hold MuzSnackbar's view from the value given
     * to {@code view}. MuzSnackbar will walk up the view tree trying to find a suitable parent,
     * which is defined as a {@link CoordinatorLayout} or the window decor's content view,
     * whichever comes first.
     * <p>
     * <p>Having a {@link CoordinatorLayout} in your view hierarchy allows MuzSnackbar to enable
     * certain features, such as swipe-to-dismiss and automatically moving of widgets like
     * {@link FloatingActionButton}.
     *
     * @param view     The view to find a parent from.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or {@link
     *                 #LENGTH_LONG}
     */
    @NonNull
    public static MuzSnackbar make(@NonNull View view, @NonNull CharSequence text,
                                   @Duration int duration) {
        final ViewGroup parent = findSuitableParent(view);
        if (parent == null) {
            throw new IllegalArgumentException("No suitable parent found from the given view. "
                    + "Please provide a valid view.");
        }

        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final MuzSnackbarContentLayout content =
                (MuzSnackbarContentLayout) inflater.inflate(
                        R.layout.layout_muz_snackbar_include, parent, false);
        final MuzSnackbar snackbar = new MuzSnackbar(parent, content, content);
        snackbar.setText(text);
        snackbar.setDuration(duration);
        return snackbar;
    }

    /**
     * Make a MuzSnackbar to display a message.
     * <p>
     * <p>MuzSnackbar will try and find a parent view to hold MuzSnackbar's view from the value given
     * to {@code view}. MuzSnackbar will walk up the view tree trying to find a suitable parent,
     * which is defined as a {@link CoordinatorLayout} or the window decor's content view,
     * whichever comes first.
     * <p>
     * <p>Having a {@link CoordinatorLayout} in your view hierarchy allows MuzSnackbar to enable
     * certain features, such as swipe-to-dismiss and automatically moving of widgets like
     * {@link FloatingActionButton}.
     *
     * @param view     The view to find a parent from.
     * @param resId    The resource id of the string resource to use. Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or {@link
     *                 #LENGTH_LONG}
     */
    @NonNull
    public static MuzSnackbar make(@NonNull View view, @StringRes int resId, @Duration int duration) {
        return make(view, view.getResources().getText(resId), duration);
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;
        do {
            if (view instanceof CoordinatorLayout) {
                // We've found a CoordinatorLayout, use it
                return (ViewGroup) view;
            } else if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    // If we've hit the decor content view, then we didn't find a CoL in the
                    // hierarchy, so use it.
                    return (ViewGroup) view;
                } else {
                    // It's not the content view but we'll use it as our fallback
                    fallback = (ViewGroup) view;
                }
            }

            if (view != null) {
                // Else, we will loop and crawl up the view hierarchy and try to find a parent
                final ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);

        // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
        return fallback;
    }

    /**
     * Update the text in this {@link MuzSnackbar}.
     *
     * @param message The new text for this {@link MuzBaseTransientBottomBar}.
     */
    @NonNull
    public MuzSnackbar setText(@NonNull CharSequence message) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        tv.setText(message);
        return this;
    }

    /**
     * Update the text in this {@link MuzSnackbar}.
     *
     * @param resId The new text for this {@link MuzBaseTransientBottomBar}.
     */
    @NonNull
    public MuzSnackbar setText(@StringRes int resId) {
        return setText(getContext().getText(resId));
    }

    /**
     * Sets the text color of the text specified in
     * {@link #setText(CharSequence)}.
     */
    @NonNull
    public MuzSnackbar setTextColor(ColorStateList colors) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        tv.setTextColor(colors);
        return this;
    }

    /**
     * Sets the text color of the text specified in
     * {@link #setText(CharSequence)}.
     */
    @NonNull
    public MuzSnackbar setTextColor(@ColorInt int color) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        tv.setTextColor(color);
        return this;
    }

    /**
     * Sets the text MaxLines of the text specified in
     * I don't recommend too many lines, my ideal maximum number of lines is 3 lines.
     *
     * @param maxLines Within the range of 1 to 3
     *                 {@link #setText(CharSequence)}.
     */
    @NonNull
    public MuzSnackbar setMaxLines(@IntRange(from = 1, to = 3) int maxLines) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        tv.setMaxLines(maxLines);
        return this;
    }

    /**
     * Sets the text icon of the text specified in
     * {@link #setText(CharSequence)}.
     */
    public MuzSnackbar setIcon(@NonNull Drawable icon) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        tv.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        tv.setCompoundDrawablePadding(getView().getResources().getDimensionPixelSize(R.dimen.muz_snackbar_icon_padding_end));
        return this;
    }

    /**
     * Sets the text icon of the text specified in
     * {@link #setText(CharSequence)}.
     */
    public MuzSnackbar setIcon(@DrawableRes int icon) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        tv.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        tv.setCompoundDrawablePadding(getView().getResources().getDimensionPixelSize(R.dimen.muz_snackbar_icon_padding_end));
        return this;
    }

    /**
     * Sets the text icon padding of the text specified in
     * dimes
     */
    public MuzSnackbar setIconPaddingDime(@DimenRes int dimes) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        tv.setCompoundDrawablePadding(getView().getResources().getDimensionPixelSize(dimes));
        return this;
    }

    /**
     * Sets the text icon padding of the text specified in
     */
    public MuzSnackbar setIconPadding(int padding) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        tv.setCompoundDrawablePadding(padding);
        return this;
    }

    /**
     * Sets the text size of the text specified in
     * Please use it reasonably
     * {@link #setText(CharSequence)}.
     */
    public MuzSnackbar setTextSize(@DimenRes int textSize) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        tv.setTextSize(getView().getResources().getDimension(textSize));
        return this;
    }

    /**
     * Sets the text typeface of the text specified in
     */
    public MuzSnackbar setTextTypeface(@NonNull Typeface typeFace) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        tv.setTypeface(typeFace);
        return this;
    }

    /**
     * Sets the text typeface of the text specified in
     *
     * @param ttf ttf.name
     *            Assets path
     */
    public MuzSnackbar setTextTypeface(@NonNull String ttf) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), ttf);
        tv.setTypeface(typeface);
        return this;
    }

    /**
     * Sets the text typeface of the text specified in
     *
     * @param path ttf.name
     *             File path ;String format
     */
    public MuzSnackbar setTextTypefaceFile(@NonNull String path) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        Typeface typeface = Typeface.createFromFile(path);
        tv.setTypeface(typeface);
        return this;
    }

    /**
     * Sets the text typeface of the text specified in
     *
     * @param path ttf.name
     *             File path;File format
     */
    public MuzSnackbar setTextTypefaceFile(@NonNull File path) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        Typeface typeface = Typeface.createFromFile(path);
        tv.setTypeface(typeface);
        return this;
    }

    /**
     * Sets the text typeface of the action specified in
     */
    public MuzSnackbar setActionTextTypeface(@NonNull Typeface typeFace) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getActionView();
        tv.setTypeface(typeFace);
        return this;
    }

    /**
     * Sets the text typeface of the action specified in
     *
     * @param ttf ttf.name
     *            Assets path
     */
    public MuzSnackbar setActionTextTypeface(@NonNull String ttf) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getActionView();
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), ttf);
        tv.setTypeface(typeface);
        return this;
    }

    /**
     * Sets the text typeface of the action specified in
     *
     * @param path ttf.name
     *             File path ;String format
     */
    public MuzSnackbar setActionTextTypefaceFile(@NonNull String path) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getActionView();
        Typeface typeface = Typeface.createFromFile(path);
        tv.setTypeface(typeface);
        return this;
    }

    /**
     * Sets the text typeface of the action specified in
     *
     * @param path ttf.name
     *             File path;File format
     */
    public MuzSnackbar setActionTextTypefaceFile(@NonNull File path) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getActionView();
        Typeface typeface = Typeface.createFromFile(path);
        tv.setTypeface(typeface);
        return this;
    }

    /**
     * Sets the text size of the action specified in
     * Please use it reasonably
     * {@link #setAction(CharSequence, View.OnClickListener)}
     */
    public MuzSnackbar setActionTextSize(@DimenRes int textSize) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getActionView();
        tv.setTextSize(getView().getResources().getDimension(textSize));
        return this;
    }

    /**
     * Restore snackbar txt and button initial gravity
     * Deprecated
     */
//    public MuzSnackbar setTxtAndBtnInitialGravity(){
//        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
//        final TextView tv = contentLayout.getMessageView();
//        tv.setGravity(0);
//        final TextView actionView = contentLayout.getActionView();
//        actionView.setGravity(0);
//        return this;
//    }

    /**
     * Set the action to be displayed in this {@link MuzBaseTransientBottomBar}.
     *
     * @param resId    String resource to display for the action
     * @param listener callback to be invoked when the action is clicked
     */
    @NonNull
    public MuzSnackbar setAction(@StringRes int resId, View.OnClickListener listener) {
        return setAction(getContext().getText(resId), listener);
    }

    /**
     * Set the action to be displayed in this {@link MuzBaseTransientBottomBar}.
     *
     * @param text     Text to display for the action
     * @param listener callback to be invoked when the action is clicked
     */
    @NonNull
    public MuzSnackbar setAction(CharSequence text, final View.OnClickListener listener) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getActionView();

        if (TextUtils.isEmpty(text) || listener == null) {
            tv.setVisibility(View.GONE);
            tv.setOnClickListener(null);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);
                    // Now dismiss the MuzSnackbar
                    dispatchDismiss(BaseCallback.DISMISS_EVENT_ACTION);
                }
            });
        }
        return this;
    }

    /**
     * Sets the text color of the action specified in
     * {@link #setAction(CharSequence, View.OnClickListener)}.
     */
    @NonNull
    public MuzSnackbar setActionTextColor(ColorStateList colors) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getActionView();
        tv.setTextColor(colors);
        return this;
    }

    /**
     * Sets the text color of the action specified in
     * {@link #setAction(CharSequence, View.OnClickListener)}.
     */
    @NonNull
    public MuzSnackbar setActionTextColor(@ColorInt int color) {
        final MuzSnackbarContentLayout contentLayout = (MuzSnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getActionView();
        tv.setTextColor(color);
        return this;
    }

    /**
     * Set a callback to be called when this the visibility of this {@link MuzSnackbar}
     * changes. Note that this method is deprecated
     * and you should use {@link #addCallback(BaseCallback)} to add a callback and
     * {@link #removeCallback(BaseCallback)} to remove a registered callback.
     *
     * @param callback Callback to notify when transient bottom bar events occur.
     * @see Callback
     * @see #addCallback(BaseCallback)
     * @see #removeCallback(BaseCallback)
     * @deprecated Use {@link #addCallback(BaseCallback)}
     */
    @Deprecated
    @NonNull
    public MuzSnackbar setCallback(Callback callback) {
        // The logic in this method emulates what we had before support for multiple
        // registered callbacks.
        if (mCallback != null) {
            removeCallback(mCallback);
        }
        if (callback != null) {
            addCallback(callback);
        }
        // Update the deprecated field so that we can remove the passed callback the next
        // time we're called
        mCallback = callback;
        return this;
    }

    /**
     * @hide Note: this class is here to provide backwards-compatible way for apps written before
     * the existence of the base {@link MuzBaseTransientBottomBar} class.
     */
    @RestrictTo(LIBRARY_GROUP)
    public static final class SnackbarLayout extends MuzBaseTransientBottomBar.SnackbarBaseLayout {
        public SnackbarLayout(Context context) {
            super(context);
        }

        public SnackbarLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            // Work around our backwards-compatible refactoring of MuzSnackbar and inner content
            // being inflated against MuzSnackbar's parent (instead of against the MuzSnackbar itself).
            // Every child that is width=MATCH_PARENT is remeasured again and given the full width
            // minus the paddings.
            int childCount = getChildCount();
            int availableWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child.getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT) {
                    child.measure(MeasureSpec.makeMeasureSpec(availableWidth, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(),
                                    MeasureSpec.EXACTLY));
                }
            }
        }
    }

}
