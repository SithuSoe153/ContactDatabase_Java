package com.uog.contactdatabase.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uog.contactdatabase.database.Contact;
//import com.uog.contactdatabase.ListActivity;
import com.uog.contactdatabase.R;
import com.uog.contactdatabase.database.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    public interface ClickListener{
        void onItemClick(int position, View v, long id);
    }

    public void setListener (ClickListener listener){
        this.listener = listener;
    }

    private static ClickListener listener;

    private List<Contact> contactList;
    public void setContactList(List<Contact> contactList){
        this.contactList = contactList;
    }

    public ContactAdapter(List<Contact> personList){
        this.contactList =personList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.lbl_Id.setText(contact.getId() + "");
        holder.lbl_Name.setText(contact.getName());
        holder.lbl_Address.setText(contact.getAddress());
        holder.lbl_Phone.setText(contact.getPhone());
        holder.lbl_Age.setText(contact.getAge() + "");

        // Load the image from the file path and set it to iv_profilepic
        String imagePath = contact.getAvatarFilePath();
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap != null) {
                holder.iv_profilepic.setImageBitmap(bitmap);
            } else {
                // Handle the case where the image couldn't be loaded
                holder.iv_profilepic.setImageResource(R.drawable.default_avatar); // Set a default image
            }
        } else {
            // Handle the case where the file path is null
            holder.iv_profilepic.setImageResource(R.drawable.default_avatar); // Set a default image
        }

//        Log.i("test", contact.getAvatarFilePath());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView lbl_Id, lbl_Name, lbl_Address, lbl_Phone, lbl_Age;
        Button btn_Remove, btn_Edit;
        ImageView iv_profilepic;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lbl_Id = itemView.findViewById(R.id.lbl_Id);
            lbl_Name = itemView.findViewById(R.id.lbl_Name);
            lbl_Address = itemView.findViewById(R.id.lbl_Address);
            lbl_Phone = itemView.findViewById(R.id.lbl_Phone);
            lbl_Age = itemView.findViewById(R.id.lbl_Age);
            iv_profilepic = itemView.findViewById(R.id.iv_profilepic);
            btn_Remove = itemView.findViewById(R.id.btn_Remove);
            btn_Edit = itemView.findViewById(R.id.btn_Edit);

            btn_Remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onItemClick(getAdapterPosition(),view,R.id.btn_Remove);

                }
            });

            btn_Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onItemClick(getAdapterPosition(),view,R.id.btn_Edit);

                }
            });

        }
    }

}
