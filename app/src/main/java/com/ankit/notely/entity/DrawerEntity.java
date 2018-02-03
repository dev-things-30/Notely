package com.ankit.notely.entity;

/**
 * Created by user on 31-01-2018.
 */

public class DrawerEntity {
    private int title;
    private int image;
    private boolean isHeader;
    private boolean isSelected;
    private boolean tempIsSelected;

    public DrawerEntity(int title, int image, boolean isHeader) {
        this.title = title;
        this.image = image;
        this.isHeader = isHeader;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isTempIsSelected() {
        return tempIsSelected;
    }

    public void setTempIsSelected(boolean tempIsSelected) {
        this.tempIsSelected = tempIsSelected;
    }
}
