package bitmap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.sample.R;

import static bitmap.BitmapUtil.getDiskBitmap;
import static bitmap.BitmapUtil.saveBitmapForSdCard;
import static config.Constants.DEFAULT_IMG_DIR;
import static config.Constants.DEFAULT_IMG_NAME;

/**
 * Created by Wing on 2018/4/30.
 */

public class BitmapActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
        private ScreenShotView screenShotView;
        private Bitmap bmp;
        private Bitmap ocrBitmap;
        private TextView certainBtn;
        private TextView cancelBtn;
        private TextView restartBtn;
        private int screenWidth;
        private int screenHeight;
        private int[] bound;
        private int statusBarHeight;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_bitmap);
            screenShotView = (ScreenShotView) findViewById(R.id.screenShotView);
            cancelBtn = (TextView) findViewById(R.id.cancel_btn);
            cancelBtn.setOnClickListener(this);
            certainBtn = (TextView) findViewById(R.id.certain_btn);
            certainBtn.setOnClickListener(this);
            restartBtn = (TextView)findViewById(R.id.restart_btn);
            restartBtn.setOnClickListener(this);
            DisplayMetrics dm = new DisplayMetrics();
            //获取屏幕信息
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;
//            Resources r = this.getResources();
//            InputStream is = r.openRawResource(R.raw.df);
//            BitmapDrawable bmpDraw = new BitmapDrawable(is);
//            bmp = bmpDraw.getBitmap();
            bmp = getDiskBitmap(DEFAULT_IMG_DIR + DEFAULT_IMG_NAME);
            screenShotView.setBitmap(bmp, bmp.getHeight(), screenWidth);
            screenShotView.setOnTouchListener(this);
            bound = new int[4];
            // 获取状态栏高度
            Rect frame = new Rect();
            BitmapActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    screenShotView.setStartDot(new Dot(motionEvent.getX(), motionEvent.getY()));
                    break;
                case MotionEvent.ACTION_MOVE:
                    screenShotView.setEndDot(new Dot(motionEvent.getX(), motionEvent.getY()));
                    screenShotView.setBitmap(bmp, screenHeight, screenWidth);
                    break;
                case MotionEvent.ACTION_UP:
                    ocrBitmap = screenShotView.getBitmap();
                    bound = screenShotView.getBound();
                    bound[1] -= statusBarHeight;
                    bound[3] -= statusBarHeight;
                    break;
            }
            return true;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cancel_btn:
                    finish();
                    break;
                case R.id.certain_btn:
                    if (ocrBitmap != null) {
                        saveBitmapForSdCard(DEFAULT_IMG_DIR,DEFAULT_IMG_NAME,ocrBitmap);
                        Intent intent = new Intent();
                        intent.putExtra("bound",bound);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }else{
                        Toast.makeText(BitmapActivity.this,"请选择截取区域",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.restart_btn:
                    screenShotView.restart();
                    break;
            }
        }

        @Override
        protected void onResume() {
            super.onResume();
            screenShotView.restart();
        }

        @Override
        protected void onRestart() {
            super.onRestart();
            screenShotView.restart();
        }

}
