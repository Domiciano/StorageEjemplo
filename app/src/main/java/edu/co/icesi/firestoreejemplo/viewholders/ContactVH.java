package edu.co.icesi.firestoreejemplo.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;

import edu.co.icesi.firestoreejemplo.R;
import edu.co.icesi.firestoreejemplo.models.User;

public class ContactVH extends RecyclerView.ViewHolder {


    private User contact;

    private ImageView userImg;
    private TextView userNameTV;
    private TextView userEmailTV;
    private Button actionBtn;

    public ContactVH(View root) {
        super(root);
        userImg = root.findViewById(R.id.contactImg);
        userNameTV = root.findViewById(R.id.contactNameTV);
        userEmailTV = root.findViewById(R.id.contactEmailTV);
        actionBtn = root.findViewById(R.id.actionBtn);
    }

    public User getContact() {
        return contact;
    }

    public void setContact(User contact) {
        this.contact = contact;
        userNameTV.setText(contact.getName());
        userEmailTV.setText(contact.getEmail());
        

        String urlStored = userImg.getContext().getSharedPreferences("appmoviles", Context.MODE_PRIVATE).getString(contact.getPhotoID(), "");
        if(urlStored.isEmpty() && contact.getPhotoID() != null) {
            FirebaseStorage.getInstance().getReference().child("profile").child(contact.getPhotoID()).getDownloadUrl().addOnSuccessListener(
                    url -> {
                        Glide.with(userImg).load(url).into(userImg);
                        userImg.getContext().getSharedPreferences("appmoviles", Context.MODE_PRIVATE)
                                .edit()
                                .putString(contact.getPhotoID(), url.toString())
                                .apply();
                    }
            );
        }else{
            Glide.with(userImg).load(urlStored).into(userImg);
        }


    }

    public ImageView getUserImg() {
        return userImg;
    }

    public TextView getUserNameTV() {
        return userNameTV;
    }

    public TextView getUserEmailTV() {
        return userEmailTV;
    }
}
