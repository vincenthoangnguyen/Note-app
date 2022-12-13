package com.vincent.hoangnguyen.notepro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton add_note_btn;
    RecyclerView recyclerView;
    ImageButton menu_btn;
    NoteAdapter noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_note_btn = findViewById(R.id.add_note_btn);
        recyclerView = findViewById(R.id.recycler_view);
        menu_btn= findViewById(R.id.menu_btn);
        add_note_btn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,NoteDetailActivity.class)));
        menu_btn.setOnClickListener(v-> showMenu());
        // setUp RecyclerView
        setUpRecyclerView();
        
    }
    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, menu_btn);
        popupMenu.getMenu().add("LogOut");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle() == "LogOut"){
                    // đăng xuất khỏi firebase
                    FirebaseAuth.getInstance().signOut();
                    // chuyển tới màn hình login
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }

                return false;
            }
        });
    }

    private void setUpRecyclerView() {
        // truy vấn database
        // orderby -> sắp xếp theo thuộc tính thời gian, giảm dần(DESENDING)
        Query query = Utility.getCollectionReferenceForeNotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        // set layout manager cho recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options, this);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}