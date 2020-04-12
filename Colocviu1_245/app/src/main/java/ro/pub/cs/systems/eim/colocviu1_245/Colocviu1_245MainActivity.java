package ro.pub.cs.systems.eim.colocviu1_245;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ro.pub.cs.systems.eim.colocviu1_245.service.Colocviu1_245Service;

public class Colocviu1_245MainActivity extends AppCompatActivity {

    private EditText termText;
    private TextView allTermsView;
    private Button addButton;
    private ArrayList<Integer> numbers = new ArrayList<>();
    private Button computeButton;
    private Integer computedSum = 0;
    private Integer serviceStatus = 0;
    private IntentFilter intentFilter = new IntentFilter();

    private AddButtonClickListener addButtonClickListener = new AddButtonClickListener();
    private class AddButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String item = termText.getText().toString();
            termText.setText("");
            if (item.length() != 0) {
                numbers.add(Integer.parseInt(item));
                if (allTermsView.getText().toString().length() == 0) {
                    allTermsView.setText(item);
                } else {
                    String newItems = allTermsView.getText().toString() + "+" + item;
                    allTermsView.setText(newItems);
                }
            }


        }
    }

    private ComputeButtonClickListener computeButtonClickListener = new ComputeButtonClickListener();
    private class ComputeButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), Colocviu1_245_SecondaryActivity.class);
            intent.putIntegerArrayListExtra("numere", numbers);
            startActivityForResult(intent, 1);
        }
    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.d("[Message]", intent.getStringExtra("message"));
            Toast.makeText(getApplication(), intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_245_main);

        termText = (EditText)findViewById(R.id.next_term);
        allTermsView = (TextView)findViewById(R.id.all_terms);
        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(addButtonClickListener);
        computeButton = (Button)findViewById(R.id.compute_button);
        computeButton.setOnClickListener(computeButtonClickListener);

        intentFilter.addAction("threadSuma");

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if (intent != null && intent.getExtras().containsKey("sumacalculata")) {
                Toast.makeText(getApplication(), "Suma este : " + intent.getIntExtra("sumacalculata", -1), Toast.LENGTH_LONG).show();
                computedSum = intent.getIntExtra("sumacalculata", -1);
            }

            if (computedSum % 10 == 0 && computedSum >= 10
                    && serviceStatus == 0) {
                Intent intentServ = new Intent(getApplicationContext(), Colocviu1_245Service.class);
                String currentTime = Calendar.getInstance().getTime().toString();
                intentServ.putExtra("data_si_ora", currentTime);
                intentServ.putExtra("suma_calculata", computedSum);
                getApplicationContext().startService(intentServ);
                serviceStatus = 1;
            } else if (computedSum % 10 == 0 && computedSum >= 10 && serviceStatus == 1) {
                Intent intentServ = new Intent(getApplicationContext(), Colocviu1_245Service.class);
                stopService(intentServ);
                Intent newIntentServ = new Intent(getApplicationContext(), Colocviu1_245Service.class);
                String currentTime = Calendar.getInstance().getTime().toString();
                newIntentServ.putExtra("data_si_ora", currentTime);
                newIntentServ.putExtra("suma_calculata", computedSum);
                getApplicationContext().startService(newIntentServ);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        int currentSum = 0;
        for (Integer i : numbers)
            currentSum += i;
        if (computedSum != currentSum || computedSum == 0) {
            allTermsView = (TextView)findViewById(R.id.all_terms);
            savedInstanceState.putString("necalculat", allTermsView.getText().toString());
            savedInstanceState.putIntegerArrayList("necalculat-numere", numbers);
        } else
            savedInstanceState.putInt("calculata", computedSum);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("calculata")) {
            TextView tl = (TextView)findViewById(R.id.all_terms);
            numbers.add(savedInstanceState.getInt("calculata"));
            tl.setText(String.valueOf(savedInstanceState.getInt("calculata")));
        } else {
            allTermsView = (TextView)findViewById(R.id.all_terms);
            allTermsView.setText(savedInstanceState.getString("necalculat"));
            numbers = savedInstanceState.getIntegerArrayList("necalculat-numere");
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, Colocviu1_245Service.class);
        stopService(intent);
        super.onDestroy();
    }
}
