package com.ankit.notely.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ankit.notely.Injection;
import com.ankit.notely.R;
import com.ankit.notely.persistence.Note;
import com.ankit.notely.util.Utility;
import com.ankit.notely.viewmodel.NoteViewModel;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NoteDetailActivity extends AppCompatActivity {

    public static final String ID = "id";

    private Toolbar toolbar;
    private TextView tvDate;
    private EditText etTitle, etDesc;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        etTitle = findViewById(R.id.et_title);
        etDesc = findViewById(R.id.et_desc);
        tvDate = findViewById(R.id.tv_date);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        int id = getIntent().getIntExtra(ID, -1);
        noteViewModel = ViewModelProviders.of(this, Injection.provideNoteViewModelFactory(this)).get(NoteViewModel.class);
        if (noteViewModel.getNote() != null) {
            etTitle.setText(noteViewModel.getNote().getTitle());
            etDesc.setText(noteViewModel.getNote().getDescription());
            tvDate.setText(String.format(getString(R.string.note_time_update), Utility.getDate(noteViewModel.getNote().getTime())));
            invalidateOptionsMenu();
        } else if (id != -1) {
            noteViewModel.getNote(id).observe(this, new Observer<Note>() {
                @Override
                public void onChanged(@Nullable Note note) {
                    etTitle.setEnabled(false);
                    etDesc.setEnabled(false);
                    etTitle.setText(note.getTitle());
                    etDesc.setText(note.getDescription());
                    tvDate.setText(String.format(getString(R.string.note_time_update), Utility.getDate(note.getTime())));
                    noteViewModel.setNote(note);
                }
            });
        } else {
            noteViewModel.setNote(new Note());
            noteViewModel.setEnableEditMode(true);
            invalidateOptionsMenu();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.note_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_edit) {
            noteViewModel.setEnableEditMode(true);
            invalidateOptionsMenu();
            return true;
        } else if (item.getItemId() == R.id.action_undo) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_save) {
            noteViewModel.getNote().setTitle(etTitle.getText().toString());
            noteViewModel.getNote().setDescription(etDesc.getText().toString());
            noteViewModel.getNote().setTime(System.currentTimeMillis());
            noteViewModel.insertorUpdateNote(noteViewModel.getNote())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            onBackPressed();
                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteViewModel.getNote().setTitle(etTitle.getText().toString());
        noteViewModel.getNote().setDescription(etDesc.getText().toString());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem edit = menu.findItem(R.id.action_edit);
        MenuItem undo = menu.findItem(R.id.action_undo);
        MenuItem save = menu.findItem(R.id.action_save);

        edit.setVisible(!noteViewModel.isEnableEditMode());
        undo.setVisible(noteViewModel.isEnableEditMode());
        save.setVisible(noteViewModel.isEnableEditMode());

        if (noteViewModel.isEnableEditMode()) {
            tvDate.setVisibility(View.GONE);
            etTitle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            etTitle.setEnabled(true);
            etDesc.setEnabled(true);
            etDesc.requestFocus();
        }

        super.onPrepareOptionsMenu(menu);
        return true;
    }
}
