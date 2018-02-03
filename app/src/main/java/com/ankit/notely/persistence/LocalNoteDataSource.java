package com.ankit.notely.persistence;

import android.arch.lifecycle.LiveData;

import com.ankit.notely.NoteDataSource;

import java.util.List;

/**
 * Created by user on 23-01-2018.
 */

public class LocalNoteDataSource implements NoteDataSource{

    private final NoteDao noteDao;

    public LocalNoteDataSource (NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    public LiveData<List<Note>> getNotes() {
        return noteDao.getNotes();
    }

    @Override
    public LiveData<Note> getNoteById(int id) {
        return noteDao.getNoteById(id);
    }

    @Override
    public void insertorUpdateNote(Note note) {
        noteDao.insertNote(note);
    }

    @Override
    public void deleteNote(Note note) {
        noteDao.deleteNote(note);
    }
}
