package pl.poznan.put.putnav.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import static java.lang.Math.round;

public class VerticalSeekBar extends SeekBar {

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(), 0);

        super.onDraw(c);
    }

    private OnSeekBarChangeListener onChangeListener;
    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onChangeListener){
        this.onChangeListener = onChangeListener;
    }

    private float lastProgress = 0.0f;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onChangeListener.onStartTrackingTouch(this);
                setPressed(true);
                setSelected(true);
                break;
            case MotionEvent.ACTION_MOVE:
                super.onTouchEvent(event);
                float progress = getMax() - (getMax() * event.getY() / getHeight());

                // Ensure progress stays within boundaries
                if(progress < 0.5f) {progress = 0;}
                if(progress > getMax()-0.5f) {progress = getMax();}
                setProgress(round(progress));  // Draw progress
                if(progress != lastProgress) {
                    // Only enact listener if the progress has actually changed
                    lastProgress = round(progress);
                    onChangeListener.onProgressChanged(this, round(progress), true);
                }

                onSizeChanged(getWidth(), getHeight() , 0, 0);
                setPressed(true);
                setSelected(true);
                break;
            case MotionEvent.ACTION_UP:
                onChangeListener.onStopTrackingTouch(this);
                setPressed(false);
                setSelected(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                super.onTouchEvent(event);
                setPressed(false);
                setSelected(false);
                break;
        }
        return true;
    }

    public synchronized void setProgressAndThumb(int progress) {
        setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
        if(progress != lastProgress) {
            // Only enact listener if the progress has actually changed
            lastProgress = progress;
            onChangeListener.onProgressChanged(this, progress, true);
        }
    }

    public synchronized void setMaximum(int maximum) {
        setMax(maximum);
        if(maximum <= 0){
            this.setVisibility(View.INVISIBLE);
        }else{
            this.setVisibility(View.VISIBLE);
        }
    }

    public synchronized int getMaximum() {
        return getMax();
    }

    public synchronized int getLastProgress() {
        return round(lastProgress);
    }
}
