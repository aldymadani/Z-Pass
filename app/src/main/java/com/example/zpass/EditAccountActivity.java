package com.example.zpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zpass.model.Account;
import com.example.zpass.utils.IntentNameExtra;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class EditAccountActivity extends AppCompatActivity {

    private static final String TAG = "EditPasswordActivity";
    private static final int INTENT_AUTHENTICATE = 100;

    private TextInputEditText etName, etEmail, etPassword;
    private TextInputLayout layoutName, layoutEmail, layoutPassword;

    private Button btnUpdateAccount;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        account = getIntent().getParcelableExtra(IntentNameExtra.ACCOUNT);

        init();
    }

    private void init() {
        etName = findViewById(R.id.EditTextAccountName);
        etEmail = findViewById(R.id.EditTextEmailEditAccount);
        etPassword = findViewById(R.id.EditTextPasswordEditAccount);
        layoutName = findViewById(R.id.TextLayoutAccountName);
        layoutEmail = findViewById(R.id.TextLayoutEmailEditAccount);
        layoutPassword = findViewById(R.id.TextLayoutPasswordEditAccount);
        btnUpdateAccount = findViewById(R.id.ButtonUpdateAccount);

        etName.setText(account.getName());
        etEmail.setText(account.getEmail());
        etPassword.setText(account.getPassword());

        /*layoutPassword.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/

        layoutPassword.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // etPassword.setTransformationMethod(null);

                KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

                if (km.isKeyguardSecure()) {
                    Intent authIntent = km.createConfirmDeviceCredentialIntent("Identity confirmation", "Authenticate now");
                    startActivityForResult(authIntent, INTENT_AUTHENTICATE);
                }
            }
        });

        btnUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    account.setName(etName.getText() != null ? etName.getText().toString() : null);
                    account.setEmail(etEmail.getText() != null ? etEmail.getText().toString() : null);
                    account.setPassword(etPassword.getText() != null ? etPassword.getText().toString() : null);

                    Log.d(TAG, "Account: " + account.toString());

                    if (validateData(account)) {
                        updateAccount();
                    }
                } catch (Exception e) {
                    Toast.makeText(EditAccountActivity.this, "Error occurred.", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error occurred: " + e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_AUTHENTICATE) {
            if (resultCode == RESULT_OK) {
                layoutPassword.setEndIconMode(TextInputLayout.END_ICON_NONE);
                etPassword.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        }
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
                Toast.makeText(EditAccountActivity.this, "Error occurred.", Toast.LENGTH_LONG).show();
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteButton) {
            this.deleteAccount();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void updateAccount() {
        db.collection("account").document(account.getId())
                .update(account.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditAccountActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void deleteAccount() {
        db.collection("account").document(account.getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        Toast.makeText(EditAccountActivity.this, "Account is successfully deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}