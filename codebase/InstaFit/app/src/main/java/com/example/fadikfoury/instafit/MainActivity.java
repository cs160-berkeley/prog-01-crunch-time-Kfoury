package com.example.fadikfoury.instafit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.widget.EditText;
import android.text.InputType;
import android.content.DialogInterface;
import android.app.TimePickerDialog;
import java.util.Calendar;
import android.content.SharedPreferences;
import android.app.DialogFragment;
import android.widget.AdapterView;

import android.support.v4.app.TaskStackBuilder;

import android.support.v4.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;



import android.app.Dialog;
import android.widget.TimePicker;
import android.text.format.DateFormat;
import android.app.AlarmManager;
import android.content.Intent;
import android.widget.Spinner;

import android.view.View;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends Activity {


    private List<instaActivity> myActivities = new ArrayList<instaActivity>();
    private String weight_variable_key = "15";


    //this variable contains the value that is selected
    private int selected_activity = 0;

    //This boolean contains which variable is selected
    private boolean isRepsSelected = true; //The app launches with the reps selected


    //This boolean contains the value of the selected (reps or minutes or calories)
    private double typed_value = 0; //The app launches with 0 reps / calories

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_weight:
                showActionSheetToSetWeight();

                break;
            case R.id.action_motivate:
                showTimePickerDialog();

        }
        return true;
    }


    public void setRepeatingAlarm() {
        AlarmManager am;
        am = (AlarmManager) getSystemService(this.ALARM_SERVICE);
        Intent intent = new Intent(this, TimeAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                //(24 * 60 * 1000), pendingIntent);

        am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                pendingIntent);
    }


    ////
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog newDia  = new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));

            newDia.setTitle("Set your gym time");

            // Create a new instance of TimePickerDialog and return it
            return newDia ;
        }





        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            pushNotification();

            Toast.makeText(getActivity(), "Your gym time has been saved ", Toast.LENGTH_SHORT).show();
            MainActivity a = (MainActivity) getActivity();
                    a.setRepeatingAlarm();



        }


        private void pushNotification()
        {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getActivity())
                            .setSmallIcon(R.drawable.run_1)
                            .setContentTitle("It's gym time")
                            .setContentText("Get ready to be twice as awesome!");

            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(getActivity(), MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
// Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mNotificationManager.notify(1, mBuilder.build());



        }



    }






    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        populateInstaActivities();

        populateSpinner();

        populateListView();


        final EditText txtCal = (EditText) findViewById(R.id.txtCalories);
        final EditText txtreps = (EditText) findViewById(R.id.txtReps);

        //This is a listener for the calorie text box
        txtCal.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here
                if (!txtreps.isFocused())
                    txtreps.setText("");

                isRepsSelected = false;


                if (txtCal.getText().toString().length()>0)
                    typed_value = Double.parseDouble(txtCal.getText().toString());


                //populateListView();


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });







        //This is a listener for the reps text box
        txtreps.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here
                if (!txtCal.isFocused())
                    txtCal.setText("");

                isRepsSelected = true;

                if (txtreps.getText().toString().length()>0)
                    typed_value = Double.parseDouble(txtreps.getText().toString());

                populateListView();


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });



    }

    //Links the data source with the view
    private void populateListView() {
        ArrayAdapter<instaActivity> adapter= new MyListAdapter();
        GridView list = (GridView) findViewById(R.id.tblExercises);
        list.setAdapter(adapter);

    }

    //Fills in the data in the spinner

    private void populateSpinner(){
        Spinner myspn = (Spinner) findViewById(R.id.ddItem);
        // myspn.setAdapter(adapter);


        ArrayList<String> itemsA = getInstaExercisesNamesArray();

        String[] items = itemsA.toArray(new String[itemsA.size()]);

        ArrayAdapter aa = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                items);




        aa.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        myspn.setAdapter(aa);

        myspn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An spinnerItem was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)

                selected_activity = pos;
                Toast.makeText(MainActivity.this, myActivities.get(pos).getName() + " has been selected !", Toast.LENGTH_SHORT).show();

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

        }); // (optional)


    }




    private class MyListAdapter extends ArrayAdapter<instaActivity>{
        public MyListAdapter()
        {
            super(MainActivity.this, R.layout.insta_item, myActivities );

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          View itemView = convertView;

            //basically de-ques item for identifier
            //it does the heavy weight lifting for us
            if (itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.insta_item,parent,false);
            }

            //Now we have the view ready to be consumed, let's populate the view

            //we need to find the exercise to work with then fill the view

            instaActivity currentActivity = myActivities.get(position);

            //Fill in the view
            ImageView imgView = (ImageView) itemView.findViewById(R.id.imgIcon);
            imgView.setImageResource(currentActivity.getIconID());

            //Fill in the txts

                // figure out the unit
                String unit = " Mins";
                if (currentActivity.isReps())
                    unit = " Reps";


            if (isRepsSelected) {
                //Here we need to convert from reps to calories
                //And from reps to equivalent reps

                double calories_conv = returnCaloriesGivenReps(selected_activity, typed_value);

                TextView txtCal = (TextView) itemView.findViewById(R.id.lblCal);
                txtCal.setText(String.format("%.2f", calories_conv) + " Cal");


                double equ_reps = returnRepsGivenReps(typed_value,selected_activity,position);


                TextView txtReps = (TextView) itemView.findViewById(R.id.lblReps);
                txtReps.setText(String.format("%.2f", equ_reps) + unit);

                TextView txtName = (TextView) itemView.findViewById(R.id.lblActivityType);
                txtName.setText(currentActivity.getName());

            }else{

                //Here we need to convert from Calories to reps
                //Note that the calories will remain as is, because the calories
                // Are our baseline
                double reps_cov = returnRepsGivenCalories(typed_value,position);

                TextView txtCal = (TextView) itemView.findViewById(R.id.lblCal);
                txtCal.setText(String.format("%.2f", typed_value) + " Cal");


                TextView txtReps = (TextView) itemView.findViewById(R.id.lblReps);
                txtReps.setText(String.format("%.2f", reps_cov) + unit);

                TextView txtName = (TextView) itemView.findViewById(R.id.lblActivityType);
                txtName.setText(currentActivity.getName());


            }



            return itemView;

          //  return super.getView(position, convertView, parent);


        }
    }



    //A function that populates the list view data source
    private void populateInstaActivities()

    {


        myActivities.add(new instaActivity("Pushup",R.drawable.pushups,true,350,0) );

        myActivities.add(new instaActivity("Situp",R.drawable.sit,true,200,0) );

        myActivities.add(new instaActivity("Squats",R.drawable.sq,true,225,0) );

        myActivities.add(new instaActivity("Leg-lift",R.drawable.leg_lift,false,0,25) );

        myActivities.add(new instaActivity("Plank", R.drawable.planks, false, 0, 25));

        myActivities.add(new instaActivity("Jumping Jacks",R.drawable.jumping_jacks,false,0,10) );

        myActivities.add(new instaActivity("Pullup", R.drawable.pulup, true, 100, 0));

        myActivities.add(new instaActivity("Cycling", R.drawable.cycling, false, 0, 12));

        myActivities.add(new instaActivity("Walking", R.drawable.walking, false, 0, 20));

        myActivities.add(new instaActivity("Jogging", R.drawable.jogging, false, 0, 12));

        myActivities.add(new instaActivity("Swimming", R.drawable.swim, false, 0, 13));

        myActivities.add(new instaActivity("Stair-Climbing", R.drawable.stair, false, 0, 15));



    }


    ///SPINNER ADAPTER







    public void showActionSheetToSetWeight()
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter your weight in pounds");

// Set up the input
        final EditText input = new EditText(this);
        input.setText(String.valueOf(getWeight()));
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);



// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                setWeight(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }

    private double getWeight(){



        SharedPreferences mPrefs = getSharedPreferences("0", 0);
        return Double.parseDouble(mPrefs.getString(weight_variable_key, "150"));



    }


    private void setWeight(String weight){


        SharedPreferences mPrefs = getSharedPreferences("0", 0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString(weight_variable_key, weight).commit();

        Toast.makeText(this, "Your weight has been saved !", Toast.LENGTH_SHORT).show();

    }

    //here we need Calories to reps

    private double returnRepsGivenReps(double repsOrMinutes, int pos_0, int pos_1)
    {

        if (repsOrMinutes == 0)
            return 0;

        instaActivity activity_selected = myActivities.get(pos_0);

        instaActivity activity_convertTo = myActivities.get(pos_1);



        //lets get the calories of the selected_activity given the reps

        double calories = returnCaloriesGivenReps(pos_0, repsOrMinutes);

        //now lets compute the equivalant amount of reps given calories
        double repsORmin_calculated = returnRepsGivenCalories(calories,pos_1);

        if (repsORmin_calculated>0)
            return repsORmin_calculated;
        else
            return 0;

    }



    private double returnCaloriesGivenReps(int pos, double repsOrMinutes)
    {
        if (repsOrMinutes == 0)
            return 0;

        instaActivity currentActivity = myActivities.get(pos);

        if (currentActivity.isReps())
        {

            //then we need to convert the reps

            double weight = getWeight();

            int rate = currentActivity.getRepsRate();



            double result = (double) (((double)repsOrMinutes/(double)rate) * 100.0 - (weight - 150.0 ) * 0.09036);

            if (result>0)
                return result ;

            else
                return 0;


        }else //duration

        {

            double weight = getWeight();

            int rate = currentActivity.getDurationRate();


            double result = (double) (((double)repsOrMinutes/(double)rate) * 100.0 - (weight - 150.0 ) * 0.09036) ;

            if (result>0)
                return result ;

            else
                return 0;
        }

    }



    //from this we only need one function
    private double returnRepsGivenCalories(double calories, int pos_1){
        if (calories == 0)
            return 0;


        instaActivity currentActivity = myActivities.get(pos_1);

        if (currentActivity.isReps())
        {

            //then we need to convert the reps

            double weight = getWeight();

            int rate = currentActivity.getRepsRate();




            double result = (double) ((  ((double)calories - (weight - 150.0 ) * 0.09036) )/(double)100) * rate  ;

            if (result>0)
                return result ;

            else
                return 0;


        }else //duration

        {

            double weight = getWeight();

            int rate = currentActivity.getDurationRate();


            double result = (double) ((  ((double)calories - (weight - 150.0 ) * 0.09036) )/(double)100) * rate  ;

            if (result>0)
                return result ;

            else
                return 0;



        }




    }

    public void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();

        newFragment.show(this.getFragmentManager(), "timePicker");
    }



    public ArrayList getInstaExercisesNamesArray()
    {

        ArrayList namesArray = new ArrayList<String>();

        for (int i = 0;i < myActivities.size();i++)
        {

            String reps = "Min";

            if (myActivities.get(i).isReps())
            {
                 reps = "Reps";
            }


            namesArray.add( reps +" - " + myActivities.get(i).getName()  );


        }

        return namesArray;

    }






}
