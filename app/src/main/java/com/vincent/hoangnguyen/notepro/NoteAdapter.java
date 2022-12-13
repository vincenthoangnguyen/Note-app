package com.vincent.hoangnguyen.notepro;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

// để sử dụng firebase data như 1 recyclerview thì adapter cần kế thừa từ firestore recyclerview nên càn thêm firebase ui dependency

public class NoteAdapter extends FirestoreRecyclerAdapter <Note, NoteAdapter.NoteViewHolder> {
    // thêm context để biết đang ở context nào
    Context context;
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note model) {
        // hàm này thêm data vào template view dựa trên đối tượng đang lưu giữ data
    holder.titleTextview.setText(model.getTitle());
    holder.contentTextview.setText(model.getContent());
    holder.timeStampTextview.setText(Utility.timeStampToString(model.getTimestamp()));
    holder.itemView.setOnClickListener(v -> {
        Intent intent = new Intent(context, NoteDetailActivity.class);
        intent.putExtra("title", model.getTitle());
        intent.putExtra("content", model.getContent());
        // get doc id
        String docID = this.getSnapshots().getSnapshot(position).getId();
        intent.putExtra("docID", docID);
        context.startActivity(intent);

    });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // khởi tạo 1 view gán dao diện recyclerview cho nó
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);

       // trả về 1 biến kiểu NoteViewHolder tham số là view vừa tạo
       return new NoteViewHolder(view);
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextview, contentTextview, timeStampTextview;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextview = itemView.findViewById(R.id.recycler_view_note_title);
            contentTextview = itemView.findViewById(R.id.recycler_view_note_content);
            timeStampTextview = itemView.findViewById(R.id.recycler_view_note_timestamp);
        }
    }
}
