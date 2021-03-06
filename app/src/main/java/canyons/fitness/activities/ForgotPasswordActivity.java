package canyons.fitness.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import canyons.fitness.R;
import canyons.fitness.utilities.Utility;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etEmail;
    Button btResetPwd;
    FirebaseAuth auth;
    String emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Enter animation
        Explode explode = new Explode();
        explode.setDuration(500);
        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(explode);
        auth = FirebaseAuth.getInstance();


        //Initialization
        etEmail=(EditText)findViewById(R.id.et_email);
        btResetPwd= (Button)findViewById(R.id.bt_reset_pwd);

        //Reset password on button click
        btResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.progressIndicator).setVisibility(View.VISIBLE);

                if(etEmail.getText().toString().isEmpty()){

                    Utility.showSnack(getApplicationContext(),btResetPwd,Utility.FIELD_EMPTY);
                }else{


                    emailAddress=etEmail.getText().toString();


                    //Reset password
                      auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    findViewById(R.id.progressIndicator).setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Log.d("SENT", "Email sent.");
                                        Utility.showSnackLong(getApplicationContext(),btResetPwd,"A reset link has been sent to your mail.");
                                    }
                                    else{
                                        Utility.showSnack(getApplicationContext(),btResetPwd,Utility.SOMETHING_WRONG);

                                    }
                                }
                            });
                }
            }
        });

    }
}
