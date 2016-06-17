package android.dominando.pointscounter;

import java.util.Random;

/**
 * Created by Derick on 15/06/2016.
 */
public class RandomObj {
    int num; //numero de faces do objeto

    public RandomObj(int n){
        num = n;
    }
    public int roll(){
        Random r = new Random();
        return (r.nextInt(num)+1);
    }
    public int getNum(){
        return num;
    }
}
