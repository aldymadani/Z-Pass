package com.example.zpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zpass.model.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditPasswordActivity extends AppCompatActivity {

    private static final String TAG = "EditPasswordActivity";

    EditText accountNameEditText, emailEditText, passwordEditText;
    Button updatePasswordButton, deletePasswordButton;
    private Account account = new Account();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String accountId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        init();
    }

    private void init() {
        accountNameEditText = findViewById(R.id.EditTextAccountNameDetails);
        emailEditText = findViewById(R.id.EditTextEmailAddPasswordDetails);
        passwordEditText = findViewById(R.id.EditTextPasswordAddPasswordDetails);
        updatePasswordButton = findViewById(R.id.ButtonEditPassword);
        deletePasswordButton = findViewById(R.id.ButtonDeletePassword);

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldValidation(accountNameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString())) {
//                    DocumentReference accountRef = db.collection("account").document(accountId);
//                    accountRef.update()
                    db.collection("account").document(accountId)
                            .update(
                                    "accountName", accountNameEditText.getText().toString(),
                                    "email", emailEditText.getText().toString(),
                                    "password", passwordEditText.getText().toString()
                            ).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(EditPasswordActivity.this, "update success", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });

        deletePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldValidation(accountNameEditText.getText().toString(), emailEditText.getText().toString(),passwordEditText.getText().toString())) {
                    db.collection("account").document(accountId)
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    Toast.makeText(EditPasswordActivity.this, "Account is successfully deleted", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                }
            }
        });
    }

    private boolean fieldValidation(String accountName, String email, String password) {
        boolean accountNameValidation = false;
        boolean emailValidation = false;
        boolean passwordValidation = false;

        if (accountName.isEmpty()) {
            Toast.makeText(EditPasswordActivity.this, "Account Name is empty!", Toast.LENGTH_SHORT).show();
        } else {
            accountNameValidation = true;
        }

        if (email.isEmpty()) {
            Toast.makeText(EditPasswordActivity.this, "Email is empty!", Toast.LENGTH_SHORT).show();
        } else {
            emailValidation = true;
        }

        if (password.isEmpty()) {
            Toast.makeText(EditPasswordActivity.this, "Password is empty!", Toast.LENGTH_SHORT).show();
        } else {
            passwordValidation = true;
        }

        return (accountNameValidation && emailValidation && passwordValidation);
    }
}