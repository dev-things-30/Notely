package com.ankit.notely.persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by user on 23-01-2018.
 */

/**
 * Table of Notes
 */
@Entity(tableName = "notes")
public class Note {
    /**
     * Auto incremented id of note
     */
    @PrimaryKey(autoGenerate = true)
    int id;
    /**
     * Title of the Note
     */
    @ColumnInfo(name = "title")
    String title;
    /**
     * Description of note
     */
    @ColumnInfo(name = "description")
    String description;
    /**
     * Time when note is edited or created
     */
    @ColumnInfo(name = "time")
    long time;
    /**
     * Note is favourite of not
     */
    @ColumnInfo(name = "favourite")
    boolean favourite;
    /**
     * Note is hearted or not
     */
    @ColumnInfo(name = "hearted")
    boolean hearted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description.trim();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public boolean isHearted() {
        return hearted;
    }

    public void setHearted(boolean hearted) {
        this.hearted = hearted;
    }
}
