package com.example.zpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.zpass.adapter.AccountAdapter;
import com.example.zpass.model.Account;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addNewPasswordFAB;
    RecyclerView accountRecyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference accountRef = db.collection("account");
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private AccountAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setUpRecyclerView();
    }

    private void init() {
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);


        addNewPasswordFAB = findViewById(R.id.ButtonAddAccount);
        accountRecyclerView = findViewById(R.id.RecyclerViewAccount);
        addNewPasswordFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpRecyclerView() {
        String uuid = auth.getCurrentUser().getUid();
        Query query = accountRef.whereEqualTo("owner", uuid);

        FirestoreRecyclerOptions<Account> options = new FirestoreRecyclerOptions.Builder<Account>()
                .setQuery(query, Account.class)
                .build();

        adapter = new AccountAdapter(options, this);
        accountRecyclerView.setHasFixedSize(true);
        accountRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        accountRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logoutButton) {
            this.logoutUser();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUser() {
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }
}