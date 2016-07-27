package android.dominando.pointscounter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Derick on 20/06/2016.
 */
public class AdapterPlayerImage extends PagerAdapter {
    final int sdk = android.os.Build.VERSION.SDK_INT;
    Context context;
    ArrayList<Integer> imgs;

    File mCaminhoFoto;
    ImageView imageView;
    CarregarImagemTask mTask;
    int mLarguraImagem;
    int mAlturaImagem;

   public AdapterPlayerImage(Context context, int[] imagesIds){
        this.context = context;
        //this.imgs = imagesIds;
       imgs = new ArrayList<>();
       int j=0;
       for (int i:imagesIds) {
           imgs.add(j,i);
           j++;
       }
       String caminhoFoto = Utilidades.getFotoSalva(context);
       if(caminhoFoto != null){
           mCaminhoFoto = new File(caminhoFoto);
       }
    }

    @Override
    public boolean isViewFromObject(View view, Object obj){
        return view == ((TextView)obj).getParent();
    }
    @Override
    public int getCount(){
        //return imgs.length;
        return imgs.size();
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position){
        //na hora de instanciar o item deve-se criar tb o seu layout
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        //cria os parametros do layout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //seta os parametros ao linearlayout
        ll.setLayoutParams(params);
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            ll.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border));
        }else{
            ll.setBackground(context.getResources().getDrawable(R.drawable.border));

        }
        //agora adiciona esse layout ao container que é um ViewGroup
        container.addView(ll);
        //pega a dimensao definida nos resources e transforma em dp
        int dp = (int) (context.getResources().getDimension(R.dimen.pagerview_height) / context.getResources().getDisplayMetrics().density);
        //depois transforma de dp para pixel
        int px = (int)convertDpToPixel(dp,context);
        mAlturaImagem = px-90;
        mLarguraImagem = px-90;
        //agora, cria-se uma image view para ser adicionada ao linearlayout
        imageView = new ImageView(context);
        //imageView.setImageResource(imgs[position]);
        Integer id = imgs.get(position);
        if(id !=  R.id.reservedIdToImage) {
            imageView.setImageResource(imgs.get(position));
        }else{
            if(mCaminhoFoto != null && mCaminhoFoto.exists()){
                if(mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING){
                    mTask = new CarregarImagemTask();
                    mTask.execute();
                }
            }
        }
        LinearLayout.LayoutParams imgparams = new LinearLayout.LayoutParams(px-90,px-90);
        imgparams.gravity = Gravity.CENTER;
        ll.addView(imageView,imgparams);
        //criar um TextView tb
        TextView textView = new TextView(context);
        textView.setText("Imagem" + (position+1));
        textView.setTextSize(20);
        LinearLayout.LayoutParams tvparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvparams.gravity = Gravity.CENTER;
        ll.addView(textView,tvparams);

        return textView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object obj){
        container.removeView((View)obj);
    }
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public void insertFoto(String pathFoto){
        Utilidades.salvarFoto(context, pathFoto);
        imgs.add(R.id.reservedIdToImage);
        notifyDataSetChanged();
        mCaminhoFoto = new File(pathFoto);

    }

    public int getItemImageID(int position){
        return imgs.get(position);
    }

    class CarregarImagemTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            return Utilidades.carregarImagem(mCaminhoFoto, mLarguraImagem, mAlturaImagem);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                Utilidades.salvarFoto(context, mCaminhoFoto.getAbsolutePath());
            }
        }
    }
}
