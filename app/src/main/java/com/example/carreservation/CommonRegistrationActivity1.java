package com.example.carreservation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CommonRegistrationActivity1 extends AppCompatActivity {

    private ImageButton backButtonIB;
    private RadioGroup signUpTypeRG;
    private RadioButton customerRB;
    private RadioButton providerRB;
    private EditText emailET;
    private EditText passwordET;
    private EditText confirmPasswordET;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_registration1);

        initializeComponents();
        
        backButtonIBOnClickListener();
        nextBtnOnClickListener();
        signUpTypeRGOnCheckedChangeListener();
    }

    private void nextBtnOnClickListener() {

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearErrors();
                boolean isValid = verifyInformation();

                if (isValid)
                {
                    String email = emailET.getText().toString();
                    String password = passwordET.getText().toString();
                    String confirmPassword = confirmPasswordET.getText().toString();
                    String userType = customerRB.isChecked() == true ? "Customer" : "Vendor";

                    Intent intent = new Intent(CommonRegistrationActivity1.this, CommonRegistrationActivity2.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    intent.putExtra("confirmPassword", confirmPassword);
                    intent.putExtra("userType", userType);

                    startActivity(intent);
                }
            }
        });
    }

    private void signUpTypeRGOnCheckedChangeListener() {

        signUpTypeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int whiteColor = ContextCompat.getColor(getApplicationContext(), R.color.white);
                int themeColor = ContextCompat.getColor(getApplicationContext(), R.color.theme_color);

                RadioButton checkedRadioButton = findViewById(checkedId);
                String selectedText = checkedRadioButton.getText().toString();
                RadioButton unCheckedRadioButton;

                checkedRadioButton.setTextColor(whiteColor);

                if (selectedText.equalsIgnoreCase("I want to park"))
                {
                    unCheckedRadioButton = findViewById(R.id.providerRB);
                }
                else
                {
                    unCheckedRadioButton = findViewById(R.id.customerRB);
                }

                unCheckedRadioButton.setTextColor(themeColor);
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

    private void initializeComponents() {

        backButtonIB = findViewById(R.id.backButtonIB);
        signUpTypeRG = findViewById(R.id.signUpTypeRG);
        customerRB = findViewById(R.id.customerRB);
        providerRB = findViewById(R.id.providerRB);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        confirmPasswordET = findViewById(R.id.confirmPasswordET);
        nextBtn = findViewById(R.id.nextBtn);
    }

    private boolean verifyInformation() {

        CharSequence email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String confirmPassword = confirmPasswordET.getText().toString();

        boolean flag = true;
        boolean passwordFlag = true;

        if (email.toString().isEmpty())
        {
            flag = false;
            emailET.setError("Required");
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            flag = false;
            emailET.setError("Invalid Email");
        }

        if (password.isEmpty())
        {
            flag = false;
            passwordFlag = false;
            passwordET.setError("Required");
        }

        if (confirmPassword.isEmpty())
        {
            flag = false;
            passwordFlag = false;
            confirmPasswordET.setError("Required");
        }

        if (!passwordFlag)
        {
            return flag;
        }

        if (!password.equals(confirmPassword))
        {
            passwordET.setError("Passwords Don't Match");
            confirmPasswordET.setError("Passwords Don't Match");
            flag = false;
        }
        else
        {
            if (password.length() < 7)
            {
                passwordET.setError("At Least 7 Chars");
                confirmPasswordET.setError("At Least 7 Chars");
                flag = false;
            }
        }

        return flag;
    }

    private void clearErrors() {

        emailET.setError(null);
        passwordET.setError(null);
        confirmPasswordET.setError(null);
    }
}