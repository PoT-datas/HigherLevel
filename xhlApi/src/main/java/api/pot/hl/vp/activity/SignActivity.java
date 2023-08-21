package api.pot.hl.vp.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import api.pot.gl.xiv.XImageView;
import api.pot.gl.xiv.tools.Forgrounder;
import api.pot.gl.xiv.tools.XivCallback;
import api.pot.hl.R;
import api.pot.hl.xiv.GeoForm;
import api.pot.system.XCast;
import api.pot.system.XtatutBar;

public class SignActivity extends AppCompatActivity {

    XImageView done, clear;
    XImageView printer;
    ImageView printerBg;
    XImageView more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        XtatutBar.transparentStatutBar(this);

        clear = findViewById(R.id.clear);
        done = findViewById(R.id.done);

        printer = findViewById(R.id.printer);
        printerBg = findViewById(R.id.printerBg);

        clear.setOnFgClickListener(new Forgrounder.OnClickListener() {
            @Override
            public void onClick(View view) {
                geoForm.clear();
                printer.invalidater();
            }
        });

        done.setOnFgClickListener(new Forgrounder.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initPrinter();
    }

    GeoForm geoForm;
    private void initPrinter() {
        geoForm = new GeoForm();
        geoForm.setStyle(GeoForm.Style.PAINT);
        ///----
        geoForm.setStrockStyle(Paint.Style.STROKE);
        geoForm.setBorderColor(Color.WHITE);
        geoForm.setBorderWidth(XCast.dt2px(getApplicationContext(), 5));
        geoForm.setSoftEdge(XCast.dt2px(getApplicationContext(), 1)/2);
        ///----
        printer.setListener(new XivCallback(){
            @Override
            public void onDrawContent(Canvas canvas) {
                super.onDrawContent(canvas);
                ///
                if(geoForm.bound==null)
                    geoForm.setBound(new RectF(0,0,canvas.getWidth(), canvas.getHeight()));
                ///
                geoForm.onDraw(canvas);
                ///---canvas.drawColor(Color.RED);
            }
        });
        ///----
        printer.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    return false;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return super.onSingleTapUp(e);
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    double h = Math.sqrt(Math.pow(Math.abs(distanceX), 2) + Math.pow(Math.abs(distanceY), 2));
                    if(distanceX<0 && distanceY>0) h=-h;

                    if(e2.getPointerCount()==2){
                        ///
                    }else {
                        if(geoForm!=null){
                            geoForm.onScroll(e2);
                            printer.invalidater();
                        }
                    }
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return super.onFling(e1, e2, velocityX, velocityY);
                }

                @Override
                public boolean onDown(MotionEvent e) {
                    if(geoForm!=null){
                        geoForm.onDown(e);
                        printer.invalidater();
                        ////Log.i(context, geoForm.paths.size()+"|");
                        ///valueParamAdapter.valueParamListener.onPrinterDown(null);
                    }
                    //
                    return super.onDown(e);
                }
            });

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                //mDetector.onTouchEvent(motionEvent);
                if(motionEvent.getAction()==MotionEvent.ACTION_UP);
                    ///valueParamAdapter.valueParamListener.onPrinterUp(null);
                return true;
            }
        });
    }
}
