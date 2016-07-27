package android.dominando.pointscounter;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.LineNumberReader;
import java.io.Serializable;

/**
 * Created by Derick on 20/06/2016.
 */
public class Player implements Parcelable, Serializable{
    int LifePoints;
    String PlayerName;
    int IndexImage;
    Bitmap PlayerPhoto;

    public Player(int lifePoints, String playerName, int indexImage){
        this.LifePoints = lifePoints;
        this.PlayerName = playerName;
        this.IndexImage = indexImage;
    }
    private Player(Parcel from){
        LifePoints = from.readInt();
        PlayerName = from.readString();
        IndexImage = from.readInt();
    }
    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>(){
        public Player createFromParcel(Parcel in){
            return new Player(in);
        }
        public Player[] newArray(int size){
            return new Player[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(LifePoints);
        dest.writeString(PlayerName);
        dest.writeInt(IndexImage);
    }
    public int getLifePoints(){
        return LifePoints;
    }
    public int getIndexImage(){
        return IndexImage;
    }
    public String getPlayerName(){
        return PlayerName;
    }
    public void setLifePoints(int lifePoints){
        this.LifePoints = lifePoints;
    }
}
