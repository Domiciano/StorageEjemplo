package edu.co.icesi.firestoreejemplo.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import edu.co.icesi.firestoreejemplo.R;

public class ProfileActivity extends AppCompatActivity {

    private Button changepassBtn;
    private TextInputLayout newpassTI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        changepassBtn = findViewById(R.id.changepassBtn);
        newpassTI = findViewById(R.id.newpassTI);


        changepassBtn.setOnClickListener(
                v->{
                    changePassword();
                }
        );


    }

    private void changePassword() {


        if(newpassTI.getEditText().getText().toString().trim().isEmpty()){
            newpassTI.setError("El campo está vacío");
            return;
        } else newpassTI.setError(null);
        if(newpassTI.getEditText().getText().toString().trim().length() < 6){
            newpassTI.setError("La contraseña debe ser al menos de 6 caracteres");
            return;
        } else newpassTI.setError(null);


        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Cambio de contraseña")
                .setMessage("¿Desea cambiar su contraseña?")
                .setPositiveButton("Si", (dialog, id)->{

                    FirebaseAuth.getInstance().getCurrentUser()
                            .updatePassword(newpassTI.getEditText().getText().toString().trim()).addOnSuccessListener(
                                    task->{
                                        Toast.makeText(this, "Contraseña cambiada!", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                    ).addOnFailureListener(
                            error->{
                                Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                    );

                })
                .setNegativeButton("No", (dialog, id)->{
                    dialog.dismiss();
                });
        builder.show();

    }
}