package com.nate.atucafeteria.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.nate.atucafeteria.R;
import com.paystack.android.core.Paystack;
import com.paystack.android.ui.paymentsheet.PaymentSheet;
import com.paystack.android.ui.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ProcessOrder extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";
    private static final String PAYSTACK_SECRET_KEY = "sk_test_1fa583b4575700d23a2c086fed3aabba1f815b9b";
    private static final String PAYMENT_URL = "https://api.paystack.co/transaction/initialize";



    private ImageView foodImage;
    private TextView subTotal, pickupCost, deliveryCost, serviceFee, discount, total, location, orderDesc, deliveryFee, amountPayable, momo;
    private RadioButton pickupRadioButton, deliveryRadioButton;
    private MaterialButton orderButton;
    private FirebaseAuth firebaseAuth;
    private String firebaseUID;
    private double totalCost;
    private double deliveryPrice = 2.50;  // Set your delivery price here
    private double servicePrice = 1.50;   // Set your service fee here
    private double discountPrice;
    private ImageView paymentLogo;

    private TextView paymentOption;
    private String selectedPaymentMethod;
    private int selectedPaymentLogo;


    private PaymentSheet paymentSheet;
    private WebView paymentWebView;
    private BottomSheetDialog bottomSheetDialog;

    String foodName;
    String image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_process_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        // Retrieve passed data
        foodName = getIntent().getStringExtra("foodName");
        image = getIntent().getStringExtra("foodImage");
        totalCost = getIntent().getDoubleExtra("totalPrice", 0);

        if (null != foodName && 0 != totalCost && null != image){
            Glide.with(this)
                    .load(image)
                    .into(foodImage);

            orderDesc.setText(foodName+ "\nGHC "+ String.format("%.2f", totalCost));
        }

        // Set initial values
        pickupCost.setText("GHC 0.00");
        deliveryCost.setText(String.format("GHC %.2f", deliveryPrice));
        serviceFee.setText(String.format("GHC %.2f", servicePrice));
        discount.setText("GHC 0.00");
        deliveryFee.setText("GHC 0.00");

        // Calculate and set total price
        updateTotalPrice();

        // Handle radio button selection
        pickupRadioButton.setOnClickListener(view -> {
            deliveryRadioButton.setChecked(false); // Uncheck delivery option
            location.setEnabled(false);
            location.setText("");
            deliveryFee.setText("GHC 0.00"); // Set delivery cost to 0 for pickup
            updateTotalPrice();
        });

        deliveryRadioButton.setOnClickListener(view -> {
            pickupRadioButton.setChecked(false); // Uncheck pickup option
            location.setEnabled(true);
            deliveryFee.setText("GHC 2.50");
            deliveryCost.setText(String.format("GHC %.2f", deliveryPrice)); // Set delivery cost
            updateTotalPrice();
        });

        // Handle proceed button click
        orderButton.setOnClickListener(view -> {
            if (deliveryRadioButton.isChecked() && TextUtils.isEmpty(location.getText().toString())) {
                Toast.makeText(ProcessOrder.this, "Please enter your location", Toast.LENGTH_SHORT).show();
            } else {
                placeOrder();
               // try {
                    //pay();
                //showWebViewBottomSheet();
               // startPaymentProcess();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
            }
        });



        Paystack.builder()
                .setPublicKey("pk_test_1724ec4330298e549b8380cb539c4f7c03fdc2cb")
                .setLoggingEnabled(true)
                .build();


        paymentSheet = new PaymentSheet(this, this::paymentComplete);


        // Handle the click to show bottom sheet
        paymentOption.setOnClickListener(view -> showPaymentOptions());
    }

    private void initViews() {
        subTotal = findViewById(R.id.sub_total);
        orderDesc = findViewById(R.id.food_name);
        foodImage = findViewById(R.id.food_image);
        pickupCost = findViewById(R.id.pickupCost);
        deliveryCost = findViewById(R.id.deliveryCost);
        deliveryFee = findViewById(R.id.delivery_fee);
        serviceFee = findViewById(R.id.service_fee);
        discount = findViewById(R.id.discount);
        total = findViewById(R.id.total);
        location = findViewById(R.id.location);
        pickupRadioButton = findViewById(R.id.pickupRadioButton);
        deliveryRadioButton = findViewById(R.id.deliveryRadioButton);
        orderButton = findViewById(R.id.order);
        firebaseAuth = FirebaseAuth.getInstance();
        amountPayable = findViewById(R.id.amount_payable);
        momo = findViewById(R.id.momo);
        paymentOption = findViewById(R.id.payment_option);
        paymentLogo = findViewById(R.id.payment_logo);
    }

    private void updateTotalPrice() {
        double deliveryPrice = deliveryRadioButton.isChecked() ? this.deliveryPrice : 0.00;
        double totalPrice = totalCost + deliveryPrice + servicePrice;
        total.setText("GHC " + String.format("%.2f", totalPrice));
        amountPayable.setText("GHC " + String.format("%.2f", totalPrice));
        momo.setText("GHC " + String.format("%.2f", totalPrice));
    }

    private void placeOrder() {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("cafeteria_orders");

        String orderID = orderRef.push().getKey();
        // Get the current date and time
        Calendar calendar = Calendar.getInstance();

        // Define the date format
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy hh:mma", Locale.getDefault());

        // Format the current date and time
        String formattedDateTime = sdf.format(calendar.getTime());
        Map<String, Object> orderDetails = new HashMap<>();
        orderDetails.put("imageUrl", image);
        orderDetails.put("id", orderID);
        orderDetails.put("buyerId", firebaseAuth.getUid());
        orderDetails.put("name", foodName);
        orderDetails.put("pickupCost", pickupCost.getText().toString());
        orderDetails.put("deliveryCost", deliveryCost.getText().toString());
        orderDetails.put("serviceFee", serviceFee.getText().toString());
        orderDetails.put("discount", discount.getText().toString());
        orderDetails.put("price", total.getText().toString());
        orderDetails.put("location", location.getText().toString());
        orderDetails.put("deliveryStatus", "Servicing");
        orderDetails.put("orderTime", formattedDateTime);



        orderRef.child(firebaseAuth.getUid()).child("orders").child(orderID).setValue(orderDetails);
        orderRef.child("all_orders").push().setValue(orderDetails);
//        userOrderRef.push().setValue(orderDetails);
//        allOrdersRef.push().setValue(orderDetails);

        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ProcessOrder.this, Home.class));
        finish();
    }


    private void showPaymentOptions() {
        // Create bottom sheet dialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.payment_options_bottom_sheet, null);
        bottomSheetDialog.setContentView(sheetView);


        // Set click listeners for payment options
        sheetView.findViewById(R.id.mtn_option).setOnClickListener(view -> {
            selectedPaymentMethod = "MTN Mobile Money";
            selectedPaymentLogo = R.drawable.mtn; // Replace with your actual drawable resource
            bottomSheetDialog.dismiss();
            handlePaymentSelection();
        });

        sheetView.findViewById(R.id.telecel_option).setOnClickListener(view -> {
            selectedPaymentMethod = "Telecel Cash";
            selectedPaymentLogo = R.drawable.telecel; // Replace with your actual drawable resource
            bottomSheetDialog.dismiss();
            handlePaymentSelection();
        });

        sheetView.findViewById(R.id.at_option).setOnClickListener(view -> {
            selectedPaymentMethod = "AT Money";
            selectedPaymentLogo = R.drawable.airteltigo; // Replace with your actual drawable resource
            bottomSheetDialog.dismiss();
            handlePaymentSelection();
        });

        sheetView.findViewById(R.id.bank_card_option).setOnClickListener(view -> {
            selectedPaymentMethod = "Bank Card";
            selectedPaymentLogo = R.drawable.bankcard; // Replace with your actual drawable resource
            bottomSheetDialog.dismiss();
            handlePaymentSelection();
        });

        bottomSheetDialog.show();
    }

    private void handlePaymentSelection() {
        // Handle the selected payment method and logo here
        paymentOption.setText(selectedPaymentMethod);
        //paymentOption.setCompoundDrawablesWithIntrinsicBounds(selectedPaymentLogo, 0, 0, 0);
        // You can use the selectedPaymentMethod and selectedPaymentLogo as needed
        paymentLogo.setImageResource(selectedPaymentLogo);
    }


    private void pay() throws IOException {


        final String[] testCode = new String[1];
        final String[] reference = new String[1];
        OkHttpClient client = new OkHttpClient();

        // Define media type and request body
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{ \"email\": \"dzrekenathan2002@gmail.com\", \"amount\": \"500000\" }");

        //RequestBody body = RequestBody.create(mediaType, "{ \"amount\": "+totalCost+",\r\n      \"email\": \"customer@email.com\",\r\n      \"currency\": \"GHS\",\r\n      \"mobile_money\": {\r\n        \"phone\" : \"0551234987\",\r\n        \"provider\" : \"mtn\"\r\n      }\r\n    }");
        // Build the request
        Request request = new Request.Builder()
                .url(PAYMENT_URL)
                .post(body) // Use POST method
                .addHeader("Authorization", "Bearer " + PAYSTACK_SECRET_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        // Execute the request and handle the response
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Handle failure
                Toast.makeText(ProcessOrder.this, "Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    // Parse the response body
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        // Extract status, message, and data
                        boolean status = jsonResponse.getBoolean("status");
                        String message = jsonResponse.getString("message");
                        JSONObject data = jsonResponse.getJSONObject("data");
                        String authorizationUrl = data.getString("authorization_url");
                        String accessCode = data.getString("access_code");
                        String reference = data.getString("reference");

                        // Log or use the extracted values
                        paymentSheet.launch(accessCode);

                        // Navigate to the URL or handle it as needed
                        // For example, open the authorization URL in a browser
                        // Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authorizationUrl));
                        // startActivity(browserIntent);

                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error", e);
                    }
                } else {
                    // Handle unsuccessful response
                    Log.e(TAG, "Response unsuccessful: " + response.message());
                }
            }
        });
    }



    private void paymentComplete(PaymentSheetResult paymentSheetResult) {
        String message;

        if (paymentSheetResult instanceof PaymentSheetResult.Cancelled) {
            message = "Cancelled";
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            PaymentSheetResult.Failed failedResult = (PaymentSheetResult.Failed) paymentSheetResult;
            Log.e("Payment failed",
                    failedResult.getError().getMessage() != null ? failedResult.getError().getMessage() : "Failed",
                    failedResult.getError());
            message = failedResult.getError().getMessage() != null ? failedResult.getError().getMessage() : "Failed";
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Log.d("Payment successful",
                    ((PaymentSheetResult.Completed) paymentSheetResult).getPaymentCompletionDetails().toString());
            message = "Successful";
            placeOrder();
        } else {
            message = "You shouldn't be here";
        }

        Toast.makeText(this, "Payment " + message, Toast.LENGTH_SHORT).show();
    }


    private void startPaymentProcess() {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{ \"email\": \"customer@email.com\", \r\n      \"amount\": \"500000\"\r\n    }");
        Request request = new Request.Builder()
                .url("https://api.paystack.co/transaction/initialize")
                .method("POST", body)
                .addHeader("Authorization", "Bearer sk_test_1fa583b4575700d23a2c086fed3aabba1f815b9b")
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(ProcessOrder.this, "Failed to initiate payment", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONObject data = jsonResponse.getJSONObject("data");
                        String authorizationUrl = data.getString("authorization_url");
                        String access_code = data.getString("access_code");

                        // Start the PaymentWebViewActivity and pass the authorization URL
                        //Intent intent = new Intent(ProcessOrder.this, PaymentWebView.class);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authorizationUrl));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                        intent.putExtra("access_code", access_code);
                        intent.putExtra("AUTHORIZATION_URL", authorizationUrl);
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(ProcessOrder.this, "Payment initialization failed", Toast.LENGTH_SHORT).show());
                }
            }
        });





    }

//    private void setupWebView(WebView paymentWebView) {
//        WebSettings webSettings = paymentWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        paymentWebView.setWebViewClient(new WebViewClient());
//    }
//
//    private void startPaymentProcess(WebView paymentWebView) {
//        OkHttpClient client = new OkHttpClient().newBuilder().build();
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{ \"email\": \"customer@email.com\", \r\n      \"amount\": \"500000\"\r\n    }");
//        Request request = new Request.Builder()
//                .url("https://api.paystack.co/transaction/initialize")
//                .method("POST", body)
//                .addHeader("Authorization", "Bearer sk_test_1fa583b4575700d23a2c086fed3aabba1f815b9b")
//                .addHeader("Content-Type", "application/json")
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(() -> Toast.makeText(ProcessOrder.this, "Failed to initiate payment", Toast.LENGTH_SHORT).show());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String responseBody = response.body().string();
//                    try {
//                        JSONObject jsonResponse = new JSONObject(responseBody);
//                        JSONObject data = jsonResponse.getJSONObject("data");
//                        String authorizationUrl = data.getString("authorization_url");
//
//                        runOnUiThread(() -> paymentWebView.loadUrl(authorizationUrl));
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    runOnUiThread(() -> Toast.makeText(ProcessOrder.this, "Payment initialization failed", Toast.LENGTH_SHORT).show());
//                }
//            }
//        });
//    }
//    private void showWebViewBottomSheet() {
//        // Inflate the bottom sheet layout
//        View bottomSheetView = getLayoutInflater().inflate(R.layout.complete_payment_sheet, null);
//
//        // Initialize the BottomSheetDialog
//        bottomSheetDialog = new BottomSheetDialog(this);
//        bottomSheetDialog.setContentView(bottomSheetView);
//
//        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
//        //bottomSheetBehavior.setPeekHeight(600);  // Set a custom height (in pixels)
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//
//
//        // Initialize the WebView
//        WebView paymentWebView = bottomSheetView.findViewById(R.id.payment_webview);
//        setupWebView(paymentWebView);
//
//        // Load the URL in WebView after API call
//        startPaymentProcess(paymentWebView);
//
//
//
//
//
//        // Show the bottom sheet
//        bottomSheetDialog.show();
//    }
    /**


    private void showWebViewInBottomSheet(String authorizationUrl) {
        // Create bottom sheet dialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.complete_payment_sheet, null);
        bottomSheetDialog.setContentView(sheetView);

        // Initialize WebView
        paymentWebView = sheetView.findViewById(R.id.payment_webview);
        paymentWebView.getSettings().setJavaScriptEnabled(true);
        paymentWebView.setWebViewClient(new WebViewClient());

        // Load the URL in WebView
        paymentWebView.loadUrl(authorizationUrl);

        // Show the bottom sheet dialog
        bottomSheetDialog.show();
    }

    private void startPaymentProcess() {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{ \"email\": \"customer@email.com\", \r\n      \"amount\": \"500000\"\r\n    }");
        Request request = new Request.Builder()
                .url("https://api.paystack.co/transaction/initialize")
                .method("POST", body)
                .addHeader("Authorization", "Bearer sk_test_1fa583b4575700d23a2c086fed3aabba1f815b9b")
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(ProcessOrder.this, "Failed to initiate payment", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONObject data = jsonResponse.getJSONObject("data");
                        String authorizationUrl = data.getString("authorization_url");

                        runOnUiThread(() -> {
                            // Open the URL in the bottom sheet WebView
                            showWebViewInBottomSheet(authorizationUrl);
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(ProcessOrder.this, "Payment initialization failed", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    **/






}
