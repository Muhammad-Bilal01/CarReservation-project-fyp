package com.example.carreservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class VendorRegistrationActivity1 extends AppCompatActivity {

    private EditText accountTitleET;
    private EditText ibanET;
    private EditText bankNameET;
    private EditText branchNameET;
    private Button nextBtn;
    private ImageButton backButtonIB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_registration1);

        initializeComponents();

        nextBtnOnClickListener();
        backButtonIBOnClickListener();
    }

    private void nextBtnOnClickListener() {

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearErrors();
                boolean isValid = verifyInformation();

                if (isValid)
                {
                    Intent intent = getIntent();

                    String email = intent.getStringExtra("email");
                    String password = intent.getStringExtra("password");
                    String confirmPassword = intent.getStringExtra("confirmPassword");
                    String userType = intent.getStringExtra("userType");
                    String fullName = intent.getStringExtra("fullName");
                    String cnicNo = intent.getStringExtra("cnicNo");
                    String mobileNo = intent.getStringExtra("mobileNo");
                    String address = intent.getStringExtra("address");
                    String imageUrl = intent.getStringExtra("imageUrl");

                    String accountTitle = accountTitleET.getText().toString();
                    String iban = ibanET.getText().toString();
                    String bankName = bankNameET.getText().toString();
                    String branchName = branchNameET.getText().toString();

                    intent = new Intent(VendorRegistrationActivity1.this, VendorRegistrationActivity2.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    intent.putExtra("confirmPassword", confirmPassword);
                    intent.putExtra("userType", userType);
                    intent.putExtra("fullName", fullName);
                    intent.putExtra("cnicNo", cnicNo);
                    intent.putExtra("mobileNo", mobileNo);
                    intent.putExtra("address", address);
                    intent.putExtra("accountTitle", accountTitle);
                    intent.putExtra("iban", iban);
                    intent.putExtra("bankName", bankName);
                    intent.putExtra("branchName", branchName);
                    intent.putExtra("imageUrl", imageUrl);

                    startActivity(intent);
                }
            }
        });
    }

    private void backButtonIBOnClickListener() {

        backButtonIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }

    private boolean verifyInformation() {

        boolean flag = true;

        String accountTitle = accountTitleET.getText().toString();
        String iban = ibanET.getText().toString();
        String bankName = bankNameET.getText().toString();
        String branchName = branchNameET.getText().toString();

        if (accountTitle.isEmpty())
        {
            flag = false;
            accountTitleET.setError("Required");
        }
        else if (!accountTitle.matches("^[a-zA-Z ]+$"))
        {
            flag = false;
            accountTitleET.setError("Invalid Title");
        }

        if (iban.isEmpty())
        {
            flag = false;
            ibanET.setError("Required");
        }

        if (bankName.isEmpty())
        {
            flag = false;
            bankNameET.setError("Required");
        }
        else if (!bankName.matches("^[a-zA-Z ]+$"))
        {
            flag = false;
            bankNameET.setError("Invalid Bank Name");
        }

        if (branchName.isEmpty())
        {
            flag = false;
            branchNameET.setError("Required");
        }
        else if (!branchName.matches("^[a-zA-Z ]+$"))
        {
            flag = false;
            branchNameET.setError("Invalid Branch Name");
        }

        return flag;
    }

    private void clearErrors() {

        accountTitleET.setError(null);
        ibanET.setError(null);
        bankNameET.setError(null);
        branchNameET.setError(null);
    }

    private void initializeComponents() {

        accountTitleET = findViewById(R.id.accountTitleET);
        ibanET = findViewById(R.id.ibanET);
        bankNameET = findViewById(R.id.bankNameET);
        branchNameET = findViewById(R.id.branchNameET);
        nextBtn = findViewById(R.id.nextBtn);
        backButtonIB = findViewById(R.id.backButtonIB);
    }
}