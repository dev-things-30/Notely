package com.ankit.notely.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by user on 23-01-2018.
 */
@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static volatile NoteDatabase mInstance;

    public abstract NoteDao noteDao();

    public static NoteDatabase getInstance (Context context) {
        if (mInstance == null) {
            synchronized (NoteDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(context.getApplicationContext(),
                            NoteDatabase.class, "notely.db").build();
                }
            }
        }
        return mInstance;
    }
}
