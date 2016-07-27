package android.dominando.pointscounter;

//import android.app.DialogFragment;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by Derick on 22/06/2016.
 */
public class EnterAmountFragment extends DialogFragment implements View.OnClickListener{
    public static final String FRAG_TAG = "AMOUNT_TAG";
    String amount;
    Button bt0, bt00, bt000, bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9;
    Button btsoma, btsubt, btcancel;
    TextView amountView;
    OnFragmentResultListener mCallback;
    // Container Activity must implement this interface
    public interface OnFragmentResultListener {
        void onGetFragmentResult(String amount, String sinal);
    }

    public EnterAmountFragment(){
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View layout = inflater.inflate(R.layout.activity_enter_amount, container,false);
        amount = new String();
        amount = "";
        bt0 = (Button)layout.findViewById(R.id.bt0);
        bt0.setOnClickListener(this);
        bt1 = (Button) layout.findViewById(R.id.bt1);
        bt1.setOnClickListener(this);
        bt2 = (Button) layout.findViewById(R.id.bt2);
        bt2.setOnClickListener(this);
        bt3 = (Button) layout.findViewById(R.id.bt3);
        bt3.setOnClickListener(this);
        bt4 = (Button) layout.findViewById(R.id.bt4);
        bt4.setOnClickListener(this);
        bt5 = (Button) layout.findViewById(R.id.bt5);
        bt5.setOnClickListener(this);
        bt6 = (Button) layout.findViewById(R.id.bt6);
        bt6.setOnClickListener(this);
        bt7 = (Button) layout.findViewById(R.id.bt7);
        bt7.setOnClickListener(this);
        bt8 = (Button) layout.findViewById(R.id.bt8);
        bt8.setOnClickListener(this);
        bt9 = (Button) layout.findViewById(R.id.bt9);
        bt9.setOnClickListener(this);
        bt00 = (Button) layout.findViewById(R.id.bt00);
        bt00.setOnClickListener(this);
        bt000 = (Button) layout.findViewById(R.id.bt000);
        bt000.setOnClickListener(this);
        btsoma = (Button) layout.findViewById(R.id.btsomar);
        btsoma.setOnClickListener(this);
        btsubt = (Button) layout.findViewById(R.id.btsubt);
        btsubt.setOnClickListener(this);
        btcancel = (Button) layout.findViewById(R.id.btcancel);
        btcancel.setOnClickListener(this);
        this.amountView = (TextView) layout.findViewById(R.id.amount);
        return layout;
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
                mCallback = (OnFragmentResultListener)getTargetFragment();
               // mCallback = (OnFragmentResultListener)getActivity();
                mCallback.onGetFragmentResult(amount,"+");
                this.dismiss();
             /*   Intent it = new Intent();
                it.putExtra(MatchActivity.PARAM1,amount);
                it.putExtra(MatchActivity.PARAM2,"+");
                setResult(RESULT_OK,it);
                finish();*/
                break;
            case R.id.btsubt:
              //  mCallback = (OnFragmentResultListener)getActivity();
                mCallback = (OnFragmentResultListener)getTargetFragment();
                mCallback.onGetFragmentResult(amount,"-");
                this.dismiss();
              /*  Intent it2 = new Intent();
                it2.putExtra(MatchActivity.PARAM1,amount);
                it2.putExtra(MatchActivity.PARAM2,"-");
                setResult(RESULT_OK,it2);
                finish();*/
                break;
            default:
                amount = "0";
             //   mCallback = (OnFragmentResultListener)getActivity();
                mCallback = (OnFragmentResultListener)getTargetFragment();
                mCallback.onGetFragmentResult(amount,"=");
                this.dismiss();
         /*       aux = "";
                Intent it3 = new Intent();
                setResult(RESULT_CANCELED,it3);
                finish();*/
                break;
        }
        //amount = new StringBuilder().append(amount).append(aux).toString();
        amount += aux;
        amountView.setText(amount);
        //       System.out.println(amountView.getText());
        amountView.invalidate();
    }

}
