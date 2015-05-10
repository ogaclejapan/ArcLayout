package com.ogaclejapan.arclayout.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * CircularReveal backport
 * https://gist.github.com/schwiz/e566f248723bb1754972
 */
public class ClipRevealFrame extends FrameLayout {

  boolean clipOutlines;
  float centerX;
  float centerY;
  float radius;
  private Path revealPath;

  public ClipRevealFrame(Context context) {
    super(context);
    init();
  }

  public ClipRevealFrame(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public ClipRevealFrame(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    revealPath = new Path();
    clipOutlines = false;
  }

  public void setClipOutLines(boolean shouldClip) {
    clipOutlines = shouldClip;
  }

  public void setClipCenter(final int x, final int y) {
    centerX = x;
    centerY = y;
  }

  public void setClipRadius(final float radius) {
    this.radius = radius;
    invalidate();
  }

  @Override
  public void draw(Canvas canvas) {
    if (!clipOutlines) {
      super.draw(canvas);
      return;
    }
    final int state = canvas.save();
    revealPath.reset();
    revealPath.addCircle(centerX, centerY, radius, Path.Direction.CW);
    canvas.clipPath(revealPath);
    super.draw(canvas);
    canvas.restoreToCount(state);
  }

}
