package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText fullNameEditText,userPhoneEditText,addressEditText;
    private TextView profileChangeTextBtn,closeTextBtn,saveTextBtn;
    private Uri imageUri;
    private String myUrl = "";
    private StorageReference storageProfilePictureRef;
    private String checker ="";
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);

        storageProfilePictureRef= FirebaseStorage.getInstance().getReference().child("Profile pictures");

        profileImageView =(CircleImageView) findViewById(R.id.settings_profile_image);
        fullNameEditText =(EditText) findViewById(R.id.settings_full_name);
        userPhoneEditText =(EditText) findViewById(R.id.settings_phone_number);
        addressEditText =(EditText) findViewById(R.id.settings_address);
        profileChangeTextBtn=(TextView) findViewById(R.id.profile_image_change);
        closeTextBtn=(TextView) findViewById(R.id.close_settings_btn);
        saveTextBtn=(TextView) findViewById(R.id.update_account_settings_btn);

        userInfoDisplay(profileImageView,fullNameEditText,userPhoneEditText,addressEditText);
        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checker.equals("clicked"))
                {
                     userInfosaved();
                }
                else
                {

                    updateOnlyUserInfo();
                }
            }
        });

        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checker="clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode== RESULT_OK && data!=null)
        {
          CropImage.ActivityResult result= CropImage.getActivityResult(data);
          imageUri=result.getUri();
          profileImageView.setImageURI(imageUri);
        }
        else {
            Toast.makeText(SettingsActivity.this,"Error",Toast.LENGTH_SHORT).show();
            startActivity(new Intent (SettingsActivity.this,SettingsActivity.class));
            finish();
        }
    }

    private void updateOnlyUserInfo()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> userMap =new HashMap<>();
        userMap.put("name",fullNameEditText.getText().toString());
        userMap.put("phoneOrder",userPhoneEditText.getText().toString());
        userMap.put("address",addressEditText.getText().toString());
        ref.child(Prevalent.currentonlineusers.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        Toast.makeText(SettingsActivity.this,"Profile Info Updated",Toast.LENGTH_SHORT).show();
        finish();
    }

    private void userInfosaved()
    {
          if(TextUtils.isEmpty(fullNameEditText.getText().toString()))
          {
              Toast.makeText(this, "Enter Full Name", Toast.LENGTH_SHORT).show();
          }

          else if(TextUtils.isEmpty(userPhoneEditText.getText().toString()))
          {
              Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
          }

          else if(TextUtils.isEmpty(addressEditText.getText().toString()))
          {
              Toast.makeText(this, "Enter Address ", Toast.LENGTH_SHORT).show();
          }
          else if (checker=="clicked")
          {
                 uploadImage();
          }

    }

    private void uploadImage()
    {
        final ProgressDialog LoadingBar =new ProgressDialog(this);
        LoadingBar.setTitle("Update Profile");
        LoadingBar.setMessage("Please wait,while we are updating your information ");
        LoadingBar.setCanceledOnTouchOutside(false);
        LoadingBar.show();

        if(imageUri!=null)
        {
            final StorageReference fileRef=storageProfilePictureRef
                    .child(Prevalent.currentonlineusers.getPhone()
                    + ".jpg");
            uploadTask =fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl =task.getResult();
                        myUrl=downloadUrl.toString();

                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String,Object> userMap =new HashMap<>();
                        userMap.put("name",fullNameEditText.getText().toString());
                        userMap.put("phoneOrder",userPhoneEditText.getText().toString());
                        userMap.put("address",addressEditText.getText().toString());
                        userMap.put("image",myUrl);
                        ref.child(Prevalent.currentonlineusers.getPhone()).updateChildren(userMap);
                        LoadingBar.dismiss();

                        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                        Toast.makeText(SettingsActivity.this,"Profile Info Updated",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    else
                    {
                        LoadingBar.dismiss();
                        Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        else {
            Toast.makeText(SettingsActivity.this, "Image is not Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(CircleImageView profileImageView, EditText fullNameEditText, EditText userPhoneEditText, EditText addressEditText)
    {
        DatabaseReference UserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentonlineusers.getPhone());
         UserRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot)
             {
                  if(dataSnapshot.exists())
                  {
                      if(dataSnapshot.child("image").exists())
                      {
                          String image= dataSnapshot.child("image").getValue().toString();
                          String name= dataSnapshot.child("name").getValue().toString();
                          String phone= dataSnapshot.child("phone").getValue().toString();
                          String address= dataSnapshot.child("address").getValue().toString();

                          Picasso.get().load(image).into(profileImageView);
                          fullNameEditText.setText(name);
                          userPhoneEditText.setText(phone);
                          addressEditText.setText(address);

                      }
                  }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
    }
}