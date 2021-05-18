package com.toman.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.toman.todo.dto.TodoItem;

import java.util.Objects;

public class Todo extends AppCompatActivity {

    private TodosService todosService = new TodosService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        Intent intent = getIntent();
        Long itemId = getItemIdFromIntent(intent);
        if(itemId != null) {
            String itemObjective = intent.getExtras().getString("itemObjective");
            EditText todoText = (EditText) findViewById(R.id.todoMultiline);
            todoText.setText(itemObjective);
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle("Upraviť záznam");

        Button button = findViewById(R.id.deleteBt);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long id = getIntent().getExtras().getLong("itemId");
                todosService.deleteData(Todo.this, id);
                Intent intent = new Intent(Todo.this, MainActivity.class);
                intent.putExtra("deletedId", id);
                startActivity(intent);
            }
        });
    }

    private Long getItemIdFromIntent(Intent intent) {
        if(intent == null || intent.getExtras() == null) {
            return null;
        }
        Long itemId = null;
        try {
            itemId = intent.getExtras().getLong("itemId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemId;
    }

    private void saveTodoItem(Long id, String objective) {
        TodoItem todoItem = new TodoItem();
        todoItem.setId(id);
        todoItem.setObjective(objective);
        todosService.saveData(this, todoItem);
    }

    @Override
    public void onBackPressed() {
        Long itemId = getItemIdFromIntent(getIntent());
        if(itemId != null) {
            String originalObjective = getIntent().getExtras().getString("objective");
            EditText todoText = (EditText) findViewById(R.id.todoMultiline);
            String objective = todoText.getText().toString();
            if (!objective.isEmpty() || !objective.equals(originalObjective)) {
                saveTodoItem(itemId, objective);
            }
        }
        super.onBackPressed();
    }


}