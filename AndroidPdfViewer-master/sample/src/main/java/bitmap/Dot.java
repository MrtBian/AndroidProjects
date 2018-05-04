package bitmap;

/**
 * Created by Wing on 2018/4/30.
 */

public class Dot{
    private float X,Y;

    public Dot(){

    }
    public Dot(float x, float y) {
        X = x;
        Y = y;
    }


    public float getX() {
        return X;
    }
    public float getY() {
        return Y;
    }
    public void setX(float x) {
        X = x;
    }
    public void setY(float y) {
        Y = y;
    }
}
