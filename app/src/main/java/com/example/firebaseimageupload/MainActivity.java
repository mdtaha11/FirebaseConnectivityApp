package com.example.firebaseimageupload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button btn_choose;
    EditText mEditTextFileName;
    Button btn_upload;
    Uri mImageUri;
    private StorageTask mUploadTask;
StorageReference storageReference;
DatabaseReference databaseReference;
ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_choose=findViewById(R.id.btn_choose);
        btn_upload=findViewById(R.id.btn_upload);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        imageView=findViewById(R.id.imageView);
        progressBar=findViewById(R.id.progress_bar);
        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        databaseReference= FirebaseDatabase.getInstance().getReference("uploads");
        btn_choose.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

     }
});
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFilee();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            mImageUri=data.getData();

            imageView.setImageURI(mImageUri);

        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));




    }
    private void uploadFilee(){
        if (mImageUri != null)
        {
            storageReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot , Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();


                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        Log.e("money", "then: " + downloadUri.toString());


                        Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                                downloadUri.toString());

                        databaseReference.push().setValue(upload);
                        String uploadId=databaseReference.push().getKey();
                        databaseReference.child(uploadId).setValue(upload);
                        Toast.makeText(MainActivity.this, "Upload successful", Toast.LENGTH_LONG).show();


                    } else
                    {
                        Toast.makeText(MainActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else
            Toast.makeText(this,"No file selected",Toast.LENGTH_SHORT).show();
    }


}
