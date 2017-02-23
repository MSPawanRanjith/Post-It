package com.bookcoast.post_it;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class TeacherAddEvent extends AppCompatActivity {


    private Button publish;
    private ImageButton imageSelect;
    private EditText Title;
    private EditText Desc;
    String title, description, eligibility, contact, imgurl, date,date1;
    boolean event;
    private String uid = "";
    private Uri imageUri =null;
    private int year_x, month_x, day_x;
    private Uri resultUri = null;
    private static  final  int GALLERY_REQUEST = 2;
    private EditText Elig;
    private StorageReference mImageReference;
    private DatabaseReference mDataBase;
    private ProgressDialog mProgress;
    static final int DIALOG_ID = 0;
    private EditText Contact;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private RadioGroup radioGroup;
    private EditText dateText;
    public String smessage;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private String type;
    String notice= sendNotification();
    private RadioButton radioButton;
    final Firebase ref1 = new Firebase("https://post-it-81fe6.firebaseio.com/");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_event);

        Firebase.setAndroidContext(this);
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        ref1.setAndroidContext(this);
        mProgress=new ProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);

        Title = (EditText) findViewById(R.id.title);
        Desc = (EditText) findViewById(R.id.description);
        Elig = (EditText) findViewById(R.id.eligiblity);
        Contact = (EditText) findViewById(R.id.contact);
        imageSelect = (ImageButton) findViewById(R.id.imageSelect);
        publish = (Button) findViewById(R.id.publish);
        mImageReference= FirebaseStorage.getInstance().getReference();
        mDataBase= FirebaseDatabase.getInstance().getReference().child("Blog");
        radioGroup = (RadioGroup) findViewById(R.id.radioType);
        dateText = (EditText) findViewById(R.id.editDate);
//listener



        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(TeacherAddEvent.this, TeacherLogin.class));
                    finish();
                }
            }
        };
        //Get UID of the user
        uid = user.getUid();

        imageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //galleryIntent.setType("images/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
                imageSelect.setImageURI(imageUri);
            }
        });
        publish.setOnClickListener(new View.OnClickListener(){
                                             @Override
                                             public void onClick(View view){
                                                 uploading();
                                             }
                                         }

        );

        dateText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                showDialog(DIALOG_ID);
                return true;

            }
        });




    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if (id == DIALOG_ID) {
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this, dpickerListener, mYear, mMonth, mDay);
        }
        else
            return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year_x = i;
            month_x = i1 + 1;
            day_x = i2;
            dateText.setText(day_x+"/"+month_x+"/"+year_x);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.teacher_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_remove:
                delete();
                return true;
            case R.id.action_signout:
                signout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void uploading() {
        mProgress.setMessage("Uploading...");
        mProgress.show();
        if(resultUri != null){
            StorageReference filePath= mImageReference.child("Blog_img").child(resultUri.getLastPathSegment());
            filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri=taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost=mDataBase.push();
                    //newPost.child("image_url").setValue(downloadUri.toString());
                    imgurl = downloadUri.toString();
                    title = Title.getText().toString();
                    description = Desc.getText().toString();
                    eligibility = Elig.getText().toString();
                    contact = Contact.getText().toString();
                    date1 = ""+day_x;
                    date1 = date1 + "/" + month_x;
                    date1 = date1 + "/" + year_x;
                    //description = description + "\n" + "date :" + date1;
                    // get selected radio button from radioGroup
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    date = ""+year_x;
                    if (month_x / 10 == 0)
                    {date = date + "0"+month_x;
//                        Log.d("df 10%0 is true", "onSuccess: ");
                     }
                    else
                    {   date = date + month_x;
//                        Log.d("df 10%0 is false", "onSuccess: ");
                    }

                    if (day_x / 10 == 0)
                    {date = date + "0"+day_x;}
                    else
                    {date = date + day_x;}


                    // find the radiobutton by returned id
                    radioButton = (RadioButton) findViewById(selectedId);
                    String temp = radioButton.getText().toString();

                    if(temp.equals("Workshop / Event"))
                    {
                        //event = true;
                        type = "event";
                    }
                    else
                    {
                        type = "intern";
                        //date1="0";

                    }

                    Data obj = new Data(title, description, eligibility, contact, imgurl, event, uid, type, date1);
                    Random rand = new Random();

                    int  n = rand.nextInt(80000000) + 11111111;
                    date = date + n;
                    ref1.child(date).setValue(obj);
                    //Toast.makeText(getApplicationContext(), "This is my Toast message!"+temp,Toast.LENGTH_SHORT).show();
                    try {
                        Log.d("Notification message ","Inside top oncreate");
                        String messageNotice= post();
                        Log.d("Notification message ","The message id is "+messageNotice+" ");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


                    //ref1.child("post").child(title).setValue(obj);
                    //urltext.setText(downloadUri.toString());
                    mProgress.dismiss();
                    startActivity(new Intent(TeacherAddEvent.this, MainActivity.class));
                    finish();
//                    Snackbar.make(view, "Thank you for using Post-It", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){
            imageUri=data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(16,9)
                    .start(this);



        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK)
            {
                resultUri = result.getUri();
                imageSelect.setImageURI(resultUri);

            }
            else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }

    public void delete()
    {
        Intent intent = new Intent(TeacherAddEvent.this, RemoveActivity.class);
        startActivity(intent);
    }

    public void signout() {
        auth.signOut();
        Intent intent = new Intent(TeacherAddEvent.this, Choose_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @TargetApi(19)
    String post() throws IOException {
        Log.d("Notification message ","Inside post");
        OkHttpClient client =new OkHttpClient();
        Log.d("Notification message ","Inside  client");
        RequestBody body = RequestBody.create(JSON,notice);
        Log.d("Notification message ","Inside RequestBody");
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                //.addHeader("Content-Type","application/json")
                .addHeader("Authorization","key=AIzaSyAUwmzrdGU3JnEfj1RFOESKomnPd7P-69Y")
                .post(body)
                .build();
        Log.d("Notification message ","Inside Request only");
        Call call =client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Notification message ","Inside onFailure");
                return;

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d("Notification message ","Inside onResponse");
                smessage=response.body().string();

            }
        });
        Log.d("Notification message ","The message got after is "+smessage+"  ");
        return smessage;
    }

    public String sendNotification() {
        Log.d("Notification message ","Inside top oncreate");
        return "{\"to\":\"/topics/news\","
                + "\"priority\":\"high\","
                + "\"notification\" : {"
                + "\"body\":\"Event is created Test notif from app \","
                + "\"title\":\"New Event Check it Out\","
                + "\"icon\":\"default\","
                + "\"sound\":\"default\""
                + "}"
                + "}";
    }

}
