package com.example.samplelabtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity
{
    RecyclerView recyclerView;
    UserAdapter userAdapter;

    Button delete;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        recyclerView = findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter();
        recyclerView.setAdapter(userAdapter);

        dbRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                List<UsersData> usersList = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren())
                {
                    UsersData user = ds.getValue(UsersData.class);
                    usersList.add(user);
                }

                userAdapter.setUsersList(usersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {}
        });

        userAdapter.setOnDeleteClickListener(new UserAdapter.OnDeleteClickListener()
        {
            @Override
            public void onDeleteClick(int position)
            {
                UsersData user = userAdapter.getUsersList().get(position);
                String mail = user.getEmail();

                deleteUserData(mail);
            }
        });
    }

    private void deleteUserData(String email)
    {
        Query query = dbRef.orderByChild("email").equalTo(email);

        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    ds.getRef().removeValue();
                }

                Toast.makeText(HomePage.this, "User data deleted successfully!", Toast.LENGTH_SHORT).show();
                refreshData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {}
        });
    }

    private void refreshData()
    {
        dbRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                List<UsersData> usersList = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren())
                {
                    UsersData user = ds.getValue(UsersData.class);
                    usersList.add(user);
                }

                userAdapter.setUsersList(usersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {}
        });
    }
}