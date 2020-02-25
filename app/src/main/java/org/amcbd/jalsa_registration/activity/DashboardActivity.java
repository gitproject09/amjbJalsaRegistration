package org.amcbd.jalsa_registration.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private TextView tvToolbarTitle;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        initializeToolbar("", true);

        tvToolbarTitle = findViewById(R.id.header_toolbar_title);
        tvToolbarTitle.setText("Details Information");
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        TextView welcomeText = findViewById(R.id.welcomeText);
        ImageView ivProfileImage = findViewById(R.id.ivProfileImage);
        String urlPath = user.getProfile_img();

        String replaceUrl = urlPath.replace("\\", "");

        String fullUrl = BaseUrl.baseUrl + "upload_image/" + replaceUrl;
        Log.d(DashboardActivity.class.getSimpleName(), "fullUrl : " + fullUrl);


        Picasso.get()
                .load(BaseUrl.baseUrl + "upload_image/" + replaceUrl)
                // .memoryPolicy(MemoryPolicy.NO_CACHE)
                // .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .fit()
                .noFade()
                .into(ivProfileImage);

        welcomeText.setText("Welcome To The 96th Jalsa Salana Bangladesh\n\n\n"
                + "\n\nName :  " + user.getName()
                + "\n\nFather Name :  " + user.getFather_name()
                + "\n\nMother Name :  " + user.getMother_name()
                + "\n\nMobile No :  " + user.getMob()
                + "\n\nJamat/Majlish :  " + user.getJamat()
                + "\n\nGroup :  " + user.getGender()
                + "\n\nBlood Group :  " + user.getBlood_group()
                + "\n\n\n" + "Your session will expire on :" + user.getSessionExpiryDate());

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
