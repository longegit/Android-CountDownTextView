package net.itgoo.countdowntextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by busyboy on 2015/9/25.
 */
public class CountDownTextView extends TextView {
    private static final long MAX_COUNTDOWN_TIME = 1000 * 60 * 30; // 30 minutes
    private CountDownTimer mTimer = null;
    private String mCss;
    private long mCountDownTime;
    private long mSecond;
    private long mMinute;
    private OnCountDownListener mOnCountDownListener;
    private String mDefaultStr;

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCountDownTime(context, attrs);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initCountDownTime(context, attrs);
    }

    public CountDownTextView(Context context) {
        super(context);
    }

    private void initCountDownTime(Context context, AttributeSet attrs) {
        TypedArray attribute = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        mCountDownTime = (long) attribute.getFloat(R.styleable.CountDownView_count_down_time, 0);
        mCss = attribute.getString(R.styleable.CountDownView_count_down_format);
        if (TextUtils.isEmpty(mCss)) {
            mCss = getContext().getString(R.string.count_down_default_format);
        }
    }

    public void setCountDownTimes(long countDownTime, String cssResId) {
        if (!TextUtils.isEmpty(cssResId)) {
            this.mCss = cssResId;
        }
        mCountDownTime = countDownTime;
    }

    public void setCountDownTimes(long countDownTime) {
        mCountDownTime = countDownTime;
    }

    public void start() {
        mDefaultStr = getText().toString();
        setEnabled(false);

        if (mCountDownTime < 0) {
            mCountDownTime = 0;
        } else {
            if (mCountDownTime > MAX_COUNTDOWN_TIME) {
                mCountDownTime = MAX_COUNTDOWN_TIME;
            }
        }
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            int countDownInterval = 1000;
            mTimer = new CountDownTimer(mCountDownTime, countDownInterval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mMinute = millisUntilFinished / (1000 * 60);
                    mSecond = (millisUntilFinished % (1000 * 60)) / 1000;

                    boolean custom = false;
                    if (mOnCountDownListener != null) {
                        custom = mOnCountDownListener.onCountDownCustomFormat(mMinute, mSecond);
                    }

                    if (!custom) {
                        CountDownTextView.this.setText(Html.fromHtml(String.format(mCss, mMinute, mSecond)));
                    }
                }

                @Override
                public void onFinish() {
                    setText(mDefaultStr);
                    setEnabled(true);
                    if (mOnCountDownListener != null) {
                        mOnCountDownListener.onCountDownFinish();
                    }
                }
            };
        }
        mTimer.start();
    }

    public void stop() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    public void setOnCountDownFinishListener(OnCountDownListener onCountDownListener) {
        this.mOnCountDownListener = onCountDownListener;
    }

    public interface OnCountDownListener {
        boolean onCountDownCustomFormat(long minute, long second);
        void onCountDownFinish();
    }
}
