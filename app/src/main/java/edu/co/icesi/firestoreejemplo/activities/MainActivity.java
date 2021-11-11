package edu.co.icesi.firestoreejemplo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.UUID;

import edu.co.icesi.firestoreejemplo.R;
import edu.co.icesi.firestoreejemplo.models.User;
import edu.co.icesi.firestoreejemplo.util.NotificationUtil;

public class MainActivity extends AppCompatActivity {


    private TextInputEditText usernameET;
    private TextInputEditText passET;
    private Button loginBtn;
    private TextView noaccountTV;

    private TextView forgotpassTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameET = findViewById(R.id.usernameET);
        passET = findViewById(R.id.passET);
        loginBtn = findViewById(R.id.loginBtn);
        noaccountTV = findViewById(R.id.noaccountTV);
        forgotpassTV = findViewById(R.id.forgotpassTV);


        loginBtn.setOnClickListener(this::login);


        NotificationUtil.showNotification(this, "AppMoviles","Hola mundo");

        FirebaseMessaging.getInstance().subscribeToTopic("promo");

        noaccountTV.setOnClickListener(
                v->{
                    Intent intent = new Intent(this, SignupActivity.class);
                    startActivity(intent);
                }
        );


        forgotpassTV.setOnClickListener(
                v->{
                    FirebaseAuth.getInstance().sendPasswordResetEmail(usernameET.getText().toString())
                    .addOnSuccessListener(
                            task->{
                                Toast.makeText(this, "Revise el email "+usernameET.getText().toString(), Toast.LENGTH_LONG).show();
                            }
                    )
                    .addOnFailureListener(
                            error->{
                                Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    );
                }
        );

    }

    private void login(View view) {
        String email = usernameET.getText().toString();
        String pass = passET.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener(
                        task->{
                            FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
                            if(fbuser.isEmailVerified()){
                                //Le damos acceso

                                //Pedir el usuario almacenado en firestore
                                FirebaseFirestore.getInstance().collection("users").document(fbuser.getUid()).get().addOnSuccessListener(
                                        document->{
                                            User user = document.toObject(User.class);
                                            saveUser(user);
                                            Intent intent = new Intent(this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                );

                            }else{
                                Toast.makeText(this, "Su email no está verificado", Toast.LENGTH_LONG).show();
                            }
                        }
                ).addOnFailureListener(
                    error->Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show()
                );
        
    }

    private void saveUser(User user) {
        String json = new Gson().toJson(user);
        getSharedPreferences("appmoviles", MODE_PRIVATE).edit().putString("user", json).apply();

    }
}