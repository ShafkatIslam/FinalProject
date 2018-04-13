package com.example.shafkatislam.finalproject;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ContactsDetailsActivity extends AppCompatActivity {

    EditText etxtName, etxtCell, etxtEmail;
    String getID, getName, getCell, getEmail, getImage;
    ImageView imageView1;

    Button btnEdit, btnUpdate, btnDelete;

    private ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_details);

        etxtName = (EditText) findViewById(R.id.etxt_name);
        etxtCell = (EditText) findViewById(R.id.etxt_cell);
        etxtEmail = (EditText) findViewById(R.id.etxt_email);

        imageView1 = (ImageView) findViewById(R.id.imageView1);

        btnEdit = (Button) findViewById(R.id.btn_edit);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnDelete = (Button) findViewById(R.id.btn_delete);

        //For disable edit text
        etxtName.setEnabled(false);
        etxtCell.setEnabled(false);
        etxtEmail.setEnabled(false);

        getID = getIntent().getExtras().getString("id");
        getName = getIntent().getExtras().getString("name");
        getCell = getIntent().getExtras().getString("cell");
        getEmail = getIntent().getExtras().getString("email");
        getImage = getIntent().getExtras().getString("image");

        /*getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("Contact Details");//for actionbar title */


        etxtName.setText(getName);
        etxtCell.setText(getCell);
        etxtEmail.setText(getEmail);

        int radius = 30; // corner radius, higher value = more rounded
        int margin = 10; // crop margin, set to 0 for corners with no crop

        Glide.with(this).load((Key.MAIN_URL + "/final/" + getImage))
                .placeholder(R.drawable.load)
                .error(R.drawable.exit)
                .override(120, 120) // resizes the image to 100x200 pixels but does not respect aspect ratio
                .fitCenter()   // scale to fit entire image within ImageView
                .bitmapTransform(new RoundedCornersTransformation(this, radius, margin))
                .into(imageView1);


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                etxtName.setEnabled(true);
                etxtCell.setEnabled(true);
                etxtEmail.setEnabled(true);


                etxtName.setTextColor(Color.BLUE);
                etxtCell.setTextColor(Color.BLUE);
                etxtEmail.setTextColor(Color.BLUE);


            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (etxtName.isEnabled()) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(ContactsDetailsActivity.this);
                    builder.setIcon(R.drawable.load)
                            .setMessage("Want to Update Contact?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    // Perform Your Task Here--When Yes Is Pressed.
                                    UpdateContact();
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Perform Your Task Here--When No is pressed
                                    dialog.cancel();
                                }
                            }).show();

                } else {
                    Toast.makeText(ContactsDetailsActivity.this, "Please edit data!", Toast.LENGTH_SHORT).show();
                }


            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(ContactsDetailsActivity.this);
                builder.setIcon(R.drawable.load)
                        .setMessage("Want to Delete Contact?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                // Perform Your Task Here--When Yes Is Pressed.
                                DataDeleteFromServer();
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
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {          //this method takes the "menu_layout.xml" file in java file

        MenuInflater menuInflater = getMenuInflater();   //"MenuInflater" is a service which coverts xml file into java file
        menuInflater.inflate(R.menu.menu_item, menu);    //"Inflate" method turns the xml file into java file

        return super.onCreateOptionsMenu(menu);
    }

    //update contact method
    public void UpdateContact() {

        final String name = etxtName.getText().toString();
        final String cell = etxtCell.getText().toString();
        final String email = etxtEmail.getText().toString();


        if (name.isEmpty()) {
            Toast.makeText(this, "Name Can't Empty", Toast.LENGTH_SHORT).show();
        } else if (cell.isEmpty()) {
            Toast.makeText(this, "Cell Can't Empty", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Email Can't Empty", Toast.LENGTH_SHORT).show();
        } else if ((cell.length() != 11) || (!(cell.startsWith("018")) && !(cell.startsWith("016")) && !(cell.startsWith("017")) && !(cell.startsWith("015")) && !(cell.startsWith("019")))) {
            etxtCell.setError("Wrong Cell Number");
            etxtCell.requestFocus();
        }
        if (!email.contains("@")) {
            etxtEmail.setError("Wrong Email Address");
            etxtEmail.requestFocus();
        } else if (!email.contains(".")) {
            etxtEmail.setError("Wrong Email Address");
            etxtEmail.requestFocus();
        } else {
            loading = new ProgressDialog(this);
            // loading.setIcon(R.drawable.wait_icon);
            loading.setTitle("Update");
            loading.setMessage("Please wait....");
            loading.show();

            String URL = Key.UPDATE_URL;


            //Creating a string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener <String>() {
                        @Override
                        public void onResponse(String response) {


                            //for track response in logcat
                            Log.d("RESPONSE", response);
                            // Log.d("RESPONSE", userCell);


                            //If we are getting success from server
                            if (response.equals("success")) {

                                loading.dismiss();
                                //Starting profile activity

                                Intent intent = new Intent(ContactsDetailsActivity.this, ProfileActivity.class);
                                Toast.makeText(ContactsDetailsActivity.this, " Successfully Updated!", Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                            }


                            //If we are getting success from server
                            else if (response.equals("failure")) {

                                loading.dismiss();
                                //Starting profile activity

                                Intent intent = new Intent(ContactsDetailsActivity.this, ProfileActivity.class);
                                Toast.makeText(ContactsDetailsActivity.this, " Update fail!", Toast.LENGTH_SHORT).show();
                                //startActivity(intent);

                            } else {

                                loading.dismiss();
                                Toast.makeText(ContactsDetailsActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                            }

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want

                            Toast.makeText(ContactsDetailsActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    }) {

                @Override
                protected Map <String, String> getParams() throws AuthFailureError {
                    Map <String, String> params = new HashMap <>();
                    //Adding parameters to request

                    params.put(Key.KEY_ID, getID);
                    params.put(Key.KEY_NAME, name);
                    params.put(Key.KEY_CELL, cell);
                    params.put(Key.KEY_EMAIL, email);


                    Log.d("ID", getID);

                    //returning parameter
                    return params;
                }
            };


            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(ContactsDetailsActivity.this);
            requestQueue.add(stringRequest);
        }

    }


    //Delete method for deleting contacts
    public void DataDeleteFromServer() {
        loading = new ProgressDialog(this);
        // loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Delete");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Key.DELETE_URL;


        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String response) {


                        //for track response in logcat
                        Log.d("RESPONSE", response);
                        // Log.d("RESPONSE", userCell);


                        //If we are getting success from server
                        if (response.equals("success")) {

                            loading.dismiss();
                            //Starting profile activity

                            Intent intent = new Intent(ContactsDetailsActivity.this, ProfileActivity.class);
                            Toast.makeText(ContactsDetailsActivity.this, " Successfully Deleted!", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                        }


                        //If we are getting success from server
                        else if (response.equals("failure")) {

                            loading.dismiss();
                            //Starting profile activity

                            Intent intent = new Intent(ContactsDetailsActivity.this, ProfileActivity.class);
                            Toast.makeText(ContactsDetailsActivity.this, " Delete fail!", Toast.LENGTH_SHORT).show();
                            //startActivity(intent);

                        } else {

                            loading.dismiss();
                            Toast.makeText(ContactsDetailsActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        Toast.makeText(ContactsDetailsActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                }) {

            @Override
            protected Map <String, String> getParams() throws AuthFailureError {
                Map <String, String> params = new HashMap <>();
                //Adding parameters to request

                params.put(Key.KEY_ID, getID);

                Log.d("ID", getID);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(ContactsDetailsActivity.this);
        requestQueue.add(stringRequest);

    }

    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;


            case R.id.callId: {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+getCell));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return true;
                }
                startActivity(callIntent);
                return true;
            }

            case R.id.shareId:
            {
                //Toast.makeText(MainActivity.this,"Share is selected",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_SEND);    //to show many option to share,i.e, Email,Messenger etc from one place to another
                intent.setType("text/plain");    //to send the messege type

                String subject = "Contact Information";         //this is the part of the share menu
                String body = "Name: "+getName+"\nCell Number: "+getCell+"\nEmail Address: "+getEmail; //this is the part of the share menu

                intent.putExtra(Intent.EXTRA_SUBJECT,subject);   //to send the subject
                intent.putExtra(Intent.EXTRA_TEXT,body);   //to send the body

                startActivity(Intent.createChooser(intent,"share with"));   //Activity Start

                return true;      //Because the method returns a boolean value
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

