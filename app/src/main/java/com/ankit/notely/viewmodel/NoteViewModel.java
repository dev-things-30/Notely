package com.ankit.notely.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ankit.notely.NoteDataSource;
import com.ankit.notely.R;
import com.ankit.notely.entity.DrawerEntity;
import com.ankit.notely.persistence.Note;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.functions.Action;

/**
 * Created by user on 23-01-2018.
 */

/**
 * Interactor between the Activity and Database to Gives list of Notes.
 */
public class NoteViewModel extends ViewModel {

    /**
     *
     */
    private NoteDataSource mNoteDataSource;
    /**
     * List of all the notes
     */
    private LiveData<List<Note>> listLiveData;
    /**
     * Returns the message id whether to Show or Not
     */
    private MutableLiveData<Integer> messageId = new MutableLiveData<>();
    /**
     * Holds the notes object which the user is editing
     */
    private Note note;
    /**
     * Tell the activity whether the note is in edit mode or not
     */
    private boolean enableEditMode = false;
    /**
     * The list shown in Drawer
     */
    private ArrayList<DrawerEntity> entityArrayList;

    /**
     * Parameterized Constructor
     * @param noteDataSource
     */
    public NoteViewModel(NoteDataSource noteDataSource) {
        this.mNoteDataSource = noteDataSource;
        listLiveData = mNoteDataSource.getNotes();
        entityArrayList = new ArrayList<>();
    }

    /**
     * returns the list of Notes
     * @return
     */
    public LiveData<List<Note>> getNotes() {
        return listLiveData;
    }
    /**
     * To get the particular note detail
     * @param id is the notes promary key
     * @return the notes
     */
    public LiveData<Note> getNote(int id) {
        return mNoteDataSource.getNoteById(id);
    }

    /**
     * Insert or update the note into the database
     * @param note
     * @return
     */
    public Completable insertorUpdateNote(final Note note) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mNoteDataSource.insertorUpdateNote(note);
            }
        });
    }

    /**
     * Delete the note from database
     * @param note
     * @return
     */
    public Completable deleteNote(final Note note) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mNoteDataSource.deleteNote(note);
            }
        });
    }

    /**
     *
     * @return the note which the user is editing
     */
    public Note getNote() {
        return note;
    }

    /**
     *
     * @param note which the user is editing
     */
    public void setNote(Note note) {
        this.note = note;
    }

    /**
     *
     * @return whether the user is editing the note or not
     */
    public boolean isEnableEditMode() {
        return enableEditMode;
    }

    /**
     * Update the note editing status
     * @param enableEditMode
     */
    public void setEnableEditMode(boolean enableEditMode) {
        this.enableEditMode = enableEditMode;
    }

    /**
     * Make the drawer list
     * @return
     */
    public ArrayList<DrawerEntity> getDrawerList () {
        if (entityArrayList.size() == 0) {
            entityArrayList.add(new DrawerEntity(R.string.filter, R.drawable.ic_close_white, true));
            entityArrayList.add(new DrawerEntity(R.string.hearted, R.drawable.ic_done_white, false));
            entityArrayList.add(new DrawerEntity(R.string.favourite, R.drawable.ic_done_white, false));
        }
        return entityArrayList;
    }

    /**
     * Check whether the user wants to see the Starred or Favourite  notes
     * @return
     */
    public List<Note> getFilteredNotes () {
        List<Note> notes = listLiveData.getValue();
        List<Note> notesList = new ArrayList<>();
        boolean isHearted = getDrawerList().get(1).isSelected();
        boolean isFavourite = getDrawerList().get(2).isSelected();
        if (isHearted || isFavourite) {
            Iterator<Note> iterator = notes.iterator();
            while(iterator.hasNext()) {
                Note next = iterator.next();
                if ((isHearted && next.isHearted()) || (isFavourite && next.isFavourite())) {
                    notesList.add(next);
                }
            }
        } else {
            notesList.addAll(notes);
        }
        if (notesList.size() == 0) {
            if (isHearted && isFavourite) {
                messageId.postValue(R.string.no_fav_hearted_note_present);
            } else if (isHearted) {
                messageId.postValue(R.string.no_hearted_note_present);
            } else if (isFavourite) {
                messageId.postValue(R.string.no_fav_note_present);
            } else {
                messageId.postValue(R.string.no_note_present);
            }
        } else {
            messageId.postValue(0);
        }
        return notesList;
    }

    public LiveData<Integer> getMessageId() {
        return messageId;
    }
}
