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

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public enum Arc {

  CENTER(270, 360) {
    @Override
    public Path computePath(int radius, int l, int t, int r, int b) {
      final Point o = computeOrigin(l, t, r, b);
      final Path path = new Path();
      path.addCircle(o.x, o.y, radius, Path.Direction.CW);
      return path;
    }
  },
  LEFT(270, 180) {
    @Override
    public Point computeOrigin(int l, int t, int r, int b) {
      return new Point(l, centerY(t, b));
    }

    @Override
    public int computeWidth(int radius) {
      return radius;
    }

  },
  RIGHT(90, 180) {
    @Override
    public Point computeOrigin(int l, int t, int r, int b) {
      return new Point(r, centerY(t, b));
    }

    @Override
    public int computeWidth(int radius) {
      return radius;
    }

  },
  TOP(0, 180) {
    @Override
    public Point computeOrigin(int l, int t, int r, int b) {
      return new Point(centerX(l, r), t);
    }

    @Override
    public int computeHeight(int radius) {
      return radius;
    }

  },
  TOP_LEFT(0, 90) {
    @Override
    public Point computeOrigin(int l, int t, int r, int b) {
      return new Point(l, t);
    }

    @Override
    public int computeWidth(int radius) {
      return radius;
    }

    @Override
    public int computeHeight(int radius) {
      return radius;
    }

  },
  TOP_RIGHT(90, 90) {
    @Override
    public Point computeOrigin(int left, int t, int r, int b) {
      return new Point(r, t);
    }

    @Override
    public int computeWidth(int radius) {
      return radius;
    }

    @Override
    public int computeHeight(int radius) {
      return radius;
    }

  },
  BOTTOM(180, 180) {
    @Override
    public Point computeOrigin(int l, int t, int r, int b) {
      return new Point(centerX(l, r), b);
    }

    @Override
    public int computeHeight(int radius) {
      return radius;
    }

  },
  BOTTOM_LEFT(270, 90) {
    @Override
    public Point computeOrigin(int l, int t, int r, int b) {
      return new Point(l, b);
    }

    @Override
    public int computeWidth(int radius) {
      return radius;
    }

    @Override
    public int computeHeight(int radius) {
      return radius;
    }

  },
  BOTTOM_RIGHT(180, 90) {
    @Override
    public Point computeOrigin(int l, int t, int r, int b) {
      return new Point(r, b);
    }

    @Override
    public int computeWidth(int radius) {
      return radius;
    }

    @Override
    public int computeHeight(int radius) {
      return radius;
    }

  };

  public final int startAngle;
  public final int sweepAngle;

  Arc(int startAngle, int sweepAngle) {
    this.startAngle = startAngle;
    this.sweepAngle = sweepAngle;
  }

  public static int x(int radius, float degrees) {
    return Math.round(Utils.computeCircleX(radius, degrees));
  }

  public static int y(int radius, float degrees) {
    return Math.round(Utils.computeCircleY(radius, degrees));
  }

  public static int centerX(int left, int right) {
    return (left + right) / 2;
  }

  public static int centerY(int top, int bottom) {
    return (top + bottom) / 2;
  }

  public static int diameter(int radius) {
    return radius * 2;
  }

  public static Arc of(int origin) {
    switch ((origin & ArcOrigin.VERTICAL_MASK)) {
      case ArcOrigin.TOP:
        return ofTop(origin);
      case ArcOrigin.BOTTOM:
        return ofBottom(origin);
      default:
        return ofCenter(origin);
    }
  }

  private static Arc ofTop(int origin) {
    switch ((origin & ArcOrigin.HORIZONTAL_MASK)) {
      case ArcOrigin.LEFT:
        return Arc.TOP_LEFT;
      case ArcOrigin.RIGHT:
        return Arc.TOP_RIGHT;
      default:
        return Arc.TOP;
    }
  }

  private static Arc ofCenter(int origin) {
    switch ((origin & ArcOrigin.HORIZONTAL_MASK)) {
      case ArcOrigin.LEFT:
        return Arc.LEFT;
      case ArcOrigin.RIGHT:
        return Arc.RIGHT;
      default:
        return Arc.CENTER;
    }
  }

  private static Arc ofBottom(int origin) {
    switch ((origin & ArcOrigin.HORIZONTAL_MASK)) {
      case ArcOrigin.LEFT:
        return Arc.BOTTOM_LEFT;
      case ArcOrigin.RIGHT:
        return Arc.BOTTOM_RIGHT;
      default:
        return Arc.BOTTOM;
    }
  }

  public float computeDegrees(int index, float perDegrees) {
    final float offsetAngle = (sweepAngle < 360) ? startAngle - (perDegrees / 2f) : startAngle;
    return offsetAngle + perDegrees + (perDegrees * index);
  }

  public float computeReverseDegrees(int index, float perDegrees) {
    final float offsetAngle = (sweepAngle < 360) ? startAngle + (perDegrees / 2f) : startAngle;
    final float shiftDegrees = (sweepAngle / 360) * perDegrees;
    return offsetAngle + sweepAngle - (perDegrees + perDegrees * index) + shiftDegrees;
  }

  public float computePerDegrees(int size) {
    return ((float) sweepAngle) / size;
  }

  public Path computePath(int radius, int l, int t, int r, int b) {
    final Point o = computeOrigin(l, t, r, b);
    final int ol = o.x - radius;
    final int ot = o.y - radius;
    final int or = o.x + radius;
    final int ob = o.y + radius;

    Path path = new Path();
    path.moveTo(o.x, o.y);
    switch (startAngle) {
      case 0:
        path.lineTo(or, o.y);
        break;
      case 90:
        path.lineTo(o.x, ob);
        break;
      case 180:
        path.lineTo(ol, o.y);
        break;
      case 270:
        path.lineTo(o.x, ot);
        break;
      default:
        throw new UnsupportedOperationException();
    }
    if (Build.VERSION.SDK_INT >= LOLLIPOP) {
      path.arcTo(ol, ot, or, ob, startAngle, sweepAngle, true);
    } else {
      path.arcTo(new RectF(ol, ot, or, ob), startAngle, sweepAngle, true);
    }
    path.lineTo(o.x, o.y);
    return path;
  }

  public Point computeOrigin(int l, int t, int r, int b) {
    return new Point(centerX(l, r), centerY(t, b));
  }

  public int computeWidth(int radius) {
    return diameter(radius);
  }

  public int computeHeight(int radius) {
    return diameter(radius);
  }

}
