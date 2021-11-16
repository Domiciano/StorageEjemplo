package edu.co.icesi.firestoreejemplo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.co.icesi.firestoreejemplo.R;
import edu.co.icesi.firestoreejemplo.models.User;
import edu.co.icesi.firestoreejemplo.viewholders.ContactVH;

public class ContactAdapter extends RecyclerView.Adapter<ContactVH> {

    private ArrayList<User> users;

    public ContactAdapter(){
        users = new ArrayList<>();
    }

    @NonNull
    @Override
    public ContactVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.contactrow, parent, false);
        ContactVH contactVH = new ContactVH(rowView);
        return contactVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactVH holder, int position) {
        User user = users.get(position);
        holder.setUser(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void addUser(User user) {
        users.add(user);
        notifyItemInserted(users.size()-1);
    }

    public void clear() {
        int size = users.size();
        users.clear();
        notifyItemRangeRemoved(0, size);
    }
}
