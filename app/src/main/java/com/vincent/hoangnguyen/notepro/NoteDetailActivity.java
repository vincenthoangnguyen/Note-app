package com.vincent.hoangnguyen.notepro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailActivity extends AppCompatActivity {
    EditText note_title_edt, note_content_edt;
    ImageButton done_btn;
    TextView statusBar;
    boolean isInEdit =false;
    String title,content,docID;
    TextView delete_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        note_title_edt = findViewById(R.id.note_title);
        note_content_edt = findViewById(R.id.note_content);
        statusBar = findViewById(R.id.status_bar_textview);
        done_btn = findViewById(R.id.done_save_note);
        delete_btn =findViewById(R.id.delete_note_tv_btn);
        // add event for save button
      
        Intent intent =getIntent();
         title = intent.getStringExtra("title");
         content = intent.getStringExtra("content");
         docID = intent.getStringExtra("docID");
        if (docID != null && !docID.isEmpty()){
            isInEdit = true;
        }


        note_title_edt.setText(title);
        note_content_edt.setText(content);
        if(isInEdit){
            statusBar.setText("Edit your note");
            delete_btn.setVisibility(View.VISIBLE);
        }
        
        done_btn.setOnClickListener(v -> saveNote());
        delete_btn.setOnClickListener(v -> deleteNoteFromFirebase());
    }



    private void saveNote() {
        String title = note_title_edt.getText().toString();
        String content = note_content_edt.getText().toString();
        if(title.isEmpty()){
            note_title_edt.setError("Title is required");
            return;
        }
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        // TimeStamp là thời gian lưu note
        note.setTimestamp(Timestamp.now());
        saveNoteToFirebase(note);
    }
    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if(isInEdit){
            //update a note
            documentReference = Utility.getCollectionReferenceForeNotes().document(docID);

        }
        else {
            // create new
            documentReference = Utility.getCollectionReferenceForeNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //notes is added
                    Utility.showToast(NoteDetailActivity.this,"Note added successfully");
                    finish();
                }else {
                    Utility.showToast(NoteDetailActivity.this,"Failed while adding note");
                }
            }
        });
    }
    private void deleteNoteFromFirebase() {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForeNotes().document(docID);
        // docID để tham chiếu đến sơ sở dữ liệu của note
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //notes is added
                    Utility.showToast(NoteDetailActivity.this,"Note deleted successfully");
                    finish();
                }else {
                    Utility.showToast(NoteDetailActivity.this,"Failed while deleting note");
                }
            }
        });

    }
}