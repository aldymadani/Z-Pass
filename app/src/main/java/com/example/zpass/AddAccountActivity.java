package com.example.zpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zpass.model.Account;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddAccountActivity extends AppCompatActivity {

    private static final String TAG = AddAccountActivity.class.getSimpleName();

    private TextInputEditText etName, etEmail, etPassword;
    private TextInputLayout layoutName, layoutEmail, layoutPassword;
    private Button btnAddAccount;
    private final Account account = new Account();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        init();
    }

    private void init() {
        etName = findViewById(R.id.EditTextAccountName);
        etEmail = findViewById(R.id.EditTextEmailAddAccount);
        etPassword = findViewById(R.id.EditTextPasswordAddAccount);
        layoutName = findViewById(R.id.TextLayoutAccountName);
        layoutEmail = findViewById(R.id.TextLayoutEmailAddAccount);
        layoutPassword = findViewById(R.id.TextLayoutPasswordAddAccount);
        btnAddAccount = findViewById(R.id.ButtonAddPassword);

        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                layoutName.setError(null);
                layoutName.setErrorEnabled(false);
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                layoutEmail.setError(null);
                layoutEmail.setErrorEnabled(false);
            }
        });

        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    account.setName(etName.getText() != null ? etName.getText().toString() : null);
                    account.setEmail(etEmail.getText() != null ? etEmail.getText().toString() : null);
                    account.setPassword(etPassword.getText() != null ? etPassword.getText().toString() : null);

                    if (validateData(account)) {
                        addAccount(account);
                    }
                } catch (Exception e) {
                    Toast.makeText(AddAccountActivity.this, "Error occurred.", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error occurred: " + e.getLocalizedMessage());
                }
            }
        });
    }

    private boolean validateData(Account account) {
        if (!account.checkEmptyData()) {
            return true;
        }

        switch (account.checkEmptyInput()) {
            case Account.FIELD_NAME:
                layoutName.setError("Name is empty!");
                return false;
            case Account.FIELD_EMAIL:
                layoutEmail.setError("Email is empty!");
                return false;
            case Account.FIELD_PASSWORD:
                layoutPassword.setError("Password is empty!");
                return false;
            default:
                Toast.makeText(AddAccountActivity.this, "Error occureed.", Toast.LENGTH_LONG).show();
                return false;
        }
    }

    private void addAccount(Account account) {
        String uuid = auth.getCurrentUser().getUid();
        if (uuid == null) {
            Toast.makeText(this, "UUID Null", Toast.LENGTH_SHORT).show();
            return;
        }

        String accountId = db.collection("account").document().getId();

        // Set the data to Account model
        account.setName(account.getName());
        account.setEmail(account.getEmail());
        account.setPassword(account.getPassword());
        account.setOwner(uuid);
        account.setId(accountId);
        account.setImageUrl("https://firebasestorage.googleapis.com/v0/b/z-pass-6377f.appspot.com/o/facebook.png?alt=media&token=0064e6b8-f843-47d4-8445-c0bfdb37f434");
        db.collection("account")
                .document(accountId)
                .set(account)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
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