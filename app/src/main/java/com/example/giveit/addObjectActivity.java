package com.example.giveit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class addObjectActivity extends AppCompatActivity {




    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();
    public Uri filepath;
    private Button gallery;
    private Button addImage;
    private ImageView selectedImage;
    private EditText name, description , adresse;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference("objects");


    List<ObjectDto> objects ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_object);

        gallery = findViewById(R.id.gallery);
        addImage = findViewById(R.id.addImage);
        selectedImage = findViewById(R.id.imageview);

        objects = new ArrayList<>();

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                objects.add(snapshot.getValue(ObjectDto.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //ask the user to select an image from the gallery: launch the gallery
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ACTION_PICK : to specify the we want from the user to be able to chose an image from the galley
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // we want to get back a result
                startActivityForResult(intent, 2);

            }
        }) ;
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                uploadPicture();
            }
        });
    }

    // getting image from data and then displaying it inside of our imageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // to make sure that the user uploaded an image and haven't canceled ..
        if(resultCode == RESULT_OK && data != null){
            //Uri : points at the data that we want to access
            //so the getData() returns the uri
            filepath = data.getData();
            selectedImage.setImageURI(filepath);


        }
    }


    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading image..");
        pd.show();


        name = (EditText) findViewById(R.id.imageName);
        description = (EditText) findViewById(R.id.imageDescription);
        adresse = (EditText) findViewById(R.id.imageAdresse);



        final String randomKey = UUID.randomUUID().toString();
        StorageReference imgRef = storageReference.child("images/"+randomKey);

        imgRef.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image uploded" , Snackbar.LENGTH_LONG).show();

                        //tranpsport image url to database realTime (object database)
                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                ObjectDto model = new ObjectDto(adresse.getText().toString(),
                                        description.getText().toString(), uri.toString() ,
                                        name.getText().toString());

                                String modelId = root.push().getKey();
                                root.child(modelId).setValue(model);
                                Toast.makeText(addObjectActivity.this, "uploaded succefully", Toast.LENGTH_SHORT );

                                startActivity(new Intent(addObjectActivity.this, MainActivity.class));

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Percentage" +  (int) progressPercent + "%");
                    }
                });



    }

}

