package android.dominando.pointscounter;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import static android.support.v4.app.ActivityCompat.startActivity;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Derick on 19/06/2016.
 * View para mostrar a foto do jogador e seu nome
 */
public class PlayerSelectionView extends View{
    public static final int TEXT_SIZE = 25;
    String pName;
    Bitmap bitImage;
    int imagId;
    Context context;
    //a View Personalizada precisa de um gesturedetector
    GestureDetector mDetector;

    class myViewDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e){return super.onDown(e);}
    };

    @Override
    public boolean onTouchEvent(MotionEvent e){
        boolean result = mDetector.onTouchEvent(e);
        if(!result){
            if(e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_DOWN){
                callOnClick();
            }
        }
        return result;
    }
    public PlayerSelectionView(Context context, AttributeSet attrs){
        super(context,attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.PlayerSelectionView,0,0);
        try{
            pName = a.getString(R.styleable.PlayerSelectionView_playerName);
        }finally {
            a.recycle();
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (bitImage == null) {
            bitImage = BitmapFactory.decodeResource(getResources(), R.drawable.playera);
            imagId = R.drawable.playera;
        }
        mDetector = new GestureDetector(this.getContext(),new myViewDetector());
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        ViewGroup.LayoutParams lay = getLayoutParams();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);
        Rect rect = new Rect(0,0,lay.width,lay.height);
        canvas.drawBitmap(bitImage,null,rect,paint);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.WHITE);
        paint.setTextSize(TEXT_SIZE);
       if(pName != null) {
           canvas.drawText(pName, lay.width/4, lay.height-TEXT_SIZE, paint);
       }else{
           canvas.drawText("Player", 100,400,paint);
       }
    }
    @Override
    public void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        bitImage = null;
        mDetector = null;
    }

    public void setpName(String name){
        pName = name;
        this.invalidate();
    }
    public void setBitImage(int id){
        this.imagId = id;
        this.bitImage = BitmapFactory.decodeResource(getResources(),id);
        this.invalidate();
    }

    public void setBitImage(String pathfoto){
        CarregarImagemTask newtask = new CarregarImagemTask();
        imagId = R.id.reservedIdToImage;
        newtask.execute(pathfoto);
    }

    public int getImagId(){
        return imagId;
    }
    public String getpName(){
        return pName;
    }

    class CarregarImagemTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            File newfile = new File(strings[0]);
            return Utilidades.carregarImagem(newfile, 90, 90);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                bitImage = bitmap;
                invalidate();
            }
        }
    }
}
