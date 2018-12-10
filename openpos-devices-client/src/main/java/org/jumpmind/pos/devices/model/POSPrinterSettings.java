package org.jumpmind.pos.devices.model;

import java.io.Serializable;
import java.util.Date;

public class POSPrinterSettings implements Serializable {

    private static final long serialVersionUID = 1L;
    
    protected Date settingsCapturedTime = new Date();
    
    protected boolean asyncMode = true;
    protected int slpLineChars;
    protected int slpLineHeight;
    protected int slpLineSpacing;
    protected int characterSet;
    protected boolean flagWhenIdle;
    protected boolean jrnLetterQuality;
    protected int jrnLineChars;
    protected int jrnLineHeight;
    protected int jrnLineSpacing;
    protected int mapMode;
    protected boolean recLetterQuality;
    protected int recLineChars;
    protected int recLineHeight;
    protected int recLineSpacing;
    protected int rotateSpecial;
    protected boolean slpLetterQuality;
    protected boolean coverOpen;
    
    public void setCoverOpen(boolean coverOpen) {
        this.coverOpen = coverOpen;
    }
    
    public boolean isCoverOpen() {
        return coverOpen;
    }
    
    public void setSettingsCapturedTime(Date settingsCaptureTime) {
        this.settingsCapturedTime = settingsCaptureTime;
    }
    
    public Date getSettingsCapturedTime() {
        return settingsCapturedTime;
    }

    public boolean isAsyncMode() {
        return asyncMode;
    }

    public void setAsyncMode(boolean asyncMode) {
        this.asyncMode = asyncMode;
    }

    public int getSlpLineChars() {
        return slpLineChars;
    }

    public void setSlpLineChars(int slpLineChars) {
        this.slpLineChars = slpLineChars;
    }

    public int getSlpLineHeight() {
        return slpLineHeight;
    }

    public void setSlpLineHeight(int slpLineHeight) {
        this.slpLineHeight = slpLineHeight;
    }

    public int getSlpLineSpacing() {
        return slpLineSpacing;
    }

    public void setSlpLineSpacing(int slpLineSpacing) {
        this.slpLineSpacing = slpLineSpacing;
    }

    public int getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(int characterSet) {
        this.characterSet = characterSet;
    }

    public boolean isFlagWhenIdle() {
        return flagWhenIdle;
    }

    public void setFlagWhenIdle(boolean flagWhenIdle) {
        this.flagWhenIdle = flagWhenIdle;
    }

    public boolean isJrnLetterQuality() {
        return jrnLetterQuality;
    }

    public void setJrnLetterQuality(boolean jrnLetterQuality) {
        this.jrnLetterQuality = jrnLetterQuality;
    }

    public int getJrnLineChars() {
        return jrnLineChars;
    }

    public void setJrnLineChars(int jrnLineChars) {
        this.jrnLineChars = jrnLineChars;
    }

    public int getJrnLineHeight() {
        return jrnLineHeight;
    }

    public void setJrnLineHeight(int jrnLineHeight) {
        this.jrnLineHeight = jrnLineHeight;
    }

    public int getJrnLineSpacing() {
        return jrnLineSpacing;
    }

    public void setJrnLineSpacing(int jrnLineSpacing) {
        this.jrnLineSpacing = jrnLineSpacing;
    }

    public int getMapMode() {
        return mapMode;
    }

    public void setMapMode(int mapMode) {
        this.mapMode = mapMode;
    }

    public boolean isRecLetterQuality() {
        return recLetterQuality;
    }

    public void setRecLetterQuality(boolean recLetterQuality) {
        this.recLetterQuality = recLetterQuality;
    }

    public int getRecLineChars() {
        return recLineChars;
    }

    public void setRecLineChars(int recLineChars) {
        this.recLineChars = recLineChars;
    }

    public int getRecLineHeight() {
        return recLineHeight;
    }

    public void setRecLineHeight(int recLineHeight) {
        this.recLineHeight = recLineHeight;
    }

    public int getRecLineSpacing() {
        return recLineSpacing;
    }

    public void setRecLineSpacing(int recLineSpacing) {
        this.recLineSpacing = recLineSpacing;
    }

    public int getRotateSpecial() {
        return rotateSpecial;
    }

    public void setRotateSpecial(int rotateSpecial) {
        this.rotateSpecial = rotateSpecial;
    }

    public boolean isSlpLetterQuality() {
        return slpLetterQuality;
    }

    public void setSlpLetterQuality(boolean slpLetterQuality) {
        this.slpLetterQuality = slpLetterQuality;
    }

}
