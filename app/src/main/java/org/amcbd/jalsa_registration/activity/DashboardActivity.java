package org.amcbd.jalsa_registration.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.amcbd.jalsa_registration.R;
import org.amcbd.jalsa_registration.base_url.BaseUrl;
import org.amcbd.jalsa_registration.model.SessionHandler;
import org.amcbd.jalsa_registration.model.User;

public class DashboardActivity extends BaseActivity {

    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        TextView welcomeText = findViewById(R.id.welcomeText);
        ImageView ivProfileImage = findViewById(R.id.ivProfileImage);

        Picasso.get()
                .load(BaseUrl.baseUrl + "upload_image/" + user.getImagePath())
                // .memoryPolicy(MemoryPolicy.NO_CACHE)
                // .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .fit()
                .noFade()
                .into(ivProfileImage);

        welcomeText.setText("Welcome To The 96th Jalsa Salana Bangladesh\n\n\n" + user.getFullName() +"\n\n\n" + "Your session will expire on :" + user.getSessionExpiryDate());

        Button logoutBtn = findViewById(R.id.btnLogout);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent i = new Intent(DashboardActivity.this, ChoosingActivity.class);
                startActivity(i);
                finish();

            }
        });
    }
}
