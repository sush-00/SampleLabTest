package com.example.samplelabtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference("Users");

    TextInputEditText email, username, password, confPassword;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confPassword = findViewById(R.id.confirm_password);
        submit = findViewById(R.id.submitButton);

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String mail, uName, passwd, confPasswd;

                mail = email.getText().toString();
                uName = username.getText().toString();
                passwd = password.getText().toString();
                confPasswd = confPassword.getText().toString();

                if (TextUtils.isEmpty(mail))
                {
                    Toast.makeText(MainActivity.this, "E-mail field cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(uName))
                {
                    Toast.makeText(MainActivity.this, "Username field cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(passwd))
                {
                    Toast.makeText(MainActivity.this, "Password field cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(confPasswd))
                {
                    Toast.makeText(MainActivity.this, "Confirm Password field cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!TextUtils.equals(passwd, confPasswd))
                {
                    Toast.makeText(MainActivity.this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbRef.orderByChild("email").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if (snapshot.exists())
                        {
                            Toast.makeText(MainActivity.this, "E-mail already exists", Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            dbRef.orderByChild("username").equalTo(uName).addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                {
                                    if (snapshot.exists())
                                    {
                                        Toast.makeText(MainActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                                    }

                                    else
                                    {
                                        saveDataToDatabase(mail, uName, passwd);

                                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error)
                                {}
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {}
                });
            }
        });
    }

    private void saveDataToDatabase(String mail, String uName, String passwd)
    {
        String uID = dbRef.push().getKey();
        UsersData usersData = new UsersData(mail, uName, passwd);
        dbRef.child(uID).setValue(usersData).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                    Toast.makeText(MainActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(MainActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
            }
        });
    }
}