package com.example.budgetpal;

import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetpal.ui.login.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class addAccount extends AppCompatActivity implements ChatAdapter.OnChatItemClickListener{

    private static final String DATABASE_NAME = "accounts.db";
    private static final int DATABASE_VERSION = 1;
    private DatabaseHelper databaseHelper;

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
        chatAdapter.setOnChatItemClickListener(this); // Set the click listener
        recyclerView.setAdapter(chatAdapter);

        // Initialize the DatabaseHelper
        databaseHelper = new DatabaseHelper(this);
    }
    @Override
    public void onChatItemClick(String senderAddress) {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(senderAddress);
        builder.setMessage("Add this as an account?");

        // Set the positive button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the "Yes" case here
                // You can add code to save the chat as an account
                Toast.makeText(addAccount.this, "Account added", Toast.LENGTH_SHORT).show();
                // Save the chat as an account
                long accountId = saveAccountAndContent(senderAddress);
                if (accountId != -1) {
                    Toast.makeText(addAccount.this, "Account added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(addAccount.this, "Failed to add account", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set the negative button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(addAccount.this, "Operation cancelled!", Toast.LENGTH_SHORT).show();
                // Handle the "No" case here
                // No action required, the dialog will be dismissed
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private List<Chat> getActualChatList() {
        // Fetch chat data from your data source (e.g., database, network API)
        List<Chat> chatList = new ArrayList<>();
        // For example, if your data source returns a list of strings
        List<Chat> chatMessages = fetchChatMessagesFromDataSource();
        // Convert fetched data to Chat objects
        for (Chat message : chatMessages) {
            chatList.add(new Chat(message.getSender(), message.getMessage(), message.getTime()));
        }

        return chatList;
    }
    private List<Chat> fetchChatMessagesFromDataSource() {
        List<Chat> chatMessages = new ArrayList<>();
        List<String> addresses = new ArrayList<>();

        try {
            // Get a reference to the SMS inbox URI
            Uri inboxUri = Uri.parse("content://sms/inbox");

            // Define the columns you want to retrieve
            String[] projection = new String[]{"body", "address", "date"};

            // Create a cursor to iterate over the SMS inbox
            Cursor cursor = getContentResolver().query(inboxUri, projection, null, null, null);

            // Iterate over the cursor and create Chat objects
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String messageBody = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                    String senderAddress = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow("date"));

                    // Convert timestamp to a readable format
                    String formattedTimestamp = formatTimestamp(timestamp);

                    if (!addresses.contains(senderAddress)) {
                        // If not, add it to the list and create a new Chat object
                        addresses.add(senderAddress);
                        // Create a Chat object and add it to the list
                        Chat chat = new Chat(senderAddress, messageBody, formattedTimestamp);
                        chatMessages.add(chat);
                    }
                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return chatMessages;
    }

    private String formatTimestamp(long timestamp) {
        return String.valueOf(timestamp);
    }
    private long saveAccountAndContent(String senderAddress) {
        // Save the account information
        long accountId = databaseHelper.insertAccount(senderAddress);
        if (accountId != -1) {
            // Fetch the messages for the sender's address
            List<Chat> chatMessages = fetchChatMessagesForSender(senderAddress);
            // Save the message content
            for (Chat chatMessage : chatMessages) {
                databaseHelper.insertAccountContent(accountId, chatMessage.getMessage(), "SMS", 0.0, chatMessage.getTime());
            }
        }
        return accountId;
    }
    private List<Chat> fetchChatMessagesForSender(String senderAddress) {
        List<Chat> chatMessages = new ArrayList<>();

        try {
            // Get a reference to the SMS inbox URI
            Uri inboxUri = Uri.parse("content://sms/inbox");

            // Define the columns you want to retrieve
            String[] projection = new String[]{"body", "address", "date"};

            // Create a cursor to iterate over the SMS inbox
            Cursor cursor = getContentResolver().query(inboxUri, projection, null, null, null);

            // Iterate over the cursor and create Chat objects for the specified sender
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String messageBody = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                    String sender = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow("date"));

                    // Convert timestamp to a readable format
                    String formattedTimestamp = formatTimestamp(timestamp);

                    // Add the chat message to the list if it's from the specified sender
                    if (sender.equals(senderAddress)) {
                        Chat chat = new Chat(sender, messageBody, formattedTimestamp);
                        chatMessages.add(chat);
                    }
                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return chatMessages;
    }
}