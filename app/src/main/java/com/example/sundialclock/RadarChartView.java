package com.example.sundialclock;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class RadarChartView extends View {

    private int center;//中心点
    private int defalutSize = 300;//默认大小

    //多边形半径
    private float r1;//最外层多边形半径
    private float r2;
    private float r3;
    private float r4;
    private float r5;

    //分割线画笔
    private Paint centerLinePaint;
    //绘制多边形的画笔
    private Paint polygonPaint;

    //多边形每一条先的起始坐标和终点坐标
    private float endX, endY;
    double hudu = Math.PI / 3;

    private int JiSha = 20, ZhuGong = 20, Wuli = 20, MoFa = 20, FangYu = 20, JinQian = 20;
    private int[] ranks = new int[6];//0-100 对应 0 - r1

    private String[] strs = {"击杀", "助攻", "物理", "魔法", "防御", "金钱"};
    private Rect str_rect;
    private Paint strPaint;

    private float rankRadius = 0;

    public RadarChartView(Context context) {
        this(context, null);
    }

    public RadarChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defalutSize = dp_px(defalutSize);

        initPaint();
    }

    private void initPaint() {
        centerLinePaint = new Paint();
        centerLinePaint.setAntiAlias(true);
        centerLinePaint.setColor(Color.WHITE);

        polygonPaint = new Paint();
        polygonPaint.setAntiAlias(true);
        polygonPaint.setStyle(Paint.Style.FILL);

        strPaint = new Paint();
        strPaint.setAntiAlias(true);
        strPaint.setColor(Color.BLACK);
        strPaint.setTextSize(dp_px(16));
        //获取字体矩阵
        str_rect = new Rect();
        //设置字体矩阵
        strPaint.getTextBounds(strs[0], 0, strs[0].length(), str_rect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ranks[0] = JiSha;
        ranks[1] = ZhuGong;
        ranks[2] = Wuli;
        ranks[3] = MoFa;
        ranks[4] = FangYu;
        ranks[5] = JinQian;
        polygonPaint.setColor(Color.parseColor("#c3e3e5"));
        drawPolygon(canvas, r1);
        polygonPaint.setColor(Color.parseColor("#85cdd4"));
        drawPolygon(canvas, r2);
        polygonPaint.setColor(Color.parseColor("#48afb6"));
        drawPolygon(canvas, r3);
        polygonPaint.setColor(Color.parseColor("#22737b"));
        drawPolygon(canvas, r4);
        polygonPaint.setColor(Color.parseColor("#00a3ea"));
        drawPolygon(canvas, r5);
        drawCenterLine(canvas);
        drawTitle(canvas);
        polygonPaint.setColor(Color.RED);
        polygonPaint.setStyle(Paint.Style.STROKE);
        polygonPaint.setStrokeWidth(6);
        drawRank(canvas);
    }

    //绘制多边形的中心线
    private void drawCenterLine(Canvas canvas) {
        canvas.save();
        canvas.rotate(0, center, center);

        float endY = getPaddingTop() + str_rect.height() * 2;

        float degree = 60;
        for (int i = 0; i < 6; i++) {
            canvas.drawLine(center, center, center, endY, centerLinePaint);
            canvas.rotate(degree, center, center);
        }
        canvas.restore();
    }

    //绘制多边形
    private void drawPolygon(Canvas canvas, float radius) {
//        canvas.save();
//        canvas.rotate(0, center, center);
//        startY = center - radius;
//        endX = (float) (center + radius * Math.cos(hudu));
//        endY = (float) (center - radius * Math.sin(hudu));
//        Log.i("LHD", "endX:  " + endX + "  endY:  " + endY);
//        for (int i = 0; i < 6; i++) {
//            canvas.drawLine(startX, startY, endX, endY, polygonPaint);
//            canvas.rotate(60, center, center);
//        }
//        canvas.restore();

        Path path = new Path();
        path.moveTo(center, center - radius);
        for (int i = 0; i < 6; i++) {
            endX = (float) (center + radius * Math.sin(hudu * i));
            endY = (float) (center - radius * Math.cos(hudu * i));
            path.lineTo(endX, endY);
        }
        path.close();
        canvas.drawPath(path, polygonPaint);

    }

    //绘制标题
    private void drawTitle(Canvas canvas) {
        canvas.drawText(strs[0], center - str_rect.width() / 2, (float) (getPaddingTop() + str_rect.height()), strPaint);

        canvas.drawText(strs[1], (float) ((center + r1 * Math.sin(hudu)) + str_rect.width() / 2), (float) (center - r1 * Math.cos(hudu)), strPaint);
        canvas.drawText(strs[2], (float) ((center + r1 * Math.sin(hudu * 2)) + str_rect.width() / 2), (float) (center - r1 * Math.cos(hudu * 2)), strPaint);

        canvas.drawText(strs[3], center - str_rect.width() / 2, (float) center + getPaddingTop() + r1 + str_rect.height(), strPaint);

        canvas.drawText(strs[4], (float) ((center + r1 * Math.sin(hudu * 4)) - str_rect.width() * 1.5), (float) (center - r1 * Math.cos(hudu * 4)), strPaint);
        canvas.drawText(strs[5], (float) ((center + r1 * Math.sin(hudu * 5)) - str_rect.width() * 1.5), (float) (center - r1 * Math.cos(hudu * 5)), strPaint);
    }

    //绘制等级
    private void drawRank(Canvas canvas) {
        Path path = new Path();

        path.moveTo(center, center - r1 * ranks[0] / 100);
        for (int i = 0; i < 6; i++) {
            rankRadius = r1 * ranks[i] / 100;
            endX = (float) (center + rankRadius * Math.sin(hudu * i));
            endY = (float) (center - rankRadius * Math.cos(hudu * i));
            path.lineTo(endX, endY);
        }
        path.close();
        canvas.drawPath(path, polygonPaint);
    }

    public void setJiSha(int jiSha) {
        JiSha = jiSha;
        invalidate();
    }

    public void setZhuGong(int zhuGong) {
        ZhuGong = zhuGong;
        invalidate();
    }

    public void setWuli(int wuli) {
        Wuli = wuli;
        invalidate();
    }

    public void setMoFa(int moFa) {
        MoFa = moFa;
        invalidate();
    }

    public void setFangYu(int fangYu) {
        FangYu = fangYu;
        invalidate();
    }

    public void setJinQian(int jinQian) {
        JinQian = jinQian;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int withMode = MeasureSpec.getMode(widthMeasureSpec);
        int withSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getSize(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;
        if (withMode == MeasureSpec.EXACTLY) {
            width = withSize;
        } else {
            width = Math.min(withSize, defalutSize);//最小为300
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = Math.min(heightSize, defalutSize);
        }

        center = width / 2;

        r1 = center - getPaddingTop() - str_rect.height() * 2;
        r2 = r1 * 4 / 5;
        r3 = r1 * 3 / 5;
        r4 = r1 * 2 / 5;
        r5 = r1 * 1 / 5;

        setMeasuredDimension(width, height);


    }

    /**
     * dp转px
     *
     * @param values
     * @return
     */
    public int dp_px(int values) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }


}
