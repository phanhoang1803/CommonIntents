package com.example.commonintents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button button;
    private static final int REQUEST_CALL_PHONE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_PICK_MUSIC = 3;

    private TextView textViewTrackName;
    private Button btnChooseTrack;
    private SeekBar seekBarProgress;
    private Button btnPlayPause;

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


        // MAKE A CALL
        button = findViewById(R.id.btn_make_phone_call);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        button = findViewById(R.id.btn_open_phone_dialer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialPhoneNumber();
            }
        });


        // SEND EMAIL
        button = findViewById(R.id.btn_send_email);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeEmail();
            }
        });

        // TAKE A PHOTO
        button = findViewById(R.id.btnTakePhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });


        // PLAY MUSIC
        btnChooseTrack = findViewById(R.id.btnChooseTrack);

        btnChooseTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);

                try{
                    startActivityForResult(intent, REQUEST_PICK_MUSIC);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(MainActivity.this, "No music app found. Please choose a track manually.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button = findViewById(R.id.btn_search_map);
        EditText map = findViewById(R.id.search_map);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_map = map.getText().toString().trim();
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + text_map);
                    Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    intent.setPackage("com.google.android.apps.maps");
                try{
                    startActivity(intent);
                }
                catch(ActivityNotFoundException activityNotFoundException) {
                    Toast.makeText(MainActivity.this, "No search web found. Please search manually.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        EditText title = findViewById(R.id.eTitle);
        EditText location = findViewById(R.id.eLocation);
        EditText start = findViewById(R.id.eBegin);
        EditText end = findViewById(R.id.eEnd);
        findViewById(R.id.btn_calendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long startTimeInMillis = convertTimeToMilliseconds(start.getText().toString());
                long endTimeInMillis = convertTimeToMilliseconds(end.getText().toString());
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.Events.TITLE, title.getText().toString())
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, location.getText().toString())
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTimeInMillis)
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTimeInMillis);
                try{
                    startActivity(intent);
                }
                catch(ActivityNotFoundException activityNotFoundException){
                    Toast.makeText(MainActivity.this, "No search web found. Please search manually.", Toast.LENGTH_SHORT).show();
                }
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


    private void makePhoneCall() {
        EditText phoneNumberEditText = findViewById(R.id.input_phone_number);
        String phoneNumber = phoneNumberEditText.getText().toString();

        if (!phoneNumber.isEmpty()) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
            } else {
                // Permission already granted
                performCall(phoneNumber);
            }
        } else {
            Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
        }
    }

    private void performCall(String phoneNumber) {
        Intent dialIntent = new Intent(Intent.ACTION_CALL);
        dialIntent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            startActivity(dialIntent);
        } catch (SecurityException e) {
            e.printStackTrace();
            // Handle the case where the app doesn't have the CALL_PHONE permission
            Toast.makeText(this, "Permission denied. Please grant the CALL_PHONE permission.", Toast.LENGTH_SHORT).show();
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, make the phone call
                makePhoneCall();
            } else {
                // Permission denied, inform the user
                Toast.makeText(this, "Permission denied. Cannot make a phone call without permission.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void dialPhoneNumber() {
        EditText phoneNumberEditText = findViewById(R.id.input_phone_number);
        String phoneNumber = phoneNumberEditText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            // Fallback: Inform the user that no app can handle the dialing action.
            Toast.makeText(this, "No dialer found. Please dial manually.", Toast.LENGTH_SHORT).show();
        }
    }

    public void composeEmail() {
        EditText recipientsEditText = findViewById(R.id.editTextRecipients);
        EditText subjectEditText = findViewById(R.id.editTextSubject);
        EditText bodyEditText = findViewById(R.id.editTextBody);

        String recipients = recipientsEditText.getText().toString();
        String subject = subjectEditText.getText().toString();
        String body = bodyEditText.getText().toString();

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // Only email apps should handle this

        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipients});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        startActivity(Intent.createChooser(emailIntent, "Choose an Email client:"));
    }

    public void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try{
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, "No camera app found. Please take a photo manually.", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = findViewById(R.id.imgCamera);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bitmap img = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(img);
            imageView.setVisibility(View.VISIBLE);
        }
    }
    private long convertTimeToMilliseconds(String inputTime) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date time;
        try {
            time = sdf.parse(inputTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // Trả về giá trị âm nếu có lỗi xảy ra
        }

        if (time != null) {
            Calendar eventTime = Calendar.getInstance();
            eventTime.setTime(time);

            calendar.set(Calendar.HOUR_OF_DAY, eventTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, eventTime.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, 0);
        }

        return calendar.getTimeInMillis();
    }




}