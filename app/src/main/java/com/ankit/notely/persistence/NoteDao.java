package com.ankit.notely.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by user on 23-01-2018.
 */
@Dao
public interface NoteDao {

    @Query("SELECT * from notes order by time DESC")
    LiveData<List<Note>> getNotes ();

    @Query("SELECT * from notes where id = :id")
    LiveData<Note> getNoteById (int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote (Note note);

    @Delete
    void deleteNote(Note note);

}
