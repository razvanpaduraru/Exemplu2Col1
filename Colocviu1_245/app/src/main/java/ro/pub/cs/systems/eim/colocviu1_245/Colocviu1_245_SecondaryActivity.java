package ro.pub.cs.systems.eim.colocviu1_245;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class Colocviu1_245_SecondaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras().containsKey("numere")) {
            ArrayList<Integer> numbers = intent.getIntegerArrayListExtra("numere");
            int sum = 0;
            for (int i = 0; i < numbers.size(); ++i)
                sum += numbers.get(i);
            intent.putExtra("sumacalculata", sum);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
