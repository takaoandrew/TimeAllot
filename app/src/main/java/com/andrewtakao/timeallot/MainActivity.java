package com.andrewtakao.timeallot;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.os.Build.VERSION_CODES.M;
import static com.andrewtakao.timeallot.R.id.minutes;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "findme";
    int mHour = 0;
    int mMinute = 0;
    int mDuration = 0;
    String mTaskName;
    Button task;
    View targetView;
    View restView;
    Button rest;
    List<String> timeArray = new ArrayList<>();
    int growScale = 6;
    int restScale = 6;
    int time = 0;
    Timer timer;
    ScrollView scheduleView;
    int totalDuration;
    int restID = 1;
    int taskID = 0;
    int savedTime = 0;
    int savedDuration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button arrow = (Button) findViewById(R.id.arrow);
        arrow.setVisibility(View.GONE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View view) {
                promptNewTask();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void promptNewTask() {
        // Create an instance of the dialog fragment and show it
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View dialogView = inflater.inflate(R.layout.dialog_make_task, null);

        final NumberPicker hourPicker = (NumberPicker) dialogView.findViewById(R.id.hours);
        hourPicker.setMaxValue(10);
        hourPicker.setMinValue(0);
        hourPicker.setWrapSelectorWheel(false);

        final NumberPicker minutePicker = (NumberPicker) dialogView.findViewById(minutes);
        minutePicker.setMaxValue(19);
        minutePicker.setMinValue(0);
        minutePicker.setWrapSelectorWheel(false);

        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                int temp = value * 5;
                return "" + temp;
            }
        };
        minutePicker.setFormatter(formatter);

        final EditText taskText = (EditText) dialogView.findViewById(R.id.task_name);

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = M)

                    public void onClick(DialogInterface dialog, int whichButton) {
                        mHour = hourPicker.getValue();
//                        Log.d(TAG, "" + mHour);
                        mMinute = minutePicker.getValue() * 5;
//                        Log.d(TAG, "" + mMinute);
                        mTaskName = taskText.getText().toString();
                        if (mTaskName.equals("")) {
                            mTaskName = "Unnamed Task";
                        }
                        mDuration = mHour * 60 + mMinute;
                        timeArray.add(mTaskName);
                        timeArray.add("" + mDuration);
                        timeArray.add("break");
                        timeArray.add("" + mDuration/restScale);
                        newTask(mDuration, mTaskName);
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void newTask(int duration, String taskName) {
        TextView introText = (TextView) findViewById(R.id.intro_text);
        if (introText.getText() != "") {
            introText.setText("");
        }
        Button arrow = (Button) findViewById(R.id.arrow);
        arrow.setVisibility(View.VISIBLE);
        task = new Button(this);
        task.setTag(taskID);
        Log.d(TAG, "task id = " + taskID);
        taskID+=2;
//        task.setBackgroundColor(randomColor());
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(5, Color.WHITE);
        drawable.setColor(randomColor());
        task.setBackground(drawable);
        task.setText(taskName + ": " + duration + " minutes");
//        task.setOnClickListener(getButtonMenu(task));

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.schedule);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, duration * growScale);
        linearLayout.addView(task, layoutParams);

        rest = new Button(this);
        rest.setTag(restID);
        Log.d(TAG, "rest id = " + restID);
        restID+=2;
//        rest.setBackgroundColor(Color.WHITE);
        rest.setTextColor(Color.BLACK);
        // box too small, no text
        if (duration>=60) {
            rest.setText("Rest: "+ duration/restScale + " minutes");
        }
        rest.setTextSize(10);
        rest.setPadding(0, 0, 0, 0);
        GradientDrawable restDrawable = new GradientDrawable();
        restDrawable.setShape(GradientDrawable.RECTANGLE);
        restDrawable.setStroke(5, Color.DKGRAY);
        restDrawable.setColor(Color.WHITE);
        rest.setBackground(restDrawable);
        task.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View v) {

                Log.d(TAG, "view = " + v.getTag()+"");
                //To register the button with context menu.
                try {
                    targetView = v;
                    registerForContextMenu(targetView);
                    openContextMenu(targetView);
                    Log.d(TAG, "success, targetView tag = " + targetView.getTag()+"");
                }
                catch (Exception e){
                    Log.d(TAG, ""+e);

                }
            }
        });
        rest.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View v) {

                Log.d(TAG, "view = " + v.getTag()+"");
                //To register the button with context menu.
                try {
                    targetView = v;
                    registerForContextMenu(targetView);
                    openContextMenu(targetView);
                    Log.d(TAG, "success, targetView tag = " + targetView.getTag()+"");
                }
                catch (Exception e){
                    Log.d(TAG, ""+e);

                }
            }
        });
//        rest.setOnClickListener(new android.view.View.OnClickListener() {
//
//            public void onClick(View v) {
//                //To register the button with context menu.
//                try {
//                    targetView = v;
//                    registerForContextMenu(rest);
//                    openContextMenu(rest);
//                }
//                catch (Exception e){
//                    Log.d(TAG, ""+e);
//                }
//            }
//        });
        LinearLayout.LayoutParams restParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, duration);
        linearLayout.addView(rest, restParams);
    }

    public int randomColor() {
        int randomNumber = (int) Math.floor(Math.random() * 10);
        int[] colorArray = {Color.BLACK, Color.GRAY, Color.MAGENTA, Color.CYAN, Color.GRAY,
                Color.DKGRAY, Color.LTGRAY, Color.RED, Color.GREEN, Color.GREEN};
        return colorArray[randomNumber];
    }


    final int CONTEXT_MENU_EDIT = 1;
    final int CONTEXT_MENU_ARCHIVE = 2;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View
            v, ContextMenu.ContextMenuInfo menuInfo) {
//        menu.add(Menu.NONE, CONTEXT_MENU_EDIT, Menu.NONE, "Edit");
        menu.add(Menu.NONE, CONTEXT_MENU_ARCHIVE, Menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.schedule);
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
//            case CONTEXT_MENU_EDIT: {
//                registerForContextMenu(targetView);
//                openContextMenu(targetView);
//            }
//            break;
            case CONTEXT_MENU_ARCHIVE: {
                linearLayout.removeView(targetView);
                //Removing corresponding rest
//                restView = findViewById(targetView.getId());
//                linearLayout.removeView(restView);

                int index = Integer.parseInt(""+targetView.getTag())*2+1;
                Log.d(TAG, "id = " + index);
                timeArray.set(index, "0");
                //no event name
                timeArray.set(index-1, "");

//                String time = timeArray.get(targetView.getId()+1);
//                Log.d(TAG, "timearray at " + time + " is " + timeArray.get(targetView.getId()+1));
//                Log.d(TAG, "after new set, timearray at " + time + " is " + timeArray.get(targetView.getId()+1));
//                Log.d(TAG, ""+targetView.getId());
//                Log.d(TAG, timeArray.get(targetView.getId()+1));
//                Log.d(TAG, ""+timeArray);
            }
            break;
        }

        return super.onContextItemSelected(item);
    }

    public void start(View view) {
        int duration;
        if (timeArray.size()==0) {
            Log.d(TAG, "add events first");
            return;
        }
        if (savedTime==0) {
            totalDuration = 0;
            for (int i = 1; i < timeArray.size(); i += 2) {
//            Log.d(TAG, "time array at " + i + " is " + timeArray.get(i));
                totalDuration += Integer.parseInt(timeArray.get(i));
            }
            Log.d(TAG, "duration from savedtime 0 = "+totalDuration);
            duration = totalDuration;
        }
        else {
            duration = savedDuration;
            Log.d(TAG, "duration from savedtime exists = "+totalDuration);
        }
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        LinearLayout scheduleLayout = (LinearLayout) findViewById(R.id.schedule);
        timer = new Timer();
        Log.d(TAG, "totaldur = "+ duration);
        scheduleLayout.setMinimumHeight(screenHeight + duration * growScale);
        TimerTask timerTask = new TimerTaskObj();
        timer.schedule(timerTask, 0, 1000 / growScale);
        time = savedTime;

        FloatingActionButton pause = (FloatingActionButton) findViewById(R.id.pause);
        pause.setVisibility(View.VISIBLE);
        FloatingActionButton play = (FloatingActionButton) findViewById(R.id.start);
        play.setVisibility(View.GONE);
    }

    public void pause(View view) {
        FloatingActionButton pause = (FloatingActionButton) findViewById(R.id.pause);
        pause.setVisibility(View.GONE);
        FloatingActionButton play = (FloatingActionButton) findViewById(R.id.start);
        play.setVisibility(View.VISIBLE);
        savedTime = time;
        savedDuration = totalDuration-savedTime;
        timer.cancel();

    }

    private class TimerTaskObj extends TimerTask {
        public void run() {
//            Log.d(TAG, "" + time);
            time++;
            scheduleView = (ScrollView) findViewById(R.id.schedule_scroll);
            scheduleView.scrollTo(0, time);
            if (time > totalDuration * growScale) {
                timer.purge();
                timer.cancel();
                timer.purge();
                timer.cancel();
                Log.d(TAG, "cancelled");
            }
        }
    }

//    TimerTask timerTaskObj = new TimerTask() {
//        public void run() {
//            Log.d(TAG, ""+time);
//            time++;
//            scheduleView = (ScrollView)findViewById(R.id.schedule_scroll);
//            scheduleView.scrollTo(0, time);
//            if (time>mDuration*growScale) {
//                timer.purge();
//                timer.cancel();
//                timer.purge();
//                timer.cancel();
//                Log.d(TAG, "cancelled");
//            }
//        }
//    };
    }
