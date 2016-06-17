package android.dominando.pointscounter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DicesActivity extends AppCompatActivity
            implements View.OnClickListener{
    public static final int MAX_TIMES = 7;
    public static final int DICE_TYPE = 6;
    public static final int COIN_TYPE = 2;

    RandomObj rObj;
    TextView result;
    ImageButton coinBtn, diceBtn;
    Handler handler;
    int cont; //contador para animação dos dados
    Bitmap imgHead, imgTail;
    ImageView coinresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dices);
        result = (TextView)findViewById(R.id.diceValue);
        coinBtn = (ImageButton)findViewById(R.id.coinButton);
        coinBtn.setOnClickListener(this);
        diceBtn = (ImageButton)findViewById(R.id.diceButton);
        diceBtn.setOnClickListener(this);
        handler = new Handler();
        cont = 0;
        coinresult = (ImageView)findViewById(R.id.imgCoin);
        imgHead = BitmapFactory.decodeResource(getResources(),R.drawable.cara);
        imgTail = BitmapFactory.decodeResource(getResources(),R.drawable.coroa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        imgTail = null;
        imgHead = null;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                Intent it = NavUtils.getParentActivityIntent(this);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this,it);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view){
        result.setTextColor(Color.GRAY);
        switch(view.getId()){
            case R.id.coinButton:
                rObj = new RandomObj(COIN_TYPE);
                result.setVisibility(View.INVISIBLE);
                coinresult.setVisibility(View.VISIBLE);
                break;
            case R.id.diceButton:
                rObj = new RandomObj(DICE_TYPE);
                result.setVisibility(View.VISIBLE);
                coinresult.setVisibility(View.INVISIBLE);
                break;
        }
        cont = 0;
        handler.removeCallbacks(mRunTask);
        handler.postDelayed(mRunTask,100);
    }
    //necessario criar um handler e um runnable para implementar a animação do dado e da moeda

    private Runnable mRunTask = new Runnable() {
        @Override
        public void run() {
            cont++;
            if (cont < MAX_TIMES && rObj.getNum() != 2) {
                result.setText(String.valueOf(rObj.roll()));
                handler.postDelayed(this,400);
            }else{
                if(rObj.getNum() != 2) {
                    result.setText(String.valueOf(rObj.roll()));
                }else{
                    if(rObj.roll() == 1){
                        coinresult.setImageBitmap(imgHead);
                        //result.setText("CARA");
                    }
                    else{
                        coinresult.setImageBitmap(imgTail);
                        //result.setText("COROA");
                    }
                }
                result.setTextColor(Color.BLUE);
                handler.removeCallbacks(this);
            }
        }
    };

}
