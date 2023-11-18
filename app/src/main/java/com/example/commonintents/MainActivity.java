package com.example.commonintents;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CREATE AN ALARM
        button = findViewById(R.id.btn_create_alarm);
        EditText message = findViewById(R.id.edt_alarm_clock_message);
        EditText hour = findViewById(R.id.edt_alarm_clock_hour);
        EditText minute = findViewById(R.id.edt_alarm_clock_minute);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimeValid(hour.getText().toString(), minute.getText().toString())) {
                    createAlarm(message.getText().toString(),
                            Integer.parseInt(hour.getText().toString()),
                            Integer.parseInt(minute.getText().toString()));
                } else {
                    Toast.makeText(MainActivity.this, "Invalid time. Please enter a valid time.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // CREATE A TIMER
        button = findViewById(R.id.btn_create_timer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimeValid(hour.getText().toString(), minute.getText().toString())) {
                    startTimer(message.getText().toString(),
                            (Integer.parseInt(hour.getText().toString()) * 60 +
                                    Integer.parseInt(minute.getText().toString())) * 60);
                } else {
                    Toast.makeText(MainActivity.this, "Invalid time. Please enter a valid time.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // SHOW ALARMS
        button = findViewById(R.id.btn_show_alarms);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllAlarms();
            }
        });


        // OPEN WEB PAGE
        button = findViewById(R.id.btn_open_web_page);
        EditText url = findViewById(R.id.edt_url);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage(url.getText().toString());
            }
        });

        button = findViewById(R.id.btn_search_web);
        EditText query = findViewById(R.id.edt_query);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchWeb(query.getText().toString());
            }
        });

    }

    public void searchWeb(String query) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            Toast.makeText(this, "No search web found. Please search manually.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isTimeValid(String hourStr, String minuteStr) {
        try {
            int hour = Integer.parseInt(hourStr);
            int minute = Integer.parseInt(minuteStr);

            // Check if hour and minute are within valid range
            return (hour >= 0 && hour <= 23) && (minute >= 0 && minute <= 59);
        } catch (NumberFormatException e) {
            return false; // Parsing failed, not a valid number
        }
    }
    public void createAlarm(String message, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            // Fallback: Prompt the user to choose an alarm app or guide them to settings.
            Toast.makeText(this, "No alarm app found. Please set the alarm manually.", Toast.LENGTH_SHORT).show();
        }
    }

    public void startTimer(String message, int seconds) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            // Fallback: Prompt the user to choose an alarm app or guide them to settings.
            Toast.makeText(this, "No alarm app found. Please set the alarm manually.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAllAlarms() {
        Intent intent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            // Fallback: Prompt the user to choose an alarm app or guide them to settings.
            Toast.makeText(this, "No alarm app found. Please set the alarm manually.", Toast.LENGTH_SHORT).show();
        }
    }

    public void openWebPage(String url) {
        if (!url.startsWith("http://") || !url.startsWith("https://"))
            url = "https://" + url;

        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            // Fallback: Inform the user that no app can handle the web browsing action.
            Toast.makeText(this, "No web browser found. Please open a web browser manually.", Toast.LENGTH_SHORT).show();
        }
    }

}