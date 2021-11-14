package edu.co.icesi.firestoreejemplo.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.co.icesi.firestoreejemplo.R;

public class UserVH extends RecyclerView.ViewHolder {

    private ImageView userImg;
    private TextView userNameTV;
    private TextView userEmailTV;

    public UserVH(View root) {
        super(root);
        userImg = root.findViewById(R.id.userImg);
        userNameTV = root.findViewById(R.id.userNameTV);
        userEmailTV = root.findViewById(R.id.userEmailTV);
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
