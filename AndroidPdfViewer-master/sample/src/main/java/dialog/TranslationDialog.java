package dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.barteksc.sample.R;

/**
 * 创建自定义的dialog，显示翻译内容
 * Created by wing on 2018/5/2.
 */
public class TranslationDialog extends Dialog {

    //    private Button yes;//确定按钮
//    private Button no;//取消按钮
    private RelativeLayout rootLayout;//根布局
    private RelativeLayout tmLayout;
    private TextView titleTv;//消息标题文本
    private TextView messageTv;//消息提示文本
    private View rectTv;//标注方框
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    private int[] bound;
    //确定文本和取消文本的显示内容
//    private String yesStr, noStr;
//
//    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
//    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

//    /**
//     * 设置取消按钮的显示内容和监听
//     *
//     * @param str
//     * @param onNoOnclickListener
//     */
//    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
//        if (str != null) {
//            noStr = str;
//        }
//        this.noOnclickListener = onNoOnclickListener;
//    }

//    /**
//     * 设置确定按钮的显示内容和监听
//     *
//     * @param str
//     * @param onYesOnclickListener
//     */
//    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
//        if (str != null) {
//            yesStr = str;
//        }
//        this.yesOnclickListener = onYesOnclickListener;
//    }

    public TranslationDialog(Context context) {
//        super(context, R.style.MyDialog);
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog, null);
        setContentView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        setContentView(R.layout.layout_dialog);
        //按空白处取消
        setCanceledOnTouchOutside(true);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
//    设置确定按钮被点击后，向外界提供监听
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslationDialog.this.dismiss();
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            titleTv.setText(titleStr);
        }
        if (messageStr != null) {
            messageTv.setText(messageStr);
        }
        if (rectTv != null) {
//            rectTv.layout(bound[0],bound[1],bound[2],bound[3]);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(rootLayout.getLayoutParams());
            layoutParams.width = bound[2] - bound[0];
            layoutParams.height = bound[3] - bound[1];
            layoutParams.setMargins(bound[0], bound[1], 0, 0);
            rectTv.setLayoutParams(layoutParams);
        }
        if (tmLayout != null) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(rootLayout.getLayoutParams());
            Display mDisplay = getWindow().getWindowManager().getDefaultDisplay();
//            int width = mDisplay.getWidth();
            int height = mDisplay.getHeight();
            boolean f = bound[1] > height - bound[3] ? true : false;
            if (f) {
//                layoutParams.addRule(RelativeLayout.ABOVE, R.id.rect);
                layoutParams.setMargins(0,rectTv.getTop()-tmLayout.getWidth(),0,height-bound[3]);
            }
            else {
                layoutParams.addRule(RelativeLayout.BELOW, R.id.rect);
            }
            tmLayout.setLayoutParams(layoutParams);
        }
        //如果设置按钮的文字
//        if (yesStr != null) {
//            yes.setText(yesStr);
//        }
//        if (noStr != null) {
//            no.setText(noStr);
//        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
//        yes = (Button) findViewById(R.id.yes);
//        no = (Button) findViewById(R.id.no);
        titleTv = (TextView) findViewById(R.id.title);
        messageTv = (TextView) findViewById(R.id.message);
        rectTv = (View) findViewById(R.id.rect);
        tmLayout = (RelativeLayout) findViewById(R.id.tmLayout);
        rootLayout = (RelativeLayout) findViewById(R.id.layout_root);
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }

    public void setBound(int[] bound) {
        this.bound = bound;
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rectLine.getLayoutParams();
//        layoutParams.leftMargin = X - _xDelta;
//        layoutParams.topMargin = Y - _yDelta;
//        layoutParams.rightMargin = -250;
//        layoutParams.bottomMargin = -250;
//        view.setLayoutParams(layoutParams);
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
//    public interface onYesOnclickListener {
//        public void onYesClick();
//    }
//
//    public interface onNoOnclickListener {
//        public void onNoClick();
//    }
}
