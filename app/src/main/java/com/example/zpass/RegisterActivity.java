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

import com.example.zpass.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = nameEditText.getText().toString().trim();
                String strEmail = emailEditText.getText().toString().trim();
                String strPassword = passwordEditText.getText().toString().trim();
                String strConfirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (strName.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Name is empty", Toast.LENGTH_SHORT).show();
                }
                if (strEmail.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Email is empty", Toast.LENGTH_SHORT).show();
                }
                if (strPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Password is empty", Toast.LENGTH_SHORT).show();
                }
                if (strConfirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Confirm password is empty", Toast.LENGTH_SHORT).show();
                }
                if (!strPassword.equals(strConfirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Password is not same!", Toast.LENGTH_SHORT).show();
                }

                if (!strName.isEmpty() && !strEmail.isEmpty() && !strPassword.isEmpty() && !strConfirmPassword.isEmpty()) {
                    registerUser(nameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString());
                } else {
                    Toast.makeText(RegisterActivity.this, "Please input all information", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser(String name, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    final FirebaseUser currentUser = auth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();

                    currentUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                addUserToDatabase(name, email);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Something wrong during set name", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(RegisterActivity.this, "Email is already used", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Sign Up is unsuccessful, please try again", Toast.LENGTH_SHORT).show();
                    }
                    Log.e(TAG, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    private void addUserToDatabase(String name, String email) {
        String uuid = auth.getCurrentUser().getUid();
        if (uuid == null) {
            Toast.makeText(this, "UUID Null", Toast.LENGTH_SHORT).show();
            return;
        }
        user.setName(name);
        user.setEmail(email);
        user.setUuid(uuid);
        db.collection("user").document(uuid)
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "DocumentSnapshot successfully written!\nThe Unique ID of user is : " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
            }
        });
    }

    private void init() {
        nameEditText = findViewById(R.id.EditTextNameRegister);
        emailEditText = findViewById(R.id.EditTextEmailRegister);
        passwordEditText = findViewById(R.id.EditTextPasswordRegister);
        confirmPasswordEditText = findViewById(R.id.EditTextConfirmPasswordRegister);
        registerButton = findViewById(R.id.ButtonRegister);
    }
}