/**
 * Copyright (C) 2015 ogaclejapan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ogaclejapan.arclayout;

import android.util.Log;
import android.view.View;

class Utils {

  static final boolean DEBUG = false; //Set to true only when developing

  private Utils() {}

  static void d(String tag, String format, Object... args) {
    Log.d(tag, String.format(format, args));
  }

  static int computeMeasureSize(int measureSpec, int defSize) {
    final int mode = View.MeasureSpec.getMode(measureSpec);
    switch (mode) {
      case View.MeasureSpec.EXACTLY:
        return View.MeasureSpec.getSize(measureSpec);
      case View.MeasureSpec.AT_MOST:
        return Math.min(defSize, View.MeasureSpec.getSize(measureSpec));
      default:
        return defSize;
    }
  }

  static float computeCircleX(float r, float degrees) {
    return (float) (r * Math.cos(Math.toRadians(degrees)));
  }

  static float computeCircleY(float r, float degrees) {
    return (float) (r * Math.sin(Math.toRadians(degrees)));
  }

  static int computeWidth(int origin, int size, int x) {
    switch (origin & ArcOrigin.HORIZONTAL_MASK) {
      case ArcOrigin.LEFT:
        //To the right edge
        return size - x;
      case ArcOrigin.RIGHT:
        //To the left edge
        return x;
      default:
        //To the shorter * 2 than the right edge and left edge
        return Math.min(x, size - x) * 2;
    }
  }

  static int computeHeight(int origin, int size, int y) {
    switch (origin & ArcOrigin.VERTICAL_MASK) {
      case ArcOrigin.TOP:
        //To the bottom edge
        return size - y;
      case ArcOrigin.BOTTOM:
        //To the top edge
        return y;
      default:
        //To the shorter * 2 than the top edge and bottom edge
        return Math.min(y, size - y) * 2;
    }
  }

}
