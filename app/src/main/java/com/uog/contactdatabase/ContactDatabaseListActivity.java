package com.uog.contactdatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.uog.contactdatabase.adapter.ContactAdapter;
import com.uog.contactdatabase.database.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactDatabaseListActivity extends AppCompatActivity {
    private List<Contact> contactList = new ArrayList<>();

    private DatabaseHelper dataBaseHelper;
    private ContactAdapter contactAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_database_list);

        dataBaseHelper = new DatabaseHelper(getBaseContext());

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactAdapter = new ContactAdapter(contactList);
        //
        contactAdapter.setListener(new ContactAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v, long id) {
                if (id==R.id.btn_Remove){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Contact person = contactList.get(position);
//                               remove
                                dataBaseHelper.delete(person.getId());
//                               List
                                contactList = dataBaseHelper.search("");
                                contactAdapter.setContactList(contactList);
                                contactAdapter.notifyDataSetChanged(); // refresh the data
                            }catch (Exception e){

                            }

                        }
                    });


                } else if (id == R.id.btn_Edit) {
                    Contact contact = contactList.get(position);
                    Intent intent = new Intent(getBaseContext(),EntryContactDatabaseActivity.class);
                    intent.putExtra(DatabaseHelper.PERSON_ID,contact.getId());
                    intent.putExtra(DatabaseHelper.NAME,contact.getName());
                    intent.putExtra(DatabaseHelper.ADDRESS,contact.getAddress());
                    intent.putExtra(DatabaseHelper.PHONE,contact.getPhone());
                    intent.putExtra(DatabaseHelper.AGE,contact.getAge());
                    intent.putExtra(DatabaseHelper.AVATAR_FILE_PATH,contact.getAvatarFilePath());
                    Log.i("test", contact.getAvatarFilePath() + "real");


                    startActivityForResult(intent,UPDATE_RESULT);

                }
            }
        });
        recyclerView.setAdapter(contactAdapter);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    contactList = dataBaseHelper.search("");
                    contactAdapter.setContactList(contactList);
                    contactAdapter.notifyDataSetChanged(); // refresh the data

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });




    }

    public static final int UPDATE_RESULT = 123;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == UPDATE_RESULT && resultCode == RESULT_OK){

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {

                        contactList = dataBaseHelper.search("");
                        contactAdapter.setContactList(contactList);
                        contactAdapter.notifyDataSetChanged(); // refresh the data

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            });

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}