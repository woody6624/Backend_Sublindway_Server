package SublindWay_server.dto;

import lombok.Data;

@Data

public class SendWebData {
    private String trainNum;
    private String upDown;
    private double locationX;
    private double locationY;
    public SendWebData(String trainNum,String upDown,double locationX, double locationY) {
        this.trainNum=trainNum;
        this.upDown=upDown;
        this.locationX=locationX;
        this.locationY=locationY;
    }
}
