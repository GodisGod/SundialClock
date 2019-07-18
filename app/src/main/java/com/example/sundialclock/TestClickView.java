package com.example.sundialclock;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TestClickView extends View {

    private Paint paint;

    public TestClickView(Context context) {
        this(context, null);
    }

    public TestClickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();

        //去锯齿
        paint.setAntiAlias(true);

        paint.setColor(Color.BLUE);

        paint.setStyle(Paint.Style.FILL);

        paint.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path3 = new Path();

        path3.moveTo(90, 340);

        path3.lineTo(350, 340);

        path3.lineTo(120, 90);

        path3.close();

        //绘制三角形

        canvas.drawPath(path3, paint);
    }

    public boolean isInTriangle(Point A, Point B, Point C, Point point) {
        double ABC = triAngleArea(A, B, C);
        double ABp = triAngleArea(A, B, point);
        double ACp = triAngleArea(A, C, point);
        double BCp = triAngleArea(B, C, point);
        if ((int) ABC == (int) (ABp + ACp + BCp)) {// 若面积之和等于原三角形面积，证明点在三角形内,这里做了一个约等于小数点之后没有算（25714.25390625、25714.255859375）
            return true;
        } else {
            return false;
        }
    }

    private double triAngleArea(Point A, Point B, Point C) {// 由三个点计算这三个点组成三角形面积

        double result = Math.abs((A.x * B.y + B.x * C.y
                + C.x * A.y - B.x * A.y - C.x
                * B.x - A.x * C.y) / 2.0D);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            Point point = new Point();
            point.set(x, y);


            Point A = new Point();
            A.set(90, 340);

            Point B = new Point();
            A.set(350, 340);

            Point C = new Point();
            A.set(120, 90);

            List<Point> points = new ArrayList<>();
            points.add(A);
            points.add(B);
            points.add(C);

            boolean is = PtInPolygon(point, points);
            Log.i("LHD", "是否在点击区域内 = " + is);
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    /**
     * 功能：判断点是否在多边形内 方法：求解通过该点的水平线与多边形各边的交点 结论：单边交点为奇数，成立!
     *
     * @param point   指定的某个点
     * @param APoints 多边形的各个顶点坐标（首末点可以不一致）
     * @return
     */
    public boolean PtInPolygon(Point point, List<Point> APoints) {
        int nCross = 0;
        for (int i = 0; i < APoints.size(); i++) {
            Point p1 = APoints.get(i);
            Point p2 = APoints.get((i + 1) % APoints.size());
            // 求解 y=p.y 与 p1p2 的交点
            if (p1.y == p2.y) // p1p2 与 y=p0.y平行
                continue;
            if (point.y < Math.min(p1.y, p2.y)) // 交点在p1p2延长线上
                continue;
            if (point.y >= Math.max(p1.y, p2.y)) // 交点在p1p2延长线上
                continue;
            // 求交点的 X 坐标
            // --------------------------------------------------------------
            double x = (double) (point.y - p1.y)
                    * (double) (p2.x - p1.x)
                    / (double) (p2.y - p1.y) + p1.x;
            if (x > point.x)
                nCross++; // 只统计单边交点
        }
        // 单边交点为偶数，点在多边形之外 ---
        return (nCross % 2 == 1);
    }


}
