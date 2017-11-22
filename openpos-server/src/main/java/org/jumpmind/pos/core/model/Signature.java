package org.jumpmind.pos.core.model;

import java.io.Serializable;
import java.util.List;

public class Signature implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private List<List<Point>> pointGroups;
    private String mediaType;
    private String base64EncodedImage;

    public Signature() {}
    
    public List<List<Point>> getPointGroups() {
        return pointGroups;
    }


    public void setPointGroups(List<List<Point>> pointGroups) {
        this.pointGroups = pointGroups;
    }


    public String getMediaType() {
        return mediaType;
    }


    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }


    public String getBase64EncodedImage() {
        return base64EncodedImage;
    }


    public void setBase64EncodedImage(String base64EncodedImage) {
        this.base64EncodedImage = base64EncodedImage;
    }


    public static class Point implements Serializable {
        private static final long serialVersionUID = 1L;
            
        private int x;
        private int y;
        private long time;
        private String color;
        
        public Point() {}
        
        public int getX() {
            return x;
        }
        public void setX(int x) {
            this.x = x;
        }
        public int getY() {
            return y;
        }
        public void setY(int y) {
            this.y = y;
        }
        public long getTime() {
            return time;
        }
        public void setTime(long time) {
            this.time = time;
        }
        public String getColor() {
            return color;
        }
        public void setColor(String color) {
            this.color = color;
        }
    }
}
