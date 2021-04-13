package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.profile;


import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo.OwnProfilePhotosAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.LanguageAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickActions;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.nearby.ManualLocationActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.settings.basicInfo.BasicInfoActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.uploads.UploadsActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.LanguageModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Objects;


public class EditProfileActivity extends AppCompatActivity {

    Toolbar toolbar;

    User mCurrentUser;

    Button mAddPhotosBtn;

    ImageView mPreview;

    ArrayList<ParseFile> parseFiles;
    OwnProfilePhotosAdapter ownProfilePhotosAdapter;

    RecyclerView mRecyclerView;

    LanguageAdapter languageAdapter;
    ArrayList<LanguageModel> languageModels;

    BottomSheetDialog sheetDialog;

    LinearLayout mBasicInfoLayout, mCurrentLocationLayout;
    TextView mBasicInfoText, mCurrentLocationText, mDescriptionText;

    LinearLayout mWhatIWantLyt, mRelationshipLyn, mSexualityLyt, mHeightLyt, mBodyTypeLyt, mLivingLyt, mKidsLyt, mSmokingLyt, mDrinkingLyt, mLanguageLyt;
    TextView mWhatIWant, mRelationship, mSexuality, mHeight, mBodyType, mLiving, mKids, mSmoking, mDrinking, mLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile_edit);
        toolbar = findViewById(R.id.toolbar);

        mCurrentUser = (User) ParseUser.getCurrentUser();

        mPreview = findViewById(R.id.editMyProfile_preview);

        mRecyclerView = findViewById(R.id.editMyProfile_photos);

        mAddPhotosBtn = findViewById(R.id.editMyProfile_addPhoto);

        mBasicInfoLayout = findViewById(R.id.editMyProfile_basicInfo);
        mBasicInfoText = findViewById(R.id.basic_info);

        mDescriptionText = findViewById(R.id.editMyProfile_aboutMe);

        mCurrentLocationLayout = findViewById(R.id.editMyProfile_location);
        mCurrentLocationText = findViewById(R.id.current_location);

        mWhatIWantLyt = findViewById(R.id.editMyProfile_work);

        mRelationshipLyn = findViewById(R.id.editMyProfile_relationship);
        mSexualityLyt = findViewById(R.id.editMyProfile_sexuality);
        mHeightLyt = findViewById(R.id.editMyProfile_height);
        mBodyTypeLyt = findViewById(R.id.editMyProfile_body);
        mLivingLyt = findViewById(R.id.editMyProfile_living);
        mKidsLyt = findViewById(R.id.editMyProfile_children);
        mSmokingLyt = findViewById(R.id.editMyProfile_smoking);
        mDrinkingLyt = findViewById(R.id.editMyProfile_drinking);
        mLanguageLyt = findViewById(R.id.editMyProfile_language);

        mWhatIWant = findViewById(R.id.what_i_want);

        mRelationship = findViewById(R.id.profile_relationship);
        mSexuality = findViewById(R.id.profile_sexuality);
        mHeight = findViewById(R.id.profile_height);
        mBodyType = findViewById(R.id.profile_body);
        mLiving = findViewById(R.id.profile_living);
        mKids = findViewById(R.id.profile_children);
        mSmoking = findViewById(R.id.profile_smoking);
        mDrinking = findViewById(R.id.profile_drinking);
        mLanguage = findViewById(R.id.profile_language);

        mLanguageLyt.setOnClickListener(view -> openLanguageChooser());

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        mBasicInfoLayout.setOnClickListener(v -> QuickHelp.goToActivityWithNoClean(this, BasicInfoActivity.class));
        mCurrentLocationLayout.setOnClickListener(v -> QuickHelp.goToActivityWithNoClean(this, ManualLocationActivity.class));
        mAddPhotosBtn.setOnClickListener(v -> QuickHelp.goToActivityWithNoClean(this, UploadsActivity.class));

        parseFiles = new ArrayList<>();
        ownProfilePhotosAdapter = new OwnProfilePhotosAdapter(this, parseFiles, mCurrentUser);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setReverseLayout(false);
        //layoutManager.setStackFromEnd(true);

        mRecyclerView.setAdapter(ownProfilePhotosAdapter);
        mRecyclerView.setItemViewCacheSize(12);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setBackgroundResource(R.color.white);
        mRecyclerView.setBackgroundColor(Color.WHITE);
        mRecyclerView.setLayoutManager(layoutManager);

        mWhatIWantLyt.setOnClickListener(v -> whatIWant());
        mDescriptionText.setOnClickListener(v -> editAbout());
        mRelationshipLyn.setOnClickListener(v -> relationship());
        mSexualityLyt.setOnClickListener(v -> sexuality());
        mHeightLyt.setOnClickListener(v -> editHeight());
        mBodyTypeLyt.setOnClickListener(v -> bodyType());
        mLivingLyt.setOnClickListener(v -> living());
        mKidsLyt.setOnClickListener(v -> kids());
        mDrinkingLyt.setOnClickListener(v -> drinking());
        mSmokingLyt.setOnClickListener(v -> smoking());

        mPreview.setOnClickListener(v -> QuickActions.showProfile(this, mCurrentUser, true));
    }

    public void loadProfile(){

        mWhatIWant.setText(QuickHelp.getWhatIWant(mCurrentUser));
        mDescriptionText.setText(QuickHelp.getAboutMe(mCurrentUser));
        mRelationship.setText(QuickHelp.getRelationShip(mCurrentUser));
        mSexuality.setText(QuickHelp.getSexuality(mCurrentUser));
        mHeight.setText(QuickHelp.getHeight(mCurrentUser));
        mBodyType.setText(QuickHelp.getBodyType(mCurrentUser));
        mLiving.setText(QuickHelp.getLiving(mCurrentUser));
        mKids.setText(QuickHelp.getKids(mCurrentUser));
        mSmoking.setText(QuickHelp.getSmoking(mCurrentUser));
        mDrinking.setText(QuickHelp.getDrinking(mCurrentUser));
        mLanguage.setText(QuickHelp.getLanguageName(mCurrentUser.getLanguage()));
    }

    public void editAbout(){

        EditText editName = new EditText(this);
        editName.setSingleLine(false);
        editName.setMinLines(3);
        editName.setImeOptions(EditorInfo.IME_ACTION_DONE);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.about_me));
        alertDialog.setCancelable(false);

        alertDialog.setView(editName);

        if (!mCurrentUser.getAboutMe().isEmpty()){
            editName.setText(mCurrentUser.getAboutMe());
        } else {
            editName.setHint(getString(R.string.tell_us_more));
        }

        alertDialog.setPositiveButton(getString(R.string.ok), (dialog, which) -> {

            String aboutMeText = editName.getText().toString().trim();

            if (!aboutMeText.isEmpty() && aboutMeText.length() > 2 ){

                mCurrentUser.setAboutMe(aboutMeText);
                mCurrentUser.saveEventually();

                loadProfile();
            }

        }).setNegativeButton(getString(R.string.cancel), null).create();
        alertDialog.show();
    }

    public void editHeight(){

        EditText editHeight = new EditText(this);
        editHeight.setSingleLine(true);
        editHeight.setMinLines(1);
        editHeight.setInputType(InputType.TYPE_CLASS_NUMBER);
        editHeight.setMaxEms(3);
        editHeight.setMinEms(2);
        editHeight.setImeOptions(EditorInfo.IME_ACTION_DONE);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.profile_height_title));
        alertDialog.setCancelable(false);

        alertDialog.setView(editHeight);

        if (mCurrentUser.getHeight() == 0){
            editHeight.setHint(R.string.cm_hint);
        } else {
            editHeight.setText(String.valueOf(mCurrentUser.getHeight()));
        }

        alertDialog.setPositiveButton(getString(R.string.ok), (dialog, which) -> {

            String myHeight = editHeight.getText().toString().trim();

            if (!myHeight.isEmpty() && myHeight.length() >= 2 ){

                mCurrentUser.setHeight(Integer.valueOf(myHeight));
                mCurrentUser.saveEventually();

                loadProfile();
            }

        }).setNegativeButton(getString(R.string.cancel), null).create();
        alertDialog.show();
    }

    public void relationship(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.profile_relationship));
        alertDialog.setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.include_profile_relationship, null);

        alertDialog.setView(dialogView);

        RadioGroup relationGroup = dialogView.findViewById(R.id.relation_group);

        RadioButton no = dialogView.findViewById(R.id.no_answer);
        RadioButton complicated = dialogView.findViewById(R.id.relation_complicated);
        RadioButton single = dialogView.findViewById(R.id.single);
        RadioButton taken = dialogView.findViewById(R.id.taken);

        switch (mCurrentUser.getRelationship()) {
            case User.RELATIONSHIP_COMPLICATED:

                complicated.setChecked(true);

                break;
            case User.RELATIONSHIP_SINGLE:
                single.setChecked(true);

                break;
            case User.RELATIONSHIP_TAKEN:

                taken.setChecked(true);
                break;
            default:

                no.setChecked(true);
                break;
        }

        relationGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.relation_complicated){
                mCurrentUser.setRelationship(User.RELATIONSHIP_COMPLICATED);
            } else if (checkedId == R.id.single){
                mCurrentUser.setRelationship(User.RELATIONSHIP_SINGLE);
            } else if (checkedId == R.id.taken){
                mCurrentUser.setRelationship(User.RELATIONSHIP_TAKEN);
            } else if (checkedId == R.id.no_answer){
                mCurrentUser.setRelationship("");
            }
        });

        alertDialog.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                    mCurrentUser.saveEventually();
                    loadProfile();
                })
                .create();
        alertDialog.show();
    }

    public void sexuality(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.profile_sexuality));
        alertDialog.setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.include_profile_sexuality, null);

        alertDialog.setView(dialogView);

        RadioGroup sexualityGroup = dialogView.findViewById(R.id.sexuality_group);
        RadioButton no = dialogView.findViewById(R.id.no_answer);

        RadioButton bisexual = dialogView.findViewById(R.id.bisexual);
        RadioButton lesbian = dialogView.findViewById(R.id.lesbian);
        RadioButton askMe = dialogView.findViewById(R.id.ask_me);
        RadioButton straight = dialogView.findViewById(R.id.straight);

        switch (mCurrentUser.getSexuality()) {
            case User.SEXUALITY_BISEXUAL:

                bisexual.setChecked(true);

                break;
            case User.SEXUALITY_LESBIAN:
                lesbian.setChecked(true);

                break;
            case User.SEXUALITY_ASK_ME:

                askMe.setChecked(true);
                break;

            case User.SEXUALITY_STRAIGHT:

                straight.setChecked(true);
                break;
            default:

                no.setChecked(true);
                break;
        }

        sexualityGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.bisexual){
                mCurrentUser.setSexuality(User.SEXUALITY_BISEXUAL);
            } else if (checkedId == R.id.lesbian){
                mCurrentUser.setSexuality(User.SEXUALITY_LESBIAN);
            } else if (checkedId == R.id.ask_me){
                mCurrentUser.setSexuality(User.SEXUALITY_ASK_ME);
            } else if (checkedId == R.id.straight){
                mCurrentUser.setSexuality(User.SEXUALITY_STRAIGHT);
            } else if (checkedId == R.id.no_answer){
                mCurrentUser.setSexuality("");
            }
        });

        alertDialog.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            mCurrentUser.saveEventually();
            loadProfile();
                })
                .create();
        alertDialog.show();
    }

    public void bodyType(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.profile_body));
        alertDialog.setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.include_profile_body_type, null);

        alertDialog.setView(dialogView);

        RadioGroup BodyTypeGroup = dialogView.findViewById(R.id.body_type_group);
        RadioButton no = dialogView.findViewById(R.id.no_answer);

        RadioButton body_athl = dialogView.findViewById(R.id.body_athl);
        RadioButton body_average = dialogView.findViewById(R.id.body_average);
        RadioButton body_big = dialogView.findViewById(R.id.body_big);
        RadioButton body_extra = dialogView.findViewById(R.id.body_extra);
        RadioButton body_musc = dialogView.findViewById(R.id.body_musc);
        RadioButton body_slim = dialogView.findViewById(R.id.body_slim);

        switch (mCurrentUser.getBodyType()) {
            case User.BODY_TYPE_ATHLETIC:

                body_athl.setChecked(true);

                break;
            case User.BODY_TYPE_AVERAGE:
                body_average.setChecked(true);

                break;
            case User.BODY_TYPE_BIG_AND_BEAUTIFUL:

                body_big.setChecked(true);
                break;

            case User.BODY_TYPE_FEW_EXTRA_POUNDS:

                body_extra.setChecked(true);
                break;
            case User.BODY_TYPE_MUSCULAR:

                body_musc.setChecked(true);
                break;
            case User.BODY_TYPE_SLIM:

                body_slim.setChecked(true);
                break;
            default:

                no.setChecked(true);
                break;
        }

        BodyTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.body_athl){
                mCurrentUser.setBodyType(User.BODY_TYPE_ATHLETIC);
            } else if (checkedId == R.id.body_average){
                mCurrentUser.setBodyType(User.BODY_TYPE_AVERAGE);
            } else if (checkedId == R.id.body_big){
                mCurrentUser.setBodyType(User.BODY_TYPE_BIG_AND_BEAUTIFUL);
            } else if (checkedId == R.id.body_extra){
                mCurrentUser.setBodyType(User.BODY_TYPE_FEW_EXTRA_POUNDS);
            } else if (checkedId == R.id.body_musc){
                mCurrentUser.setBodyType(User.BODY_TYPE_MUSCULAR);
            } else if (checkedId == R.id.body_slim){
                mCurrentUser.setBodyType(User.BODY_TYPE_SLIM);
            } else if (checkedId == R.id.no_answer){
                mCurrentUser.setBodyType("");
            }
        });

        alertDialog.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            mCurrentUser.saveEventually();
            loadProfile();
        })
                .create();
        alertDialog.show();
    }

    public void living(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.profile_living));
        alertDialog.setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.include_profile_living, null);

        alertDialog.setView(dialogView);

        RadioGroup livingGroup = dialogView.findViewById(R.id.living_group);
        RadioButton no = dialogView.findViewById(R.id.no_answer);

        RadioButton living_myself = dialogView.findViewById(R.id.living_myself);
        RadioButton living_student = dialogView.findViewById(R.id.living_student);
        RadioButton living_parents = dialogView.findViewById(R.id.living_parents);
        RadioButton living_partner = dialogView.findViewById(R.id.living_partner);
        RadioButton living_room = dialogView.findViewById(R.id.living_room);

        switch (mCurrentUser.getLiving()) {
            case User.LIVING_BY_MYSELF:

                living_myself.setChecked(true);

                break;
            case User.LIVING_STUDENT_DORMITORY:
                living_student.setChecked(true);

                break;
            case User.LIVING_WITH_PARENTS:

                living_parents.setChecked(true);
                break;

            case User.LIVING_WITH_PARTNER:

                living_partner.setChecked(true);
                break;

            case User.LIVING_WITH_ROOMMATES:

                living_room.setChecked(true);
                break;
            default:

                no.setChecked(true);
                break;
        }

        livingGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.living_myself){
                mCurrentUser.setLiving(User.LIVING_BY_MYSELF);
            } else if (checkedId == R.id.living_student){
                mCurrentUser.setLiving(User.LIVING_STUDENT_DORMITORY);
            } else if (checkedId == R.id.living_parents){
                mCurrentUser.setLiving(User.LIVING_WITH_PARENTS);
            } else if (checkedId == R.id.living_partner){
                mCurrentUser.setLiving(User.LIVING_WITH_PARTNER);
            } else if (checkedId == R.id.living_room){
                mCurrentUser.setLiving(User.LIVING_WITH_ROOMMATES);
            } else if (checkedId == R.id.no_answer){
                mCurrentUser.setLiving("");
            }
        });

        alertDialog.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            mCurrentUser.saveEventually();
            loadProfile();
        })
                .create();
        alertDialog.show();
    }

    public void kids(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.profile_children));
        alertDialog.setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.include_profile_kids, null);

        alertDialog.setView(dialogView);

        RadioGroup kidsGroup = dialogView.findViewById(R.id.kids_group);
        RadioButton no = dialogView.findViewById(R.id.no_answer);

        RadioButton kids_grown = dialogView.findViewById(R.id.kids_grown);
        RadioButton kids_already = dialogView.findViewById(R.id.kids_already);
        RadioButton kids_no_never = dialogView.findViewById(R.id.kids_no_never);
        RadioButton kids_someday = dialogView.findViewById(R.id.kids_someday);

        switch (mCurrentUser.getKids()) {
            case User.KIDS_GROWN_UP:

                kids_grown.setChecked(true);

                break;
            case User.KIDS_ALREADY_HAVE:
                kids_already.setChecked(true);

                break;
            case User.KIDS_NO_NOVER:

                kids_no_never.setChecked(true);
                break;

            case User.KIDS_SOMEDAY:

                kids_someday.setChecked(true);
                break;
            default:

                no.setChecked(true);
                break;
        }

        kidsGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.kids_grown){
                mCurrentUser.setKids(User.KIDS_GROWN_UP);
            } else if (checkedId == R.id.kids_already){
                mCurrentUser.setKids(User.KIDS_ALREADY_HAVE);
            } else if (checkedId == R.id.kids_no_never){
                mCurrentUser.setKids(User.KIDS_NO_NOVER);
            } else if (checkedId == R.id.kids_someday){
                mCurrentUser.setKids(User.KIDS_SOMEDAY);
            } else if (checkedId == R.id.no_answer){
                mCurrentUser.setKids("");
            }
        });

        alertDialog.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            mCurrentUser.saveEventually();
            loadProfile();
        })
                .create();
        alertDialog.show();
    }

    public void drinking(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.profile_drinking));
        alertDialog.setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.include_profile_drinking, null);

        alertDialog.setView(dialogView);

        RadioGroup drinkingGroup = dialogView.findViewById(R.id.drinking_group);
        RadioButton no = dialogView.findViewById(R.id.no_answer);

        RadioButton drink_against = dialogView.findViewById(R.id.drink_against);
        RadioButton drink_do_not = dialogView.findViewById(R.id.drink_do_not);
        RadioButton drink_drink_lot = dialogView.findViewById(R.id.drink_drink_lot);
        RadioButton drink_socially = dialogView.findViewById(R.id.drink_socially);

        switch (mCurrentUser.getDrinking()) {
            case User.DRINKING_I_DO_NOT_DRINK:

                drink_do_not.setChecked(true);

                break;
            case User.DRINKING_I_DRINK_A_LOT:
                drink_drink_lot.setChecked(true);

                break;
            case User.DRINKING_I_DRINK_SOCIALLY:

                drink_socially.setChecked(true);
                break;

            case User.DRINKING_IAM_AGAINST_DRINKING:

                drink_against.setChecked(true);
                break;
            default:

                no.setChecked(true);
                break;
        }

        drinkingGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.drink_do_not){
                mCurrentUser.setDrinking(User.DRINKING_I_DO_NOT_DRINK);
            } else if (checkedId == R.id.drink_drink_lot){
                mCurrentUser.setDrinking(User.DRINKING_I_DRINK_A_LOT);
            } else if (checkedId == R.id.drink_socially){
                mCurrentUser.setDrinking(User.DRINKING_I_DRINK_SOCIALLY);
            } else if (checkedId == R.id.drink_against){
                mCurrentUser.setDrinking(User.DRINKING_IAM_AGAINST_DRINKING);
            } else if (checkedId == R.id.no_answer){
                mCurrentUser.setDrinking("");
            }
        });

        alertDialog.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            mCurrentUser.saveEventually();
            loadProfile();
        })
                .create();
        alertDialog.show();
    }

    public void smoking(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.profile_smoking));
        alertDialog.setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.include_profile_smoking, null);

        alertDialog.setView(dialogView);

        RadioGroup smokingGroup = dialogView.findViewById(R.id.smoking_group);
        RadioButton no = dialogView.findViewById(R.id.no_answer);

        RadioButton smoke_hate = dialogView.findViewById(R.id.smoke_hate);
        RadioButton smoke_heavy = dialogView.findViewById(R.id.smoke_heavy);
        RadioButton smoke_not_like = dialogView.findViewById(R.id.smoke_not_like);
        RadioButton smoke_occasionally = dialogView.findViewById(R.id.smoke_occasionally);
        RadioButton smoke_social = dialogView.findViewById(R.id.smoke_social);

        switch (mCurrentUser.getSmoking()) {
            case User.SMOKING_I_DO_NOT_LIKE_IT:

                smoke_not_like.setChecked(true);

                break;
            case User.SMOKING_I_HATE_SMOKING:
                smoke_hate.setChecked(true);

                break;
            case User.SMOKING_I_SMOKE_OCCASIONALLY:

                smoke_occasionally.setChecked(true);
                break;

            case User.SMOKING_IAM_A_HEAVY_SMOKER:

                smoke_heavy.setChecked(true);
                break;

            case User.SMOKING_IAM_A_SOCIAL_SMOKER:

                smoke_social.setChecked(true);
                break;
            default:

                no.setChecked(true);
                break;
        }

        smokingGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.smoke_not_like){
                mCurrentUser.setSmoking(User.SMOKING_I_DO_NOT_LIKE_IT);
            } else if (checkedId == R.id.smoke_hate){
                mCurrentUser.setSmoking(User.SMOKING_I_HATE_SMOKING);
            } else if (checkedId == R.id.smoke_occasionally){
                mCurrentUser.setSmoking(User.SMOKING_I_SMOKE_OCCASIONALLY);
            } else if (checkedId == R.id.smoke_heavy){
                mCurrentUser.setSmoking(User.SMOKING_IAM_A_HEAVY_SMOKER);
            } else if (checkedId == R.id.smoke_social){
                mCurrentUser.setSmoking(User.SMOKING_IAM_A_SOCIAL_SMOKER);
            } else if (checkedId == R.id.no_answer){
                mCurrentUser.setSmoking("");
            }
        });

        alertDialog.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            mCurrentUser.saveEventually();
            loadProfile();
        })
                .create();
        alertDialog.show();
    }

    public void whatIWant(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.profile_what_i_want));
        alertDialog.setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.include_profile_what_i_want, null);

        alertDialog.setView(dialogView);

        RadioGroup whatIWantGroup = dialogView.findViewById(R.id.what_i_want_group);

        RadioButton what_just_chat = dialogView.findViewById(R.id.what_just_chat);
        RadioButton what_casual = dialogView.findViewById(R.id.what_casual);
        RadioButton what_serious = dialogView.findViewById(R.id.what_serious);
        RadioButton what_lets_see = dialogView.findViewById(R.id.what_lets_see);

        switch (mCurrentUser.getWhatIWant()) {
            case User.WHAT_I_WANT_JUST_TO_CHAT:

                what_just_chat.setChecked(true);

                break;
            case User.WHAT_I_WANT_SOMETHING_CASUAL:
                what_casual.setChecked(true);

                break;
            case User.WHAT_I_WANT_SOMETHING_SERIOUS:

                what_serious.setChecked(true);
                break;

            case User.WHAT_I_WANT_LET_SEE_WHAT_HAPPENS:

                what_lets_see.setChecked(true);
                break;
            default:

                what_lets_see.setChecked(true);
                break;
        }

        whatIWantGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.what_just_chat){
                mCurrentUser.setWhatIWant(User.WHAT_I_WANT_JUST_TO_CHAT);
            } else if (checkedId == R.id.what_casual){
                mCurrentUser.setWhatIWant(User.WHAT_I_WANT_SOMETHING_CASUAL);
            } else if (checkedId == R.id.what_serious){
                mCurrentUser.setWhatIWant(User.WHAT_I_WANT_SOMETHING_SERIOUS);
            } else if (checkedId == R.id.what_lets_see){
                mCurrentUser.setWhatIWant(User.WHAT_I_WANT_LET_SEE_WHAT_HAPPENS);
            }
        });

        alertDialog.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            mCurrentUser.saveEventually();
            loadProfile();
        })
                .create();
        alertDialog.show();
    }

    public void openLanguageChooser(){

        languageModels = new ArrayList<>();
        languageAdapter = new LanguageAdapter(this, languageModels);

        sheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        sheetDialog.setContentView(R.layout.include_langauges);
        sheetDialog.setCancelable(true);
        sheetDialog.setCanceledOnTouchOutside(true);
        sheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        RecyclerView recyclerView = sheetDialog.findViewById(R.id.lang_rv);

        assert recyclerView != null;

        LinearLayoutManager layoutManagerSpotlight = new LinearLayoutManager(this);
        layoutManagerSpotlight.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setAdapter(languageAdapter);
        recyclerView.setItemViewCacheSize(12);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setBackgroundResource(R.color.white);
        recyclerView.setBackgroundColor(Color.WHITE);
        recyclerView.setLayoutManager(layoutManagerSpotlight);

        languageModels.clear();
        languageModels.addAll(QuickHelp.getLanguages());
        languageAdapter.notifyDataSetChanged();

        //sheetDialog.setOnDismissListener(dialogInterface -> anyActionSelected = null);

        if (sheetDialog != null && !sheetDialog.isShowing()){
            sheetDialog.show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onResume(){
        super.onResume();

        if (mCurrentUser != null){

            mCurrentUser.fetchIfNeededInBackground();

            if (mCurrentUser.getBirthDate() != null){

                mBasicInfoText.setText(String.format("%s, %s, %s", mCurrentUser.getColFirstName(), QuickHelp.getAgeFromDate(mCurrentUser.getBirthDate()), QuickHelp.getGenderName(mCurrentUser)));
            } else {
                mBasicInfoText.setText(String.format("%s, %s", mCurrentUser.getColFirstName(), QuickHelp.getGenderName(mCurrentUser)));
            }

            mCurrentLocationText.setText(QuickHelp.getOnlyCityFromLocation(mCurrentUser));

            loadProfile();

            parseFiles.clear();
            parseFiles.addAll(mCurrentUser.getProfilePhotos());
            ownProfilePhotosAdapter.notifyDataSetChanged();

            mRecyclerView.scrollToPosition(mCurrentUser.getAvatarPhotoPosition());
        }
    }

}
