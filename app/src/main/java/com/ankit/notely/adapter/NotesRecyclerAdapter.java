package com.ankit.notely.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ankit.notely.R;
import com.ankit.notely.activity.NoteDetailActivity;
import com.ankit.notely.persistence.Note;
import com.ankit.notely.util.Utility;
import com.ankit.notely.viewmodel.NoteViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by user on 23-01-2018.
 */

public class NotesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Note> mNotesList = null;
    private Context mContext;
    private NoteViewModel mNoteViewModel;

    public NotesRecyclerAdapter(@NonNull Context context, @NonNull List<Note> notesList, @NonNull NoteViewModel noteViewModel) {
        this.mContext = context;
        this.mNotesList = notesList;
        this.mNoteViewModel = noteViewModel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        final NoteViewHolder noteViewHolder = new NoteViewHolder(view);
        noteViewHolder.ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = mNotesList.get(noteViewHolder.getAdapterPosition());
                note.setFavourite(!note.isFavourite());
                mNoteViewModel.insertorUpdateNote(note)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
        });
        noteViewHolder.ivHearted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = mNotesList.get(noteViewHolder.getAdapterPosition());
                note.setHearted(!note.isHearted());
                mNoteViewModel.insertorUpdateNote(note)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NoteDetailActivity.class);
                intent.putExtra(NoteDetailActivity.ID, mNotesList.get(noteViewHolder.getAdapterPosition()).getId());
                mContext.startActivity(intent);
            }
        });
        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NoteViewHolder) {
            NoteViewHolder noteViewHolder = (NoteViewHolder)holder;
            Note note = mNotesList.get(position);
            noteViewHolder.tvTitle.setText(note.getTitle());
            noteViewHolder.tvDesc.setText(note.getDescription());
            noteViewHolder.tvDate.setText(Utility.getDate(note.getTime()));
            noteViewHolder.ivHearted.setImageResource(note.isHearted() ? R.drawable.ic_hearted_red : R.drawable.ic_hearted_grey);
            noteViewHolder.ivFav.setImageResource(note.isFavourite() ? R.drawable.ic_fav_yellow : R.drawable.ic_fav_grey);
        }
    }

    @Override
    public int getItemCount() {
        return mNotesList.size();
    }

    public void refresh (List<Note> notesList, int deletePosition) {
        mNotesList = notesList;
        if (deletePosition != -1) {
            notifyItemRemoved(deletePosition);
        } else {
            notifyDataSetChanged();
        }
    }

    public List<Note> getList () {
        return mNotesList;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvDesc, tvDate;
        private ImageView ivHearted, ivFav;
        public ConstraintLayout frontView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivHearted = itemView.findViewById(R.id.iv_hearted);
            ivFav = itemView.findViewById(R.id.iv_fav);
            frontView = itemView.findViewById(R.id.front_view);
        }
    }

}
