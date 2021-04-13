package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.profile;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.ReportModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Objects;

public class ReportBlockActivity extends AppCompatActivity {

    public static String USER_OBJECT = "userObject";

    Toolbar toolbar;

    ImageView mDoneBtn;

    CheckBox mBlockCheck;

    User mUser;

    User mCurrentUser;

    CheckedTextView dontReport, reportFake, reportInappropriate, reportRude, reportSpam, reportScam;

    String mTypeSelected = ReportModel.NO_REPORT;
    boolean mBlockChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_user);

        mUser = getIntent().getParcelableExtra(USER_OBJECT);

        toolbar = findViewById(R.id.toolbar);

        mDoneBtn = findViewById(R.id.done_btn);
        mBlockCheck = findViewById(R.id.blockUser_blockCheck);

        dontReport = findViewById(R.id.blockUser_dontReport);
        reportFake = findViewById(R.id.blockUser_reportFake);
        reportInappropriate = findViewById(R.id.blockUser_reportInappropriate);
        reportRude = findViewById(R.id.blockUser_reportRude);
        reportSpam = findViewById(R.id.blockUser_reportSpam);
        reportScam = findViewById(R.id.blockUser_reportScam);

        mCurrentUser = (User) ParseUser.getCurrentUser();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.blocked_report));
        getSupportActionBar().setElevation(4);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        selectReportType(ReportModel.NO_REPORT);

        mBlockCheck.setOnClickListener(v -> {

            if (mBlockChecked){

                mBlockCheck.setChecked(false);
                mBlockChecked = false;

            } else {

                mBlockCheck.setChecked(true);
                mBlockChecked = true;
            }
        });

        dontReport.setOnClickListener(v -> selectReportType(ReportModel.NO_REPORT));
        reportFake.setOnClickListener(v -> selectReportType(ReportModel.REPORT_STOLEN_PHOTOS));
        reportInappropriate.setOnClickListener(v -> selectReportType(ReportModel.REPORT_INAPPROPRIATE_CONTENT));
        reportRude.setOnClickListener(v -> selectReportType(ReportModel.REPORT_RUSE_OR_ABUSIVE));
        reportSpam.setOnClickListener(v -> selectReportType(ReportModel.REPORT_SPAM));
        reportScam.setOnClickListener(v -> selectReportType(ReportModel.REPORT_SCAM));

        mDoneBtn.setOnClickListener(v -> {

            if (!mBlockCheck.isChecked() && mTypeSelected.equals(ReportModel.NO_REPORT)){

                QuickHelp.showToast(this, getString(R.string.select_report_or_block), true);

            } else if (mBlockCheck.isChecked() && mTypeSelected.equals(ReportModel.NO_REPORT)) {

                blockUser();

            } else {
                checkIfAbuseExist(mTypeSelected, mBlockCheck.isChecked());
            }
        });

    }

    public void checkIfAbuseExist(String reportType, boolean isBlockChecked){

        QuickHelp.showLoading(this, false);

        ParseQuery<ReportModel> reportModelParseQuery = ReportModel.getQuery();
        reportModelParseQuery.whereEqualTo(ReportModel.FROM_AUTHOR, mCurrentUser);
        reportModelParseQuery.whereEqualTo(ReportModel.TO_AUTHOR, mUser);
        reportModelParseQuery.getFirstInBackground((reportedUser, e) -> {
            if (e == null){

                if (isBlockChecked){

                    blockUser();

                } else {

                    QuickHelp.hideLoading(this);
                    QuickHelp.showToast(ReportBlockActivity.this, getString(R.string.already_reported), true);
                }

            } else if (e.getCode() == ParseException.OBJECT_NOT_FOUND){

                reportUser(reportType, isBlockChecked);
            }
        });
    }

    public void reportUser(String reportType, boolean isBlockChecked){

        ReportModel reportUser = new ReportModel();
        reportUser.setFromAuthor(mCurrentUser);
        reportUser.setToAuthor(mUser);
        reportUser.setReportType(reportType);
        reportUser.saveInBackground(e -> {

            if (e == null){

                if (isBlockChecked){

                    blockUser();

                } else {

                    QuickHelp.hideLoading(this);
                    QuickHelp.showToast(ReportBlockActivity.this, getString(R.string.user_reported), false);
                    finish();
                }

            } else {

                QuickHelp.hideLoading(this);
                QuickHelp.showToast(ReportBlockActivity.this, getString(R.string.error_ocurred), true);
            }
        });

    }

    public void blockUser(){

        mCurrentUser.setBlockedUser(mUser);
        mCurrentUser.saveEventually();

        QuickHelp.hideLoading(this);

        onBackPressed();
    }

    public void selectReportType(String type){

        switch (type) {
            case ReportModel.NO_REPORT:

                mTypeSelected = ReportModel.NO_REPORT;

                dontReport.setChecked(true);
                dontReport.setCheckMarkDrawable(R.drawable.ic_radio_button_checked);

                reportFake.setChecked(false);
                reportFake.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportInappropriate.setChecked(false);
                reportInappropriate.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportRude.setChecked(false);
                reportRude.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportSpam.setChecked(false);
                reportSpam.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportScam.setChecked(false);
                reportScam.setCheckMarkDrawable(R.drawable.ic_unchecked);

                break;
            case ReportModel.REPORT_STOLEN_PHOTOS:

                mTypeSelected = ReportModel.REPORT_STOLEN_PHOTOS;

                dontReport.setChecked(false);
                dontReport.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportFake.setChecked(true);
                reportFake.setCheckMarkDrawable(R.drawable.ic_radio_button_checked);

                reportInappropriate.setChecked(false);
                reportInappropriate.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportRude.setChecked(false);
                reportRude.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportSpam.setChecked(false);
                reportSpam.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportScam.setChecked(false);
                reportScam.setCheckMarkDrawable(R.drawable.ic_unchecked);

                break;
            case ReportModel.REPORT_INAPPROPRIATE_CONTENT:

                mTypeSelected = ReportModel.REPORT_INAPPROPRIATE_CONTENT;

                dontReport.setChecked(false);
                dontReport.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportFake.setChecked(false);
                reportFake.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportInappropriate.setChecked(true);
                reportInappropriate.setCheckMarkDrawable(R.drawable.ic_radio_button_checked);

                reportRude.setChecked(false);
                reportRude.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportSpam.setChecked(false);
                reportSpam.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportScam.setChecked(false);
                reportScam.setCheckMarkDrawable(R.drawable.ic_unchecked);

                break;
            case ReportModel.REPORT_RUSE_OR_ABUSIVE:

                mTypeSelected = ReportModel.REPORT_RUSE_OR_ABUSIVE;

                dontReport.setChecked(false);
                dontReport.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportFake.setChecked(false);
                reportFake.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportInappropriate.setChecked(false);
                reportInappropriate.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportRude.setChecked(true);
                reportRude.setCheckMarkDrawable(R.drawable.ic_radio_button_checked);

                reportSpam.setChecked(false);
                reportSpam.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportScam.setChecked(false);
                reportScam.setCheckMarkDrawable(R.drawable.ic_unchecked);

                break;
            case ReportModel.REPORT_SPAM:

                mTypeSelected = ReportModel.REPORT_SPAM;

                dontReport.setChecked(false);
                dontReport.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportFake.setChecked(false);
                reportFake.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportInappropriate.setChecked(false);
                reportInappropriate.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportRude.setChecked(false);
                reportRude.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportSpam.setChecked(true);
                reportSpam.setCheckMarkDrawable(R.drawable.ic_radio_button_checked);

                reportScam.setChecked(false);
                reportScam.setCheckMarkDrawable(R.drawable.ic_unchecked);

                break;
            case ReportModel.REPORT_SCAM:

                mTypeSelected = ReportModel.REPORT_SCAM;

                dontReport.setChecked(false);
                dontReport.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportFake.setChecked(false);
                reportFake.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportInappropriate.setChecked(false);
                reportInappropriate.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportRude.setChecked(false);
                reportRude.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportSpam.setChecked(false);
                reportSpam.setCheckMarkDrawable(R.drawable.ic_unchecked);

                reportScam.setChecked(true);
                reportScam.setCheckMarkDrawable(R.drawable.ic_radio_button_checked);

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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