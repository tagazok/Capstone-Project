package com.leplus.mybooklibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.api.services.books.model.Volume;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.leplus.mybooklibrary.model.Book;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SearchBookTask.SearchListener{

    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private SearchBookTask mSearchBookTask;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[] {
                    new LibraryFragment(),
                    new WishlistFragment(),
            };
            private final String[] mFragmentNames = new String[] {
                    getString(R.string.library),
                    getString(R.string.wishlist)
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            1);
                }
            }
        };
    }

    public void scanBarcode(View view) {

        //TEST
/*
        String query = "isbn:9780618640157";

        if (mSearchBookTask != null) {
            mSearchBookTask.cancel(true);
        }

        mSearchBookTask = new SearchBookTask();
        mSearchBookTask.setSearchListener(this);
        mSearchBookTask.execute(query);
*/

        IntentIntegrator integrator = new IntentIntegrator((Activity)this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                try {
                    String query = "isbn:" + result.getContents();

                    if (mSearchBookTask != null) {
                        mSearchBookTask.cancel(true);
                    }
                    mSearchBookTask = new SearchBookTask();
                    mSearchBookTask.setSearchListener(this);
                    mSearchBookTask.execute(query);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "An error occured", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResult(List<Volume> volumes) {
        List<Volume> volumeList = volumes;
        if (!volumeList.isEmpty()) {
            Volume volume = volumes.get(0);
            Book book = new Book(
                    volume.getId(),
                    volume.getVolumeInfo().getTitle(),
                    volume.getVolumeInfo().getSubtitle(),
                    volume.getVolumeInfo().getPublisher(),
                    volume.getVolumeInfo().getPublishedDate(),
                    volume.getVolumeInfo().getDescription(),
                    volume.getVolumeInfo().getPageCount(),
                    volume.getVolumeInfo().getAuthors(),
                    volume.getVolumeInfo().getImageLinks() == null ? null : volume.getVolumeInfo().getImageLinks().getThumbnail(),
                    volume.getSearchInfo().getTextSnippet()
            );

            Bundle arguments = new Bundle();
            arguments.putSerializable(NewbookDialogFragment.ARG_ITEM, book);
            NewbookDialogFragment fragment = new NewbookDialogFragment();
            fragment.setArguments(arguments);

            fragment.show(getSupportFragmentManager(), "Dialog");
        } else {
            Toast.makeText(this, "No book found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
