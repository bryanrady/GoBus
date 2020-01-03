package com.hxd.gobus.views.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.hxd.gobus.R;

import java.lang.reflect.Field;
import java.util.Calendar;

public class DateTimeYearPicker extends FrameLayout {
    private final NumberPicker mYearSpinner;
    private final NumberPicker mMonthSpinner;
    private final NumberPicker mDaySpinner;
    private final NumberPicker mAPSpinner;
    private TextView tvtype;
    private Calendar mDate;
    private int mYear, mMonth;
    private int map = 0;
    private String[] mDateDisplayValues = new String[7];
    private String[] mYearDisplayValues = new String[152];
    private String[] mMonthDisplayValues = new String[12];
    private String[] mAPDisplayValues = new String[2];
    private OnDateTimeChangedListener mOnDateTimeChangedListener;

    public DateTimeYearPicker(Context context, boolean last) {
        super(context);
        mDate = Calendar.getInstance();
        mYear = mDate.get(Calendar.YEAR);
        mMonth = mDate.get(Calendar.MONTH);
        inflate(context, R.layout.dialog_yeardialog, this);
        tvtype = (TextView) this.findViewById(R.id.tv_type);
        tvtype.setText("请选择");
        mYearSpinner = (NumberPicker) this.findViewById(R.id.np_year);
        setNumberPickerDividerColor(mYearSpinner);
        setNumberPickerDividerHeight(mYearSpinner);
        mYearSpinner.getChildAt(0).setFocusable(false);
        mYearSpinner.setMinValue(mYear - 101);
        mYearSpinner.setMaxValue(mYear + 50);
        updateYearControl();
        mYearSpinner.setValue(mYear);
        mYearSpinner.setWrapSelectorWheel(false);//设置为不可循环
        mYearSpinner.setOnValueChangedListener(mOnYearChangedListener);

        mMonthSpinner = (NumberPicker) this.findViewById(R.id.np_month);
        setNumberPickerDividerColor(mMonthSpinner);
        setNumberPickerDividerHeight(mMonthSpinner);
        mMonthSpinner.getChildAt(0).setFocusable(false);
        mMonthSpinner.setMaxValue(12);
        mMonthSpinner.setMinValue(1);
        updateMonthControl();
        mMonthSpinner.setWrapSelectorWheel(false);
        mMonthSpinner.setOnValueChangedListener(mOnMonthChangedListener);

        mDaySpinner = (NumberPicker) this.findViewById(R.id.np_day);
        setNumberPickerDividerColor(mDaySpinner);
        setNumberPickerDividerHeight(mDaySpinner);
        mDaySpinner.getChildAt(0).setFocusable(false);
        mDaySpinner.setMaxValue(6);
        mDaySpinner.setMinValue(0);
        updateDateControl();
        mDaySpinner.setWrapSelectorWheel(false);
        mDaySpinner.setOnValueChangedListener(mOnDayChangedListener);

        mAPSpinner = (NumberPicker) this.findViewById(R.id.np_AP);
        setNumberPickerDividerColor(mAPSpinner);
        setNumberPickerDividerHeight(mAPSpinner);
        mAPSpinner.getChildAt(0).setFocusable(false);
        updateAPControl();
        mAPSpinner.setMaxValue(1);
        mAPSpinner.setMinValue(0);
        mAPSpinner.setWrapSelectorWheel(false);
        mAPSpinner.setOnValueChangedListener(mOnAPChangedListener);
        if (last) {
            mAPSpinner.setVisibility(VISIBLE);
        } else {
            mAPSpinner.setVisibility(GONE);
        }
    }

    private OnValueChangeListener mOnDayChangedListener = new OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mDate.add(Calendar.DAY_OF_MONTH, newVal - oldVal);
            mDate.set(mYear, mMonth, mDate.get(Calendar.DAY_OF_MONTH));
            updateDateControl();
            onDateTimeChanged();
        }
    };

    private OnValueChangeListener mOnYearChangedListener = new OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mYear = mYearSpinner.getValue();
            mDate.set(mYear, mMonth, mDate.get(Calendar.DAY_OF_MONTH));
            onDateTimeChanged();
        }
    };

    private OnValueChangeListener mOnMonthChangedListener = new OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mMonth = mMonthSpinner.getValue() - 1;
            mDate.set(mYear, mMonth, mDate.get(Calendar.DAY_OF_MONTH));
            onDateTimeChanged();
        }
    };
    private OnValueChangeListener mOnAPChangedListener = new OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            map = mAPSpinner.getValue();
            mDate.set(mYear, mMonth, mDate.get(Calendar.DAY_OF_MONTH));
            onDateTimeChanged();
        }
    };

    private void updateYearControl() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mDate.getTimeInMillis());
        cal.add(Calendar.YEAR, 1);
        mYearSpinner.setDisplayedValues(null);
        for (int i = 101; i >= 0; --i) {
            cal.add(Calendar.YEAR, -1);
            mYearDisplayValues[i] = (String) DateFormat.format("yyyy年", cal);
        }
        cal.setTimeInMillis(mDate.getTimeInMillis());
        cal.add(Calendar.YEAR, 0);
        for (int i = 102; i < 152; ++i) {
            cal.add(Calendar.YEAR, 1);
            mYearDisplayValues[i] = (String) DateFormat.format("yyyy年", cal);
        }

        mYearSpinner.setDisplayedValues(mYearDisplayValues);
        mYearSpinner.setValue(mYear);
        mYearSpinner.invalidate();
    }

    private void updateMonthControl() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mDate.getTimeInMillis());
        cal.add(Calendar.MONTH, -1);
        mMonthSpinner.setDisplayedValues(null);
        for (int i = 0; i < 12; ++i) {
            if (i < 9) {
                mMonthDisplayValues[i] = "0" + (i + 1) + "月";
            } else {
                mMonthDisplayValues[i] = (i + 1) + "月";
            }
        }
        mMonthSpinner.setDisplayedValues(mMonthDisplayValues);
        mMonthSpinner.setValue(mMonth + 1);
        mMonthSpinner.invalidate();
    }

    private void updateDateControl() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mDate.getTimeInMillis());
        cal.add(Calendar.DAY_OF_MONTH, -7 / 2 - 1);
        mDaySpinner.setDisplayedValues(null);
        for (int i = 0; i < 7; ++i) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            mDateDisplayValues[i] = (String) DateFormat.format("dd日", cal);
//            mDateDisplayValues[i] = (String) DateFormat.format("dd日  EEEE", cal);
        }
        mDaySpinner.setDisplayedValues(mDateDisplayValues);
        mDaySpinner.setValue(7 / 2);
        mDaySpinner.invalidate();
    }

    private void updateAPControl() {
        mAPDisplayValues[0] = "上午";
        mAPDisplayValues[1] = "下午";
//        mAPSpinner.setDisplayedValues(null);
        mAPSpinner.setDisplayedValues(mAPDisplayValues);
        mAPSpinner.setValue(map + 1);
        mAPSpinner.invalidate();
    }

    public interface OnDateTimeChangedListener {
        void onDateTimeChanged(DateTimeYearPicker view, int year, int month, int day, int map);
    }

    public void setOnDateTimeChangedListener(OnDateTimeChangedListener callback) {
        mOnDateTimeChangedListener = callback;
    }

    private void onDateTimeChanged() {
        if (mOnDateTimeChangedListener != null) {
            mOnDateTimeChangedListener.onDateTimeChanged(this, mYear,
                    mMonth, mDate.get(Calendar.DAY_OF_MONTH), map);
        }
    }

    /**
     * 通过反射修改分割线高度
     * @param numberPicker
     */
    private void setNumberPickerDividerHeight(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDividerHeight")) {
                pf.setAccessible(true);
                try {
                    pf.set(picker, 0);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * 通过反射修改分割线颜色
     * @param numberPicker
     */
    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                //    pf.set(picker, new ColorDrawable(this.getResources().getColor(R.color.green)));
                    pf.set(picker, new ColorDrawable(this.getResources().getColor(R.color.white)));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * 通过反射修改字体颜色
     * @param numberPicker
     * @param color
     * @return
     */
    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                Field selectorWheelPaintField;
                try {
                    selectorWheelPaintField = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    try {
                        ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
