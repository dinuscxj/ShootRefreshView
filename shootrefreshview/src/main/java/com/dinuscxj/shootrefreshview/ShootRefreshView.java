package com.dinuscxj.shootrefreshview;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class ShootRefreshView extends View implements IRefreshStatus {
    private static final int DEFAULT_STROKE_COLOR = Color.parseColor("#ffc6c6c6");
    private static final int DEFAULT_GRADIENT_START_COLOR = Color.parseColor("#ffababab");
    private static final int DEFAULT_GRADIENT_END_COLOR = Color.parseColor("#0dababab");

    private static final int DEGREE_60 = 60;
    private static final int DEGREE_360 = 360;
    private static final int START_ANGLE = -90;

    private static final int PRE_SHOOT_LINE_TOTAL_ROTATE_DURATION = 10000;
    private static final int SHOOT_LINE_ROTATE_DURATION = 5000;
    private static final int SHOOT_LINE_STRETCH_DURATION = 500;
    private static final int OUT_RING_ROTATE_DURATION = 500;

    private static final int TOTAL_DURATION = PRE_SHOOT_LINE_TOTAL_ROTATE_DURATION +
            SHOOT_LINE_ROTATE_DURATION + SHOOT_LINE_STRETCH_DURATION;

    private static final float PRE_SHOOT_LINE_TOTAL_ROTATE_END_OFFSET =
            (float) PRE_SHOOT_LINE_TOTAL_ROTATE_DURATION / (float) TOTAL_DURATION;
    private static final float SHOOT_LINE_ROTATE_END_OFFSET = PRE_SHOOT_LINE_TOTAL_ROTATE_END_OFFSET +
            (float) SHOOT_LINE_ROTATE_DURATION / (float) TOTAL_DURATION;
    private static final float SHOOT_LINE_STRETCH_END_OFFSET = 1.0f;

    private static final float SHOOT_LINE_ROTATE_END_RADIANS = (float) (Math.PI / 6.0);
    private static final float SHOOT_LINE_ROTATE_START_RADIANS = (float) (Math.PI / 2.5);
    private static final float SHOOT_LINE_ROTATE_START_DEGREE =
            (float) Math.toDegrees(SHOOT_LINE_ROTATE_END_RADIANS);
    private static final float INTERVAL_RADIANS = (float) (Math.PI / 3.0);
    private static final float SQRT_3 = (float) Math.sqrt(3.0);

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF mBounds = new RectF();

    private int mRadius;
    private int mCenterX;
    private int mCenterY;

    private int mStrokeColor;
    private int mGradientStartColor;
    private int mGradientEndColor;

    private int mStrokeWidth;

    private float mOutRingRotateAngle;

    private float mShootLineRotateRadians;
    private float mShootLineTotalRotateAngle;

    private Shader mRefreshingShader;

    private ValueAnimator mPreShootLineTotalRotateAnimator;
    private ValueAnimator mShootLineRotateAnimator;
    private ValueAnimator mShootLineStretchAnimator;
    private ValueAnimator mOutRingRotateAnimator;

    public static final Property<ShootRefreshView, Float> SHOOT_LINE_ROTATE_RADIANS =
            new Property<ShootRefreshView, Float>(Float.class, null) {
                @Override
                public Float get(ShootRefreshView object) {
                    return object.mShootLineRotateRadians;
                }

                @Override
                public void set(ShootRefreshView object, Float value) {
                    object.mShootLineRotateRadians = value;
                    object.invalidate();
                }
            };

    public static final Property<ShootRefreshView, Float> SHOOT_LINE_TOTAL_ROTATE_DEGREE =
            new Property<ShootRefreshView, Float>(Float.class, null) {
                @Override
                public Float get(ShootRefreshView object) {
                    return object.mShootLineTotalRotateAngle;
                }

                @Override
                public void set(ShootRefreshView object, Float value) {
                    object.mShootLineTotalRotateAngle = value;
                    object.invalidate();
                }
            };

    public ShootRefreshView(Context context) {
        this(context, null);
    }

    public ShootRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShootRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        resolveAttrs(context, attrs);
        initPaint();
        initAnimator();

        reset();
    }

    private void resolveAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShootRefreshView);
        mStrokeColor = ta.getColor(R.styleable.ShootRefreshView_strokeColor,
                DEFAULT_STROKE_COLOR);
        mGradientStartColor = ta.getColor(R.styleable.ShootRefreshView_gradientStartColor,
                DEFAULT_GRADIENT_START_COLOR);
        mGradientEndColor =
                ta.getColor(R.styleable.ShootRefreshView_gradientEndColor, DEFAULT_GRADIENT_END_COLOR);
        mStrokeWidth =
                ta.getDimensionPixelSize(R.styleable.ShootRefreshView_strokeWidth,
                        (int) DensityUtil.dp2px(getContext(), 1.0f));
        ta.recycle();

        mRefreshingShader = new SweepGradient(0, 0, new int[]{mGradientStartColor, mGradientEndColor},
                new float[]{0.0f, 1.0f});
    }

    private void initPaint() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mStrokeColor);
    }

    private void initAnimator() {
        //Note: the following uses the 'kwai Line' represent the six lines of the shutter

        //Step1: Rotate the 'kwai Line', but the shutter does not open
        mPreShootLineTotalRotateAnimator =
                ValueAnimator.ofFloat(-(SHOOT_LINE_ROTATE_START_DEGREE / 2.0f) - 240.0f,
                        -(SHOOT_LINE_ROTATE_START_DEGREE / 2.0f) - 120.0f);
        mPreShootLineTotalRotateAnimator.setInterpolator(new LinearInterpolator());
        mPreShootLineTotalRotateAnimator.setDuration(PRE_SHOOT_LINE_TOTAL_ROTATE_DURATION);
        mPreShootLineTotalRotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mShootLineTotalRotateAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        //Step 2: Rotate the 'Kwai Line' and open the shutter
        PropertyValuesHolder shootLineIntersectHolder = PropertyValuesHolder
                .ofFloat(SHOOT_LINE_ROTATE_RADIANS, SHOOT_LINE_ROTATE_START_RADIANS,
                        SHOOT_LINE_ROTATE_END_RADIANS);
        PropertyValuesHolder shootLineTotalRotateAnimatorHolder = PropertyValuesHolder
                .ofFloat(SHOOT_LINE_TOTAL_ROTATE_DEGREE, -(SHOOT_LINE_ROTATE_START_DEGREE / 2.0f) - 120.0f,
                        -(SHOOT_LINE_ROTATE_START_DEGREE / 2.0f));
        mShootLineRotateAnimator = ObjectAnimator.ofPropertyValuesHolder(this,
                shootLineIntersectHolder, shootLineTotalRotateAnimatorHolder);
        mShootLineRotateAnimator.setInterpolator(new LinearInterpolator());
        mShootLineRotateAnimator.setDuration(SHOOT_LINE_ROTATE_DURATION);

        //Step3: Take the center of the 'Kwai Line' as the base point, and zoom 'Kwai Line'
        mShootLineStretchAnimator = ValueAnimator.ofFloat(SHOOT_LINE_ROTATE_END_RADIANS, 0);
        mShootLineStretchAnimator.setInterpolator(new LinearInterpolator());
        mShootLineStretchAnimator.setDuration(SHOOT_LINE_STRETCH_DURATION);
        mShootLineStretchAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mShootLineRotateRadians = (Float) animation.getAnimatedValue();
                mShootLineTotalRotateAngle = -(float) (Math.toDegrees(mShootLineRotateRadians) / 2.0f);

                invalidate();
            }
        });

        //Step4: Perform a refresh animation, rotate the gradient ring
        mOutRingRotateAnimator = ValueAnimator.ofFloat(0, DEGREE_360);
        mOutRingRotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mOutRingRotateAnimator.setInterpolator(new LinearInterpolator());
        mOutRingRotateAnimator.setDuration(OUT_RING_ROTATE_DURATION);
        mOutRingRotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOutRingRotateAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOuterRing(canvas);
        drawShootLine(canvas);
    }

    private void drawOuterRing(Canvas canvas) {
        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        if (mOutRingRotateAnimator.isRunning()) {
            canvas.rotate(START_ANGLE + mOutRingRotateAngle);
            if (mPaint.getShader() != mRefreshingShader) {
                mPaint.setShader(mRefreshingShader);
            }
        } else {
            mPaint.setShader(null);
        }

        canvas.drawCircle(0.0f, 0.0f, mRadius, mPaint);
        canvas.restore();
    }

    private void drawShootLine(Canvas canvas) {
        if (mShootLineRotateRadians <= 0.0f || mOutRingRotateAnimator.isRunning()) {
            return;
        }

        mPaint.setShader(null);

        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        canvas.rotate(mShootLineTotalRotateAngle);

        for (int i = 0; i < 6; i++) {
            canvas.save();
            canvas.rotate(-DEGREE_60 * i);

            if (mShootLineRotateRadians > SHOOT_LINE_ROTATE_END_RADIANS) {
                double tanRotateAngle = Math.tan(mShootLineRotateRadians);
                double tanRotateAngleOffset60 = Math.tan(mShootLineRotateRadians + INTERVAL_RADIANS);

                //The intersection formula of 'Kwai Line'
                float stopX = (float) ((1.0 - SQRT_3 * tanRotateAngleOffset60)
                        / (2.0 * (tanRotateAngle - tanRotateAngleOffset60))) * mRadius;
                float stopY = (float) ((2.0 * tanRotateAngleOffset60 - tanRotateAngle
                        - SQRT_3 * tanRotateAngle * tanRotateAngleOffset60)
                        / (2.0 * (tanRotateAngle - tanRotateAngleOffset60))) * mRadius;
                //Note: (0, -Radius) is Y-axis negative direction
                canvas.drawLine(0, -mRadius, stopX, stopY, mPaint);
            } else {
                double tanRotateAngle = Math.tan(mShootLineRotateRadians);

                //The zoom formula of 'Kwai Line'
                float stopX =
                        (float) (2 * tanRotateAngle * mRadius / (Math.pow(tanRotateAngle, 2.0) + 1.0));
                float stopY = (float) ((Math.pow(tanRotateAngle, 2.0) - 1.0) * mRadius
                        / (Math.pow(tanRotateAngle, 2.0) + 1.0));

                canvas.drawLine(0, -mRadius, stopX, stopY, mPaint);
            }

            canvas.restore();
        }

        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds.set(0 + getPaddingLeft(), 0 + getPaddingTop(), w - getPaddingRight(),
                h - getPaddingBottom());
        mBounds.inset(mStrokeWidth, mStrokeWidth);

        mRadius = (int) (Math.min(mBounds.width(), mBounds.height()) / 2);
        mCenterX = (int) mBounds.centerX();
        mCenterY = (int) mBounds.centerY();
    }

    @Override
    protected void onDetachedFromWindow() {
        reset();
        super.onDetachedFromWindow();
    }

    @Override
    public void reset() {
        mOutRingRotateAnimator.cancel();

        mShootLineRotateRadians = SHOOT_LINE_ROTATE_START_RADIANS;
        mShootLineTotalRotateAngle = -(SHOOT_LINE_ROTATE_START_DEGREE / 2.0f) - 240.0f;
        mOutRingRotateAngle = 0.0f;
        invalidate();
    }

    @Override
    public void refreshing() {
        mOutRingRotateAngle = 0.0f;
        mShootLineTotalRotateAngle = 0.0f;
        mShootLineRotateRadians = 0.0f;

        if (mOutRingRotateAnimator.isRunning()) {
            mOutRingRotateAnimator.cancel();
        }
        mOutRingRotateAnimator.start();
    }

    @Override
    public void refreshComplete() {
    }

    @Override
    public void pullToRefresh() {

    }

    @Override
    public void releaseToRefresh() {

    }

    @Override
    public void pullProgress(float pullDistance, float pullProgress) {
        pullProgress = Math.min(1.0f, Math.max(0.0f, pullProgress));

        if (pullProgress < PRE_SHOOT_LINE_TOTAL_ROTATE_END_OFFSET) {
            mPreShootLineTotalRotateAnimator.setCurrentPlayTime((long) (pullProgress
                    / PRE_SHOOT_LINE_TOTAL_ROTATE_END_OFFSET * PRE_SHOOT_LINE_TOTAL_ROTATE_DURATION));
        } else if (pullProgress < SHOOT_LINE_ROTATE_END_OFFSET) {
            mShootLineRotateAnimator.setCurrentPlayTime(
                    (long) ((pullProgress - PRE_SHOOT_LINE_TOTAL_ROTATE_END_OFFSET)
                            / (SHOOT_LINE_ROTATE_END_OFFSET - PRE_SHOOT_LINE_TOTAL_ROTATE_END_OFFSET)
                            * SHOOT_LINE_ROTATE_DURATION));
        } else {
            if (pullProgress == 1.0f) {
                mShootLineStretchAnimator.setCurrentPlayTime(SHOOT_LINE_STRETCH_DURATION);
            } else {
                mShootLineStretchAnimator.setCurrentPlayTime(
                        (long) ((pullProgress - SHOOT_LINE_ROTATE_END_OFFSET)
                                / (SHOOT_LINE_STRETCH_END_OFFSET - SHOOT_LINE_ROTATE_END_OFFSET)
                                * SHOOT_LINE_STRETCH_DURATION));
            }
        }
    }
}
