package com.example.sundialclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SundialView extends View {

    private int startAngle = 90;//绘制的起始角度

    private final String[] words = {
            "一", "二", "三", "四", "五",
            "六", "七", "八", "九", "十",
    };

    private String[] secondsArray = new String[60];
    private String[] minitessArray = new String[60];
    private String[] hoursArray = new String[12];
    private String[] daysArray = new String[30];
    private String[] weekArray = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private String[] monthArray = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

    private int height = 100;
    private int with = 100;
    private int centerX = 50;
    private int centerY = 50;
    private int radius = 50;

    private int month = 1;
    private int day = 1;
    private int week = 1;
    private int hour = 1;
    private int minute = 1;
    private int second = 1;

    private String strMonth = "一月";
    private String strDay = "五日";
    private String strWeek = "星期四";
    private String strHour = "十时";
    private String strMinute = "五分";
    private String strSecond = "三十三秒";

    private Paint paint;

    private Paint paintText;

    private int weekDegree = 360 / 7;


    private int offsetMonthX = 60;
    private int offsetDayX = 140;
    private int offsetWeekX = 240;
    private int offsetHourX = 320;
    private int offsetMinuteX = 380;
    private int offsetSecondX = 480;

    private int degreeDivideSixty = 6;
    private int degreeDivideTwelve = 30;


    public SundialView(Context context) {
        this(context, null);
    }

    public SundialView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SundialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(25);


        paintText = new Paint();
        paintText.setColor(Color.BLACK);
        paintText.setAntiAlias(true);
        paintText.setStrokeWidth(1);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextSize(25);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        height = getMeasuredHeight();
        with = getMeasuredWidth();

        int d = Math.min(height, with);
        radius = d / 2;

        centerX = with / 2;
        centerY = height / 2;

        Log.i("LHD", "getMeasuredHeight = " + getMeasuredHeight() + "  height = " + getHeight() + "  r = " + radius);


        caculTime();

    }

    private void caculTime() {
        //获取系统的日期
        Calendar calendar = Calendar.getInstance();
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        month = calendar.get(Calendar.MONTH) + 1;

        //日
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //星期
        week = calendar.get(Calendar.DAY_OF_WEEK);
        //获取系统时间
        //小时
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分钟
        minute = calendar.get(Calendar.MINUTE);
        //秒
        second = calendar.get(Calendar.SECOND);

        Log.i("LHD", "month = " + month + " week = " + week + " day = " + day + " hour = " + hour + " min = " + minute + " sec = " + second);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        String weekStr = "";
        /*星期日:Calendar.SUNDAY=1
         *星期一:Calendar.MONDAY=2
         *星期二:Calendar.TUESDAY=3
         *星期三:Calendar.WEDNESDAY=4
         *星期四:Calendar.THURSDAY=5
         *星期五:Calendar.FRIDAY=6
         *星期六:Calendar.SATURDAY=7 */
        switch (week) {
            case 1:
                weekStr = "周日";
                break;
            case 2:
                weekStr = "周一";
                break;
            case 3:
                weekStr = "周二";
                break;
            case 4:
                weekStr = "周三";
                break;
            case 5:
                weekStr = "周四";
                break;
            case 6:
                weekStr = "周五";
                break;
            case 7:
                weekStr = "周六";
                break;
            default:
                break;
        }

        strMonth = getStrFromNum(month, "月");
        strDay = getStrFromNum(day, "日");
        strHour = getStrFromNum(hour, "时");
        strMinute = getStrFromNum(minute, "分");
        strSecond = getStrFromNum(second, "秒");

        Log.i("LHD", "获取到的时间 = " + strMonth + "  " + weekStr + "  " + strDay + "  " + strHour + "  " + strMinute + "  " + strSecond);

        getWordsArray();

    }

    private void getWordsArray() {
        for (int i = 0; i < 30; i++) {
            String day = getStrFromNum(i + 1, "日");
            daysArray[i] = day;
        }

        for (int i = 0; i < 12; i++) {
            String hour = getStrFromNum(i + 1, "时");
            hoursArray[i] = hour;
        }

        for (int i = 0; i < 60; i++) {
            String minute = getStrFromNum(i + 1, "分");
            minitessArray[i] = minute;
        }

        for (int i = 0; i < 60; i++) {
            String sec = getStrFromNum(i + 1, "秒");
            secondsArray[i] = sec;
        }

        monthArray = resortWordsArray(monthArray, month);
        weekArray = resortWordsArray(weekArray, week);
        daysArray = resortWordsArray(daysArray, day);
        hoursArray = resortWordsArray(hoursArray, hour);
        minitessArray = resortWordsArray(minitessArray, minute);
        secondsArray = resortWordsArray(secondsArray, second);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawMonth(canvas);
        drawDay(canvas);
        drawWeek(canvas);
        drawHour(canvas);
        drawMinute(canvas);
        drawSecond(canvas);

    }

    private void drawSecond(Canvas canvas) {
        canvas.save();
        for (int i = 0; i < secondsArray.length; i++) {
            Log.i("LHD", "secondsArray = " + secondsArray[i]);
            canvas.drawText(secondsArray[i], centerX + offsetSecondX, centerY, paintText);
            canvas.rotate(degreeDivideSixty, centerX, centerY);
        }
        canvas.restore();
    }

    private void drawMinute(Canvas canvas) {
        canvas.save();
        for (int i = 0; i < minitessArray.length; i++) {
            Log.i("LHD", "hours = " + minitessArray[i]);
            canvas.drawText(minitessArray[i], centerX + offsetMinuteX, centerY, paintText);
            canvas.rotate(degreeDivideSixty, centerX, centerY);
        }
        canvas.restore();
    }

    private void drawHour(Canvas canvas) {
        canvas.save();
        for (int i = 0; i < hoursArray.length; i++) {
            Log.i("LHD", "hours = " + hoursArray[i]);
            canvas.drawText(hoursArray[i], centerX + offsetHourX, centerY, paintText);
            canvas.rotate(degreeDivideTwelve, centerX, centerY);
        }
        canvas.restore();
    }

    private void drawWeek(Canvas canvas) {
        canvas.save();
        for (int i = 0; i < weekArray.length; i++) {
            Log.i("LHD", "week = " + weekArray[i]);
            canvas.drawText(weekArray[i], centerX + offsetWeekX, centerY, paintText);
            canvas.rotate(weekDegree, centerX, centerY);
        }
        canvas.restore();
    }

    private void drawDay(Canvas canvas) {
        canvas.save();
        for (int i = 0; i < daysArray.length; i++) {
            Log.i("LHD", "days = " + daysArray[i]);
            canvas.drawText(daysArray[i], centerX + offsetDayX, centerY, paintText);
            canvas.rotate(12, centerX, centerY);
        }
        canvas.restore();
    }

    private void drawMonth(Canvas canvas) {
        canvas.save();
        canvas.drawCircle(with / 2, height / 2, radius, paint);
        for (int i = 0; i < 12; i++) {
            Log.i("LHD", "draw = " + monthArray[i]);
            canvas.drawText(monthArray[i], centerX + offsetMonthX, centerY, paintText);
            canvas.rotate(degreeDivideTwelve, centerX, centerY);
        }
        canvas.restore();
    }

    private String getStrFromNum(int i, String type) {
        int shang = i / 10;
        int yu = i % 10;

        if (shang <= 0) {
            return numToString(i) + type;
        } else {
            return numToString(shang) + "十" + numToString(yu) + type;
        }

    }

    private String numToString(int i) {
        switch (i) {
            case 0:
                return "";
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            case 7:
                return "七";
            case 8:
                return "八";
            case 9:
                return "九";
            case 10:
                return "十";
            default:
                return "";
        }

    }


    public void start() {
        Observable.interval(1, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.i("LHD", "start = " + aLong);
                        caculTime();
                        invalidate();
                    }
                });
    }


    //根据当前日期对各个数组重新排列
    private String[] resortWordsArray(String[] value, int curValue) {

        List<String> listMin = new ArrayList<>();
        List<String> listMax = new ArrayList<>();

        for (int i = 0; i < value.length; i++) {
            if (i <= curValue) {
                listMin.add(value[i]);
            } else {
                listMax.add(value[i]);
            }
        }

        String[] result = new String[value.length];

        for (int i = 0; i < listMax.size(); i++) {
            Log.i("LHD", "添加数据 大 = " + listMax.get(i));
            result[i] = listMax.get(i);
        }

        for (int i = 0; i < listMin.size(); i++) {
            Log.i("LHD", "添加数据 小 = " + listMin.get(i));
            result[listMax.size() + i] = listMin.get(i);
        }
        Log.i("LHD", "重新排序后的数组 = " + result + "  " + result.length);
        return result;

    }


}
