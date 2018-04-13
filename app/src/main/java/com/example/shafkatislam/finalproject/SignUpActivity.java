package com.example.shafkatislam.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText signUpNameEditText,signUpUserCellEditText,signUpPasswordEditText;
    private TextView signin;
    private Button signButton;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpNameEditText = (EditText) findViewById(R.id.signUpNameEditTextId);
        signUpUserCellEditText = (EditText) findViewById(R.id.signUpUserCellEditTextId);
        signUpPasswordEditText = (EditText) findViewById(R.id.signUpPasswordEditTextId);
        signButton = (Button) findViewById(R.id.signButtonId);
        signin = (TextView) findViewById(R.id.terms);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sign_up();
            }
        });
    }

    private void sign_up() {

          //Getting values from editTexts

        final String name = signUpNameEditText.getText().toString().trim();
        final String cell = signUpUserCellEditText.getText().toString().trim();
        final String password = signUpPasswordEditText.getText().toString().trim();

        //checking usercell field/validation

        if (name.isEmpty())
        {
            signUpNameEditText.setError("Please Enter your name");
            signUpNameEditText.requestFocus();
        }

        else if(cell.isEmpty())
        {
            signUpUserCellEditText.setError("Please Enter your cell");
            signUpUserCellEditText.requestFocus();
        }

        //checking password field/validation
        else if(password.isEmpty())
        {
            signUpPasswordEditText.setError("Please Enter Password");
            signUpPasswordEditText.requestFocus();
        }

        else if ((name.length()>20) || (name.length()<5))
        {
            signUpNameEditText.setError("Please Enter your Full name");
            signUpNameEditText.requestFocus();
        }

        else if((cell.length()!=11) || (!(cell.startsWith("018")) && !(cell.startsWith("016")) && !(cell.startsWith("017")) && !(cell.startsWith("015")) && !(cell.startsWith("019"))) )
        {
            signUpUserCellEditText.setError("Wrong Cell Number");
            signUpUserCellEditText.requestFocus();
        }


        else if((password.length()<5) || (password.length()>10))
        {
            signUpPasswordEditText.setError("Password must be 5 to 10 in length");
            signUpPasswordEditText.requestFocus();
        }

        //Showing progress Dialog

        else {
            loading = new ProgressDialog(this);
            loading.setIcon(R.drawable.load);
            loading.setTitle("Login");
            loading.setMessage("Please wait...");
            loading.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Key.SIGNUP_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //for track response in Logcat
                    Log.d("RESPONSE", "" + response);

                    //if we are getting success from server
                    if (response.equals("success")) {
                        //creating a shared preference
                        loading.dismiss();
                        //starting profile activity
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);

                        Toast.makeText(SignUpActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();

                        signUpNameEditText.setText("");
                        signUpUserCellEditText.setText("");
                        signUpPasswordEditText.setText("");

                    } else if (response.equals("exists")) {
                        Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    } else if (response.equals("failure")) {
                        Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SignUpActivity.this, "There is an error", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //return super.getParams();

                    Map<String,String> params = new HashMap<>();

                    //Ading parameters to request
                    params.put(Key.KEY_NAME,name);
                    params.put(Key.KEY_CELL,cell);
                    params.put(Key.KEY_PASSWORD,password);

                    Log.d("info",""+name+" "+cell);
                    //returning parameter
                    return params;

                }
            };

            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }
    }
    //for request focus
    private void requestFocus(View view)
    {
        if(view.requestFocus()){

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
