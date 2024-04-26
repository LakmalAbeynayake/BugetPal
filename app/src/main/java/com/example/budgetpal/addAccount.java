package com.example.budgetpal;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetpal.ui.login.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class addAccount extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize and set adapter
        chatAdapter = new ChatAdapter(getActualChatList());
        recyclerView.setAdapter(chatAdapter);
    }

    // Method to generate sample chat data
    private List<Chat> getSampleChatList() {
        List<Chat> chatList = new ArrayList<>();
        // Add sample chat items here (you may replace this with actual chat data)
        chatList.add(new Chat("John", "Hello!", "10:00 AM"));
        chatList.add(new Chat("Alice", "Hi there!", "10:05 AM"));
        chatList.add(new Chat("Bob", "How are you?", "10:10 AM"));
        // Add more chat items as needed
        return chatList;
    }
    private List<Chat> getActualChatList() {
        List<Chat> chatList = new ArrayList<>();

        // Fetch chat data from your data source (e.g., database, network API)
        // For example, if your data source returns a list of strings
        List<String> chatMessages = fetchChatMessagesFromDataSource();

        // Assume a default sender and timestamp for simplicity
        String defaultSender = "John Doe";
        String defaultTimestamp = "12:00 PM";

        // Convert fetched data to Chat objects
        for (String message : chatMessages) {
            chatList.add(new Chat(defaultSender, message, defaultTimestamp));
        }

        return chatList;
    }
    private List<String> fetchChatMessagesFromDataSource() {
        List<String> chatMessages = new ArrayList<>();

        try {
            // Get a reference to the SMS inbox URI
            Uri inboxUri = Uri.parse("content://sms/inbox");

            // Define the columns you want to retrieve
            String[] projection = new String[]{"body"};

            // Create a cursor to iterate over the SMS inbox
            Cursor cursor = getContentResolver().query(inboxUri, projection, null, null, null);

            // Iterate over the cursor and add each message body to the list
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String messageBody = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                    chatMessages.add(messageBody);
                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return chatMessages;
    }
}