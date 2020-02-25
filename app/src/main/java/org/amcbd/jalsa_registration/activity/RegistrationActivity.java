package org.amcbd.jalsa_registration.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.amcbd.jalsa_registration.R;
import org.amcbd.jalsa_registration.app.AppController;
import org.amcbd.jalsa_registration.base_url.BaseUrl;
import org.amcbd.jalsa_registration.compressor.Compressor;
import org.amcbd.jalsa_registration.crop.ImagePickerActivity;
import org.amcbd.jalsa_registration.helper.Helper;
import org.amcbd.jalsa_registration.helper.SpinnerDialog;
import org.amcbd.jalsa_registration.interfaces.DialogClickListener;
import org.amcbd.jalsa_registration.interfaces.OnSpinerItemClick;
import org.amcbd.jalsa_registration.model.SessionHandler;
import org.amcbd.jalsa_registration.utils.DialogChooser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RegistrationActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = RegistrationActivity.class.getSimpleName();

    private TextView tvToolbarTitle;
    private ImageView ivBack;

    public static final int REQUEST_IMAGE = 100;
    private String userChoosenTask;

    private EditText etFullName;
    private EditText etFatherName;
    private EditText etMotherName;
    private EditText etAge;
    private EditText etMajlish;
    private EditText etGroup;
    private EditText etMobileNo;

    private EditText etEmail;
    private EditText etBudget;
    private CheckBox cvMaritalStatus;
    private CheckBox cvAhmadiByBirth;
    private EditText etDateOfBayat;
    private EditText etBloodGroup;
    private EditText etOccupation;

    private ImageView userIcon;
    private Button btnRegister;

    private String fullName;
    private String detailsString = "";
    private String fatherName;
    private String motherName;
    private String age;
    private String majlish;//Jamat
    private String group;
    private String mobileNo;
    private String imageLink = "";

    private String email = "";
    private String budget = "";
    private int maritalStatus = 0;
    private int ahmadiByBirthStatus = 0;
    private String dateOfBayat = "";
    private String bloodGroup;
    private String occupation;
    private String jobTitle;

    private static final int RC_STORAGE_PERMS1 = 101;
    private static final int RC_STORAGE_PERMS2 = 102;
    private static final int RC_GALLERY = 103;
    private static final int RC_CAMERA = 104;
    private static final int REQUEST_CROP = 105;

    private int hasWriteExtStorePMS;
    private TextView mTextView;
    private String[] majlishList;
    private String[] groupList;
    private String[] bloodGroupList;
    private String[] monthList;
    SpinnerDialog spMajlishDialog;
    SpinnerDialog spGroupDialog;
    SpinnerDialog spBloodGroupDialog;

    DialogChooser customDialog;
    int bitmap_size = 80; // range 1 - 100

    private static final String KEY_STATUS = "status";
    private static final String KEY_IMAGE_PATH = "imagePath";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    private static final String KEY_NAME = "name";
    private static final String KEY_FATHER_NAME = "father_name";
    private static final String KEY_MOTHER_NAME = "mother_name";
    private static final String KEY_MOB = "mob";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DOB = "DOB";
    private static final String KEY_JAMAT = "jamat";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_MAJLIS = "majlis";
    private static final String KEY_BY_BIRTHAHMADI = "by_birth_ahmadi";
    private static final String KEY_BUDGET = "budget";
    private static final String KEY_BLOOD_GROUP = "blood_group";
    private static final String KEY_MARITAL_STATUS = "maritial_status";
    private static final String KEY_OCCUPATION = "occupation";
    private static final String KEY_PROFILE_IMG = "profile_img";

    private String KEY_IMAGE = "image";

    private static final String KEY_EMPTY = "";

    private String username;

    private ProgressDialog pDialog;
    //private String register_url = "http://192.168.36.151/member/register.php";
    private String register_url = BaseUrl.baseUrl + "register.php";
    private String UPLOAD_URL = BaseUrl.baseUrl + "upload_image/upload.php";
    private SessionHandler session;

    Bitmap bitmap, decoded;
    int success;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "register_upload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        session = new SessionHandler(getApplicationContext());

        initializeToolbar("", true);

        tvToolbarTitle = findViewById(R.id.header_toolbar_title);
        tvToolbarTitle.setText("Registration");
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //For Signup
        etFullName = findViewById(R.id.etFullName);
        etFatherName = findViewById(R.id.etFatherName);
        etMotherName = findViewById(R.id.etMotherName);
        etAge = findViewById(R.id.etAge);
        etMajlish = findViewById(R.id.etMajlish);
        etGroup = findViewById(R.id.etGroup);
        etMobileNo = findViewById(R.id.etMobileNo);
        btnRegister = findViewById(R.id.btnRegister);
        userIcon = findViewById(R.id.userIcon);
        mTextView = findViewById(R.id.textViewFail);

        etEmail = findViewById(R.id.etEmail);
        etBudget = findViewById(R.id.etBudget);
        cvMaritalStatus = findViewById(R.id.cvMaritalStatus);
        cvAhmadiByBirth = findViewById(R.id.cvAhmadiByBirth);
        etDateOfBayat = findViewById(R.id.etDateOfBayat);
        etBloodGroup = findViewById(R.id.etBloodGroup);
        etOccupation = findViewById(R.id.etOccupation);


        userIcon.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        etMajlish.setOnClickListener(this);
        etGroup.setOnClickListener(this);
        etDateOfBayat.setOnClickListener(this);
        etBloodGroup.setOnClickListener(this);
        cvMaritalStatus.setOnCheckedChangeListener(this);
        cvAhmadiByBirth.setOnCheckedChangeListener(this);

        majlishList = getResources().getStringArray(R.array.majlish_list);
        groupList = getResources().getStringArray(R.array.group_list);
        bloodGroupList = getResources().getStringArray(R.array.blood_group_list);

        spMajlishDialog = new SpinnerDialog(RegistrationActivity.this, majlishList, "Select or Search Jamat");
        spGroupDialog = new SpinnerDialog(RegistrationActivity.this, groupList, "Select your Group");
        spBloodGroupDialog = new SpinnerDialog(RegistrationActivity.this, bloodGroupList, "Select your Blood Group");

        spMajlishDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                etMajlish.setText(item);
            }
        });

        spGroupDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                etGroup.setText(item);
            }
        });

        spBloodGroupDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                etBloodGroup.setText(item);
            }
        });
        ImagePickerActivity.clearCache(this);
    }

    private void signUp() {
        registerNewMember();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Helper.dismissProgressDialog();
        Helper.dismissDialog();
    }

    @SuppressLint("ResourceAsColor")
    private boolean isValidateSignUp() {

        boolean isValidate = true;

        fullName = etFullName.getText().toString().trim();
        fatherName = etFatherName.getText().toString().trim();
        motherName = etMotherName.getText().toString().trim();
        age = etAge.getText().toString().trim();
        majlish = etMajlish.getText().toString().trim();
        group = etGroup.getText().toString().trim();
        mobileNo = etMobileNo.getText().toString().trim();

        etEmail = findViewById(R.id.etEmail);
        etBudget = findViewById(R.id.etBudget);
        cvMaritalStatus = findViewById(R.id.cvMaritalStatus);
        etDateOfBayat = findViewById(R.id.etDateOfBayat);
        etBloodGroup = findViewById(R.id.etBloodGroup);
        etOccupation = findViewById(R.id.etOccupation);

        email = etEmail.getText().toString().trim();
        budget = etBudget.getText().toString().trim();
        dateOfBayat = etDateOfBayat.getText().toString().trim();
        bloodGroup = etBloodGroup.getText().toString().trim();
        occupation = etOccupation.getText().toString().trim();


        if (fullName.length() == 0) {
            etFullName.setError("Required Field");
            isValidate = false;
        }
        if (fatherName.length() == 0) {
            etFatherName.setError("Required Field");
            isValidate = false;
        }
        if (motherName.length() == 0) {
            etMotherName.setError("Required Field");
            isValidate = false;
        }

        if (age.length() == 0) {
            etAge.setError("Required Field");
            isValidate = false;
        }

        if (majlish.length() == 0) {
            etMajlish.setError("Required Field");
            isValidate = false;
        }

        if (group.length() == 0) {
            etGroup.setError("Required Field");
            isValidate = false;
        }

        if (mobileNo.length() == 0) {
            etMobileNo.setError("Required Field");
            isValidate = false;
        } else if (mobileNo.length() < 11) {
            etMobileNo.setError("Please enter valid mobile number");
            isValidate = false;
        }

        if (bloodGroup.length() == 0) {
            etBloodGroup.setError("Required Field");
            isValidate = false;
        }

        if (occupation.length() == 0) {
            etOccupation.setError("Required Field");
            isValidate = false;
        }


       /* if (imageLink.equals("")) {
            mTextView.setText("Please upload image");
            mTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            Toast.makeText(this, "Please upload image", Toast.LENGTH_LONG).show();
            isValidate = false;
        }*/


        return isValidate;
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case RC_STORAGE_PERMS1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Camera"))
                        //cameraIntent();
                        launchCameraIntent();
                    else if (userChoosenTask.equals("Gallery"))
                        // galleryIntent();
                        launchGalleryIntent();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setMessage("You need to allow storage permission");
                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, requestCode);
                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                }
                break;

            case RC_STORAGE_PERMS2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (requestCode == RC_STORAGE_PERMS1) {
                        Toast.makeText(RegistrationActivity.this, "Permission Granted Please click again.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setMessage("You need to allow permission");
                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, requestCode);
                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                }
                break;
        }
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }

    }

    @SuppressLint("CheckResult")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_IMAGE:

                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getParcelableExtra("path");
                    try {
                        // You can update this bitmap to your server
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                        setToImageView(getResizedBitmap(bitmap, 512));

                        // loading profile image from local cache
                        loadProfile(uri.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;

            case RC_STORAGE_PERMS2:
                hasWriteExtStorePMS = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWriteExtStorePMS == PackageManager.PERMISSION_GRANTED) {
                    if (requestCode == RC_STORAGE_PERMS1) {
                        if (resultCode == RESULT_OK) {
                            String imagePathAfterPermission = Helper.getPath(this, Uri.parse(data.getData().toString()));
                            //uploadFromFile(imagePathAfterPermission);
                        }
                    }
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_STORAGE_PERMS2);
                }
                break;

            case REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        try {
                            Bundle bundle = data.getExtras();

                            Bitmap bitmap = bundle.getParcelable("data");

                            OutputStream fOut = null;
                            // Uri outputFileUri;
                            // File root = new File(Environment.getExternalStorageDirectory()+ File.separator + "crop" + File.separator);
                            File root = getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + "crop" + File.separator);
                            root.mkdirs();
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            String imageFileName = "JPEG_" + timeStamp + "_";
                            File sdImageMainDirectory = new File(root, imageFileName + ".jpg");
                            // outputFileUri = Uri.fromFile(sdImageMainDirectory);
                            fOut = new FileOutputStream(sdImageMainDirectory);

                            try {
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            } catch (Exception e) {
                            } finally {

                                // String sourcePath = getRealPathFromURIPath(data.getData(), this);
                                // File sourceFile = new File(sourcePath);
                                File sourceFile = new File(sdImageMainDirectory.getAbsolutePath());
                                Log.e("Compressor", "Source Length After crop: " + sourceFile.length());

                                new Compressor(this)
                                        .compressToFileAsFlowable(sourceFile)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<File>() {
                                            @Override
                                            public void accept(File file) {

                                                //uploadFromFile(file.getAbsolutePath());

                                                Log.e("Compressor", "Compress Length after crop : " + file.length());
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) {
                                                throwable.printStackTrace();

                                            }
                                        });
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            // String imagePath = Helper.getPath(this, Uri.parse(data.getData().toString()));
                            Toast.makeText(this, "Error occured. Please try again later.", Toast.LENGTH_SHORT).show();
                            // uploadFromFile(imagePath);

                        }
                    }
                }
                break;

        }
    }

    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);

        Glide.with(this).load(url).into(userIcon);
        userIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.cvMaritalStatus:

                if (isChecked) {
                    maritalStatus = 1;
                } else {
                    maritalStatus = 0;
                }
                break;

            case R.id.cvAhmadiByBirth:

                if (isChecked) {
                    ahmadiByBirthStatus = 1;
                } else {
                    ahmadiByBirthStatus = 0;
                }
                break;
        }

    }

    public void OnDatePickerClicked() {

        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub

                String englishDayYear = selectedday + ", " + selectedyear;
                monthList = RegistrationActivity.this.getResources().getStringArray(R.array.month_list);
                String banglaMonth = monthList[selectedmonth];
                etDateOfBayat.setText(banglaMonth + " " + englishDayYear);

            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select date");
        mDatePicker.show();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnRegister:
                if (isNetworkAvailable()) {
                    if (isValidateSignUp()) {
                        //presenter.checkDuplicateEntry();
                        //checkDuplicateEntry(group, majlish);
                        signUp();
                    }
                } else {
                    showNetworkError();
                }
                break;

            case R.id.userIcon:
                showChoosingDialog();
                break;

            case R.id.etMajlish:
                spMajlishDialog.showSpinerDialog();
                break;

            case R.id.etGroup:
                spGroupDialog.showSpinerDialog();
                break;

            case R.id.etDateOfBayat:
                OnDatePickerClicked();
                break;

            case R.id.etBloodGroup:
                spBloodGroupDialog.showSpinerDialog();
                break;

        }

    }

    public void showChoosingDialog() {

        String message = "Select photo using...";

        customDialog = new DialogChooser(this, getString(R.string.choose_image), getString(R.string.gallery), getString(R.string.camera), new DialogClickListener() {
            @Override
            public void onYesClick(View view) {

                userChoosenTask = "Gallery";
                hasWriteExtStorePMS = ActivityCompat.checkSelfPermission(RegistrationActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWriteExtStorePMS == PackageManager.PERMISSION_GRANTED) {
                    //galleryIntent();
                    launchGalleryIntent();
                } else {
                    ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_STORAGE_PERMS1);
                }

            }

            @Override
            public void onNoClick(View view) {
               /* boolean permissionResult = CommonUtils.checkPermission(view.getContext());
                userChoosenTask = "Camera";
                if (permissionResult)
                    cameraIntent();*/
                userChoosenTask = "Camera";
                hasWriteExtStorePMS = ActivityCompat.checkSelfPermission(RegistrationActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWriteExtStorePMS == PackageManager.PERMISSION_GRANTED) {
                    //cameraIntent();
                    launchCameraIntent();
                } else {
                    ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_STORAGE_PERMS1);
                }
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


    private void launchCameraIntent() {
        Intent intent = new Intent(RegistrationActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(RegistrationActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void registerNewMember() {
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.e("Response: JSONobject", jObj.toString());

                               // session.loginUser(username, detailsString, jObj.getString("imagePath"));

                                session.loginUser(username, fullName, fatherName, motherName, mobileNo, email, age, majlish, "male", majlish,
                                        ""+ahmadiByBirthStatus, budget, bloodGroup, ""+maritalStatus, occupation, jObj.getString("imagePath"));
                                loadDashboard();

                                Toast.makeText(RegistrationActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                            } else if (success == 2) {
                                //Display error message if username is already existsing
                                etFullName.setError("This name already taken!");
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
                        Toast.makeText(RegistrationActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                username = fullName;


               /*String mullString = "Name : " +fullName + "\n\nFather's Name : "+ fatherName+ "\n\nMother's Name : "+ motherName
                        + "\n\nAge : "+ age+ "\n\nMajlish : "+ majlish+ "\n\nGroup : "+ group + "\n\nMobile Number : "+mobileNo+ "\n\nBlood Group : "+bloodGroup;*/

              // detailsString = mullString;


                params.put(KEY_USERNAME, username);
                params.put(KEY_NAME, fullName);
                params.put(KEY_FATHER_NAME, fatherName);
                params.put(KEY_MOTHER_NAME, motherName);
                params.put(KEY_MOB, mobileNo);
                params.put(KEY_EMAIL, email);
                params.put(KEY_DOB, dateOfBayat);
                params.put(KEY_JAMAT, majlish);
                params.put(KEY_GENDER, group);
                params.put(KEY_MAJLIS, majlish);
                params.put(KEY_BY_BIRTHAHMADI, "ahmadi : "+ahmadiByBirthStatus);
                params.put(KEY_BUDGET, budget);
                params.put(KEY_BLOOD_GROUP, bloodGroup);
                params.put(KEY_MARITAL_STATUS, "Married : "+maritalStatus);
                params.put(KEY_OCCUPATION, occupation);
                params.put(KEY_PROFILE_IMG, getStringImage(decoded));

                params.put(KEY_PASSWORD, "admin");

                //kembali ke parameters
                Log.e(TAG, "Parameters : " + params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    /**
     * Launch Dashboard Activity on Successful Sign Up
     */
    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(i);

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
       // imageView.setImageBitmap(decoded);
    }

}
