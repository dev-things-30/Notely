package com.ankit.notely;

import android.content.Context;

import com.ankit.notely.factory.NoteViewModelFactory;
import com.ankit.notely.persistence.LocalNoteDataSource;
import com.ankit.notely.persistence.NoteDatabase;

/**
 * Created by user on 23-01-2018.
 */

public class Injection {
    /**
     * returns the Object of NoteDataSource which have the methods of Database
     * @param context
     * @return
     */
    public static NoteDataSource provideNoteDataSource (Context context) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(context);
        return new LocalNoteDataSource(noteDatabase.noteDao());
    }

    /**
     * Create the instance of NoteViewModel
     * @param context
     * @return
     */
    public static NoteViewModelFactory provideNoteViewModelFactory (Context context) {
        return new NoteViewModelFactory(provideNoteDataSource(context));
    }

}
