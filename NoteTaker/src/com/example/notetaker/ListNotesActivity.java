package com.example.notetaker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListNotesActivity extends ActionBarActivity 
{
    private List<Note> notes = new ArrayList<Note>();
    private ListView notesListView;
    private int editingNoteId = -1;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);
        notesListView = (ListView)findViewById(R.id.notesListView);
        
        notesListView.setOnItemClickListener(new OnItemClickListener() 
            {
                @Override
                public void onItemClick(
                        AdapterView<?> adapter, 
                        View view,
                        int itemNumber, 
                        long id)
                {
                    Intent editNoteIntent = new Intent(view.getContext(), EditNoteActivity.class);
                    editNoteIntent.putExtra("Note", notes.get(itemNumber));
                    editingNoteId = itemNumber;
                    startActivityForResult(editNoteIntent, 1);
                }
            });
        
        registerForContextMenu(notesListView);
        
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        
        loadList();
        populateList();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_notes, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addNoteItem) 
        {
            // notes.add(new Note("Added note", "Wei Ren", new Date()));
            
            // populateList();
            
            Intent editNoteIntent = new Intent(this, EditNoteActivity.class);
            startActivityForResult(editNoteIntent, 1);
        }
        
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_CANCELED)
        {
            return;
        }
        else if (resultCode == EditNoteActivity.RESULT_DELETE)
        {
            notes.remove(editingNoteId);
            editingNoteId = -1;
            populateList();
        }
        else
        {
            Serializable extra = data.getSerializableExtra("Note");
            if (extra != null)
            {
                Note newNote = (Note)extra;
                if (editingNoteId > -1)
                {
                    notes.set(editingNoteId, newNote);
                    editingNoteId = -1;
                }
                else
                {
                    notes.add(newNote);
                }
                
                populateList();
            }
        }
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
        notes.remove(info.position);
        populateList();
        
        return true;
    }

    @Override
    protected void onStop()
    {
        saveList();
        
        super.onStop();
    }

    private void loadList()
    {
        int cntNotes = sharedPref.getInt(getString(R.string.cntnotes), 0);
        for (int i = 0; i < cntNotes; i++)
        {
            // Load note title.
            String noteTitle = sharedPref.getString(getString(R.string.noteTitle) + i, null);
            if (noteTitle == null)
            {
                noteTitle = getString(R.string.notAvailable);
            }
            
            // Load note date.
            String noteDate = sharedPref.getString(getString(R.string.noteDate) + i, null);
            if (noteDate == null)
            {
                noteDate = "00-00-0000 00:00:00";
            }
            
            // Load note content.
            String noteContent = sharedPref.getString(getString(R.string.noteContent) + i, null);
            if (noteContent == null)
            {
                noteContent = getString(R.string.notAvailable);
            }
            
            notes.add(new Note(noteTitle, noteDate, noteContent));
        }
    }
    
    private void saveList()
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        int cntNotes = notes.size();
        
        editor.putInt(getString(R.string.cntnotes), cntNotes);
        
        int i = 0;
        for (Note note : notes)
        {
            // Save note title.
            editor.putString(getString(R.string.noteTitle) + i, note.getTitle());
            
            // Save note date.
            editor.putString(getString(R.string.noteDate) + i, note.getDate());
            
            // Save note content.
            editor.putString(getString(R.string.noteContent) + i, note.getNote());
            
            i++;
        }
        
        editor.commit();
    }
    
    private void populateList()
    {
        List<String> values = new ArrayList<String>();
        for (Note note : notes)
        {
            values.add(note.getTitle());
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            this, 
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            values);
      
        notesListView.setAdapter(adapter);
    }
}
