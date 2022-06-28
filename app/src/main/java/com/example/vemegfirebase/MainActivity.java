package com.example.vemegfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 322;
    Uri filePath;
    List<Contact> lst= new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    DatabaseReference myRefObject = database.getReference("EPI/contactx");
    DatabaseReference myRefCollection = database.getReference("tabContacts");
    TextView tvMessage,tvObject,tvCollection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMessage=findViewById(R.id.textViewMessage);
        tvObject=findViewById(R.id.textViewObject);
        tvCollection=findViewById(R.id.textViewCollection);
        myRef.addValueEventListener(velMessage);
        myRefObject.addValueEventListener(velObject);
        myRefCollection.addValueEventListener(velCollection);
    }

    public void MessageClick(View view) {

        myRef.setValue("Dsl Mr");
        //myRef.setValue(null);
    }

    public void ObjectClick(View view) {
        Contact c = new Contact("ali","yyy","9685855");


        myRefObject.setValue(c);
    }

    public void CollectionClick(View view) {
        Contact c = new Contact("ali","ali","9685855");
        Contact c1 = new Contact("salh","salh","99999");
        DatabaseReference myRef = database.getReference("tabContacts");

        //myRef.child("c1").setValue(c);
        //myRef.child("c2").setValue(c1);
        //myRef.child("c").setValue(c);
        myRefCollection.push().setValue(c);
        myRefCollection.push().setValue(c1);
    }
    ValueEventListener velMessage= new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
         String ch=   snapshot.getValue(String.class);
         tvMessage.setText(ch);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    ValueEventListener velObject = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Contact c=   snapshot.getValue(Contact.class);
            tvObject.setText(c.getPrenom()+":"+c.getNom()+":"+c.getTel());

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    ValueEventListener velCollection = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Contact c;
            String ch="";
            lst.clear();
            for (DataSnapshot ds:snapshot.getChildren()) {
               c =   ds.getValue(Contact.class);
              ch=ch+"\n"+ ds.getKey();
               lst.add(c);
              // ch=ch+"\n"+c.getPrenom()+":"+c.getNom()+":"+c.getTel();
            }

            tvObject.setText(ch);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void StorageClick(View view) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        UUID uuid = UUID.randomUUID();
        StorageReference ref = storageRef.child("imagesEPI/"+uuid.toString());

       ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               //Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
               ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       Log.d("AffichageURI",uri.toString());
                   }
               });
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
           }
       });

    }

    public void GalleryClick(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null ) {
             filePath = data.getData();
        }
    }
}