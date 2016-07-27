package android.dominando.animation_sprite_test;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final int FRAME_W = 85;
    private static final int FRAME_H = 121;
    private static final int NB_FRAME = 14;//numero de frames
    private static final int COUNT_X = 5; //numero de colunas
    private static final int COUNT_Y = 3; //numero de linhas
    private static final int FRAME_DURATION = 200; //duração de 1 frame em ms
    private static final int SCALE_FACTOR = 5;
    private static final int COIN_NFRAMES = 10;

    private ImageView img, img2;
    private Bitmap[] bmps, sprites; //vetor com os sprites

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView)findViewById(R.id.img);
        img2 = (ImageView)findViewById(R.id.img2);
        int sprite_H;
        int sprite_W;
        //carrega a imagem com os sprites
        Bitmap birdBmp = getBitmapFromAssets(this, "grossini_dance-1.png");
        Bitmap bitmap = getBitmapFromAssets(this, "moeda_spin.png");
        sprite_H = bitmap.getHeight();
        sprite_W = bitmap.getWidth()/COIN_NFRAMES;
        if(birdBmp != null){
            //cria o array de bitmaps
            bmps = new Bitmap[NB_FRAME];
            int currentFrame = 0;
            for(int i=0; i<COUNT_Y; i++){
                for(int j=0; j< COUNT_X; j++){
                    bmps[currentFrame] = Bitmap.createBitmap(birdBmp,FRAME_W*j,FRAME_H*i,FRAME_W, FRAME_H);
                    //aplica fator de escala (?)
                    bmps[currentFrame] = Bitmap.createScaledBitmap(bmps[currentFrame],FRAME_W*SCALE_FACTOR, FRAME_H*SCALE_FACTOR, true);
                    if(++currentFrame>= NB_FRAME){
                        break;
                    }
                }
            }
            final AnimationDrawable animation = new AnimationDrawable();
            animation.setOneShot(false);//repetir
            for(int i=0; i<NB_FRAME; i++){// faz a animação com a troca de sprites
                animation.addFrame(new BitmapDrawable(getResources(),bmps[i]),FRAME_DURATION);
            }
            //carrega a animação na imagem
            if(Build.VERSION.SDK_INT < 16){
                img.setBackgroundDrawable(animation);
            }else{
                img.setBackground(animation);
            }
            img.post(new Runnable() {
                @Override
                public void run() {
                    animation.start();
                }
            });
        }
        if(bitmap != null){
            sprites = new Bitmap[COIN_NFRAMES];
            int cframe= 0;
            for(int i=0; i<COIN_NFRAMES; i++){
                sprites[i] = Bitmap.createBitmap(bitmap,i*sprite_W,0,sprite_W,sprite_H);
                sprites[i] = Bitmap.createScaledBitmap(sprites[i],sprite_W*SCALE_FACTOR,sprite_H*SCALE_FACTOR,false);
            }
            final AnimationDrawable anim = new AnimationDrawable();
            anim.setOneShot(true);
            //adiciona os sprites na animação
            int frameduration = FRAME_DURATION/10;
            int t = 1;
            for(int j=0; j<10; j++){
                for(int i=0; i<COIN_NFRAMES; i++){
                    switch (j){
                        case 5:
                            t=2;
                            break;
                        case 7:
                            t = 4;
                            break;
                        case 8:
                            t = 6;
                            break;
                        default:
                            break;
                    }

                    anim.addFrame(new BitmapDrawable(getResources(),sprites[i]),frameduration*t);
                }
            }
            //carrega a animação na imagem
            if(Build.VERSION.SDK_INT < 16){
                img2.setBackgroundDrawable(anim);
            }else{
                img2.setBackground(anim);
            }
            img2.post(new Runnable() {
                @Override
                public void run() {
                    anim.start();
                }
            });

        }
    }

    private Bitmap getBitmapFromAssets(MainActivity mainActivity, String filepath){
        AssetManager assetManager = mainActivity.getAssets();
        InputStream istr = null;
        Bitmap bitmap = null;

        try{
            istr = assetManager.open(filepath);
            bitmap = BitmapFactory.decodeStream(istr);
        }catch (IOException ioe){

        }finally {
            if(istr != null){
                try{
                    istr.close();
                }catch (IOException e){

                }
            }
        }
        return bitmap;
    }
}
