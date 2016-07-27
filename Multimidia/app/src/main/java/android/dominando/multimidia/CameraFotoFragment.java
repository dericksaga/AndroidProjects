package android.dominando.multimidia;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.io.File;
import java.util.jar.Manifest;

/**
 * Created by Derick on 27/06/2016.
 */
public class CameraFotoFragment extends Fragment implements View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {

    File mCaminhoFoto;
    ImageView mImageViewFoto;
    CarregarImagemTask mTask;
    int mLarguraImagem;
    int mAlturaImagem;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        String caminhoFoto = Util.getUltimaMidia(getActivity(), Util.MIDIA_FOTO);
        if(caminhoFoto != null){
            mCaminhoFoto = new File(caminhoFoto);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){
        View layout = inflater.inflate(R.layout.fragment_camera_foto, container, false);
        layout.findViewById(R.id.btnFoto).setOnClickListener(this);
        mImageViewFoto = (ImageView) layout.findViewById(R.id.imgFoto);
        layout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        return layout;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == Activity.RESULT_OK && requestCode == Util.REQUESTCODE_FOTO){
            carregarImagem();
        }
    }
    private void carregarImagem(){
        if(mCaminhoFoto != null && mCaminhoFoto.exists()){
            if(mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING){
                mTask = new CarregarImagemTask();
                mTask.execute();
            }
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnFoto){
            if(ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_GRANTED){
                abrirCamera();
            }else{
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }
    }
    private void abrirCamera(){
        mCaminhoFoto = Util.novaMidia(Util.MIDIA_FOTO);
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCaminhoFoto));
        startActivityForResult(it, Util.REQUESTCODE_FOTO);
    }

    @Override
    public void onGlobalLayout() {
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
            getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }else {
            getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        mLarguraImagem = mImageViewFoto.getWidth();
        mAlturaImagem = mImageViewFoto.getHeight();
        carregarImagem();
    }

    class CarregarImagemTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            return Util.carregarImagem(mCaminhoFoto, mLarguraImagem, mAlturaImagem);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                mImageViewFoto.setImageBitmap(bitmap);
                Util.salvarUltimaMidia(getActivity(), Util.MIDIA_FOTO, mCaminhoFoto.getAbsolutePath());
            }
        }
    }
}
