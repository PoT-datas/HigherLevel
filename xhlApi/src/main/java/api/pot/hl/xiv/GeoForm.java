package api.pot.hl.xiv;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.MotionEvent;/**

import com.pot.views.civTools.TextPainter;
import com.pot.views.model.Global;*/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeoForm {
    public GeoForm.Style style = Style.RECTANGLE;

    public RectF bound;//w/h/pos
    private Path path = null;
    //
    public int color = Color.WHITE;
    public float borderWidth = 0;
    public int borderColor = Color.BLACK;
    public Paint.Style strockStyle = Paint.Style.FILL;
    public ColorFilter colorFilter = null;
    public float softEdge = 0;
    public Shader shader, borderShader;
    //
    public float radius_x = 0, radius_y = 0;
    public float arrowElevation = 30;
    public int number_arrow = 3;
    public float dialog_angle = 60;
    //
    public int needleColor = Color.RED;
    public long time = System.currentTimeMillis();/**
    private TextPainter.TextDatas textDatas;*/
    //
    public TimeMode timeMode = TimeMode.FRENCH_TIME;

    Paint paint = new Paint(), borderPaint = new Paint();

    private Paint cPaint;
    private boolean isSecDraw = false;

    public List<Path> paths = new ArrayList<Path>();
    private int path_index = -1;

    public GeoForm() {
        setStyle(Style.NONE);
        this.paint = new Paint();
        borderPaint = new Paint();
        //
        this.paint.setStyle(Paint.Style.FILL);
        this.borderPaint.setStyle(Paint.Style.STROKE);/**
        this.textDatas = new TextPainter.TextDatas();
        textDatas.paint = null;
        textDatas.isMonoLine = true;*/
    }

    public GeoForm(GeoForm.Style style) {
        setStyle(style);
        this.paint = new Paint();
        borderPaint = new Paint();
        //
        this.paint.setStyle(Paint.Style.FILL);
        this.borderPaint.setStyle(Paint.Style.STROKE);/***
        this.textDatas = new TextPainter.TextDatas();
        textDatas.paint = null;
        textDatas.isMonoLine = true;*/
    }

    public GeoForm(GeoForm geoForm) {
        this.style = geoForm.style;
        if(geoForm.bound!=null) this.bound = new RectF(geoForm.bound);
        this.path = null;
        this.color = geoForm.color;
        this.borderWidth = geoForm.borderWidth;
        this.borderColor = geoForm.borderColor;
        this.strockStyle = geoForm.strockStyle;
        this.colorFilter = geoForm.colorFilter;
        this.softEdge = geoForm.softEdge;
        this.shader = geoForm.shader;///
        this.borderShader = geoForm.borderShader;///
        this.radius_x = geoForm.radius_x;
        this.radius_y = geoForm.radius_y;
        this.arrowElevation = geoForm.arrowElevation;
        this.number_arrow = geoForm.number_arrow;
        this.dialog_angle = geoForm.dialog_angle;
        this.needleColor = geoForm.needleColor;
        this.time = geoForm.time;
        /***if(geoForm.textDatas!=null) this.textDatas = new TextPainter.TextDatas(geoForm.textDatas);*/
        this.timeMode = geoForm.timeMode;
        if(geoForm.paint!=null) this.paint = new Paint(geoForm.paint);
        if(geoForm.borderPaint!=null) this.borderPaint = new Paint(geoForm.borderPaint);
        if(geoForm.cPaint!=null) this.cPaint = new Paint(geoForm.cPaint);
        this.isSecDraw = geoForm.isSecDraw;
        //
        setPaths(geoForm.paths);
        this.path_index = geoForm.path_index;
    }

    ///{
    private void setPaths(List<Path> paths) {
        if(paths!=null && paths.size()>0){
            this.paths.addAll(paths);
        }
    }

    public void setTimeMode(TimeMode timeMode) {
        this.timeMode = timeMode;
    }

    public void setCornerRadius(float rx, float ry) {
        this.radius_x = rx;
        this.radius_y = ry;
        path = null;
    }

    public void setRadiusX(float rx) {
        setCornerRadius(rx, this.radius_y);
    }

    public void setRadiusY(float ry) {
        setCornerRadius(this.radius_x, ry);
    }

    public void setDialogAngle(float dialog_angle) {
        this.dialog_angle = dialog_angle;
        path = null;
    }

    public void setBound(RectF bound) {
        this.bound = bound;
        //this.bound = new RectF(bound.left+bound.width()/3, bound.top, bound.right, bound.bottom);
        path = null;
    }

    public void setStyle(Style style) {
        this.style = style;
        if(style== Style.LANCE || style== Style.ARROW || style== Style.ARROW_CHAIN)
            setStrockStyle(Paint.Style.STROKE);
        setSoftEdge(this.softEdge);
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }

    public void setShader(Shader shader) {
        if(shader==null) return;
        this.shader = shader;
        paint.setShader(shader);
        //borderPaint.setShader(shader);
    }

    public void initShader() {
        this.shader = null;
        paint.setShader(shader);
        //borderPaint.setShader(shader);
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        borderPaint.setStrokeWidth(borderWidth);
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        borderPaint.setColor(borderColor);
    }

    public void setStrockStyle(Paint.Style strockStyle) {
        this.strockStyle = strockStyle;
        if(strockStyle!= Paint.Style.STROKE && (style== Style.LANCE || style== Style.ARROW || style== Style.ARROW_CHAIN))
            this.strockStyle = Paint.Style.STROKE;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.colorFilter = colorFilter;
        this.paint.setColorFilter(colorFilter);
        this.borderPaint.setColorFilter(colorFilter);
    }

    public void setSoftEdge(float softEdge) {
        this.softEdge = softEdge;
        if(softEdge>0) {
            paint.setMaskFilter(new BlurMaskFilter(softEdge, BlurMaskFilter.Blur.NORMAL));
            borderPaint.setMaskFilter(new BlurMaskFilter(softEdge, BlurMaskFilter.Blur.NORMAL));
        }else {
            paint.setMaskFilter(null);
            borderPaint.setMaskFilter(null);
        }
    }

    public void onDraw(Canvas canvas, Paint paint){
        if(bound==null || bound.width()<=0 || bound.height()<=0) return;
        //
        setColorFilter(paint.getColorFilter());
        //
        onDraw(canvas);
    }

    public void onScroll(MotionEvent e) {
        try {
            paths.get(path_index).lineTo(e.getX(), e.getY());
        }catch (Exception ex){
            onDown(e);
        }
    }

    public void onDown(MotionEvent e) {
        Path p = new Path();
        p.moveTo(e.getX(), e.getY());
        paths.add(p);
        path_index++;
    }

    public void clear() {
        paths.clear();
        path_index = -1;
    }
    ///}

    public enum TimeMode {
        ENGLAND_TIME, FRENCH_TIME;
    }

    public enum Style {
        RECTANGLE, ELIPSE, TRIANGLE, LANCE, ARROW, ARROW_CHAIN, STAR, RECT_DIALOG, ELIPSE_DIALOG, CARTOON_DIALOG, WATCH, NUMERIC_WATCH, DATE, PAINT, NONE;
    }

    public void onDraw(Canvas canvas){
        if(strockStyle == Paint.Style.FILL || strockStyle == Paint.Style.STROKE){
            isSecDraw = true;
            if(strockStyle == Paint.Style.FILL) cPaint = paint;
            if(strockStyle == Paint.Style.STROKE) cPaint = borderPaint;
        }else if(strockStyle == Paint.Style.FILL_AND_STROKE){
            if(!isSecDraw) cPaint = paint;
            else cPaint = borderPaint;
        }
        //
        switch (style){
            case RECTANGLE:
                onDrawRectangle(canvas, cPaint);
                break;
            case ELIPSE:
                onDrawElipse(canvas, cPaint);
                break;
            case TRIANGLE:
                onDrawTriangle(canvas, cPaint);
                break;
            case LANCE:
                onDrawLance(canvas, cPaint);
                break;
            case ARROW:
                onDrawArrow(canvas, cPaint);
                break;
            case ARROW_CHAIN:
                onDrawArrowChain(canvas, cPaint);
                break;
            case STAR:
                onDrawStar(canvas, cPaint);
                break;
            case RECT_DIALOG:
                onDrawRectDialog(canvas, cPaint);
                break;
            case ELIPSE_DIALOG:
                onDrawElipseDialog(canvas, cPaint);
                break;
            case CARTOON_DIALOG:
                onDrawCartoonDialog(canvas, cPaint);
                break;
            case WATCH:
                onDrawWatch(canvas, cPaint);
                if(!isSecDraw) onDrawWatchContent(canvas);
                break;
            case NUMERIC_WATCH:
                onDrawNumericWatch(canvas, cPaint);
                if(!isSecDraw) onDrawNumericWatchContent(canvas);
                break;
            case DATE:
                if(isSecDraw) {
                    onDrawDateContent(canvas);
                    if(borderWidth>0) onDrawDate(canvas, cPaint);
                }
                break;
            case PAINT:
                if(isSecDraw)
                    onDrawPaint(canvas, cPaint);
                break;
        }
        //
        if(!isSecDraw) {
            isSecDraw = true;
            onDraw(canvas);
        }else isSecDraw = false;
    }

    private void onDrawPaint(Canvas canvas, Paint paint) {
        Path p = new Path();
        for(Path np : paths){
            if(np!=null) p.addPath(np);
        }
        canvas.drawPath(p, paint);
    }

    private void onDrawDate(Canvas canvas, Paint paint) {
        canvas.drawRoundRect(bound, radius_x, radius_y>(bound.height()/3)?(bound.height()/3):radius_y, paint);
    }

    private void onDrawDateContent(Canvas canvas) {
        /***Date date = new Date(time);
        //
        Paint p = new Paint();
        p.setColorFilter(this.colorFilter);
        textDatas.colorFilter = this.colorFilter;
        //
        float rx = radius_x, ry = radius_y>(bound.height()/3)?(bound.height()/3):radius_y;
        //
        Path p1 = new Path(), p2 = new Path();
        //
        p1.moveTo(bound.left, bound.top+bound.height()/3);
        p1.rLineTo(0, ry-bound.height()/3);
        p1.quadTo(bound.left, bound.top, bound.left+rx, bound.top);
        p1.rLineTo(bound.width()-2*rx, 0);
        p1.quadTo(bound.right, bound.top, bound.right, bound.top+ry);
        p1.lineTo(bound.right, bound.top+bound.height()/3);
        p1.close();
        //
        p.setColor(Color.argb((Color.alpha(color)+Color.alpha(borderColor))/2, (Color.red(color)+Color.red(borderColor))/2,
        (Color.green(color)+Color.green(borderColor))/2, (Color.blue(color)+Color.blue(borderColor))/2));
        p.setStyle(Paint.Style.FILL);
        //
        canvas.drawPath(p1, p);
        //
        p2.moveTo(bound.left, bound.top+bound.height()/3);
        p2.rLineTo(0, (2*bound.height()/3)-ry);
        p2.quadTo(bound.left, bound.bottom, bound.left+rx, bound.bottom);
        p2.rLineTo(bound.width()-2*rx, 0);
        p2.quadTo(bound.right, bound.bottom, bound.right, bound.bottom-ry);
        p2.lineTo(bound.right, bound.bottom-2*bound.height()/3);
        p2.close();
        //
        canvas.drawPath(p2, paint);
        //
        p.setColor(Color.GRAY);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(Math.max(borderWidth/5, 1));
        //
        float b_width = borderWidth<=0 ? (bound.width()/40) : borderWidth + (bound.width()/40);
        canvas.drawRoundRect(new RectF(bound.left+b_width, bound.top+b_width, bound.right-b_width, bound.bottom-b_width), rx-b_width, ry-b_width, p);
        //
        RectF b = new RectF(bound.left+b_width, bound.top+b_width*2, bound.right-b_width*2, bound.bottom-2*bound.height()/3-b_width*2);
        textDatas.text = Global.DateDatas.getMonth(date, Global.Language.FRENCH);
        textDatas.text = textDatas.text.toUpperCase().substring(0, 1) + textDatas.text.toLowerCase().substring(1);
        textDatas.bounds = b;
        TextPainter.drawTextCentered(canvas, textDatas);
        //
        b = new RectF(bound.left+b_width*2, bound.top+bound.height()/3+b_width*2, bound.right-b_width, bound.bottom-bound.height()/2-b_width);
        textDatas.text = Global.DateDatas.getYear(date, Global.Language.FRENCH);
        textDatas.bounds = b;
        textDatas.alpha = 100;
        TextPainter.drawTextCentered(canvas, textDatas);
        textDatas.alpha = 255;
        //
        b = new RectF(bound.left+b_width*2, bound.top+bound.height()/2+b_width, bound.right-bound.width()/2-b_width, bound.bottom-b_width);
        textDatas.text = Global.DateDatas.getDay(date, Global.Language.FRENCH);
        textDatas.bounds = b;
        TextPainter.drawTextCentered(canvas, textDatas);
        //
        b = new RectF(textDatas.bounds.right + b_width*5, bound.top+bound.height()/2+b_width, (float) (bound.right -rx*Math.cos(Math.toRadians(45)) - b_width), (float) (bound.bottom -ry*Math.cos(Math.toRadians(45)) - b_width));
        b = new RectF(b.centerX() - Math.min(b.width(), b.height())/2 + b_width/2, b.centerY() - Math.min(b.width(), b.height())/2 + b_width/2,
                b.centerX() + Math.min(b.width(), b.height())/2 - b_width/2, b.centerY() + Math.min(b.width(), b.height())/2 - b_width/2);
        Point c;
        float delta = b.height()/2;
        p.setColor(Color.GRAY);
        p.setAlpha(150);
        p.setStyle(Paint.Style.FILL);
        //
        for(int i=0;i<9;i++){
            c = new Point(b.left + delta*(i%3), b.top + delta*(i/3));
            canvas.drawRect(new RectF(c.x-b_width, c.y-b_width, c.x+b_width, c.y+b_width), p);
        }*/
    }

    private void onDrawNumericWatch(Canvas canvas, Paint paint) {
        canvas.drawRoundRect(bound, Math.min(bound.width(), bound.height())/2, Math.min(bound.width(), bound.height())/2, paint);
    }

    private void onDrawNumericWatchContent(Canvas canvas) {
        /**Date date = new Date(time);
        //
        textDatas.colorFilter = this.colorFilter;
        //
        long hour = (timeMode== TimeMode.FRENCH_TIME ? date.getHours() : date.getHours()==12?12:date.getHours()%12);
        textDatas.text = (hour<10?"0"+hour:hour)+":"+(date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes());
        float radius = Math.min(bound.width(), bound.height())/2;
        float w = (float) (bound.width() - radius*2 + (Math.min(bound.width(), bound.height())/Math.max(bound.width(), bound.height()))*2*radius*Math.cos(Math.toRadians(45))), h = bound.height();
        textDatas.bounds = new RectF(bound.centerX()-w/2, bound.centerY()-h/2, bound.centerX()+w/2, bound.centerY()+h/2);
        TextPainter.drawTextCentered(canvas, textDatas);
        //
        if(timeMode== TimeMode.ENGLAND_TIME){
            textDatas.text = date.getHours()>=12?"pm":"am";
            textDatas.bounds = new RectF((float) (bound.left+3*radius*Math.cos(Math.toRadians(45))/2), (float) (bound.centerY()+radius*Math.cos(Math.toRadians(45))/4),
                    (float) (bound.left+3*radius*Math.cos(Math.toRadians(45))/2 + 2*radius/3), (float) (bound.centerY()+radius*Math.cos(Math.toRadians(45))/4) + 2*radius/3);
            TextPainter.drawTextCentered(canvas, textDatas);
        }*/
    }

    private void onDrawWatch(Canvas canvas, Paint paint) {
        canvas.drawCircle(bound.centerX(), bound.centerY(), Math.min(bound.width(), bound.height())/2, paint);
    }

    private void onDrawWatchContent(Canvas canvas) {
        /**textDatas.colorFilter = this.colorFilter;
        //
        float radius = Math.min(bound.width(), bound.height())/2;
        float textWidth = radius/4;
        float textRadius = radius-3*textWidth/4-borderWidth;
        float angle = 0;
        Point c;//centre
        RectF b;
        for(int i=1;i<=12;i++){
            angle = -90 + i*30;
            c = new Point(bound.centerX() + textRadius*Math.cos(Math.toRadians(angle)), bound.centerY() + textRadius*Math.sin(Math.toRadians(angle)));
            b = new RectF(c.x-textWidth/2, c.y-textWidth/2, c.x+textWidth/2, c.y+textWidth/2);
            textDatas.text = i+"";
            textDatas.bounds = b;
            TextPainter.drawTextCentered(canvas, textDatas);
        }
        Date date = new Date(time);

        borderPaint.setStyle(Paint.Style.FILL);

        borderPaint.setColor(needleColor);

        borderPaint.setStrokeWidth((borderPaint.getStrokeWidth()>0?borderPaint.getStrokeWidth():radius/40)*2);
        angle = -90 + 360 * date.getHours()/12 + 30 * date.getMinutes()/60;

        canvas.save();
        canvas.rotate(angle, bound.centerX(), bound.centerY());
        canvas.drawRoundRect(new RectF(bound.centerX()-(textRadius/4), bound.centerY()-borderPaint.getStrokeWidth()/2,
                        bound.centerX()+(textRadius/2), bound.centerY()+borderPaint.getStrokeWidth()/2),
                borderPaint.getStrokeWidth()/2, borderPaint.getStrokeWidth()/2, borderPaint);
        canvas.restore();

        borderPaint.setStrokeWidth(borderPaint.getStrokeWidth()/2);
        angle = -90 + 360 * date.getMinutes()/60 + 6 * date.getSeconds()/60;

        canvas.save();
        canvas.rotate(angle, bound.centerX(), bound.centerY());
        canvas.drawRoundRect(new RectF(bound.centerX()-(textRadius/3), bound.centerY()-borderPaint.getStrokeWidth()/2,
                        bound.centerX()+(textRadius), bound.centerY()+borderPaint.getStrokeWidth()/2),
                borderPaint.getStrokeWidth()/2, borderPaint.getStrokeWidth()/2, borderPaint);
        canvas.restore();

        canvas.drawCircle(bound.centerX(), bound.centerY(), textWidth/2, borderPaint);

        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);*/
    }

    private void onDrawCartoonDialog(Canvas canvas, Paint paint) {
        if(path==null) {
            path = getCartoonDialogPath(bound, dialog_angle);
        }
        canvas.drawPath(path, paint);
    }

    private void onDrawElipseDialog(Canvas canvas, Paint paint) {
        if(path==null) {
            path = getElipseDialogPath(bound, dialog_angle);
        }
        canvas.drawPath(path, paint);
    }

    private void onDrawRectDialog(Canvas canvas, Paint paint) {
        if(path==null) {
            path = getRectDialogPath(bound, radius_x, radius_y, dialog_angle);
        }
        canvas.drawPath(path, paint);
    }

    private void onDrawStar(Canvas canvas, Paint paint) {
        if(path==null) {
            path = getStarPath(bound);
        }
        canvas.drawPath(path, paint);
    }

    private void onDrawArrowChain(Canvas canvas, Paint paint) {
        if(path==null) {
            path = new Path();
            //
            float arrow_length = (float) Math.pow(Math.pow(bound.left-bound.right, 2) + Math.pow(bound.top-bound.bottom, 2), 1f/2);
            double arrow_angle;
            if(bound.height()!=0)
                arrow_angle = Math.atan(bound.width()/bound.height());
            else arrow_angle = Math.PI/2;
            //
            int space = number_arrow+3;
            //
            for(int i=1;i<=number_arrow;i++) {
                path.moveTo((bound.right-i*arrow_length/space) + (float) (-(arrow_length / 4) * Math.sin(arrow_angle + Math.toRadians(arrowElevation))), (bound.top+i*arrow_length/space) + (float) ((arrow_length / 4) * Math.cos(arrow_angle + Math.toRadians(arrowElevation))));
                path.lineTo((bound.right-i*arrow_length/space), (bound.top+i*arrow_length/space));
                path.rLineTo((float) (-(arrow_length / 4) * Math.sin(arrow_angle - Math.toRadians(arrowElevation))), (float) ((arrow_length / 4) * Math.cos(arrow_angle - Math.toRadians(arrowElevation))));
            }
        }
        canvas.drawPath(path, paint);
    }

    private void onDrawArrow(Canvas canvas, Paint paint) {
        if(path==null) {
            path = new Path();
            //
            double arrow_length = Math.pow(Math.pow(bound.left-bound.right, 2) + Math.pow(bound.top-bound.bottom, 2), 1f/2);
            double arrow_angle;
            if(bound.height()!=0)
                arrow_angle = Math.atan(bound.width()/bound.height());
            else arrow_angle = Math.PI/2;
            //
            path.moveTo(bound.left, bound.bottom);
            path.lineTo(bound.right, bound.top);
            path.moveTo(bound.right + (float) (-(arrow_length/4)*Math.sin(arrow_angle+Math.toRadians(arrowElevation))), bound.top + (float) ((arrow_length/4)*Math.cos(arrow_angle+Math.toRadians(arrowElevation))));
            path.lineTo(bound.right, bound.top);
            path.rLineTo((float) (-(arrow_length/4)*Math.sin(arrow_angle-Math.toRadians(arrowElevation))), (float) ((arrow_length/4)*Math.cos(arrow_angle-Math.toRadians(arrowElevation))));
        }
        canvas.drawPath(path, paint);
    }

    private void onDrawLance(Canvas canvas, Paint paint) {
        if(path==null) {
            path = new Path();
            //
            double arrow_length = Math.pow(Math.pow(bound.left-bound.right, 2) + Math.pow(bound.top-bound.bottom, 2), 1/2);
            //
            path.moveTo(bound.left, bound.bottom);
            path.lineTo(bound.right, bound.top);
            path.rLineTo((float) (-(arrow_length/5)*Math.cos(Math.toRadians(30))), (float) ((arrow_length/5)*Math.sin(Math.toRadians(30))));
            path.lineTo(bound.right, bound.top);
            path.rLineTo((float) (-(arrow_length/5)*Math.sin(Math.toRadians(30))), (float) ((arrow_length/5)*Math.cos(Math.toRadians(30))));
        }
        canvas.drawPath(path, paint);
    }

    private void onDrawTriangle(Canvas canvas, Paint paint) {
        if(path==null) {
            path = new Path();
            path.moveTo((bound.centerX()+bound.left)/2, (bound.top+bound.bottom)/2);
            path.lineTo(bound.left, bound.bottom);
            path.lineTo(bound.right, bound.bottom);
            path.lineTo(bound.centerX(), bound.top);
            path.lineTo((bound.centerX()+bound.left)/2, (bound.top+bound.bottom)/2);
        }
        canvas.drawPath(path, paint);
    }

    private void onDrawElipse(Canvas canvas, Paint paint) {
        canvas.drawOval(bound, paint);
    }

    private void onDrawRectangle(Canvas canvas, Paint paint) {
        canvas.drawRoundRect(bound, radius_x, radius_y, paint);
    }

    private Path getStarPath(RectF bound){
        Point[] points = getPointsForStar(bound);
        if (points.length < 2) {
            return null;
        }
        // path
        Path polyPath = new Path();
        polyPath.moveTo(points[0].x, points[0].y);
        int i, len;
        len = points.length;
        for (i = 0; i < len; i++) {
            polyPath.lineTo(points[i].x, points[i].y);
        }
        //
        return polyPath;
    }

    private Point[] getPointsForStar(RectF bound) {
        //if(psDatas==null){
        float widthRatio = 10.0f/75.0f;
        PentagonStarDatas psDatas = new PentagonStarDatas();
        float radius = bound.width()>bound.height()?bound.height()/2:bound.width()/2;
        if(paint.getStyle()!= Paint.Style.FILL){
            if(0>=paint.getStrokeWidth())paint.setStrokeWidth(radius*widthRatio);
            if(paint.getStrokeWidth()>radius/2)paint.setStrokeWidth(radius/2);
            radius -= 1.5*paint.getStrokeWidth();
        }
        psDatas.init(new Point(bound.centerX(), bound.centerY()), radius);
        //}

        /*psDatas.first
         * n'est pas uiliser en premier, car il y-aura un problem de jointure.
         * du coup on utilisera le point millieu entre le vrai first et le vrai second*/
        Point[] points = new Point[]{
                new Point(psDatas.first.x+Math.sin(Math.toRadians(-18))*psDatas.starBigSide/2, psDatas.first.y+Math.cos(Math.toRadians(-18))*psDatas.starBigSide/2),
                //new Point(psDatas.first.x, psDatas.first.y),
                new Point(psDatas.first.x+Math.sin(Math.toRadians(-18))*psDatas.starBigSide, psDatas.first.y+Math.cos(Math.toRadians(-18))*psDatas.starBigSide),
                new Point(psDatas.first.x+Math.sin(Math.toRadians(-54))*psDatas.bigSide, psDatas.first.y+Math.cos(Math.toRadians(-54))*psDatas.bigSide),
                new Point(psDatas.first.x+Math.sin(Math.toRadians(-18))*(psDatas.starBigSide+psDatas.littleSide), psDatas.first.y+Math.cos(Math.toRadians(-18))*(psDatas.starBigSide+psDatas.littleSide)),
                new Point(psDatas.first.x+Math.sin(Math.toRadians(-18))*(2*psDatas.starBigSide+psDatas.littleSide), psDatas.first.y+Math.cos(Math.toRadians(-18))*(2*psDatas.starBigSide+psDatas.littleSide)),
                //
                new Point(psDatas.first.x+Math.sin(Math.toRadians(0))*(psDatas.bigRadius+psDatas.littleRadius), psDatas.first.y+Math.cos(Math.toRadians(0))*(psDatas.bigRadius+psDatas.littleRadius)),
                //
                new Point(psDatas.first.x+Math.sin(Math.toRadians(18))*(2*psDatas.starBigSide+psDatas.littleSide), psDatas.first.y+Math.cos(Math.toRadians(18))*(2*psDatas.starBigSide+psDatas.littleSide)),
                new Point(psDatas.first.x+Math.sin(Math.toRadians(18))*(psDatas.starBigSide+psDatas.littleSide), psDatas.first.y+Math.cos(Math.toRadians(18))*(psDatas.starBigSide+psDatas.littleSide)),
                new Point(psDatas.first.x+Math.sin(Math.toRadians(54))*psDatas.bigSide, psDatas.first.y+Math.cos(Math.toRadians(54))*psDatas.bigSide),
                new Point(psDatas.first.x+Math.sin(Math.toRadians(18))*psDatas.starBigSide, psDatas.first.y+Math.cos(Math.toRadians(18))*psDatas.starBigSide),
                new Point(psDatas.first.x, psDatas.first.y),
                new Point(psDatas.first.x+Math.sin(Math.toRadians(-18))*psDatas.starBigSide/2, psDatas.first.y+Math.cos(Math.toRadians(-18))*psDatas.starBigSide/2)};

        return points;
    }

    public static class Point extends android.graphics.Point {
        public float x = 0;
        public float y = 0;
        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Point(double x, double y) {
            this.x = (float) x;
            this.y = (float) y;
        }

        public Point() {}
    }

    private class PentagonStarDatas{
        public Point first;
        public float bigRadius, bigSide, littleRadius, littleSide, starBigSide, starHeight;

        public void init(Point central, float input_bigRadius){
            bigRadius = input_bigRadius;
            bigSide = (float) (2*bigRadius*Math.sin(Math.toRadians(36)));
            littleRadius = (float) (bigRadius*Math.cos(Math.toRadians(36)) - bigSide/(2*Math.tan(Math.toRadians(54))));
            littleSide = (float) (2*littleRadius*Math.sin(Math.toRadians(36)));
            starBigSide = (float) (2*littleRadius*Math.cos(Math.toRadians(18)));
            //
            starHeight = (float) Math.sqrt( Math.pow( (starBigSide*2+littleSide), 2) - Math.pow( (bigSide/2), 2) );
            first = new Point(central.x, central.y-starHeight/2+((paint.getStyle()!= Paint.Style.FILL)?paint.getStrokeWidth()/8:0));
        }
    }

    private Path getRectDialogPath(RectF bound, float rx, float ry, float dialog_angle){
        return getRectDialogPath(bound.left, bound.top, bound.right, bound.bottom, rx, ry, dialog_angle);
    }

    private Path getRectDialogPath(
            float left, float top, float right, float bottom, float rx, float ry,
            float dialog_angle
    ){
        Path path = new Path();
        //
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        //
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));
        //
        if(dialog_angle<-90) dialog_angle = -90;
        else if(dialog_angle>90) dialog_angle = 90;
        float dialog_width = width/5, /*ref to diagonal*/dialog_height = height/4, dialog_center_x = (left+width/2) - (dialog_angle/90)*(width/2);

        path.moveTo(right-rx, bottom);

        path.rQuadTo(rx, 0, rx, -ry);//coordo sommet(xo,yo)  coordo autre extremiter(x1,y1)
        path.rLineTo(0, -heightMinusCorners);

        path.rQuadTo(0, -ry, -rx, -ry);
        path.rLineTo(-widthMinusCorners, 0);

        path.rQuadTo(-rx, 0, -rx, ry);
        path.rLineTo(0, heightMinusCorners);

        path.rQuadTo(0, ry, rx, ry);

        path.lineTo(dialog_center_x-dialog_width/2, bottom);
        path.rLineTo(dialog_width/2 + (float) (-dialog_height*Math.sin(Math.toRadians(dialog_angle))), (float) (dialog_height*Math.cos(Math.toRadians(dialog_angle))));
        path.lineTo(dialog_center_x+dialog_width/2, bottom);
        path.lineTo(right-rx, bottom);

        return path;
    }

    private Path getElipseDialogPath(RectF bound, float dialog_angle){
        return getElipseDialogPath(bound.left, bound.top, bound.right, bound.bottom, dialog_angle);
    }

    private Path getElipseDialogPath(float left, float top, float right, float bottom, float dialog_angle){
        Path path = new Path();
        //
        float width = right - left;
        float height = bottom - top;
        //
        if(dialog_angle<-90) dialog_angle = -90;
        else if(dialog_angle>90) dialog_angle = 90;
        float dialogLength = Math.max(width, height)/2 + Math.min(width, height)/4;

        path.addArc(new RectF(left, top, right, bottom), Math.abs(90+dialog_angle) - 15, -Math.abs(90+dialog_angle) -150 -Math.abs(90-dialog_angle));

        /*
        path.addArc(new RectF(left, top, right, bottom), Math.abs(90+dialog_angle) - 15, -Math.abs(90+dialog_angle) + 15);
        path.addArc(new RectF(left, top, right, bottom), 0, -180);
        path.addArc(new RectF(left, top, right, bottom), 180, -Math.abs(90-dialog_angle) + 15);
        //resume to
        path.addArc(new RectF(left, top, right, bottom), Math.abs(90+dialog_angle) - 15, -Math.abs(90+dialog_angle) + 15 -180 -Math.abs(90-dialog_angle) + 15);
        */

        path.lineTo(left+width/2 + (float) (-dialogLength*Math.sin(Math.toRadians(dialog_angle))), top+height/2 + (float) (dialogLength*Math.cos(Math.toRadians(dialog_angle))));

        path.close();

        return path;
    }



    private Path getCartoonDialogPath(RectF bound, float dialog_angle){
        return getCartoonDialogPath(bound.left, bound.top, bound.right, bound.bottom, dialog_angle);
    }

    private Path getCartoonDialogPath(float left, float top, float right, float bottom, float dialog_angle){
        return null;
        /**Path path = new Path(), modelPathDown = new Path(), modelPathUp = new Path();
        //
        float width = right - left;
        float height = bottom - top;
        //
        if(dialog_angle<-90) dialog_angle = -90;
        else if(dialog_angle>90) dialog_angle = 90;
        float littleRadius = 0;

        float random = (float) Global.random(0, 1);

        modelPathUp.addArc(new RectF(left, top, right, bottom), 0, -180);
        modelPathDown.addArc(new RectF(left, top, right, bottom), -180, -180);

        PathMeasure pm = new PathMeasure(modelPathUp, false);
        float coordo[] = {0f, 0f}, coordo2[] = {0f, 0f};

        pm.getPosTan(pm.getLength() * 0, coordo, null);
        pm.getPosTan(pm.getLength() * 1f/5, coordo2, null);
        littleRadius = (float) Math.pow(Math.pow(coordo[0]-coordo2[0] ,2) + Math.pow(coordo[1]-coordo2[1] ,2), 1f/2)/2;
        path.arcTo(new RectF(coordo[0]-littleRadius, coordo[1]-littleRadius, coordo[0]+littleRadius, coordo[1]+littleRadius), 90, -180);
        ///
        path.arcTo(new RectF(coordo[0]-littleRadius, coordo[1]-littleRadius, coordo[0]+littleRadius, coordo[1]+littleRadius), 90 - 180, -30*random);
        path.arcTo(new RectF(coordo[0]-littleRadius, coordo[1]-littleRadius, coordo[0]+littleRadius, coordo[1]+littleRadius), 90 - 180 - 30*random, 30*random);

        float dialogLength1 = Math.min(width, height)/2+(Math.max(width, height)/2-Math.min(width, height)/2)*(Math.abs(dialog_angle)/90) +
                Math.max(width, height)/16 + borderWidth*2 + littleRadius;
        float dialogLength2 = dialogLength1 + Math.max(width, height)/16 + Math.max(width, height)/32 + borderWidth*2;
        Point c1 = new Point(left+width/2 + (float) (-dialogLength1*Math.sin(Math.toRadians(dialog_angle))), top+height/2 + (float) (dialogLength1*Math.cos(Math.toRadians(dialog_angle))));
        Point c2 = new Point(left+width/2 + (float) (-dialogLength2*Math.sin(Math.toRadians(dialog_angle))), top+height/2 + (float) (dialogLength2*Math.cos(Math.toRadians(dialog_angle))));

        for(int i=1;i<=5;i++){
            random = (float) Global.random(0, 1);
            //
            coordo2[0] = coordo[0];
            coordo2[1] = coordo[1];
            pm.getPosTan(pm.getLength() * Float.valueOf(i)/(5), coordo, null);
            littleRadius = (float) Math.pow(Math.pow(coordo[0]-coordo2[0] ,2) + Math.pow(coordo[1]-coordo2[1] ,2), 1f/2) - littleRadius;
            path.arcTo(new RectF(coordo[0]-littleRadius, coordo[1]-littleRadius, coordo[0]+littleRadius, coordo[1]+littleRadius), 90-180*i/5f, -180);
            ///
            path.arcTo(new RectF(coordo[0]-littleRadius, coordo[1]-littleRadius, coordo[0]+littleRadius, coordo[1]+littleRadius), 90-180*i/5f - 180, -30*random);
            path.arcTo(new RectF(coordo[0]-littleRadius, coordo[1]-littleRadius, coordo[0]+littleRadius, coordo[1]+littleRadius), 90-180*i/5f - 180 - 30*random, 30*random);
        }

        pm = new PathMeasure(modelPathDown, false);

        for(int i=1;i<6;i++){
            random = (float) Global.random(0, 1);
            //
            coordo2[0] = coordo[0];
            coordo2[1] = coordo[1];
            pm.getPosTan(pm.getLength() * Float.valueOf(i)/6, coordo, null);
            littleRadius = (float) Math.pow(Math.pow(coordo[0]-coordo2[0] ,2) + Math.pow(coordo[1]-coordo2[1] ,2), 1f/2) - littleRadius;
            path.arcTo(new RectF(coordo[0]-littleRadius, coordo[1]-littleRadius, coordo[0]+littleRadius, coordo[1]+littleRadius), -90-150*i/5f, -180);
            ///
            path.arcTo(new RectF(coordo[0]-littleRadius, coordo[1]-littleRadius, coordo[0]+littleRadius, coordo[1]+littleRadius), -90-150*i/5f - 180, -30*random);
            path.arcTo(new RectF(coordo[0]-littleRadius, coordo[1]-littleRadius, coordo[0]+littleRadius, coordo[1]+littleRadius), -90-150*i/5f - 180 - 30*random, 30*random);
        }

        path.close();

        path.addOval(new RectF(c1.x-width/16, c1.y-height/16, c1.x+width/16, c1.y+height/16), Path.Direction.CW);

        path.addOval(new RectF(c2.x-width/32, c2.y-height/32, c2.x+width/32, c2.y+height/32), Path.Direction.CW);

        return path;*/
    }

}
