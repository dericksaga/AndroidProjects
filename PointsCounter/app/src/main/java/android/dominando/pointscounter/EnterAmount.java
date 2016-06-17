package android.dominando.pointscounter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.TextView;

import java.io.Console;

public class EnterAmount extends AppCompatActivity implements View.OnClickListener {

    String amount;
    Button bt0, bt00, bt000, bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9;
    Button btsoma, btsubt, btcancel;
    TextView amountView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_amount);
        amount = new String();
        amount = "";
        bt0 = (Button) findViewById(R.id.bt0);
        bt0.setOnClickListener(this);
        bt1 = (Button) findViewById(R.id.bt1);
        bt1.setOnClickListener(this);
        bt2 = (Button) findViewById(R.id.bt2);
        bt2.setOnClickListener(this);
        bt3 = (Button) findViewById(R.id.bt3);
        bt3.setOnClickListener(this);
        bt4 = (Button) findViewById(R.id.bt4);
        bt4.setOnClickListener(this);
        bt5 = (Button) findViewById(R.id.bt5);
        bt5.setOnClickListener(this);
        bt6 = (Button) findViewById(R.id.bt6);
        bt6.setOnClickListener(this);
        bt7 = (Button) findViewById(R.id.bt7);
        bt7.setOnClickListener(this);
        bt8 = (Button) findViewById(R.id.bt8);
        bt8.setOnClickListener(this);
        bt9 = (Button) findViewById(R.id.bt9);
        bt9.setOnClickListener(this);
        bt00 = (Button) findViewById(R.id.bt00);
        bt00.setOnClickListener(this);
        bt000 = (Button) findViewById(R.id.bt000);
        bt000.setOnClickListener(this);
        btsoma = (Button) findViewById(R.id.btsomar);
        btsoma.setOnClickListener(this);
        btsubt = (Button) findViewById(R.id.btsubt);
        btsubt.setOnClickListener(this);
        btcancel = (Button) findViewById(R.id.btcancel);
        btcancel.setOnClickListener(this);
        this.amountView = (TextView) findViewById(R.id.amount);
    }

    @Override
    public void onClick(View v) {
        String aux = "";
        switch (v.getId()) {
            case R.id.bt1:
                aux = "1";
                break;
            case R.id.bt2:
                aux = "2";
                break;
            case R.id.bt3:
                aux = "3";
                break;
            case R.id.bt4:
                aux = "4";
                break;
            case R.id.bt5:
                aux = "5";
                break;
            case R.id.bt6:
                aux = "6";
                break;
            case R.id.bt7:
                aux = "7";
                break;
            case R.id.bt8:
                aux = "8";
                break;
            case R.id.bt9:
                aux = "9";
                break;
            case R.id.bt0:
                aux = "0";
                break;
            case R.id.bt00:
                aux = "00";
                break;
            case R.id.bt000:
                aux = "000";
                break;
            case R.id.btsomar:
                Intent it = new Intent();
                it.putExtra(MatchActivity.PARAM1,amount);
                it.putExtra(MatchActivity.PARAM2,"+");
                setResult(RESULT_OK,it);
                finish();
                break;
            case R.id.btsubt:
                Intent it2 = new Intent();
                it2.putExtra(MatchActivity.PARAM1,amount);
                it2.putExtra(MatchActivity.PARAM2,"-");
                setResult(RESULT_OK,it2);
                finish();
                break;
            default:
                aux = "";
                Intent it3 = new Intent();
                setResult(RESULT_CANCELED,it3);
                finish();
                break;
        }
        //amount = new StringBuilder().append(amount).append(aux).toString();
        amount += aux;
        amountView.setText(amount);
 //       System.out.println(amountView.getText());
        amountView.invalidate();
    }

}
