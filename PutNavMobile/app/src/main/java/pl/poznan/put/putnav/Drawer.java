package pl.poznan.put.putnav;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.util.ArrayList;

class Drawer {

    Paint setLineStyle() {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        return paint;
    }

    void drawLines(Canvas canvasCopy, Paint paint, ArrayList<Line> lines) {
        for (Line line : lines) {
            canvasCopy.drawLine(line.getStartX(), line.getStartY(), line.getStopX(), line.getStopY(), paint);
        }
    }

    Bitmap createMapBitmap(String path) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        bmOptions.inMutable = true;
        return BitmapFactory.decodeFile(path, bmOptions);
    }

    Bitmap createScaledMap(Bitmap mapBitmap) {
        float aspect_ratio = (float) mapBitmap.getHeight() / (float) mapBitmap.getWidth();
        if (aspect_ratio < 1) {
            return Bitmap.createScaledBitmap(mapBitmap, 4096, (int) ((4096) * aspect_ratio), true);
        } else {
            return Bitmap.createScaledBitmap(mapBitmap, (int) (4096.0 / aspect_ratio), 4096, true);
        }
    }
}
