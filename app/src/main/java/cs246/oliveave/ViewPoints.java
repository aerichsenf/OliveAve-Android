package cs246.oliveave;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ViewPoints extends AppCompatActivity {

    private static final String TAG = "AndroidBash";
    private DatabaseReference myFirebaseRef;
    private FirebaseAuth mAuth;
    private TextView name;
    private TextView welcomeText;

    String newUid;
    User userClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_points);
        Bundle extras = getIntent().getExtras();
        userClient = new User();
        if(extras == null) {
            newUid= null;
        } else {
            newUid= extras.getString("uid");
        }

        //Creates a reference for  your Firebase database
        //Add YOUR Firebase Reference URL instead of the following URL
        //myFirebaseRef = new Firebase("https://androidbashfirebaseupdat-bd094.firebaseio.com/users/");
        myFirebaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://oliveavecs246.firebaseio.com/Users/" + newUid);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        name = (TextView) findViewById(R.id.text_view_name);
        welcomeText = (TextView) findViewById(R.id.text_view_welcome);


        //Referring to the name of the User who has logged in currently and adding a valueChangeListener
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("E_VALUE", "Data: " + dataSnapshot.getValue());
                userClient=dataSnapshot.getValue(User.class);
                name.setText("Welcome " + userClient.getName() + " to Olive Ave");
                welcomeText.setText("You have : " + userClient.getPhoneNumber()+ " points!");


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(getApplicationContext(), "" + DatabaseError., Toast.LENGTH_LONG).show();
            }
        });


        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //A firebase reference to the welcomeText can be created in following ways :
        // You can use this :
        //Firebase myAnotherFirebaseRefForWelcomeText=new Firebase("https://androidbashfirebaseupdat-bd094.firebaseio.com/welcomeText");*/
        //OR as shown below
        /*myFirebaseRef.child("welcomeText").addValueEventListener(new ValueEventListener() {
            //onDataChange is called every time the data changes in your Firebase Database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Inside onDataChange we can get the data as an Object from the dataSnapshot
                //getValue returns an Object. We can specify the type by passing the type expected as a parameter
                String data = dataSnapshot.getValue(String.class);
                welcomeText.setText(data);
            }

            //onCancelled is called in case of any error
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "" + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });*/

    }

    @Override
    public void onStop() {
        super.onStop();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        /*if (id == R.id.action_logout) {
            mAuth.signOut();
            finish();
        }
*/
        return super.onOptionsItemSelected(item);
    }


    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }
}