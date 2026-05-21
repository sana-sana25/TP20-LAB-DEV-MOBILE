package com.example.numberbook;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int CONTACT_PERMISSION_CODE = 1;

    private Button btnLoadContacts;
    private Button btnSyncContacts;
    private Button btnSearch;

    private EditText etKeyword;

    private RecyclerView recyclerViewContacts;

    private ContactAdapter adapter;

    private List<Contact> contactList;

    private ContactApi contactApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        btnLoadContacts = findViewById(R.id.btnLoadContacts);
        btnSyncContacts = findViewById(R.id.btnSyncContacts);
        btnSearch = findViewById(R.id.btnSearch);

        etKeyword = findViewById(R.id.etKeyword);

        recyclerViewContacts =
                findViewById(R.id.recyclerViewContacts);

        recyclerViewContacts.setLayoutManager(
                new LinearLayoutManager(this)
        );

        contactList = new ArrayList<>();

        adapter = new ContactAdapter(contactList);

        recyclerViewContacts.setAdapter(adapter);

        contactApi = RetrofitClient
                .getClient()
                .create(ContactApi.class);

        btnLoadContacts.setOnClickListener(v -> {

            if (ContextCompat.checkSelfPermission(
                    MainActivity.this,
                    Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{
                                Manifest.permission.READ_CONTACTS
                        },
                        CONTACT_PERMISSION_CODE
                );

            } else {

                loadContacts();
            }
        });

        btnSyncContacts.setOnClickListener(v -> {

            if (contactList.isEmpty()) {

                Toast.makeText(
                        MainActivity.this,
                        "Aucun contact chargé",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            syncContactsToServer();
        });

        btnSearch.setOnClickListener(v -> {

            searchContacts();
        });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == CONTACT_PERMISSION_CODE) {

            if (grantResults.length > 0
                    && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {

                loadContacts();

            } else {

                Toast.makeText(
                        this,
                        "Permission refusée",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    private void loadContacts() {

        contactList.clear();

        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                        )
                );

                String phone = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                        )
                );

                contactList.add(
                        new Contact(name, phone)
                );
            }

            cursor.close();
        }

        adapter.updateData(contactList);

        Toast.makeText(
                this,
                "Contacts chargés : " + contactList.size(),
                Toast.LENGTH_LONG
        ).show();
    }

    private void syncContactsToServer() {

        for (Contact contact : contactList) {

            contactApi.insertContact(contact)
                    .enqueue(new Callback<ApiResponse>() {

                        @Override
                        public void onResponse(
                                @NonNull Call<ApiResponse> call,
                                @NonNull Response<ApiResponse> response
                        ) {
                        }

                        @Override
                        public void onFailure(
                                @NonNull Call<ApiResponse> call,
                                @NonNull Throwable t
                        ) {

                            Toast.makeText(
                                    MainActivity.this,
                                    t.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();

                            t.printStackTrace();
                        }
                    });
        }

        Toast.makeText(
                this,
                "Synchronisation terminée",
                Toast.LENGTH_LONG
        ).show();
    }

    private void searchContacts() {

        String keyword = etKeyword.getText()
                .toString()
                .trim();

        if (keyword.isEmpty()) {

            Toast.makeText(
                    this,
                    "Saisir un nom ou un numéro",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        contactApi.searchContacts(keyword)
                .enqueue(new Callback<List<Contact>>() {

                    @Override
                    public void onResponse(
                            @NonNull Call<List<Contact>> call,
                            @NonNull Response<List<Contact>> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            adapter.updateData(
                                    response.body()
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            @NonNull Call<List<Contact>> call,
                            @NonNull Throwable t
                    ) {

                        Toast.makeText(
                                MainActivity.this,
                                "Erreur recherche",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}