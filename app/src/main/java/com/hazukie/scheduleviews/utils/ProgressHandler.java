package com.hazukie.scheduleviews.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;

public class ProgressHandler extends Handler{
    //进度调处理工具
    public int mDstProgressIndex;
    public int mDuration;
    private ObjectAnimator mAnimator;
    private final static int PRORESS_PROGRESS = 0;
    private final static int PROGRESS_GONE = 1;
    public boolean mIsPageFinished = false;
    private ProgressBar mProgressBar;

    public ProgressHandler(ProgressBar mProgressBar) {
        this.mProgressBar = mProgressBar;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case PRORESS_PROGRESS:
                mIsPageFinished = false;
                mDstProgressIndex = msg.arg1;
                mDuration = msg.arg2;
                mProgressBar.setVisibility(View.VISIBLE);
                if (mAnimator != null && mAnimator.isRunning()) {
                        mAnimator.cancel();
                }
                mAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", mDstProgressIndex);
                mAnimator.setDuration(mDuration);
                mAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (mProgressBar.getProgress() == 100) {
                            sendEmptyMessageDelayed(PROGRESS_GONE, 500);
                        }
                    }
                });
                mAnimator.start();
                break;
            case PROGRESS_GONE:
                mDstProgressIndex = 0;
                mDuration = 0;
                mProgressBar.setProgress(0);
                mProgressBar.setVisibility(View.GONE);
                if (mAnimator != null && mAnimator.isRunning()) {
                    mAnimator.cancel();
                }
                mAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", 0);
                mAnimator.setDuration(0);
                mAnimator.removeAllListeners();
                mIsPageFinished = true;
                break;
            default:
                break;
            }
        }
    }
