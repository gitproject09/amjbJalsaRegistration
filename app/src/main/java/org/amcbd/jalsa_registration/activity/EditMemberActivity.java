package org.amcbd.jalsa_registration.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.amcbd.jalsa_registration.R;
import org.amcbd.jalsa_registration.adapter.MemberListAdapter;
import org.amcbd.jalsa_registration.app.AppController;
import org.amcbd.jalsa_registration.base_url.BaseUrl;
import org.amcbd.jalsa_registration.crop.ImagePickerActivity;
import org.amcbd.jalsa_registration.helper.Helper;
import org.amcbd.jalsa_registration.interfaces.DialogClickListener;
import org.amcbd.jalsa_registration.interfaces.MemberClickListener;
import org.amcbd.jalsa_registration.model.SessionHandler;
import org.amcbd.jalsa_registration.pojo.MemberListData;
import org.amcbd.jalsa_registration.utils.DialogChooser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditMemberActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = EditMemberActivity.class.getSimpleName();

    private TextView tvToolbarTitle;
    private ImageView ivBack;

    private String userChoosenTask;

    private EditText etFullName;
    private EditText etUserId;
    private EditText etFatherName;
    private EditText etMotherName;
    private EditText etMobileNo;
    private EditText etEmail;
    private Button btnSearhMember;

    private SessionHandler session;

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

    private Context mContext;
    private List<MemberListData> memberList;
    private MemberListAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_member);

        initializeToolbar("", true);

        tvToolbarTitle = findViewById(R.id.header_toolbar_title);
        tvToolbarTitle.setText("Search Member");
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //For Signup
        etUserId = findViewById(R.id.etId);
        etFullName = findViewById(R.id.etFullName);
        etFatherName = findViewById(R.id.etFatherName);
        etMotherName = findViewById(R.id.etMotherName);
        etMobileNo = findViewById(R.id.etMobileNo);
        etEmail = findViewById(R.id.etEmail);
        btnSearhMember = findViewById(R.id.btnSearhMember);
        recyclerView = findViewById(R.id.recyclerView);
        btnSearhMember.setOnClickListener(this::onClick);

        session = new SessionHandler(getApplicationContext());

        mContext = this;

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        memberList = new ArrayList<>();

        adapter = new MemberListAdapter(this, memberList, new MemberClickListener() {

            @Override
            public void onYesClick(View view, int position) {

                session.loginUser(memberList.get(position).getUser_id(), memberList.get(position).getName(), memberList.get(position).getFather_name(),
                        memberList.get(position).getMother_name(), memberList.get(position).getMob(),
                        "", "", memberList.get(position).getJamat(), "", "",
                        "", "", memberList.get(position).getBlood_group(), "", "", memberList.get(position).getProfile_img());

                loadDashboard();

            }

            @Override
            public void onNoClick(View view, int position) {

                showAlertDialog("Print Option Coming soon!!!");

            }

            @Override
            public void onCrossClick(View view, int position) {

            }
        });

        recyclerView.setAdapter(adapter);
        /*if (isNetworkAvailable()) {
            presenter.doFriendListRequest();
        } else {
            showSnakeBarMessage(getResources().getString(R.string.message_online));
        }*/

        ImagePickerActivity.clearCache(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Helper.dismissProgressDialog();
        Helper.dismissDialog();
    }

    /**
     * Launch Dashboard Activity on Successful Login
     */
    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(i);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSearhMember:
                if (isNetworkAvailable()) {

                    JSONObject request = new JSONObject();
                    boolean isSetAnyValue = false;

                    userId = etUserId.getText().toString().trim();
                    fullName = etFullName.getText().toString().trim();
                    fatherName = etFatherName.getText().toString().trim();
                    motherName = etMotherName.getText().toString().trim();
                    mobileNo = etMobileNo.getText().toString().trim();
                    email = etEmail.getText().toString().trim();


                    try {
                        //Populate the request parameters
                        if (!TextUtils.isEmpty(userId)) {
                            isSetAnyValue = true;
                            request.put(KEY_ID, userId);
                        }
                        if (!TextUtils.isEmpty(fullName)) {
                            isSetAnyValue = true;
                            request.put(KEY_NAME, fullName);
                        }
                        if (!TextUtils.isEmpty(fatherName)) {
                            isSetAnyValue = true;
                            request.put(KEY_FATHER_NAME, fatherName);
                        }
                        if (!TextUtils.isEmpty(motherName)) {
                            isSetAnyValue = true;
                            request.put(KEY_MOTHER_NAME, motherName);
                        }
                        if (!TextUtils.isEmpty(mobileNo)) {
                            isSetAnyValue = true;
                            request.put(KEY_MOB, mobileNo);
                        }
                        if (!TextUtils.isEmpty(email)) {
                            isSetAnyValue = true;
                            request.put(KEY_EMAIL, email);
                        }


                        Log.d(TAG, "Request parameters : " + request.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (isSetAnyValue){
                        searchMemberInThisWay(request);
                    } else {
                        Toast.makeText(getApplicationContext(), "Input should not Empty", Toast.LENGTH_SHORT).show();
                    }


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


    public void showAlertDialog(String message) {

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    private void searchMemberInThisWay(JSONObject request) {
        final ProgressDialog searching = ProgressDialog.show(this, "Searching...", "Please wait...", false, false);
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, searchUrl, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Search Response : ", response.toString());
                        searching.dismiss();

                        try {
                            success = response.getInt(TAG_SUCCESS);

                            if (success == 1) {

                                JSONArray jsonarray = response.getJSONArray("members");

                                if (jsonarray.length() > 0) {
                                    Toast.makeText(EditMemberActivity.this, response.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                    memberList.clear();
                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        MemberListData memberListData = new MemberListData();
                                        // String name = jsonobject.getString("name");
                                        memberListData.setUser_id(jsonobject.getString("user_id"));
                                        memberListData.setName(jsonobject.getString("name"));
                                        memberListData.setFather_name(jsonobject.getString("father_name"));
                                        memberListData.setMother_name(jsonobject.getString("mother_name"));
                                        memberListData.setMob(jsonobject.getString("mob"));
                                        memberListData.setJamat(jsonobject.getString("jamat"));
                                        memberListData.setBlood_group(jsonobject.getString("blood_group"));
                                        memberListData.setProfile_img(jsonobject.getString("profile_img"));
                                        memberList.add(memberListData);
                                    }

                                    // memberList.addAll(itemList);
                                    adapter.setFriendList(memberList);

                                } else {
                                    Toast.makeText(EditMemberActivity.this, "No Member Found", Toast.LENGTH_LONG).show();
                                }

                            } else if (success == 3) {
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

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        searching.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        // MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
        AppController.getInstance().addToRequestQueue(jsArrayRequest, "search_req");
    }

    /**
     * Launch Dashboard Activity on Successful Sign Up
     */
    private void goToNextActivity() {
        Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(i);

    }

}
