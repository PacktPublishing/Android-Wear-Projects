package com.ashok.packt.wear_note_1.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashok.packt.wear_note_1.R;
import com.ashok.packt.wear_note_1.model.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashok.kumar on 15/02/17.
 */

public class RecyclerViewAdapter
        extends WearableRecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ItemSelectedListener mItemSelectedListener;
    private List<Note> mListNote = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.note);
        }

        void bind(final String title, final int position, final ItemSelectedListener listener) {
            mTextView.setText(title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemSelected(position);
                    }
                }
            });
        }
    }

    public void setListener(ItemSelectedListener itemSelectedListener) {
        mItemSelectedListener = itemSelectedListener;
    }

    public void setListNote(List<Note> listNote) {
        mListNote.clear();
        mListNote.addAll(listNote);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mListNote.get(position).getNotes(), position, mItemSelectedListener);
    }

    @Override
    public int getItemCount() {
        return mListNote.size();
    }

    public interface ItemSelectedListener {
        void onItemSelected(int position);
    }
}
