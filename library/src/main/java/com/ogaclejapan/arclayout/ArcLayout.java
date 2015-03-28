/**
 * Copyright (C) 2015 ogaclejapan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ogaclejapan.arclayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.WeakHashMap;

public class ArcLayout extends ViewGroup {

    private static final String TAG = "ArcLayout";

    private static final float DEFAULT_CHILD_ANGLE = 0f;

    private static final int DEFAULT_CHILD_ORIGIN = ArcOrigin.CENTER;

    private static final int DEFAULT_ORIGIN = ArcOrigin.CENTER;

    private static final int DEFAULT_COLOR = Color.TRANSPARENT;

    private static final int DEFAULT_RADIUS = 144;

    private static final int DEFAULT_AXIS_RADIUS = -1; //default: radius / 2

    private static final boolean DEFAULT_FREE_ANGLE = false;

    private static final boolean DEFAULT_REVERSE_ANGLE = false;

    private final WeakHashMap<View, Float> mChildAngleHolder = new WeakHashMap<>();

    private Arc mArc = Arc.CENTER;

    private ArcDrawable mArcDrawable;

    private int mAxisRadius;

    private Point mSize = new Point();

    private boolean mIsFreeAngle = DEFAULT_FREE_ANGLE;

    private boolean mIsReverseAngle = DEFAULT_REVERSE_ANGLE;

    public ArcLayout(Context context) {
        this(context, null);
    }

    public ArcLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcLayout(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ArcLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressWarnings("NewApi")
    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setWillNotDraw(false);

        if (isInEditMode()) {
            return;
        }

        final TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.arc_ArcLayout, defStyleAttr, defStyleRes);
        int origin = a.getInt(R.styleable.arc_ArcLayout_arc_origin, DEFAULT_ORIGIN);
        int color = a.getColor(R.styleable.arc_ArcLayout_arc_color, DEFAULT_COLOR);
        int radius = a.getDimensionPixelSize(R.styleable.arc_ArcLayout_arc_radius, DEFAULT_RADIUS);
        int axisRadius = a.getDimensionPixelSize(R.styleable.arc_ArcLayout_arc_axisRadius, DEFAULT_AXIS_RADIUS);
        boolean isFreeAngle = a.getBoolean(R.styleable.arc_ArcLayout_arc_freeAngle, DEFAULT_FREE_ANGLE);
        boolean isReverseAngle = a.getBoolean(R.styleable.arc_ArcLayout_arc_reverseAngle, DEFAULT_REVERSE_ANGLE);

        a.recycle();

        if (Utils.JELLY_BEAN_MR1_OR_LATER) {
            origin = ArcOrigin.getAbsoluteOrigin(origin, getLayoutDirection());
        }

        mArc = Arc.of(origin);
        mArcDrawable = new ArcDrawable(mArc, radius, color);
        mAxisRadius = axisRadius;
        mIsFreeAngle = isFreeAngle;
        mIsReverseAngle = isReverseAngle;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (Utils.DEBUG) {
            Utils.d(TAG, "onMeasure: w=%s, h=%s",
                    MeasureSpec.toString(widthMeasureSpec),
                    MeasureSpec.toString(heightMeasureSpec));
        }

        mSize.x = Utils.computeMeasureSize(widthMeasureSpec, mArcDrawable.getIntrinsicWidth());
        mSize.y = Utils.computeMeasureSize(heightMeasureSpec, mArcDrawable.getIntrinsicHeight());

        setMeasuredDimension(mSize.x, mSize.y);

        if (Utils.DEBUG) {
            Utils.d(TAG, "setMeasuredDimension: w=%d, h=%d", mSize.x, mSize.y);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (Utils.DEBUG) {
            Utils.d(TAG, "onLayout: l=%d, t=%d, r=%d, b=%d", l, t, r, b);
        }
        mArcDrawable.setBounds(0, 0, r - l, b - t);

        final Point o = mArc.computeOrigin(0, 0, mSize.x, mSize.y);
        final int radius = (mAxisRadius == DEFAULT_AXIS_RADIUS)
                ? mArcDrawable.getRadius() / 2
                : mAxisRadius;
        final float perDegrees = mArc.computePerDegrees(getChildCountWithoutGone());

        int arcIndex = 0;

        for (int i = 0, size = getChildCount(); i < size; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            float childAngle;
            if (mIsFreeAngle) {
                childAngle = mArc.startAngle + lp.angle;
            } else if (mIsReverseAngle) {
                childAngle = mArc.computeReverseDegrees(arcIndex++, perDegrees);
            } else {
                childAngle = mArc.computeDegrees(arcIndex++, perDegrees);
            }

            final int x = o.x + Arc.x(radius, childAngle);
            final int y = o.y + Arc.y(radius, childAngle);

            childMeasureBy(child, x, y);
            childLayoutBy(child, x, y);

            mChildAngleHolder.put(child, childAngle);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mArcDrawable.draw(canvas);
    }

    public int getArcColor() {
        return mArcDrawable.getColor();
    }

    public void setArcColor(int color) {
        mArcDrawable.setColor(color);
        invalidate();
    }

    public Arc getArc() {
        return mArc;
    }

    public void setArc(Arc arc) {
        mArc = arc;
        mArcDrawable.setArc(arc);
        requestLayout();
    }

    public int getRadius() {
        return mArcDrawable.getRadius();
    }

    public void setRadius(int radius) {
        mArcDrawable.setRadius(radius);
        requestLayout();
    }

    public int getAxisRadius() {
        return mAxisRadius;
    }

    public void setAxisRadius(int radius) {
        mAxisRadius = radius;
        requestLayout();
    }

    public boolean isFreeAngle() {
        return mIsFreeAngle;
    }

    public void setFreeAngle(boolean b) {
        mIsFreeAngle = b;
        requestLayout();
    }

    public boolean isReverseAngle() {
        return mIsReverseAngle;
    }

    public void setReverseAngle(boolean b) {
        mIsReverseAngle = b;
        requestLayout();
    }

    public Point getOrigin() {
        return mArc.computeOrigin(getLeft(), getTop(), getRight(), getBottom());
    }

    public float getChildAngleAt(int index) {
        return getChildAngleAt(getChildAt(index));
    }

    public float getChildAngleAt(View v) {
        return (mChildAngleHolder.containsKey(v)) ? mChildAngleHolder.get(v) : 0f;
    }

    public int getChildCountWithoutGone() {
        int childCount = 0;
        for (int i = 0, len = getChildCount(); i < len; i++) {
            if (getChildAt(i).getVisibility() != View.GONE) {
                childCount++;
            }
        }
        return childCount;
    }

    @SuppressWarnings("NewApi")
    protected void childMeasureBy(View child, int x, int y) {
        if (Utils.DEBUG) {
            Utils.d(TAG, "childMeasureBy: x=%d, y=%d", x, y);
        }

        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int origin = lp.origin;
        if (Utils.JELLY_BEAN_MR1_OR_LATER) {
            origin = ArcOrigin.getAbsoluteOrigin(origin, getLayoutDirection());
        }

        int widthSize;
        int widthMode;

        switch (lp.width) {
            case LayoutParams.MATCH_PARENT:
                widthSize = Utils.computeWidth(origin, mSize.x, x);
                widthMode = MeasureSpec.EXACTLY;
                break;
            case LayoutParams.WRAP_CONTENT:
                widthSize = Utils.computeWidth(origin, mSize.x, x);
                widthMode = MeasureSpec.AT_MOST;
                break;
            default:
                widthSize = lp.width;
                widthMode = MeasureSpec.EXACTLY;
        }

        int heightSize;
        int heightMode;

        switch (lp.height) {
            case LayoutParams.MATCH_PARENT:
                heightSize = Utils.computeHeight(origin, mSize.y, y);
                heightMode = MeasureSpec.EXACTLY;
                break;
            case LayoutParams.WRAP_CONTENT:
                heightSize = Utils.computeHeight(origin, mSize.y, y);
                heightMode = MeasureSpec.AT_MOST;
                break;
            default:
                heightSize = lp.height;
                heightMode = MeasureSpec.EXACTLY;
        }

        child.measure(
                MeasureSpec.makeMeasureSpec(widthSize, widthMode),
                MeasureSpec.makeMeasureSpec(heightSize, heightMode)
        );

    }

    @SuppressWarnings("NewApi")
    protected void childLayoutBy(View child, int x, int y) {
        if (Utils.DEBUG) {
            Utils.d(TAG, "childLayoutBy: x=%d, y=%d", x, y);
        }

        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int origin = lp.origin;
        if (Utils.JELLY_BEAN_MR1_OR_LATER) {
            origin = ArcOrigin.getAbsoluteOrigin(origin, getLayoutDirection());
        }

        final int width = child.getMeasuredWidth();
        final int height = child.getMeasuredHeight();

        int left;
        switch (origin & ArcOrigin.HORIZONTAL_MASK) {
            case ArcOrigin.LEFT:
                left = x;
                break;
            case ArcOrigin.RIGHT:
                left = x - width;
                break;
            default:
                left = x - (width / 2);
        }

        int top;
        switch (origin & ArcOrigin.VERTICAL_MASK) {
            case ArcOrigin.TOP:
                top = y;
                break;
            case ArcOrigin.BOTTOM:
                top = y - height;
                break;
            default:
                top = y - (height / 2);
        }

        child.layout(left, top, left + width, top + height);

        if (Utils.DEBUG) {
            Utils.d(TAG, "l=%d, t=%d, r=%d, b=%d", left, top, left + width, top + height);
        }

    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public static class LayoutParams extends MarginLayoutParams {

        public int origin = DEFAULT_CHILD_ORIGIN;
        public float angle = DEFAULT_CHILD_ANGLE;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.arc_ArcLayout_Layout, 0, 0);
            origin = a.getInt(R.styleable.arc_ArcLayout_Layout_arc_origin, DEFAULT_CHILD_ORIGIN);
            angle = a.getFloat(R.styleable.arc_ArcLayout_Layout_arc_angle, DEFAULT_CHILD_ANGLE);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

    }
}
