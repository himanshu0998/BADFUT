package com.example.himanshu.trial4;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.auth.util.ui.SupportVectorDrawablesButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class Booking extends AppCompatActivity {

    //private PaymentParams mPaymentParams;

    CalendarView calendarView;
    DatabaseReference dbref ;
    FirebaseDatabase firebasedb;
    Bookings bookings,bookings1;
    ListView timingList;
    String[] timings;
    String currentDate;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    Vector<Bookings> bvector;
    Vector<String> timvector;
    boolean flag;
    public static Bookings book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Toast.makeText(Booking.this,"Tap on the Date to view the slots",Toast.LENGTH_SHORT).show();
        timings = PostSignUp.selectedCourt.getTimings().split(",");

        timingList = (ListView)findViewById(R.id.timingList);
        calendarView = (CalendarView)findViewById(R.id.calendarView);
        firebasedb = FirebaseDatabase.getInstance();
        dbref = firebasedb.getReference("BADFUT/BOOKINGS");
        bookings= new Bookings();
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        flag=false;
        calendarView.setMinDate(c.getTimeInMillis());

       calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                currentDate = String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                timvector=new Vector<>();
                for(Bookings bk:bvector)
                {
                    //System.out.println(currentDate);
                    if(bk.getDate().equals(currentDate))
                        timvector.addElement(bk.getSlots());

                }
                changeList();
            }
        });


        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toGetBookings(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        timingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                book=new Bookings(PostSignUp.selectedCourt.getName(),currentDate,arrayList.get(position));
               openDialog();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Booking.this,Main.class);
            startActivity(intent);

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openDialog()

    {
        new AlertDialog.Builder(Booking.this)
                .setTitle("PROCEED TO PAYMENT?")
                .setMessage("")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Booking.this, PayAct.class);
                        startActivity(intent);
                    }
                }).create().show();
    }

    public void toGetBookings(DataSnapshot dataSnapshot)
    {
        bvector = new Vector<>();
        for (DataSnapshot dp: dataSnapshot.getChildren())
        {
            bookings = dp.getValue(Bookings.class);
            bookings1=new Bookings(bookings.getCourt(),bookings.getDate(),bookings.getSlots());
            if(bookings1.getCourt().equals(PostSignUp.selectedCourt.getName()))
                bvector.addElement(bookings1);
        }
    }

    private void changeList()
    {
        arrayList = new ArrayList<String>();
        for(String timstr:timings)
        {
            if(!timvector.contains(timstr)) {
                arrayList.add(timstr);
                flag = true;
            }
        }
        if(!flag)
            arrayList.add("Oops!!! No slots available!!");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        timingList.setAdapter(adapter);

    }


}
