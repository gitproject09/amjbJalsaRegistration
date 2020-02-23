package org.amcbd.jalsa_registration.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.amcbd.jalsa_registration.R;
import org.amcbd.jalsa_registration.app.AppController;
import org.amcbd.jalsa_registration.base_url.BaseUrl;
import org.amcbd.jalsa_registration.crop.ImagePickerActivity;
import org.amcbd.jalsa_registration.helper.Helper;
import org.amcbd.jalsa_registration.interfaces.DialogClickListener;
import org.amcbd.jalsa_registration.utils.DialogChooser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditMemberActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = EditMemberActivity.class.getSimpleName();

    private String userChoosenTask;

    private EditText etFullName;
    private EditText etUserId;
    private EditText etFatherName;
    private EditText etMotherName;
    private EditText etMobileNo;
    private EditText etEmail;
    private Button btnSearhMember;


    private String fullName = "";
    private String userId = "";
    private String fatherName = "";
    private String motherName = "";
    private String mobileNo = "";
    private String email = "";

    DialogChooser customDialog;

    private static final String KEY_ID = "user_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_FATHER_NAME = "father_name";
    private static final String KEY_MOTHER_NAME = "mother_name";
    private static final String KEY_MOB = "mob";
    private static final String KEY_EMAIL = "email";

    private String searchUrl = BaseUrl.baseUrl + "search_member.php";

    int success;

    private static final String TAG_SUCCESS = "status";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "search_member";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_member);
        //For Signup
        etUserId = findViewById(R.id.etId);
        etFullName = findViewById(R.id.etFullName);
        etFatherName = findViewById(R.id.etFatherName);
        etMotherName = findViewById(R.id.etMotherName);
        etMobileNo = findViewById(R.id.etMobileNo);
        etEmail = findViewById(R.id.etEmail);
        btnSearhMember = findViewById(R.id.btnSearhMember);
        btnSearhMember.setOnClickListener(this::onClick);

        ImagePickerActivity.clearCache(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Helper.dismissProgressDialog();
        Helper.dismissDialog();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSearhMember:
                if (isNetworkAvailable()) {

                    userId = etUserId.getText().toString().trim();
                    fullName = etFullName.getText().toString().trim();
                    fatherName = etFatherName.getText().toString().trim();
                    motherName = etMotherName.getText().toString().trim();
                    mobileNo = etMobileNo.getText().toString().trim();
                    email = etEmail.getText().toString().trim();

                    searchMember();

                } else {
                    showNetworkError();
                }
                break;

            case R.id.userIcon:
                showChoosingDialog();
                break;

        }

    }

    public void showChoosingDialog() {

        String message = "Select photo using...";

        customDialog = new DialogChooser(this, getString(R.string.choose_image), getString(R.string.gallery), getString(R.string.camera), new DialogClickListener() {
            @Override
            public void onYesClick(View view) {

                userChoosenTask = "Gallery";

            }

            @Override
            public void onNoClick(View view) {
               /* boolean permissionResult = CommonUtils.checkPermission(view.getContext());
                userChoosenTask = "Camera";
                if (permissionResult)
                    cameraIntent();*/
                userChoosenTask = "Camera";
            }

            @Override
            public void onCrossClick(View view) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }


    public void showDuplicateAlertDialog(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setMessage(message);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //RegistrationActivity.this.recreate();
                    }
                });

        alertDialog.show();

    }


    private void searchMember() {
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Searching...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, searchUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response);

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.e("Response: JSONobject", jObj.toString());

                                Toast.makeText(EditMemberActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                            } else if (success == 2) {
                                //Display error message if username is already existsing
                                etFullName.setError("Missing Mandatory Parameters!!!");
                                etFullName.requestFocus();

                            } else if (success == 0) {
                                //Display error message if username is already existsing
                                etFullName.setError("No Member Found");
                                etFullName.requestFocus();

                            } else {
                                Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();

                        //menampilkan toast
                        Toast.makeText(EditMemberActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                params.put(KEY_ID, userId);
                params.put(KEY_NAME, fullName);
                params.put(KEY_FATHER_NAME, fatherName);
                params.put(KEY_MOTHER_NAME, motherName);
                params.put(KEY_MOB, mobileNo);
                params.put(KEY_EMAIL, email);

                Log.e(TAG, "Parameters : " + params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    /**
     * Launch Dashboard Activity on Successful Sign Up
     */
    private void goToNextActivity() {
        Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(i);

    }

}
