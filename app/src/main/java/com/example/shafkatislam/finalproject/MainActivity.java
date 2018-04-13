package com.example.shafkatislam.finalproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

public class MainActivity extends AppCompatActivity {

    private EditText signInCellNumberEditText,signInPasswordEditText;
    private Button signInButton;
    private ProgressDialog loading;
    private TextView signUp,forget;
    private AlertDialog.Builder alertdialogBuilder; //alertDialogBuilder variable declare

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        signInCellNumberEditText = (EditText) findViewById(R.id.signInCellNumberEditTextId);
        signInPasswordEditText = (EditText) findViewById(R.id.signInPasswordEditTextId);
        signInButton = (Button) findViewById(R.id.signInButtonId);
        signUp = (TextView) findViewById(R.id.signUpButtonId);
        forget=(TextView)findViewById(R.id.forgetId);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void login() {

        //Getting values from editTexts

        final String cell = signInCellNumberEditText.getText().toString().trim();
        final String password = signInPasswordEditText.getText().toString().trim();

        //checking usercell field/validation

        if(cell.isEmpty())
        {
            signInCellNumberEditText.setError("Please Enter Cell");
            signInCellNumberEditText.requestFocus();
        }

        //checking password field/validation
        else if(password.isEmpty())
        {
            signInPasswordEditText.setError("Please Enter Password");
            signInPasswordEditText.requestFocus();
        }


        //Showing progress Dialog

        else
        {
            loading = new ProgressDialog(this);
            loading.setIcon(R.drawable.load);
            loading.setTitle("Login");
            loading.setMessage("Please wait...");
            loading.show();

            //creating a string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Key.LOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("RESPONSE", "" + response);

                    //if we are getting success from server
                    if (response.equals("success")) {

                        //creating a shared preference

                        SharedPreferences sp = MainActivity.this.getSharedPreferences(Key.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                        //Creating editor to store values to shared preferences
                        SharedPreferences.Editor editor = sp.edit();
                        //Adding values to editor
                        editor.putString(Key.CELL_SHARED_PREF, cell);

                        //Saving values to editor
                        editor.commit();

                        loading.dismiss();
                        //starting profile activity
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);

                        Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();

                    } else if (response.equals("failure")) {
                        //if the server response is not success
                        //displaying an error message or toast
                        Toast.makeText(MainActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    } else {
                        //if the server response is not success
                        //displaying an error message or toast
                        Toast.makeText(MainActivity.this, "Invalid Usercell or password", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "There is an error", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //return super.getParams();

                    Map<String,String> params = new HashMap<>();

                    //Ading parameters to request
                    params.put(Key.KEY_CELL,cell);
                    params.put(Key.KEY_PASSWORD,password);

                    //returning parameter
                    return params;

                }
            };

            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }
    }

    @Override
    public void onBackPressed() {           //creating method of onBackPressed
        alertdialogBuilder = new AlertDialog.Builder(this);    //creating object of alertDialogBuilder

        //setting the properties of alertDialogBuilder:

        //for setting title
        alertdialogBuilder.setTitle(R.string.alert_name);

        //for setting message
        alertdialogBuilder.setMessage(R.string.message_name);

        //for setting icon
        alertdialogBuilder.setIcon(R.drawable.exit);

        //for setting cancelable
        alertdialogBuilder.setCancelable(false);

        //for setting Button
        alertdialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //exit
                finish();;
            }
        });
        alertdialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Not exit
                // Toast.makeText(MainActivity.this,"You have clicked on no button",Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        alertdialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Not exit
                //Toast.makeText(MainActivity.this,"You have clicked on cancel button",Toast.LENGTH_SHORT).show();
            }
        });

        //showing alertDialog by creating alertDialog in object and creating alertDialogBuilder in this object
        AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();
    }

    private void TestUpload(){
        int i=1;
        i= i+1;

    }
}
