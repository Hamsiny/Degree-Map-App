package com.randomone.androidmonsterc3;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


public class ModuleActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String EXTRA_MANAGERMODE = "com.randomone.androidmonsterc3.EXTRA_MANAGERMODE";

    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private ExtendedFloatingActionButton efabStudent, efabModule;
    private Animation fabOpen, fabClose;

    public Boolean managerMode = false;
    Boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        managerMode = intent.getBooleanExtra(EXTRA_MANAGERMODE, false);


        if (managerMode == true) {
            setContentView(R.layout.activity_modules_manager);
        }
        else {
            setContentView(R.layout.activity_modules_student);
        }

        fab = findViewById(R.id.fab);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);                   //replacing action bar with toolbar for navmenu

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);        //used to call the onNavigationItemSelected Method
        navigationView.setNavigationItemSelectedListener(this);

        efabStudent = findViewById(R.id.fab2);
        efabModule = findViewById(R.id.fab1);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new CoreFragment()).commit();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    efabStudent.startAnimation(fabClose);
                    efabModule.startAnimation(fabClose);
                    efabStudent.setClickable(false);
                    efabModule.setClickable(false);
                    isOpen = false;
                }
                else{
                    efabStudent.startAnimation(fabOpen);
                    efabModule.startAnimation(fabOpen);
                    efabStudent.setClickable(true);
                    efabModule.setClickable(true);
                    isOpen = true;
                }
            }
        });

        efabStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabHider();
                newModule();
            }
        });
        efabModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabHider();
                newStudent();
            }
        });

        if (managerMode == false) {
            fab.setVisibility(View.GONE);
        }

    }

    public void fabHider(){
        if (isOpen == true) {
            efabStudent.startAnimation(fabClose);
            efabModule.startAnimation(fabClose);
            efabStudent.setClickable(false);
            efabModule.setClickable(false);
            isOpen = false;
        }
    }

    @Override
    public void onBackPressed() {
        fabHider();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        fabHider();
        switch (menuItem.getItemId()) {
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AboutFragment()).commit();
                break;
            case R.id.software_stream:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SoftwareFragment()).commit();
                break;
            case R.id.database_stream:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DatabaseFragment()).commit();
                break;
            case R.id.networking_stream:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NetworkingFragment()).commit();
                break;
            case R.id.web_stream:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WebFragment()).commit();
                break;
            case R.id.nav_students:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new StudentFragment()).commit();
                break;
            case R.id.nav_student_profile:
                Intent intent = new Intent(ModuleActivity.this, StudentProfile.class);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void newModule(){
        Intent intent = new Intent(this, ModuleCreatorActivity.class);
        startActivity(intent);
    }

    public void newStudent(){
        Intent intent = new Intent(this, StudentCreatorActivity.class);
        startActivity(intent);
    }

    public Boolean getManagerMode() {
        return managerMode;
    }
}

