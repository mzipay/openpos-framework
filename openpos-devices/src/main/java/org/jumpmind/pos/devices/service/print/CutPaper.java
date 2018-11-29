package org.jumpmind.pos.devices.service.print;

public class CutPaper extends DocumentElement {

    int percentage;

    public CutPaper() {
    }

    public CutPaper(int percentage) {
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getPercentage() {
        return percentage;
    }
}
