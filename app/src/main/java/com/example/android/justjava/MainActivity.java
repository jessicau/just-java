package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if (quantity >=100) {
            quantity = 100;

            // Display error message as a toast
            Toast.makeText(this, getString(R.string.max_coffees_toast), Toast.LENGTH_SHORT).show();
        } else {
            quantity++;
        }
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity <= 0) {
            quantity = 0;

            // Display error message as a toast
            Toast.makeText(this, getString(R.string.min_coffees_toast), Toast.LENGTH_SHORT).show();
        } else {
            quantity--;
        }
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        EditText nameTextField = (EditText) findViewById(R.id.name_text_field);
        String orderName = nameTextField.getText().toString();

        CheckBox whippedCreamCheckbox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckbox.isChecked();

        CheckBox chocolateCheckbox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckbox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        composeEmail(orderName, createOrderSummary(price, orderName, hasWhippedCream, hasChocolate));
    }

    /**
     * This method opens an email app to send in the coffee order
     * @param message
     */
    public void composeEmail(String orderName, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject) + orderName);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method calculates the price of the coffee order
     *
     * @param hasWhippedCream is whether or not the order should have whipped cream
     * @param hasChocolate is whether or not the order should have chocolate
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        // Base price of a cup of coffee
        int basePrice = 5;

        // Add $1 if customer selects whipped cream
        if(hasWhippedCream) {
            basePrice = basePrice + 1;
        }

        // Add $2 if customer selects chocolate
        if (hasChocolate) {
            basePrice = basePrice + 2;
        }

        return quantity * basePrice;
    }

    private String createOrderSummary(int price, String orderName, boolean hasWhippedCream, boolean hasChocolate) {
        String priceMessage = getString(R.string.order_name) + orderName;
        priceMessage += getString(R.string.whipped_cream_order) + hasWhippedCream;
        priceMessage += getString(R.string.chocolate_order) + hasChocolate;
        priceMessage += getString(R.string.order_quantity) + quantity;
        priceMessage += getString(R.string.order_total) + price;
        priceMessage += getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

}