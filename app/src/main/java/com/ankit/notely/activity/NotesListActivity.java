package com.ankit.notely.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ankit.notely.Injection;
import com.ankit.notely.R;
import com.ankit.notely.RecyclerItemTouchHelper;
import com.ankit.notely.adapter.DrawerRecyclerAdapter;
import com.ankit.notely.adapter.NotesRecyclerAdapter;
import com.ankit.notely.entity.DrawerEntity;
import com.ankit.notely.persistence.Note;
import com.ankit.notely.viewmodel.NoteViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NotesListActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, View.OnClickListener, DrawerLayout.DrawerListener {

    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private int deletePosition = -1;
    private RecyclerView rvDrawer;
    private Button btnApply;
    private TextView tvNoNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        recyclerView = findViewById(R.id.recycler_view);
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        rvDrawer = findViewById(R.id.rv_drawer);
        btnApply = findViewById(R.id.btn_apply);
        tvNoNote = findViewById(R.id.tv_no_note);

        btnApply.setOnClickListener(this);
        drawer.addDrawerListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvDrawer.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar(toolbar);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        noteViewModel = ViewModelProviders.of(this, Injection.provideNoteViewModelFactory(this)).get(NoteViewModel.class);
        rvDrawer.setAdapter(new DrawerRecyclerAdapter(this, noteViewModel.getDrawerList()));
        noteViewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                List<Note> noteList = noteViewModel.getFilteredNotes();
                if (recyclerView.getAdapter() == null) {
                    recyclerView.setAdapter(new NotesRecyclerAdapter(NotesListActivity.this, noteList, noteViewModel));
                } else {
                    ((NotesRecyclerAdapter) recyclerView.getAdapter()).refresh(noteList, deletePosition);
                    deletePosition = -1;
                }
            }
        });
        noteViewModel.getMessageId().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer.intValue() > 0) {
                    tvNoNote.setVisibility(View.VISIBLE);
                    tvNoNote.setText(integer.intValue());
                } else {
                    tvNoNote.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }
            return true;
        }
        if (id == R.id.action_add) {
            Intent intent = new Intent(NotesListActivity.this, NoteDetailActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NotesRecyclerAdapter.NoteViewHolder) {
            Note note = ((NotesRecyclerAdapter) recyclerView.getAdapter()).getList().get(viewHolder.getAdapterPosition());
            deletePosition = viewHolder.getAdapterPosition();
            noteViewModel.deleteNote(note)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_apply) {
            for (DrawerEntity drawerEntity : noteViewModel.getDrawerList()) {
                drawerEntity.setSelected(drawerEntity.isTempIsSelected());
                drawerEntity.setTempIsSelected(false);
            }
            List<Note> filteredNotes = noteViewModel.getFilteredNotes();
            ((NotesRecyclerAdapter) recyclerView.getAdapter()).refresh(filteredNotes, deletePosition);
            drawer.closeDrawer(GravityCompat.END);
        }
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        for (DrawerEntity drawerEntity : noteViewModel.getDrawerList()) {
            drawerEntity.setTempIsSelected(drawerEntity.isSelected());
        }
        rvDrawer.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        for (DrawerEntity drawerEntity : noteViewModel.getDrawerList()) {
            drawerEntity.setTempIsSelected(false);
        }
        rvDrawer.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
