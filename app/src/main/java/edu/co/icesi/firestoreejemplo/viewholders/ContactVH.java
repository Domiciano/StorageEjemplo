package edu.co.icesi.firestoreejemplo.viewholders;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import edu.co.icesi.firestoreejemplo.R;
import edu.co.icesi.firestoreejemplo.activities.ChatActivity;
import edu.co.icesi.firestoreejemplo.models.User;

public class ContactVH extends RecyclerView.ViewHolder {


    //State
    private User contact;

    //UI
    private ImageView userImg;
    private TextView userNameTV;
    private TextView userEmailTV;
    private Button actionBtn;


    public ContactVH(@NonNull View itemView) {
        super(itemView);

        userImg = itemView.findViewById(R.id.contactImg);
        userNameTV = itemView.findViewById(R.id.contactNameTV);
        userEmailTV = itemView.findViewById(R.id.contactEmailTV);
        actionBtn = itemView.findViewById(R.id.actionBtn);

        actionBtn.setOnClickListener(v->openChat());
    }


    public void setUser(User user) {
        this.contact = user;
        userNameTV.setText(user.getName());
        userEmailTV.setText(user.getEmail());

        //Download photo
        if(user.getPhotoID() != null){
            FirebaseStorage.getInstance().getReference().child("profile").child(user.getPhotoID()).getDownloadUrl().addOnSuccessListener(
                    url->{
                        Glide.with(this.userImg).load(url).into(this.userImg);
                    }
            );
        }

    }

    public void openChat(){
        User contact = this.contact;
        User user = loadUser();
        Intent intent = new Intent(this.actionBtn.getContext(), ChatActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("contact", contact);
        this.actionBtn.getContext().startActivity(intent);
    }

    private User loadUser() {
        String json = this.actionBtn.getContext().getSharedPreferences("appmoviles", Context.MODE_PRIVATE).getString("user", "NO_USER");
        if(json.equals("NO_USER")){
            return null;
        }else{
            return new Gson().fromJson(json, User.class);
        }
    }

}
