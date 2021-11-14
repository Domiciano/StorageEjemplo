package edu.co.icesi.firestoreejemplo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.co.icesi.firestoreejemplo.R;
import edu.co.icesi.firestoreejemplo.models.User;
import edu.co.icesi.firestoreejemplo.viewholders.UserVH;

public class UserAdapter extends RecyclerView.Adapter<UserVH> {

    private ArrayList<User> users;

    public UserAdapter(){
        users = new ArrayList<>();
    }

    @NonNull
    @Override
    public UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.userrow, parent, false);
        UserVH userVH = new UserVH(root);
        return userVH;
    }

    @Override
    public void onBindViewHolder(@NonNull UserVH holder, int position) {
        User user = users.get(position);
        holder.getUserNameTV().setText(user.getName());
        holder.getUserEmailTV().setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
