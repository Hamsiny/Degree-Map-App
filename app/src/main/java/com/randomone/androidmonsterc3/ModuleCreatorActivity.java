package com.randomone.androidmonsterc3;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleCreatorActivity extends AppCompatActivity {
    private EditText codeInput, titleInput, descriptionInput, creditsInput, levelInput, prerequisiteInput, corequisiteInput;
    private Spinner semesterInput;
    private CheckBox coreBox, softwareBox, networkBox, webBox, databaseBox;
    private String time;
    private List<String> pathway = new ArrayList<String>();
    private List<String> prerequisites = new ArrayList<String>();
    private List<String> corequisites = new ArrayList<String>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "ModuleCreatorActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_creator);

        codeInput = findViewById(R.id.code_input);
        titleInput = findViewById(R.id.title_input);
        descriptionInput = findViewById(R.id.desc_input);
        creditsInput = findViewById(R.id.credits_input);
        levelInput = findViewById(R.id.level_input);
        prerequisiteInput = findViewById(R.id.prerequisite_input);
        corequisiteInput = findViewById(R.id.corequisite_input);
        semesterInput = findViewById(R.id.time_spinner);
        coreBox = findViewById(R.id.core_box);
        softwareBox = findViewById(R.id.software_box);
        networkBox = findViewById(R.id.network_box);
        webBox = findViewById(R.id.web_box);
        databaseBox = findViewById(R.id.database_box);



        //making semester spinner functional
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.time_picker_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterInput.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.create_module_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_module:
                uploadModule();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //CORE is in all pathways, therefore no other pathways can be selected while it is selected
    public void coreBox(View view) {
        if (coreBox.isChecked()) {
            softwareBox.setChecked(false);
            networkBox.setChecked(false);
            webBox.setChecked(false);
            databaseBox.setChecked(false);

        }
    }
    //Disables core when specific streams are selected
    public void specificBox(View view) {
        if (softwareBox.isChecked() || networkBox.isChecked() || webBox.isChecked() || databaseBox.isChecked()) {
            coreBox.setChecked(false);
        }
    }

    public void uploadModule() {
        //turns the spinner text into a position that gets turned into a valid semester time (e.g. Semester 1 = S1)
        int semesterPosition = semesterInput.getSelectedItemPosition();
        String[] semesterActual = getResources().getStringArray(R.array.semester_array);
        time = String.valueOf(semesterActual[semesterPosition]);

        //uses the checkboxes to determine what pathways to set (core = core + all)
        if (!pathway.isEmpty()){            //todo check if the EditText boxes are empty
            pathway.clear();
        }
        if(coreBox.isChecked()) {
            pathway.add("core");
            pathway.add("database");
            pathway.add("networking");
            pathway.add("software");
            pathway.add("web");
        }
        if (databaseBox.isChecked()) {
            pathway.add("database");
        }
        if (networkBox.isChecked()) {
            pathway.add("networking");
        }
        if (softwareBox.isChecked()) {
            pathway.add("software");
        }
        if (webBox.isChecked()) {
            pathway.add("web");
        }


        String raw = prerequisiteInput.getText().toString();
        if (raw != null && !raw.trim().isEmpty()){
           String[] preprerequisites = raw.split("\\s*,\\s*");
           prerequisites = Arrays.asList(preprerequisites);

        }

        raw = corequisiteInput.getText().toString();
        if (raw != null && !raw.trim().isEmpty()) {
            String[] precorequisites = raw.split("\\s*,\\s*");
            corequisites = Arrays.asList(precorequisites);
        }

        Module module = new Module(
                codeInput.getText().toString(),
                titleInput.getText().toString(),
                descriptionInput.getText().toString(),
                time,
                Integer.parseInt(creditsInput.getText().toString()),
                Integer.parseInt(levelInput.getText().toString()),
                pathway,
                prerequisites,
                corequisites);

        Log.d(TAG, module.toString());

        db.collection("modules").add(module);
        finish();
    }
}
