package com.example.shafkatislam.finalproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AddContactsActivity extends AppCompatActivity {

    //Java object declaration
    Button btnSave,btnImage;
    EditText etxtName,etxtCell,etxtEmail;
    ImageView imageViewId;
    private ZoomControls zoomControls;
    private ProgressDialog loading;
    private  final int IMG_REQUEST = 1;
    private Bitmap bitmap;

    String getUserCell;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        //Link to java and xml id
        btnSave=(Button)findViewById(R.id.btn_save);
        btnImage=(Button)findViewById(R.id.btn_image);
        etxtName=(EditText)findViewById(R.id.etxt_name);
        etxtCell=(EditText)findViewById(R.id.etxt_cell);
        etxtEmail=(EditText)findViewById(R.id.etxt_email);
        imageViewId = (ImageView) findViewById(R.id.imageViewId);
        zoomControls = (ZoomControls) findViewById(R.id.zoomControlId);

        imageViewId.setVisibility(View.GONE);
        zoomControls.setVisibility(View.GONE);


        /*getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("Add Contacts");//for actionbar title */

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences =getSharedPreferences(Key.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getUserCell = sharedPreferences.getString(Key.CELL_SHARED_PREF, "Not Available");

        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                float x= imageViewId.getScaleX();
                float y = imageViewId.getScaleY();

                if(x<100 && y<100)
                {
                    //Toast.makeText(AddContactsActivity.this,"Zoom in",Toast.LENGTH_SHORT).show();

                    imageViewId.setScaleX((float) x+1);
                    imageViewId.setScaleY((float)y+1);

                }

            }
        });

        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                float x= imageViewId.getScaleX();
                float y = imageViewId.getScaleY();

                if(x>1 && y>1)
                {
                    //Toast.makeText(AddContactsActivity.this,"Zoom out",Toast.LENGTH_SHORT).show();
                    imageViewId.setScaleX((float) x-1);
                    imageViewId.setScaleY((float)y-1);

                }


            }
        });


        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    selectImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bitmap==null)
                {
                    Toast.makeText(AddContactsActivity.this,"Please Upload Image",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddContactsActivity.this);
                    builder.setIcon(R.drawable.load)
                            .setMessage("Want to Save Contact?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    // Perform Your Task Here--When Yes Is Pressed.
                                    SaveContact(); //call SaveContact function
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Perform Your Task Here--When No is pressed
                                    dialog.cancel();
                                }
                            }).show();
                }




            }
        });

    }

    /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {         //Whwnever we select a menu Item the id of that menu Item goes to the "item " variable

       if(item.getItemId()==R.id.feedbackId)        //compare with the selected item id with item by "getItemId" method
        {
            //Toast.makeText(MainActivity.this,"Feedback is selected",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this,FeedbackActivity.class);  //to go to the feedback Activity
            startActivity(intent);
            return true;      //Because the method returns a boolean value
        }
        )

        return super.onOptionsItemSelected(item);
    }
    */


    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imageViewId.setImageBitmap(bitmap);
                imageViewId.setVisibility(View.VISIBLE);
                zoomControls.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(imgBytes, android.util.Base64.DEFAULT);
    }

    //Save contact method
    public void  SaveContact()
    {

        final String name=etxtName.getText().toString();
        final String cell=etxtCell.getText().toString();
        final String email=etxtEmail.getText().toString();
        final String image = imageToString(bitmap);


        if (name.isEmpty())
        {
            Toast.makeText(this, "Name Can't Empty", Toast.LENGTH_SHORT).show();
        }
        else if (cell.isEmpty())
        {
            Toast.makeText(this, "Cell Can't Empty", Toast.LENGTH_SHORT).show();
        }

        else if(email.isEmpty())
        {
            Toast.makeText(this, "Email Can't Empty", Toast.LENGTH_SHORT).show();
        }

        else if((cell.length()!=11) || (!(cell.startsWith("018")) && !(cell.startsWith("016")) && !(cell.startsWith("017")) && !(cell.startsWith("015")) && !(cell.startsWith("019"))) )
        {
            etxtCell.setError("Wrong Cell Number");
            etxtCell.requestFocus();
        }

        else if(!email.contains("@"))
        {
            etxtEmail.setError("Wrong Email Address");
            etxtEmail.requestFocus();
        }

        else if(!email.contains("."))
        {
            etxtEmail.setError("Wrong Email Address");
            etxtEmail.requestFocus();
        }

        /*else if(!email.matches(emailPattern))
        {
            etxtEmail.setError("Wrong Email Address");
            etxtEmail.requestFocus();
        }*/

        else {
            loading = new ProgressDialog(this);
            loading.setIcon(R.drawable.wait_icon);
            loading.setTitle("Adding");
            loading.setMessage("Please wait....");
            loading.show();

            String URL = Key.SAVE_URL;


            //Creating a string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST,URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            //for track response in logcat
                            Log.d("RESPONSE", response);
                            // Log.d("RESPONSE", userCell);


                            //If we are getting success from server
                            if (response.equals("success")) {

                                loading.dismiss();
                                //Starting profile activity

                                Intent intent = new Intent(AddContactsActivity.this, ProfileActivity.class);
                                Toast.makeText(AddContactsActivity.this, " Successfully Saved!", Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                            }

                            else if (response.equals("exists")) {
                                //Toast.makeText(AddContactsActivity.this, "Cell Number already exists", Toast.LENGTH_SHORT).show();
                                etxtCell.setError("Cell Number already exists");
                                etxtCell.requestFocus();
                                loading.dismiss();
                            }

                            //If we are getting success from server
                            else if (response.equals("failure")) {

                                loading.dismiss();
                                //Starting profile activity

                                Intent intent = new Intent(AddContactsActivity.this, ProfileActivity.class);
                                Toast.makeText(AddContactsActivity.this, " Save fail!", Toast.LENGTH_SHORT).show();
                                //startActivity(intent);

                            } else {

                                loading.dismiss();
                                Toast.makeText(AddContactsActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                            }

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want

                            Toast.makeText(AddContactsActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request


                    params.put(Key.KEY_USER_CELL, getUserCell);
                    params.put(Key.KEY_NAME, name);
                    params.put(Key.KEY_CELL, cell);
                    params.put(Key.KEY_EMAIL, email);
                    params.put(Key.KEY_IMAGE,image);



                    // Log.d("ID", getID);

                    //returning parameter
                    return params;
                }
            };


            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(AddContactsActivity.this);
            requestQueue.add(stringRequest);
        }

    }

    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}

