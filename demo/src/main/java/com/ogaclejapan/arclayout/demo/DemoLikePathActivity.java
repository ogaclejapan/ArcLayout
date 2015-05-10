package com.ogaclejapan.arclayout.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.List;

public class DemoLikePathActivity extends ActionBarActivity implements View.OnClickListener {

  private static final String KEY_DEMO = "demo";
  Toast toast = null;
  View fab;
  View menuLayout;
  ArcLayout arcLayout;

  public static void startActivity(Context context, Demo demo) {
    Intent intent = new Intent(context, DemoLikePathActivity.class);
    intent.putExtra(KEY_DEMO, demo.name());
    context.startActivity(intent);
  }

  private static Demo getDemo(Intent intent) {
    return Demo.valueOf(intent.getStringExtra(KEY_DEMO));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.like_a_path);

    Demo demo = getDemo(getIntent());

    ActionBar bar = getSupportActionBar();
    bar.setTitle(demo.titleResId);
    bar.setDisplayHomeAsUpEnabled(true);

    fab = findViewById(R.id.fab);
    menuLayout = findViewById(R.id.menu_layout);
    arcLayout = (ArcLayout) findViewById(R.id.arc_layout);

    for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
      arcLayout.getChildAt(i).setOnClickListener(this);
    }

    fab.setOnClickListener(this);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.fab) {
      onFabClick(v);
      return;
    }

    if (v instanceof Button) {
      showToast((Button) v);
    }

  }

  private void showToast(Button btn) {
    if (toast != null) {
      toast.cancel();
    }

    String text = "Clicked: " + btn.getText();
    toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
    toast.show();

  }

  private void onFabClick(View v) {
    if (v.isSelected()) {
      hideMenu();
    } else {
      showMenu();
    }
    v.setSelected(!v.isSelected());
  }

  @SuppressWarnings("NewApi")
  private void showMenu() {
    menuLayout.setVisibility(View.VISIBLE);

    List<Animator> animList = new ArrayList<>();

    for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
      animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
    }

    AnimatorSet animSet = new AnimatorSet();
    animSet.setDuration(400);
    animSet.setInterpolator(new OvershootInterpolator());
    animSet.playTogether(animList);
    animSet.start();
  }

  @SuppressWarnings("NewApi")
  private void hideMenu() {

    List<Animator> animList = new ArrayList<>();

    for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
      animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
    }

    AnimatorSet animSet = new AnimatorSet();
    animSet.setDuration(400);
    animSet.setInterpolator(new AnticipateInterpolator());
    animSet.playTogether(animList);
    animSet.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        menuLayout.setVisibility(View.INVISIBLE);
      }
    });
    animSet.start();

  }

  private Animator createShowItemAnimator(View item) {

    float dx = fab.getX() - item.getX();
    float dy = fab.getY() - item.getY();

    item.setRotation(0f);
    item.setTranslationX(dx);
    item.setTranslationY(dy);

    Animator anim = ObjectAnimator.ofPropertyValuesHolder(
        item,
        AnimatorUtils.rotation(0f, 720f),
        AnimatorUtils.translationX(dx, 0f),
        AnimatorUtils.translationY(dy, 0f)
    );

    return anim;
  }

  private Animator createHideItemAnimator(final View item) {
    float dx = fab.getX() - item.getX();
    float dy = fab.getY() - item.getY();

    Animator anim = ObjectAnimator.ofPropertyValuesHolder(
        item,
        AnimatorUtils.rotation(720f, 0f),
        AnimatorUtils.translationX(0f, dx),
        AnimatorUtils.translationY(0f, dy)
    );

    anim.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        item.setTranslationX(0f);
        item.setTranslationY(0f);
      }
    });

    return anim;
  }

}
