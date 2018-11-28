package org.jumpmind.pos.devices.service.print;

public class RuleLine extends DocumentElement {

    String positionList;
    int lineDirection;
    int lineWidth;
    int lineStyle;
    int lineColor;

    public RuleLine() {
    }
    
    public RuleLine(String positionList, int lineDirection, int lineWidth, int lineStyle, int lineColor) {
        super();
        this.positionList = positionList;
        this.lineDirection = lineDirection;
        this.lineWidth = lineWidth;
        this.lineStyle = lineStyle;
        this.lineColor = lineColor;
    }

    public String getPositionList() {
        return positionList;
    }

    public void setPositionList(String positionList) {
        this.positionList = positionList;
    }

    public int getLineDirection() {
        return lineDirection;
    }

    public void setLineDirection(int lineDirection) {
        this.lineDirection = lineDirection;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }
}
