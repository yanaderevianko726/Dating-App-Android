package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.settings.basicInfo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class BasicInfoActivity extends AppCompatActivity {

    Toolbar toolbar;

    User mCurrentUser;

    TextView mBirthday, mName;

    RadioGroup mGenderGroup;
    AppCompatRadioButton mGenderMale, mGenderFemale;

    LinearLayout mBirthdayLayout, mNameLayout;

    Date date;
    Date birthday;
    long Birthdate;
    int AgeString;
    private int year;
    private int month;
    private int day;
    String dateCheck = null;
    Integer ageInt;
    protected String genderSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);

        toolbar = findViewById(R.id.toolbar);

        mNameLayout = findViewById(R.id.basic_info);
        mBirthdayLayout = findViewById(R.id.birthday_layout);

        mName = findViewById(R.id.textName);
        mBirthday = findViewById(R.id.birthday);

        mGenderGroup = findViewById(R.id.gender_group);
        mGenderMale = findViewById(R.id.gender_male);
        mGenderFemale = findViewById(R.id.gender_female);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.basic_info));
        getSupportActionBar().setElevation(4);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        mCurrentUser = (User) User.getCurrentUser();

        if (mCurrentUser != null){

            if (mCurrentUser.getUid() == 0){
                mCurrentUser.setUid(QuickHelp.generateUId());
            }

            if (!mCurrentUser.getColFullName().isEmpty()){

                mName.setText(mCurrentUser.getColFullName());
            } else mName.setVisibility(View.GONE);

            if (mCurrentUser.getBirthDate() != null){

                mBirthday.setText(QuickHelp.getBirthdayFromDate(mCurrentUser.getBirthDate()));

                dateCheck = "true";
            } else {
                dateCheck = null;
                mBirthday.setVisibility(View.GONE);
            }

            if (!mCurrentUser.getColGender().isEmpty()){

                if (mCurrentUser.getColGender().equals(User.GENDER_MALE)){

                    mGenderMale.setChecked(true);
                    genderSelected = User.GENDER_MALE;

                } else if (mCurrentUser.getColGender().equals(User.GENDER_FEMALE)){

                    mGenderFemale.setChecked(true);
                    genderSelected = User.GENDER_FEMALE;

                } else genderSelected = null;
            }
        }

        mBirthdayLayout.setOnClickListener(v -> DateSelect());

        mBirthdayLayout.setOnFocusChangeListener((v13, hasFocus) -> {

            if (hasFocus){
                hideKeyboard(v13);
                DateSelect();
            }
        });

        mGenderGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (group.getCheckedRadioButtonId() == mGenderMale.getId()){

                genderSelected = User.GENDER_MALE;

                mCurrentUser.setColGender(genderSelected);
                mCurrentUser.saveEventually();

            } else if (group.getCheckedRadioButtonId() == mGenderFemale.getId()){

                genderSelected = User.GENDER_FEMALE;

                mCurrentUser.setColGender(genderSelected);
                mCurrentUser.saveEventually();

            }
        });

        mNameLayout.setOnClickListener(v -> changeName());

    }

    public void changeName(){

        EditText editName = new EditText(this);
        editName.setImeOptions(EditorInfo.IME_ACTION_DONE);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.name));
        alertDialog.setCancelable(false);

        alertDialog.setView(editName);

        editName.setText(mCurrentUser.getColFullName());

        alertDialog.setPositiveButton(getString(R.string.ok), (dialog, which) -> {

            String name = editName.getText().toString().trim();

            if (!name.isEmpty() && name.length() > 2 && !name.equals(mCurrentUser.getColFullName())){

                String[] parts = name.split(" ");

                String firstName = parts[0];

                String username = (name+firstName).toLowerCase().trim().replaceAll(" ", "");

                mCurrentUser.setColFirstName(firstName);
                mCurrentUser.setColFullName(name);
                mCurrentUser.setUsername(username);
                mCurrentUser.saveEventually();

                runOnUiThread(() -> {
                    mName.setVisibility(View.VISIBLE);
                    mName.setText(name);
                });
            }

        }).setNegativeButton(getString(R.string.cancel), null).create();
        alertDialog.show();


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void DateSelect(){

        DatePickerDialog dpd = new DatePickerDialog(this, R.style.DatePickerTheme, mDateSetListener,year,month,day);

        final Calendar calendar = Calendar.getInstance();
        final Calendar calendar2 = Calendar.getInstance();

        calendar2.add(Calendar.YEAR,-18);

        dpd.getDatePicker().setMaxDate(calendar2.getTimeInMillis());

        calendar.add(Calendar.YEAR,-65);

        dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());

        dpd.show();
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int mYear, int monthOfYear, int dayOfMonth) {

            year = mYear;
            month = monthOfYear;
            day = dayOfMonth;

            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,day);

            Date BirDate = calendar.getTime();

            Calendar dob = Calendar.getInstance();
            Calendar today = Calendar.getInstance();


            dob.set(year,month,day);

            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            ageInt = age;

            date = BirDate;

            dateCheck = "true";


            Birthdate = BirDate.getTime();

            AgeString = ageInt;

            birthday = new Date(BirDate.getTime());

            mBirthday.setText(QuickHelp.getBirthdayFromDate(birthday));

            mBirthday.setVisibility(View.VISIBLE);

            mCurrentUser.setColBirthdate(date);
            mCurrentUser.saveEventually();
        }

    };

    @Override
    protected void onResume() {
        super.onResume();

        if (mCurrentUser != null){

            if (!mCurrentUser.getColFullName().isEmpty()){
                mName.setText(mCurrentUser.getColFullName());
                mName.setVisibility(View.VISIBLE);
            }

            if (mCurrentUser.getBirthDate() != null){
                mBirthday.setText(QuickHelp.getBirthdayFromDate(mCurrentUser.getBirthDate()));
                dateCheck = "true";
                mBirthday.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}