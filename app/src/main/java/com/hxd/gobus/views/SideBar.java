package com.hxd.gobus.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.hxd.gobus.R;

/**
 * Created by pengyu520 on 2016/6/30.
 */
public class SideBar extends View {

    private static char[] chars = {'#', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private char currentChar;//当前选择的字符
    private TextView tvTip;
    private OnSideBarListener onSideBarListener;

    public SideBar(Context context) {
        super(context);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置提示的文本框
     *
     * @param tvTip
     */
    public void setTextView(TextView tvTip) {
        this.tvTip = tvTip;
    }

    public char getCurrentChar() {
        return currentChar;
    }

    public void setCurrentChar(char currentChar) {
        this.currentChar = currentChar;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float xPos = getWidth() / 2;// 字符在X轴的坐标
        int yPos = 0;// 字符在Y轴的坐标
        if (chars.length > 0) {
            int singleHeight = getHeight() / chars.length;//获取单个字符的高度
            for (int i = 0; i < chars.length; i++) {
                yPos = (i + 1) * singleHeight;
//                chars[i]==currentChar?getPaint(1):getPaint(0)
                canvas.drawText(String.valueOf(chars[i]), xPos, yPos, getPaint());
            }
        }
        this.invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int index=getCurrentIndex(event);
        setCurrentChar(chars[index]);//设置当前按下的字符
        onSideBarListener.onLetterChanged(String.valueOf(chars[index]));
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        //    setBackgroundColor(getResources().getColor(R.color.colorAccent));
            if (tvTip !=null) {
                tvTip.setVisibility(View.VISIBLE);
                tvTip.setText(String.valueOf(chars[index]));
            }
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            if (tvTip !=null) {
                tvTip.setVisibility(View.GONE);
            }
        }else if (event.getAction() == MotionEvent.ACTION_UP) {
        //    setBackgroundColor(getResources().getColor(R.color.main_background));
            if (tvTip !=null) {
                tvTip.setVisibility(View.GONE);
            }
        }
        return true;
    }

    /**
     * 获取索引值
     * @param event
     * @return
     */
    private int getCurrentIndex(MotionEvent event) {
        if (event!=null){
            int y = (int) event.getY();
            int index = y / (getMeasuredHeight() / chars.length);
            if (index < 0) {
                index = 0;
            } else if (index >= chars.length) {
                index = chars.length - 1;
            }
            return index;
        }
        return 0;
    }
    private Paint getPaint(){
        Paint paint=new Paint();
        paint.setColor(getResources().getColor(R.color.grey_6));
        paint.setTextSize(30);
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextAlign(Paint.Align.CENTER);
        return  paint;
    }

    public void setOnSideBarListener(OnSideBarListener onSideBarListener){
        this.onSideBarListener=onSideBarListener;
    }
    public interface OnSideBarListener{
        void onLetterChanged(String letter);
    }

}
