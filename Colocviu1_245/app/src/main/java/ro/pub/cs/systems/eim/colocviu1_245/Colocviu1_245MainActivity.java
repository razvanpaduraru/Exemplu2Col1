package ro.pub.cs.systems.eim.colocviu1_245;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Colocviu1_245MainActivity extends AppCompatActivity {

    private EditText termText;
    private TextView allTermsView;
    private Button addButton;
    private ArrayList<Integer> numbers = new ArrayList<>();
    private Button computeButton;
    private Integer computedSum = 0;

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if (intent != null && intent.getExtras().containsKey("sumacalculata")) {
                Toast.makeText(getApplication(), "Suma este : " + intent.getIntExtra("sumacalculata", -1), Toast.LENGTH_LONG).show();
                computedSum = intent.getIntExtra("sumacalculata", -1);
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
            System.out.println(allTermsView.getText().toString());
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
            System.out.println("Intru aici\n");
            allTermsView = (TextView)findViewById(R.id.all_terms);
            allTermsView.setText(savedInstanceState.getString("necalculat"));
            numbers = savedInstanceState.getIntegerArrayList("necalculat-numere");
        }
    }
}
