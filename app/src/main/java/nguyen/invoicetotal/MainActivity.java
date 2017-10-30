package nguyen.invoicetotal;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.content.SharedPreferences.Editor;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity
    implements OnEditorActionListener{

    //define widget variables
    private EditText subtotalET;
    private TextView discountPercentTV;
    private TextView discountTotalTV;
    private TextView totalTV;

    //string instance variable
    private String subtotalString = "";

    private float subtotal;
    private float discount;
    private float discountTotal;
    private float total;

    //format for percent and currency
    NumberFormat percent = NumberFormat.getPercentInstance();
    NumberFormat currency = NumberFormat.getCurrencyInstance();

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get reference to the widget
        subtotalET = (EditText) findViewById(R.id.subtotalET);
        discountPercentTV = (TextView) findViewById(R.id.discountPercentTV);
        discountTotalTV = (TextView) findViewById(R.id.discountTotalTV);
        totalTV = (TextView) findViewById(R.id.totalTV);

        subtotalET.setOnEditorActionListener(this);

        savedValues = getSharedPreferences("savedValues", MODE_PRIVATE);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE ||
            actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

            calculateAndDisplay();
        }
        return false;
    }

    private void calculateAndDisplay() {
        //get the subtotal from user
        subtotalString = subtotalET.getEditableText().toString();

        if(subtotalString.equals("")) {
            subtotal = 0;
        } else {
            subtotal = Float.parseFloat(subtotalString);
        }

        if(subtotal >= 200) {
            discount = 0.2f;
        } else if(subtotal >= 100) {
            discount = 0.1f;
        } else {
            discount = 0.0f;
        }

        //calculate the discount total and total
        discountTotal = subtotal * discount;
        total = subtotal - discountTotal;

        //display the widgets
        discountPercentTV.setText(percent.format(discount));
        discountTotalTV.setText(currency.format(discountTotal));
        totalTV.setText(currency.format(total));
    }

    @Override
    protected void onPause() {
        //save the instance variables
        Editor editor = savedValues.edit();
        editor.putString("subtotalString", subtotalString);
        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //get the instance variables
        subtotalString = savedValues.getString("subtotalString", "");

        //set the bill amount on its widget
        subtotalET.setText(subtotalString);

        //calculate and display
        calculateAndDisplay();
    }
}
