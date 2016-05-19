package com.example.admin.projetointegrado_5semestre;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

public class DrawView extends View {

    Paint paint = new Paint();

    public DrawView(Context context) {
        super(context.getApplicationContext());
        paint.setColor(Color.RED);
        setWillNotDraw(false);
    }

    public DrawView(Context cx, AttributeSet attrs) {
        super(cx.getApplicationContext(), attrs);
        setWillNotDraw(false);
    }

        @Override
    protected void onDraw(Canvas canvas){

            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(10);

            float left = 30;
            float top = 30;
            float right = 100;
            float bottom = 100;

            new Canvas().drawLine(left, top, right, bottom, paint);
            canvas.drawLine(left, top, right, bottom, paint);

    }

}
