package com.example.zpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zpass.model.Account;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddPasswordActivity extends AppCompatActivity {

    private static final String TAG = "AddPasswordActivity";

    EditText accountNameEditText, emailEditText, passwordEditText;
    Button addPasswordButton;
    private Account account = new Account();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);
        init();
    }

    private void init() {
        accountNameEditText = findViewById(R.id.EditTextAccountName);
        emailEditText = findViewById(R.id.EditTextEmailAddPassword);
        passwordEditText = findViewById(R.id.EditTextPasswordAddPassword);
        addPasswordButton = findViewById(R.id.ButtonAddPassword);

        addPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean accountNameValidation = false;
                boolean emailValidation = false;
                boolean passwordValidation = false;

                if(!accountNameEditText.getText().toString().isEmpty()) {
                    accountNameValidation = true;
                } else {
                    Toast.makeText(AddPasswordActivity.this, "Account Name is empty!", Toast.LENGTH_SHORT).show();
                }

                if(!emailEditText.getText().toString().isEmpty()) {
                    emailValidation = true;
                } else {
                    Toast.makeText(AddPasswordActivity.this, "Email is empty!", Toast.LENGTH_SHORT).show();
                }

                if(!passwordEditText.getText().toString().isEmpty()) {
                    passwordValidation = true;
                } else {
                    Toast.makeText(AddPasswordActivity.this, "Password is empty!", Toast.LENGTH_SHORT).show();
                }

                if (accountNameValidation && emailValidation && passwordValidation) {
                    addAccountToDatabase(accountNameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString());
                }
            }
        });
    }

    private void addAccountToDatabase(String accountName, String email, String password) {
        String uuid = auth.getCurrentUser().getUid();
        if (uuid == null) {
            Toast.makeText(this, "UUID Null", Toast.LENGTH_SHORT).show();
            return;
        }

        String accountId = db.collection("account").document().getId();

        // Set the data to Account model
        account.setAccountName(accountName);
        account.setEmail(email);
        account.setPassword(password);
        account.setUserUUID(uuid);
        account.setAccountId(accountId);
        db.collection("account").document(accountId)
                .set(account).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
//                Intent intent = new Intent(AddPasswordActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
            }
        });
    }
}