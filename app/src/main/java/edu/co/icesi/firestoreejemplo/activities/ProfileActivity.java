package edu.co.icesi.firestoreejemplo.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;

import java.util.UUID;

import edu.co.icesi.firestoreejemplo.R;
import edu.co.icesi.firestoreejemplo.models.User;

public class ProfileActivity extends AppCompatActivity {

    private Button changepassBtn;
    private TextInputLayout newpassTI;

    private ImageView profileImg;
    private TextView profileUserTV;
    private TextView profileEmailTV;

    private ActivityResultLauncher<Intent> launcher;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = new Gson().fromJson(
                getSharedPreferences("appmoviles", MODE_PRIVATE).getString("user", "NO_USER"),
                User.class
        );

        getUpdatedUser();

        launcher = registerForActivityResult(new StartActivityForResult(), this::onGalleryResult);

        changepassBtn = findViewById(R.id.changepassBtn);
        newpassTI = findViewById(R.id.newpassTI);

        profileUserTV = findViewById(R.id.profileUserTV);
        profileUserTV.setText(user.getName());
        profileEmailTV = findViewById(R.id.profileEmailTV);
        profileEmailTV.setText(user.getEmail());

        profileImg = findViewById(R.id.profileImg);


        changepassBtn.setOnClickListener(
                v->{
                    changePassword();
                }
        );


        profileImg.setOnLongClickListener(
                v->{
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.setType("image/*");
                    launcher.launch(i);
                    return true;
                }
        );


    }

    private void getUpdatedUser() {
        FirebaseFirestore.getInstance().collection("users").document(user.getId()).get().addOnSuccessListener(
                task->{
                    User updatedUser = task.toObject(User.class);
                    String photoID = updatedUser.getPhotoID();
                    showImageFromStorage(photoID);
                }
        );
    }

    private void showImageFromStorage(String photoID) {
        String urlStored = getSharedPreferences("appmoviles", MODE_PRIVATE).getString(photoID, "");

        if(urlStored.isEmpty() && photoID != null) {
            FirebaseStorage.getInstance().getReference().child("profile").child(photoID).getDownloadUrl().addOnSuccessListener(
                    url -> {
                        Glide.with(this).load(url).into(profileImg);
                        getSharedPreferences("appmoviles", MODE_PRIVATE)
                                .edit()
                                .putString(photoID, url.toString())
                                .apply();
                    }
            );
        }else{
            Glide.with(this).load(urlStored).into(profileImg);
        }
    }

    private void onGalleryResult(ActivityResult result) {
        if(result.getResultCode() == RESULT_OK) {
            Uri uri = result.getData().getData();
            profileImg.setImageURI(uri);
            //Upload
            String photoID = UUID.randomUUID().toString();
            FirebaseStorage.getInstance().getReference().child("profile").child(photoID).putFile(uri);
            FirebaseFirestore.getInstance().collection("users").document(user.getId()).update("photoID", photoID);
        }
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