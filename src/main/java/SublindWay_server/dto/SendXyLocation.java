package SublindWay_server.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SendXyLocation {
    private double locationX;
    private double locationY;

    public SendXyLocation(double locationX, double locationY) {
        this.locationX=locationX;
        this.locationY=locationY;
    }
}
