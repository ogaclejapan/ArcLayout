package com.ogaclejapan.arclayout.demo;

import android.content.Context;

import com.ogaclejapan.arclayout.Arc;

public enum Demo {

  //Basic
  CENTER(R.string.title_center, R.string.note_center, R.layout.arc_large, Arc.CENTER),
  LEFT(R.string.title_left, R.string.note_left, R.layout.arc_medium, Arc.LEFT),
  RIGHT(R.string.title_right, R.string.note_right, R.layout.arc_medium, Arc.RIGHT),
  TOP(R.string.title_top, R.string.note_top, R.layout.arc_medium, Arc.TOP),
  TOP_LEFT(R.string.title_top_left, R.string.note_top_left, R.layout.arc_small, Arc.TOP_LEFT),
  TOP_RIGHT(R.string.title_top_right, R.string.note_top_right, R.layout.arc_small, Arc.TOP_RIGHT),
  BOTTOM(R.string.title_bottom, R.string.note_bottom, R.layout.arc_medium, Arc.BOTTOM),
  BOTTOM_LEFT(R.string.title_bottom_left, R.string.note_bottom_left, R.layout.arc_small,
      Arc.BOTTOM_LEFT),
  BOTTOM_RIGHT(R.string.title_bottom_right, R.string.note_bottom_right, R.layout.arc_small,
      Arc.BOTTOM_RIGHT),

  //Advanced
  ADVANCED_FREE_ANGLE(R.string.title_advanced_freeangle, R.string.note_advanced_freeangle,
      R.layout.arc_free_angle, Arc.CENTER) {
    @Override
    public void startActivity(Context context) {
      DemoFreeAngleActivity.startActivity(context, this);
    }
  },

  ADVANCED_TUMBLR(R.string.title_advanced_tumblr, 0, 0, null) {
    @Override
    public void startActivity(Context context) {
      DemoLikeTumblrActivity.startActivity(context, this);
    }
  },
  ADVANCED_PATH(R.string.title_advanced_path, 0, 0, null) {
    @Override
    public void startActivity(Context context) {
      DemoLikePathActivity.startActivity(context, this);
    }
  };

  public final int titleResId;
  public final int noteResId;
  public final int layoutResId;
  public final Arc arc;

  Demo(int titleResId, int noteResId, int layoutResId, Arc arc) {
    this.titleResId = titleResId;
    this.noteResId = noteResId;
    this.layoutResId = layoutResId;
    this.arc = arc;
  }

  public void startActivity(Context context) {
    DemoActivity.startActivity(context, this);
  }

}
