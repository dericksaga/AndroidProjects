package android.dominando.pointscounter;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Derick on 27/06/2016.
 */
public abstract class Utilidades {
    public static final String FOTO_SALVA = "Foto_Salva";
    private static final String PREFERENCIA_FOTO = "foto_pref";
    private static final String PASTA_MIDIA = "PointsCounter";
    private static final String EXTENSAO = ".jpg";
    public static final int REQUESTCODE_FOTO = 1;

    public static File novoArquivoMidia(){
        String nome = DateFormat.format("yyyy-MM-dd-hhmmss", new Date()).toString();
        File dirMidia = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),PASTA_MIDIA);
        if(!dirMidia.exists()){
            dirMidia.mkdirs();
        }
        return new File(dirMidia,nome+EXTENSAO);
    }

    public static void salvarFoto(Context context, String nomeFoto){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
              //  context.getSharedPreferences(PREFERENCIA_FOTO,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FOTO_SALVA,nomeFoto);
        editor.commit();
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.parse(nomeFoto);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static String getFotoSalva(Context context){
    //    return context.getSharedPreferences(PREFERENCIA_FOTO,Context.MODE_PRIVATE).getString(FOTO_SALVA, null);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String aux = sharedPreferences.getString(FOTO_SALVA,null);
        return aux;
    }

    public static Bitmap carregarImagem(File imagem, int largura, int altura){
        if(largura == 0|| altura == 0) return null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagem.getAbsolutePath(), bmOptions);
        int larguraFoto = bmOptions.outWidth;
        int alturaaFoto = bmOptions.outHeight;
        int escala = Math.min(larguraFoto/largura, alturaaFoto/altura);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = escala;
        bmOptions.inPurgeable = true;
        bmOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(imagem.getAbsolutePath(),bmOptions);
        bitmap = rotacionar(bitmap, imagem.getAbsolutePath());
        return bitmap;
    }

    public static Bitmap rotacionar(Bitmap bitmap, String path){
        try{
            ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch(orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotacionar(bitmap,90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotacionar(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotacionar(bitmap,270);
                    break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }

    private static Bitmap rotacionar(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap bitmap = Bitmap.createBitmap(source, 0,0,source.getWidth(),source.getHeight(),matrix,true);
        return bitmap;
    }
}
