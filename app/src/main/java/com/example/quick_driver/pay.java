package com.example.quick_driver;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.razorpay.Checkout;

import org.json.JSONException;
import org.json.JSONObject;

public class pay extends AppCompatActivity implements pay1 {
    Button btPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        btPay = findViewById(R.id.bt_pay);
        String sAmount=getIntent().getStringExtra("key");
        String name=getIntent().getStringExtra("name");
        int amount= Math.round(Float.parseFloat(sAmount)*100);
        btPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Checkout checkout=new Checkout();
                checkout.setKeyID("rzp_test_PuWsKZ79BPFMdG");
                checkout.setImage(R.drawable.rzp_logo);
                JSONObject object=new JSONObject();
                try {
                    object.put("name",name);
                    object.put("description","Test Payment");
                    object.put("theme.color","0093DD");
                    object.put("currency","INR");
                    object.put("amount",amount);
                    object.put("prefill.contact","9677488536");
                    object.put("prefill.email","kowshimani1101@gmail.com");
                    checkout.open(pay.this,object);
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });
    }

    public void onPaymentSuccess(String s) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Payment ID");
        builder.setMessage(s);
        builder.show();

    }


    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext()
                ,s,Toast.LENGTH_SHORT).show();

    }

}