package edu.co.icesi.firestoreejemplo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.co.icesi.firestoreejemplo.R;
import edu.co.icesi.firestoreejemplo.models.User;

public class SignupActivity extends AppCompatActivity {

    private TextView ihaveacountTV;

    private TextInputLayout nameTI, emailTI, passTI, repassTI;
    private Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ihaveacountTV = findViewById(R.id.ihaveacountTV);
        nameTI = findViewById(R.id.nameTI);
        emailTI = findViewById(R.id.emailTI);
        passTI = findViewById(R.id.passTI);
        repassTI = findViewById(R.id.repassTI);
        signupBtn = findViewById(R.id.signupBtn);

        ihaveacountTV.setOnClickListener(
                v-> finish()

        );

        signupBtn.setOnClickListener(this::register);



    }

    private void register(View view) {

        //1. Registrarse en la db de auth
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                emailTI.getEditText().getText().toString(),
                passTI.getEditText().getText().toString()
        ).addOnSuccessListener(
                task->{
                    //2. Registrar al usuario en la base de datos
                    FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
                    User user = new User(
                            fbUser.getUid(),
                            nameTI.getEditText().getText().toString(),
                            emailTI.getEditText().getText().toString()
                    );
                    FirebaseFirestore.getInstance().collection("users").document(user.getId()).set(user).addOnSuccessListener(
                            firetask->{
                                sendVerificationEmail();
                                finish();
                            }
                    );
                }
        ).addOnFailureListener(
                error->{
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );

    }

    private void sendVerificationEmail() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(
                task->{
                    Toast.makeText(this, "Verifique su email antes de entrar", Toast.LENGTH_LONG).show();
                }
        ).addOnFailureListener(
                error->{
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );
    }
}