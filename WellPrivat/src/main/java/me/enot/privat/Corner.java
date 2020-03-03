package me.enot.privat;

public class Corner {

    private Integer X;
    private Integer Y;
    private Integer Z;

    public Corner(Integer X, Integer Y, Integer Z){
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    public Integer getX() {
        return X;
    }

    public Integer getY() {
        return Y;
    }

    public Integer getZ() {
        return Z;
    }

    public Boolean equals(Corner corner){
        return this.X.equals(corner.getX()) && this.Y.equals(corner.getY()) && this.Z.equals(corner.getZ());
    }

    public String toString(){
        return "X: " + this.getX() + " Y: " + this.getY() + " Z: " + this.getZ();
    }
}
