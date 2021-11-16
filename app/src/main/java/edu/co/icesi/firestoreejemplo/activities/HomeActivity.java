package edu.co.icesi.firestoreejemplo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;

import edu.co.icesi.firestoreejemplo.R;
import edu.co.icesi.firestoreejemplo.adapters.ContactAdapter;
import edu.co.icesi.firestoreejemplo.models.User;
import edu.co.icesi.firestoreejemplo.viewholders.ContactVH;

public class HomeActivity extends AppCompatActivity {

    private User user;

    private RecyclerView userListView;
    private ContactAdapter adapter;

    private Button logoutBTN;

    private TextView nameTV;

    private SwipeRefreshLayout usersSRL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);




        //Load user from SP
        User loadedUser = loadUser();
        if(loadedUser == null || FirebaseAuth.getInstance().getCurrentUser() == null || !FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }else{
            this.user = loadedUser;
        }



        FirebaseMessaging.getInstance().subscribeToTopic(user.getId());


        nameTV = findViewById(R.id.nameTV);
        usersSRL = findViewById(R.id.usersSRL);


        userListView = findViewById(R.id.userListView);
        adapter = new ContactAdapter();

        userListView.setAdapter(adapter);
        userListView.setLayoutManager(new LinearLayoutManager(this));
        userListView.setHasFixedSize(true);

        logoutBTN = findViewById(R.id.logoutBTN);


        getUsers();


        /*
        userListView.setOnItemClickListener((parent, view, position, id) -> {

        });

         */


        logoutBTN.setOnClickListener(v->{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            FirebaseMessaging.getInstance().unsubscribeFromTopic(user.getId());
            getSharedPreferences("appmoviles", MODE_PRIVATE).edit().clear().apply();
            FirebaseAuth.getInstance().signOut();
        });

        nameTV.setText(user.getName());
        nameTV.setOnClickListener(
                v->{
                    Intent intent = new Intent(this, ProfileActivity.class);
                    startActivity(intent);
                }
        );


        usersSRL.setOnRefreshListener(
                ()->{
                    getUsers();
                }
        );

    }

    private void getUsers(){
        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(
                task ->{
                    adapter.clear();
                    for(DocumentSnapshot doc : task.getResult()){
                        User user = doc.toObject(User.class);
                        adapter.addUser(user);
                    }
                    usersSRL.setRefreshing(false);
                }
        );
    }

    private User loadUser() {
        String json = getSharedPreferences("appmoviles", MODE_PRIVATE).getString("user", "NO_USER");
        if(json.equals("NO_USER")){
            return null;
        }else{
            return new Gson().fromJson(json, User.class);
        }
    }

}