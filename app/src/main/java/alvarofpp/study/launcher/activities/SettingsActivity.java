package alvarofpp.study.launcher.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import alvarofpp.study.launcher.R;

public class SettingsActivity extends AppCompatActivity {
    ImageView mHomeScreenImage;
    EditText mNumRow;
    EditText mNumColumn;
    Uri imageUri;

    int REQUEST_CODE_IMAGE = 1;
    String PREFS_NAME = "NovaPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button mHomeScreenButton = findViewById(R.id.homeScreenButton);
        Button mGridSizeButton = findViewById(R.id.gridSizeButton);

        this.mHomeScreenImage = findViewById(R.id.homeScreenImage);
        this.mNumRow = findViewById(R.id.numRow);
        this.mNumColumn = findViewById(R.id.numColumn);

        mGridSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        mHomeScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

        getData();
    }

    private void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences(this.PREFS_NAME, MODE_PRIVATE);
        String imageUriString = sharedPreferences.getString("imageUri", null);
        int numRow = sharedPreferences.getInt("numRow", 7);
        int numColumn = sharedPreferences.getInt("numColumn", 5);

        if (imageUriString != null) {
            this.imageUri = Uri.parse(imageUriString);
            this.mHomeScreenImage.setImageURI(this.imageUri);
        }

        this.mNumRow.setText(String.valueOf(numRow));
        this.mNumColumn.setText(String.valueOf(numColumn));
    }

    private void saveData() {
        SharedPreferences.Editor sharedPreferences = getSharedPreferences(this.PREFS_NAME, MODE_PRIVATE).edit();

        if (this.imageUri != null) {
            sharedPreferences.putString("imageUri", this.imageUri.toString());
        }
        sharedPreferences.putInt("numRow", Integer.valueOf(this.mNumRow.getText().toString()));
        sharedPreferences.putInt("numColumn", Integer.valueOf(this.mNumColumn.getText().toString()));
        sharedPreferences.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK) {
            this.imageUri = data.getData();
            this.mHomeScreenImage.setImageURI(imageUri);
            saveData();
        }
    }
}
