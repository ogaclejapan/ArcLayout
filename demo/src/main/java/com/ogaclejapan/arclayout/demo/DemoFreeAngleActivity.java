package com.ogaclejapan.arclayout.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ogaclejapan.arclayout.ArcLayout;

public class DemoFreeAngleActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String KEY_DEMO = "demo";

    public static void startActivity(Context context, Demo demo) {
        Intent intent = new Intent(context, DemoFreeAngleActivity.class);
        intent.putExtra(KEY_DEMO, demo.name());
        context.startActivity(intent);
    }

    private static Demo getDemo(Intent intent) {
        return Demo.valueOf(intent.getStringExtra(KEY_DEMO));
    }

    Toast mToast = null;
    ArcLayout mArcLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Demo demo = getDemo(getIntent());

        setContentView(demo.layoutResId);
        mArcLayout = (ArcLayout) findViewById(R.id.arc_layout);
        mArcLayout.setArc(demo.arc);

        for (int i = 0, size = mArcLayout.getChildCount(); i < size; i++) {
            mArcLayout.getChildAt(i).setOnClickListener(this);
        }

        TextView note = (TextView) findViewById(R.id.note_text);
        note.setText(Html.fromHtml(getString(demo.noteResId)));
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)note.getLayoutParams();
        switch (demo.arc) {
            case TOP:
            case TOP_LEFT:
            case TOP_RIGHT:
                lp.gravity = Gravity.BOTTOM;
                break;
            case BOTTOM:
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
                lp.gravity = Gravity.TOP;
                break;
            default:
                lp.gravity = Gravity.TOP;
        }

        ActionBar bar = getSupportActionBar();
        bar.setTitle(demo.titleResId);
        bar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            showToast((Button) v);
        }
    }

    private void showToast(Button btn) {
        if (mToast != null) {
            mToast.cancel();
        }

        String text = "Clicked: " + btn.getText();
        mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        mToast.show();

    }
}
