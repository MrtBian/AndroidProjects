/**
 * Copyright 2016 Bartosz Schiller
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.barteksc.sample;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.translate.TransApi;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.shockwave.pdfium.PdfDocument;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import bitmap.BitmapActivity;
import dialog.TranslationDialog;
import ocr.OCRTess;
import translation.WordInfo;

import static bitmap.BitmapUtil.captureScreen;
import static bitmap.BitmapUtil.getDiskBitmap;
import static bitmap.BitmapUtil.saveBitmapForSdCard;
import static config.Constants.DEFAULT_IMG_DIR;
import static config.Constants.DEFAULT_IMG_NAME;
import static translation.StringTools.decodeUnicode;
import static translation.StringTools.repairSentence;
import static translation.StringTools.repairWord;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.options)
public class PDFViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {

    private static final String TAG = PDFViewActivity.class.getSimpleName();

    private final static int REQUEST_CODE = 42;
    public static final int PERMISSION_CODE = 42042;
    public static final int GETPICTURE_CODE = 21021;

    public static final String SAMPLE_FILE = "sample.pdf";
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";

    private OCRTess ocrTess;
    /*手势识别设置*/
    private float xDown, yDown, xUp, yUp;
    private long timeUp, timeDown;
    private static final int TIMELONGCLICK = 300;
    private String result, wordExplanation;

    private TranslationDialog translationDialog;

    private SharedPreferences sp;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            int what = msg.what;
            if (what == 0) {    //update
                TextView tv = (TextView) translationDialog.findViewById(R.id.message);
                tv.setText(wordExplanation);
//                if (translationDialog.isShowing()) {
//                    mHandler.sendEmptyMessageDelayed(0, 200);
//                }
            } else {
                translationDialog.cancel();
            }
        }
    };
    @ViewById
    PDFView pdfView;

    @NonConfigurationInstance
    Uri uri;

    @NonConfigurationInstance
    Integer pageNumber = 0;
    String pdfFileName;


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 一定要spuer，否则事件打住,不会在向下调用了
        super.dispatchTouchEvent(event);
        // 获取状态栏高度
        Rect frame = new Rect();
        PDFViewActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        switch (event.getAction()) {
            // 记录用户手指点击的位置
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY() - statusBarHeight;
                timeDown = System.currentTimeMillis();
                Log.v("PDFViewActivity", "Down");
                return false;
            case MotionEvent.ACTION_UP:
                xUp = event.getRawX();
                yUp = event.getRawY() - statusBarHeight;
                timeUp = System.currentTimeMillis();
                if (Math.abs(xDown - xUp) < 20 && Math.abs(yDown - yUp) < 20) {
                    if (timeUp - timeDown > TIMELONGCLICK) {
                        //长按事件
                        // 生成一个Intent对象
                        Bitmap bitmap = captureScreen(PDFViewActivity.this);
                        saveBitmapForSdCard(DEFAULT_IMG_DIR, DEFAULT_IMG_NAME, bitmap);
                        Intent intent = new Intent();
                        intent.setClass(PDFViewActivity.this, BitmapActivity.class); //设置跳转的Activity
                        PDFViewActivity.this.startActivityForResult(intent, GETPICTURE_CODE);
                        Log.v("PDFViewActivity", "LONGCLICK");
                    }
                }
        }
        return false;// return false,继续向下传递，return true;拦截,不向下传递
    }

    /**
     * 复制res/raw中的文件到指定目录
     *
     * @param context     上下文
     * @param fileName    文件名
     * @param storagePath 目标文件夹的路径
     */
    public static void copyFilesFromRaw(Context context, String fileName, String storagePath) {
        try {
            //获得原文件流
            InputStream inputStream = context.getResources().getAssets().open(fileName);
            File file = new File(storagePath);
            if (!file.exists()) {//如果文件夹不存在，则创建新的文件夹
                file.mkdirs();
            }
            //输出流
            OutputStream outputStream = new FileOutputStream(new File(storagePath + File.separator + fileName));
            byte[] data = new byte[1024];
            //开始处理流
            while (inputStream.read(data) != -1) {
                outputStream.write(data);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化Tess-two
     */
    private void initOCR() {
        ocrTess = new OCRTess(this);
    }


    @OptionsItem(R.id.pickFile)
    void pickFile() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE
            );

            return;
        }

        launchPicker();
    }

    void launchPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            //alert user that file manager not working
            Toast.makeText(this, R.string.toast_pick_file_error, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示对话框
     *
     * @param title
     * @param content
     */
    private void showDialog(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog dialog = builder.create();
//        try {
//            Thread.sleep(500);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        dialog.show();
    }

    @AfterViews
    void afterViews() {
        pdfView.setBackgroundColor(Color.LTGRAY);
        initOCR();
        sp = this.getSharedPreferences("data", 0);
        pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件
                Log.v("Click XY", xDown + " " + yDown);
                Bitmap bitmap = captureScreen(PDFViewActivity.this);
                if (bitmap != null) {
                    int[] bound = new int[4];
                    result = ocrTess.xyOCR(bitmap, xDown, yDown, bound);
                    if (result != "") {
                        result = repairWord(result.toLowerCase());
//                                  rectDialog = new RectDialog(PDFViewActivity.this,bound);
                        Log.v("Word:", result);
                        Log.v("Box", bound[0] + " " + bound[1] + " " + bound[2] + " " + bound[3]);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String explanation = new WordInfo(result).getExplanation();
                                wordExplanation = explanation;
                                mHandler.sendEmptyMessage(0);

                            }
                        }).start();
//                                    showDialog(result, wordExplanation);
                        translationDialog = new TranslationDialog(PDFViewActivity.this);
                        translationDialog.setTitle(result);
                        translationDialog.setMessage("Wait...");
                        translationDialog.setBound(bound);
                        translationDialog.show();

                    }
                }
            }
        });
        pdfView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //长按事件
                // 生成一个Intent对象
                Bitmap bitmap = captureScreen(PDFViewActivity.this);
                saveBitmapForSdCard(DEFAULT_IMG_DIR, DEFAULT_IMG_NAME, bitmap);
                Intent intent = new Intent();
//                            intent.putExtra("testIntent", "123");
                intent.setClass(PDFViewActivity.this, BitmapActivity.class); //设置跳转的Activity
                PDFViewActivity.this.startActivityForResult(intent, GETPICTURE_CODE);
                Log.v("TAG", "LONGCLICK");
                return true;
            }
        });
//        pdfView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
////                pdfView.requestDisallowInterceptTouchEvent(false);
//
//                // 获取状态栏高度
//                Rect frame = new Rect();
//                PDFViewActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//                int statusBarHeight = frame.top;
//                //当按下时处理
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    xDown = event.getRawX();
//                    yDown = event.getRawY()-statusBarHeight;
//                    timeDown = System.currentTimeMillis();
//                    Log.v("OnTouchListener", "Down");
//                    return false;
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {// 松开处理
//                    //获取松开时的坐标
//                    xUp = event.getRawX();
//                    yUp = event.getRawY()-statusBarHeight;
//                    timeUp = System.currentTimeMillis();
//                    if (Math.abs(xDown - xUp) < 20 && Math.abs(yDown - yUp) < 20) {
//                        if (timeUp - timeDown > TIMELONGCLICK) {
//                            //长按事件
//                            // 生成一个Intent对象
//                            Bitmap bitmap = captureScreen(PDFViewActivity.this);
//                            saveBitmapForSdCard(DEFAULT_IMG_DIR, DEFAULT_IMG_NAME, bitmap);
//                            Intent intent = new Intent();
////                            intent.putExtra("testIntent", "123");
//                            intent.setClass(PDFViewActivity.this, BitmapActivity.class); //设置跳转的Activity
//                            PDFViewActivity.this.startActivity(intent);
//                            Bitmap bitmapt = getDiskBitmap(DEFAULT_IMG_DIR + DEFAULT_IMG_NAME);
//                            int[] bound = new int[4];
//                            result = englishOCR(bitmapt, xDown, yDown, bound);
//                            if (result != "") {
////                                  rectDialog = new RectDialog(PDFViewActivity.this,bound);
//                                Log.v("Word:", result);
//                                Log.v("Box", bound[0] + " " + bound[1] + " " + bound[2] + " " + bound[3]);
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        String explanation = new WordInfo(result).getExplanation();
//                                        wordExplanation = explanation;
//                                        mHandler.sendEmptyMessage(0);
//
//                                    }
//                                }).start();
//                                translationDialog = new TranslationDialog(PDFViewActivity.this);
//                                translationDialog.setTitle(result);
//                                translationDialog.setMessage("Explanation");
//                                translationDialog.setBound(bound);
//                                translationDialog.show();
//                            }
//
//                            Log.v("OnTouchListener", "LONGCLICK");
//                        } else {
//                            //点击事件
//                            Log.v("Click XY", xDown + " " + yDown);
//                            Bitmap bitmap = captureScreen(PDFViewActivity.this);
//                            if (bitmap != null) {
//                                int[] bound = new int[4];
//                                result = englishOCR(bitmap, xDown, yDown, bound);
//                                if (result != "") {
////                                  rectDialog = new RectDialog(PDFViewActivity.this,bound);
//                                    Log.v("Word:", result);
//                                    Log.v("Box", bound[0] + " " + bound[1] + " " + bound[2] + " " + bound[3]);
//                                    new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            String explanation = new WordInfo(result).getExplanation();
//                                            wordExplanation = explanation;
//                                            mHandler.sendEmptyMessage(0);
//
//                                        }
//                                    }).start();
////                                    showDialog(result, wordExplanation);
//                                    translationDialog = new TranslationDialog(PDFViewActivity.this);
//                                    translationDialog.setTitle(result);
//                                    translationDialog.setMessage("Explanation");
//                                    translationDialog.setBound(bound);
//                                    translationDialog.show();
//
//                                }
//                            }
//                        }
//                    }
//                    Log.v("OnTouchListener", "Up");
//                }
//
//                return false;
//            }
//        });
        if (uri != null)

        {
            displayFromUri(uri);
        } else

        {
            displayFromAsset(SAMPLE_FILE);
        }

        setTitle(pdfFileName);

    }

    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;

        pdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .pageFitPolicy(FitPolicy.BOTH)
                .load();
    }

    private void displayFromUri(Uri uri) {
        pdfFileName = getFileName(uri);

        pdfView.fromUri(uri)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .load();
    }

    @OnActivityResult(REQUEST_CODE)
    public void onResult(int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            uri = intent.getData();
            displayFromUri(uri);
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.e(TAG, "modDate = " + meta.getModDate());

        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    /**
     * Listener for response to user permission request
     *
     * @param requestCode  Check that permission request code matches
     * @param permissions  Permissions that requested
     * @param grantResults Whether permissions granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchPicker();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the request went well (OK) and the request was PICK_CONTACT_REQUEST
        if (resultCode == Activity.RESULT_OK && requestCode == GETPICTURE_CODE) {
            Bitmap bitmapt = getDiskBitmap(DEFAULT_IMG_DIR + DEFAULT_IMG_NAME);
            int[] bound = data.getIntArrayExtra("bound");
            result = ocrTess.defaultOCR(bitmapt);
            if (result != "") {
                result = repairSentence(result);
                Log.v("Sentence:", result);
                Log.v("Box", bound[0] + " " + bound[1] + " " + bound[2] + " " + bound[3]);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        String explanation = new WordInfo(result).getSenTrans();
//                        wordExplanation = explanation;
                        //百度翻译
                        wordExplanation = new TransApi().getTransResult(result, "en", "zh");
//                        wordExplanation = unicodeToString(wordExplanation);
                        wordExplanation = decodeUnicode(wordExplanation);
                        mHandler.sendEmptyMessage(0);

                    }
                }).start();
                translationDialog = new TranslationDialog(PDFViewActivity.this);
//                translationDialog.setTitle(result);
                translationDialog.setTitle("译文：");
                translationDialog.setMessage("Wait...");
                translationDialog.setBound(bound);
                translationDialog.show();
            }
        }
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.e(TAG, "Cannot load page " + page);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mTess.end();
        if (translationDialog != null)
            if (translationDialog.isShowing())
                translationDialog.dismiss();

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        SharedPreferences.Editor editor = sp.edit();
//        if(uri==null){
//
//            editor.putString("uri", "");
//        }else {
//        editor.putString("uri", uri.toString());}
//        editor.commit();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        String temp = sp.getString("uri", "");
//        if (temp == "") {
//            uri = null;
//        } else
//            uri = Uri.parse(temp);
//    }
}
