package com.bookcoast.post_it;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class RemoveActivity extends AppCompatActivity {


    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private String uid;
    private Query userfEvents;
    private FirebaseRecyclerAdapter<Data,PostviewHolder> firebaseRecyclerAdapter;
    private RecyclerView recylceview;
    private DatabaseReference mDatabase;
    final Firebase ref1 = new Firebase("https://post-it-81fe6.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);

        Firebase.setAndroidContext(this);

        auth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference chatRef = mDataBase.child("");
       Query userfEvents = mDatabase.orderByChild("uid").equalTo(uid);
        recylceview = (RecyclerView) findViewById(R.id.list);
        recylceview.setHasFixedSize(true);
        recylceview.setLayoutManager(new LinearLayoutManager(this));

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(RemoveActivity.this, TeacherLogin.class));
                    finish();
                }

            }

        };



    }

    @Override
    protected void onStart() {
        super.onStart();
        Query userfEvents = mDatabase.orderByChild("uid").equalTo(uid);
          firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Data, PostviewHolder>(
                Data.class,
                R.layout.list_cards,
                PostviewHolder.class,
                userfEvents

        ) {
            @Override
            protected void populateViewHolder(PostviewHolder viewHolder, Data model, final int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setdesc(model.getDescription());
                viewHolder.setelig(model.getEligibility());
                viewHolder.setdate(model.getDate());
                viewHolder.setcontact(model.getContact());
                viewHolder.setImage(getApplicationContext(),model.getImgurl());
                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RemoveActivity.this);

                        builder.setTitle("Confirm");
                        builder.setMessage("Are you sure?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog
                                firebaseRecyclerAdapter.getRef(position).removeValue();
                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }


        };
        recylceview.setAdapter(firebaseRecyclerAdapter);




    }

    public static class PostviewHolder extends RecyclerView.ViewHolder{
        View mview;
        public PostviewHolder(View itemView) {
            super(itemView);
            mview=itemView;
        }
        public void setTitle(String title){
            TextView posttitle = (TextView) mview.findViewById(R.id.tit_rec);
            posttitle.setText(title);
        }
        public void setdesc(String desc){
            TextView posttitle = (TextView) mview.findViewById(R.id.desc_rec);
            posttitle.setText(desc);
        }
        public void setelig(String elig){
            TextView posttitle = (TextView) mview.findViewById(R.id.eligibility_rec);
            posttitle.setText(elig);
        }
        public void setdate(String date){
            TextView posttitle = (TextView) mview.findViewById(R.id.date_rec);
            posttitle.setText("Date: "+date);
        }
        public void setcontact(String contact){
            TextView posttitle = (TextView) mview.findViewById(R.id.contact_rec);
            posttitle.setText(contact);
        }
        public void setImage(Context cts, String imgurl){
            ImageView postimg = (ImageView) mview.findViewById(R.id.img_rec);
            Picasso.with(cts).load(imgurl).into(postimg);

        }
    }


    }

