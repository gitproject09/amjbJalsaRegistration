package org.amcbd.jalsa_registration.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.amcbd.jalsa_registration.R;

public class ChoosingActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing);

        findViewById(R.id.btnRegister).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnLogin:
                Intent regList = new Intent(ChoosingActivity.this, LoginActivity.class);
                startActivity(regList);
                break;

            case R.id.btnRegister:
                Intent intent = new Intent(ChoosingActivity.this, RegistrationActivity.class);
                startActivity(intent);
                /*if (isNetworkAvailable()) {
                    Intent intent = new Intent(ChoosingActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "No internet Connection!!!", Toast.LENGTH_SHORT).show();
                }*/
                break;


        }
    }
}