package android.dominando.pointscounter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.zip.Inflater;


public class ConfigPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String INTENT_ORIGIN =  "IntentOrigin";
    public static final int ORIGIN_NEW_MATCH_ACTIVITY = 1;
    public static final int ORIGIN_CONFIGURE_USER = 2;

    private int[] imgs ={R.drawable.player, R.drawable.playera, R.drawable.playerb};
    Button btDefinePlayer;
    EditText newNamePlayer;
    ViewPager vp;
    int IntentOrigin;

    File mCaminhoFoto;
    ImageView btnTirarFoto;
    int mLarguraImagem;
    int mAlturaImagem;

    public ConfigPlayerActivity(){
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_player);
        Intent it = getIntent();
        IntentOrigin = it.getIntExtra(INTENT_ORIGIN,ORIGIN_NEW_MATCH_ACTIVITY);
        vp = (ViewPager)findViewById(R.id.viewPager);
        vp.setBackgroundColor(Color.WHITE);
        vp.setAdapter(new AdapterPlayerImage(this,imgs));
        btDefinePlayer = (Button)findViewById(R.id.bt_definirPlayer);
        btDefinePlayer.setOnClickListener(this);
        newNamePlayer = (EditText)findViewById(R.id.newNameEdit);
        newNamePlayer.setOnClickListener(this);
        btnTirarFoto = (ImageView)findViewById(R.id.btnTakeFoto);
        btnTirarFoto.setOnClickListener(this);
    }
    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_definirPlayer:
                if(IntentOrigin == ORIGIN_NEW_MATCH_ACTIVITY) {
                    Intent it = new Intent();
                    AdapterPlayerImage adap = (AdapterPlayerImage) vp.getAdapter();
                    int idImg = adap.getItemImageID(vp.getCurrentItem());
                    it.putExtra(NewMatchActivity.IMAGE_INDEX, idImg);
                    it.putExtra(NewMatchActivity.PLAYER_NAME, newNamePlayer.getText().toString());
                    setResult(RESULT_OK, it);
                    finish();
                }else if(IntentOrigin == ORIGIN_CONFIGURE_USER){
                    //Neste caso salva no shared preferences e nao envia resultado para a activity que o chamou
                    SharedPreferences prefs = getSharedPreferences(MainActivity.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                    //é necessario um editor para inserir dados na shared preferences
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(MainActivity.SHARED_PREFERENCES_USERNAME, newNamePlayer.getText().toString());
                    AdapterPlayerImage adap = (AdapterPlayerImage) vp.getAdapter();
                    editor.putInt(MainActivity.SHARED_PREFERENCES_USERIMAGEID, adap.getItemImageID(vp.getCurrentItem()));
                    editor.commit();
                    finish();
                }
                break;
            case R.id.newNameEdit:
                newNamePlayer.setText("");
             /* Teste!!!  AdapterPlayerImage adap = (AdapterPlayerImage) vp.getAdapter();
                vp.setCurrentItem(adap.getCount()-1);*/
                break;
            case R.id.btnTakeFoto:
                if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)==
                        PackageManager.PERMISSION_GRANTED){
                    abrirCamera();
                }else{
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
                break;
        }
    }
    private void abrirCamera(){
        mCaminhoFoto = Utilidades.novoArquivoMidia();
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCaminhoFoto));
        startActivityForResult(it, Utilidades.REQUESTCODE_FOTO);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == Activity.RESULT_OK && requestCode == Utilidades.REQUESTCODE_FOTO){
            AdapterPlayerImage adap = (AdapterPlayerImage) vp.getAdapter();
            if(mCaminhoFoto != null && mCaminhoFoto.exists()) {
                adap.insertFoto(mCaminhoFoto.getAbsolutePath());
    //            vp.setCurrentItem(adap.getCount() - 1);
            }
        }
    }
}
