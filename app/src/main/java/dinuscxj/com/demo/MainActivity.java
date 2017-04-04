package dinuscxj.com.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dinuscxj.shootrefreshview.ShootRefreshView;

public class MainActivity extends AppCompatActivity {

    private ShootRefreshView mShootRefreshView;

    private SeekBar mPullProgressBar;

    private Button mResetView;
    private Button mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mShootRefreshView = (ShootRefreshView) findViewById(R.id.shoot_refresh_view);
        mPullProgressBar = (SeekBar) findViewById(R.id.pull_progress_bar);
        mLoadingView = (Button) findViewById(R.id.loading_view);
        mResetView = (Button) findViewById(R.id.reset_view);

        mPullProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mShootRefreshView.pullProgress(0, ((float) progress) / ((float) seekBar.getMax()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mLoadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPullProgressBar.setProgress(0);
                mShootRefreshView.refreshing();
            }
        });

        mResetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPullProgressBar.setProgress(0);
                mShootRefreshView.reset();
            }
        });
    }

}
