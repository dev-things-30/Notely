package com.ankit.notely;

import android.arch.lifecycle.LiveData;

import com.ankit.notely.persistence.Note;

import java.util.List;

/**
 * Created by user on 23-01-2018.
 */

public interface NoteDataSource {

    LiveData<List<Note>> getNotes ();

    LiveData<Note> getNoteById (int id);

    void insertorUpdateNote (Note note);

    void deleteNote(Note note);

}
