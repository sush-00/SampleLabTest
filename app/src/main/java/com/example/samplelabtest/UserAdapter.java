package com.example.samplelabtest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>
{
    List<UsersData> usersList = new ArrayList<>();

    public void setUsersList(List<UsersData> usersList)
    {
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    public interface OnDeleteClickListener
    {
        void onDeleteClick(int position);
    }

    public OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener)
    {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public List<UsersData> getUsersList()
    {
        return usersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        UsersData user = usersList.get(position);
        holder.email.setText(user.getEmail());
        holder.username.setText(user.getUsername());
        holder.password.setText(user.getPasswd());
    }

    @Override
    public int getItemCount()
    {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView email, username, password;
        Button delete;

        public ViewHolder(View itemView)
        {
            super(itemView);
            email = itemView.findViewById(R.id.emailDisp);
            username = itemView.findViewById(R.id.usernameDisp);
            password = itemView.findViewById(R.id.passwordDisp);
            delete = itemView.findViewById(R.id.deleteButton);

            delete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (onDeleteClickListener != null)
                    {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION)
                        {
                            onDeleteClickListener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}
