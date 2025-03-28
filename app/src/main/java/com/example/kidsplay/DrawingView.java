package com.example.kidsplay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;

public class DrawingView extends View {
    private Paint currentPaint;
    private Path currentPath;

    private ArrayList<PathPaintPair> paths = new ArrayList<>();
    private ArrayList<PathPaintPair> undonePaths = new ArrayList<>();

    private int brushSize = 8; // Default brush size
    private int brushColor = Color.BLACK; // Default color

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        currentPaint = createNewPaint(brushColor, brushSize);
        currentPath = new Path();
    }

    private Paint createNewPaint(int color, float strokeWidth) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (PathPaintPair pair : paths) {
            canvas.drawPath(pair.path, pair.paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentPath = new Path();
                currentPath.moveTo(x, y);

                // Create a new paint instance for each stroke
                Paint newPaint = createNewPaint(brushColor, brushSize);
                paths.add(new PathPaintPair(currentPath, newPaint));

                undonePaths.clear();
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                currentPath.lineTo(x, y);
                invalidate();
                break;
        }
        return true;
    }

    public void setBrushSize(int size) {
        brushSize = size;
    }

    public void setBrushColor(int color) {
        brushColor = color;
    }

    public void clearDrawing() {
        paths.clear();
        undonePaths.clear();
        invalidate();
    }

    public void undo() {
        if (!paths.isEmpty()) {
            undonePaths.add(paths.remove(paths.size() - 1));
            invalidate();
        }
    }

    public void redo() {
        if (!undonePaths.isEmpty()) {
            paths.add(undonePaths.remove(undonePaths.size() - 1));
            invalidate();
        }
    }

    private static class PathPaintPair {
        Path path;
        Paint paint;

        PathPaintPair(Path path, Paint paint) {
            this.path = path;
            this.paint = paint;
        }
    }
}
