package cn.edu.seu.udo.entities;

/**
 * Created by 1113090731@qq.com on 2016/7/23.
 */
public class UploadResult {
    private double rank;
    private int state;

    public double getRank(){
        return  rank;
    }

    public void setRank(double rank){
        this.rank = rank;
    }

    public int getState(){
        return state;
    }

    public void setState(int state){
        this.state = state;
    }
}
