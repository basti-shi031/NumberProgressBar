package com.bzt.numberprogressbar;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.bzt.num_progressbarlib.CircleProgressBar;
import com.bzt.num_progressbarlib.LineProgressBar;
import com.bzt.num_progressbarlib.ProgressListener;

import java.util.Timer;
import java.util.TimerTask;

import tyrantgit.explosionfield.ExplosionField;

public class MainActivity extends AppCompatActivity implements ProgressListener {

    private CircleProgressBar mCircleProgressBar;
    private LineProgressBar mLineProgressBar;
    private Timer timer;
    private ExplosionField mExplosionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mExplosionField = ExplosionField.attach2Window(this);

        mCircleProgressBar = (CircleProgressBar) findViewById(R.id.circle);
        mCircleProgressBar.setOnProgressListener(this);
        mLineProgressBar = (LineProgressBar) findViewById(R.id.lineprogressbar);
        mCircleProgressBar.setProgress(0);
        mLineProgressBar.setProgress(0);
        //mLineProgressBar.setOnProgressListener(this);
        timer = new Timer();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "开始下载", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mCircleProgressBar.setProgress(mCircleProgressBar.getProgress() + 3);
                                mLineProgressBar.setProgress(mLineProgressBar.getProgress() + 3);
                            }
                        });
                    }
                }, 1000, 100);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void progressChanged(int finish, int total) {
        if (finish>=total){
            mExplosionField.explode(mCircleProgressBar);
            mExplosionField.explode(mLineProgressBar);
        }
    }
}
