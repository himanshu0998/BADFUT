package com.example.himanshu.trial4;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class PayAct extends AppCompatActivity {

    Button pay;
    FirebaseDatabase db;
    DatabaseReference dbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        db = FirebaseDatabase.getInstance();
        dbref=db.getReference("BADFUT/BOOKINGS");
        pay = (Button)findViewById(R.id.pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PayAct.this)
                        .setTitle("PAYMENT SUCCESSFUL")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addInDatabase();
                                Intent intent = new Intent(PayAct.this, PostSignUp.class);
                                startActivity(intent);
                            }
                        }).create().show();
            }
        });
    }

    public void addInDatabase()
    {
        String id=dbref.push().getKey();
        dbref.child(id).setValue(Booking.book);
    }


}
