package com.example.todoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = findViewById(R.id.lvItems);
        items = new ArrayList<>();
        readItems();

        /* Displays no data text if list is empty */
        View empty = getLayoutInflater().inflate(R.layout.empty_list_item, null, false);
        addContentView(empty, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        lvItems.setEmptyView(empty);

        itemsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        //items.add("Task 1");
        //items.add("Task 2");

        /* Deletes an item from the list upon short press */
        setupListViewListener();
    }

    public void addTask(View v){
        EditText etNewTask = findViewById(R.id.editText);
        itemsAdapter.add(etNewTask.getText().toString());
        etNewTask.setText("");
        saveItems();
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                saveItems();
                return true;
            }
        });
    }

    private void readItems(){
        File fileDir = getFilesDir();
        File toDoFile = new File(fileDir,"toDo.txt");
        try{
            items = new ArrayList<>(FileUtils.readLines(toDoFile, Charset.defaultCharset()));
        }catch (IOException e){
            items = new ArrayList<>();
            e.printStackTrace();
        }
    }

    private void saveItems(){
        File fileDir = getFilesDir();
        File toDoFile = new File(fileDir,"toDo.txt");
        try{
            FileUtils.writeLines(toDoFile, items);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
