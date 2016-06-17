package android.dominando.pointscounter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Derick on 14/06/2016.
 */
public class PlayerViewMatch extends Fragment {
    private static final String PLAYER = "playerId";

    TextView playerName;
    TextView lifePoints;
    Bitmap playerPhoto;
    ImageView photo;

    //é necessário criar um método construtor da classe
    //caso se queira passar parametros usa-se um construtor estático
    //a principio vou usar um construtor default
    public PlayerViewMatch(){
        super();
    }
    //sobreescrever o metodo onCreate passando o Bundle
    //q o fragment usa para armazenar parametros internos
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    //sobreescrever o metodo onCreateView
    //onde a view do fragment é criada para ser colocada na tela
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        super.onCreateView(inflater,viewGroup,savedInstanceState);
        View layout = inflater.inflate(R.layout.player_view_match, viewGroup,false);
        playerName = (TextView)layout.findViewById(R.id.playername);
        lifePoints = (TextView)layout.findViewById(R.id.lifepoints);
        photo = (ImageView)layout.findViewById(R.id.playerphoto);
        playerPhoto = BitmapFactory.decodeResource(getResources(),R.drawable.jinxschroider);
        playerName.setText(R.string.p1_name);
        lifePoints.setText("8000");
        photo.setImageBitmap(playerPhoto);
        return layout;
    }
    public void setImgPlayer(int id){
        playerPhoto = BitmapFactory.decodeResource(getResources(),id);
        photo.setImageBitmap(playerPhoto);
    }
    public void setPlayerName(String name){
        playerName.setText(name);
    }

    public interface FragmentActions
    {
        void lifePointsClick (View v);
        void surrenderClick (View v);
        /*Intent intent = new Intent(getActivity(), EnterAmount.class);
        startActivity(intent);*/
    }
}
