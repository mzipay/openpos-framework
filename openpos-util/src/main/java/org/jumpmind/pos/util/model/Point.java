package org.jumpmind.pos.util.model;

import java.io.Serializable;

public class Point implements Serializable {
    private static final long serialVersionUID = 1L;
        
    private int x;
    private int y;
    private long time;
    private String color;
    
    public Point() {}
    public Point(int x, int y, long time) {
        this(x,y,time,null);
    }
    public Point(int x, int y, long time, String color) {
        this.x = x;
        this.y = y;
        this.time = time;
        this.color = color;
    }
    
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
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + (int) (time ^ (time >>> 32));
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
            return false;
        if (time != other.time)
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }
}
