package com.example.notetaker;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditNoteActivity extends ActionBarActivity 
{
    public static final int RESULT_DELETE = -500;
    private boolean isInEditMode = true;
    private boolean isAddingNote = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        
        final Button saveButton = (Button)findViewById(R.id.saveButton);
        final Button cancelButton = (Button)findViewById(R.id.cancelButton);
        final EditText titleEditText = (EditText)findViewById(R.id.titleEditText);
        final EditText noteEditText = (EditText)findViewById(R.id.noteEditText);
        final TextView dateTextView = (TextView)findViewById(R.id.dateValueTextView);
        
        Serializable extra = getIntent().getSerializableExtra("Note");
        if (extra != null)
        {
            Note note = (Note)extra;
            titleEditText.setText(note.getTitle());
            noteEditText.setText(note.getNote());
            
            DateFormat dataFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            String date = dataFormat.format(note.getDate());
            
            dateTextView.setText(date);
            
            isInEditMode = false;
            titleEditText.setEnabled(false);
            noteEditText.setEnabled(false);
            
            saveButton.setText(R.string.saveButtonEdit);
            
            isAddingNote = false;
        }
        
        saveButton.setOnClickListener(new OnClickListener() 
            {
                @Override
                public void onClick(View v)
                {
                    if (isInEditMode)
                    {
                        Intent returnIntent = new Intent();
                        Note note = new Note(
                                titleEditText.getText().toString(),
                                noteEditText.getText().toString(),
                                Calendar.getInstance().getTime());
                        returnIntent.putExtra("Note", note);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                    else
                    {
                        isInEditMode = true;
                        saveButton.setText(R.string.saveButtonSave);
                        titleEditText.setEnabled(true);
                        noteEditText.setEnabled(true);
                    }
                }
            });
        
        cancelButton.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    setResult(RESULT_CANCELED, new Intent());
                    finish();
                }
                
            });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        if (isAddingNote)
        {
            menu.removeItem(R.id.deleteNote);
        }
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.deleteNote) 
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.deleteAlertDialogMessage);
            builder.setTitle(R.string.deleteAlertDialogTitle);
            builder.setPositiveButton(R.string.deleteAlertDialogDeleteButton, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent retIntent = new Intent();
                        setResult(RESULT_DELETE, retIntent);
                        finish();
                    }
                });
            builder.setNegativeButton(R.string.deleteAlertDialogCancelButton, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    // TODO Auto-generated method stub
                    
                }
            });
            
            builder.create().show();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
