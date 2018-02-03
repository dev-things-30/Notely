package com.ankit.notely.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ankit.notely.NoteDataSource;
import com.ankit.notely.viewmodel.NoteViewModel;

/**
 * Created by user on 23-01-2018.
 */

public class NoteViewModelFactory implements ViewModelProvider.Factory {

    private final NoteDataSource noteDataSource;

    public NoteViewModelFactory (NoteDataSource noteDataSource) {
        this.noteDataSource = noteDataSource;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NoteViewModel.class)) {
            return (T) new NoteViewModel(noteDataSource);
        }
        return null;
    }
}
