package canyons.fitness.activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import canyons.fitness.R;
import canyons.fitness.utilities.Settings;
import canyons.fitness.utilities.Utility;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etPassword;
    private Button btGo;
    private EditText etUsername;
    CardView cv;
    FloatingActionButton fab;
    TextView forgotPasswordTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialization
        etPassword= (EditText)findViewById(R.id.et_password);
        btGo= (Button) findViewById(R.id.bt_go);
        etUsername= (EditText)findViewById(R.id.et_username);
        cv = (CardView)findViewById(R.id.cv);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        forgotPasswordTextView= (TextView)findViewById(R.id.forgot_password_textview);
        mAuth = FirebaseAuth.getInstance();
        //Enable notifications on app start (session start)
        Settings.setSharedPreference(getApplicationContext(),"notifications","true");


        Context context = getApplicationContext();

        MessageDigest md = null;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        Log.d("SecretKey = ", Base64.encodeToString(md.digest(), Base64.DEFAULT));


        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Explode explode = new Explode();
                explode.setDuration(500);

                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);
                ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                Intent i2 = new Intent(MainActivity.this,ForgotPasswordActivity.class);
                startActivity(i2, oc2.toBundle());

            }
        });


        //sign in user with uuser credentials
        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(validateData()) {
                        signInUser(etUsername.getText().toString().trim(),etPassword.getText().toString().trim());
                    }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, fab, fab.getTransitionName());
                    startActivity(new Intent(MainActivity.this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                }
            }
        });



    }
    private boolean validateData(){
        if(etUsername.getText().toString().trim().isEmpty()||etPassword.getText().toString().isEmpty()){
            Utility.showSnack(getApplicationContext(),btGo,Utility.FIELD_EMPTY);
            return false;
        }
        return true;
    }

    private void signInUser(String email, String password) {

        findViewById(R.id.progressIndicator).setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            Explode explode = new Explode();
                            explode.setDuration(500);

                            getWindow().setExitTransition(explode);
                            getWindow().setEnterTransition(explode);
                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                            Intent i2 = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(i2, oc2.toBundle());
                            finish();
                        }else{
                            Log.d("Failed", "signInWithEmail:failed", task.getException());
                            Utility.showSnack(getApplicationContext(),btGo,Utility.SOMETHING_WRONG);

                        }

                        findViewById(R.id.progressIndicator).setVisibility(View.GONE);
                        // ...
                    }
                });
    }


}
