package com.example.mukul.rekhta;

import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * Created by TBX on 7/8/2017.
 */

public class IScaleGestureDetector implements ScaleGestureDetector.OnScaleGestureListener, SupportScaleGestureDetector.OnSupportScaleGestureListener {

    private static final String TAG = IScaleGestureDetector.class.getSimpleName();

////=========================================================================================
//// Member variables.
////=========================================================================================

    private ScaleGestureDetector mScaleGestureDetector;
    private SupportScaleGestureDetector mSupportScaleGestureDetector;
    private IOnScaleGestureListener mListener;

    private boolean mUseSupport;

////=========================================================================================
//// Abstractions
////=========================================================================================

    /**
     * @param context
     * @param listener
     */
    public IScaleGestureDetector(Context context, IOnScaleGestureListener listener) {
        mListener = listener;
        mUseSupport = Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN;

        if (useSupport()) {
            mSupportScaleGestureDetector = new SupportScaleGestureDetector(context, this);
        } else {
            mScaleGestureDetector = new ScaleGestureDetector(context, this);
        }
    }

    /**
     * Direct the MotionEvent to the correct ScaleGestureDetector.
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (useSupport()) {
            return mSupportScaleGestureDetector.onTouchEvent(event);
        } else {
            return mScaleGestureDetector.onTouchEvent(event);
        }
    }

    /**
     * @return The ScaleGestureDetector's scaleFactor.
     */
    public float getScaleFactor() {
        if (useSupport()) {
            return mSupportScaleGestureDetector.getScaleFactor();
        } else {
            return mScaleGestureDetector.getScaleFactor();
        }
    }

    /**
     * @return The Y coordinate of the pinch's focal point.
     */
    public float getFocusY() {
        if (useSupport()) {
            return mSupportScaleGestureDetector.getFocusY();
        } else {
            return mScaleGestureDetector.getFocusY();
        }
    }

    /**
     * @return True if a scale gesture is in Progress.
     */
    public boolean isInProgress() {
        if (useSupport()) {
            return mSupportScaleGestureDetector.isInProgress();
        } else {
            return mScaleGestureDetector.isInProgress();
        }
    }


////=========================================================================================
//// Versioning
////=========================================================================================

    /**
     * @return If the SupportScaleGestureDetector should be used.
     */
    private boolean useSupport() {
        return mUseSupport;
    }


////=========================================================================================
//// IOnScaleGestureListener
////=========================================================================================

    /**
     * Mocks ScaleGestureDector's OnScaleGestureListener
     * @author jmhend
     *
     */
    public static interface IOnScaleGestureListener  {


        public boolean onScale(IScaleGestureDetector detector);


        public boolean onScaleBegin(IScaleGestureDetector detector);


        public void onScaleEnd(IScaleGestureDetector detector);
    }


    /*
     * (non-Javadoc)
     * @see me.jmhend.PinchListView.SupportScaleGestureDetector.OnSupportScaleGestureListener#onScale(me.jmhend.PinchListView.SupportScaleGestureDetector)
     */
    @Override
    public boolean onScale(SupportScaleGestureDetector detector) {
        return mListener.onScale(this);
    }

    /*
     * (non-Javadoc)
     * @see me.jmhend.PinchListView.SupportScaleGestureDetector.OnSupportScaleGestureListener#onScaleBegin(me.jmhend.PinchListView.SupportScaleGestureDetector)
     */
    @Override
    public boolean onScaleBegin(SupportScaleGestureDetector detector) {
        return mListener.onScaleBegin(this);
    }

    /*
     * (non-Javadoc)
     * @see me.jmhend.PinchListView.SupportScaleGestureDetector.OnSupportScaleGestureListener#onScaleEnd(me.jmhend.PinchListView.SupportScaleGestureDetector)
     */
    @Override
    public void onScaleEnd(SupportScaleGestureDetector detector) {
        mListener.onScaleEnd(this);
    }

    /*
     * (non-Javadoc)
     * @see android.view.ScaleGestureDetector.OnScaleGestureListener#onScale(android.view.ScaleGestureDetector)
     */
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return mListener.onScale(this);
    }

    /*
     * (non-Javadoc)
     * @see android.view.ScaleGestureDetector.OnScaleGestureListener#onScaleBegin(android.view.ScaleGestureDetector)
     */
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return mListener.onScaleBegin(this);
    }

    /*
     * (non-Javadoc)
     * @see android.view.ScaleGestureDetector.OnScaleGestureListener#onScaleEnd(android.view.ScaleGestureDetector)
     */
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        mListener.onScaleEnd(this);
    }

}