package com.yellow.adviceby.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.yellow.adviceby.R;
import com.yellow.adviceby.activities.login.GoogleConnection;
import com.yellow.adviceby.activities.login.LoginActivity;
import com.yellow.adviceby.activities.login.State;
import com.yellow.adviceby.db.DBUserHandler;
import com.yellow.adviceby.model.User;

import java.util.Observable;
import java.util.Observer;

public class AdviceActivity extends AppCompatActivity
        implements Observer, NavigationView.OnNavigationItemSelectedListener {

    //First We Declare Titles And Icons For Our Navigation drawerLayout List View
    //This Icons And Titles Are holded in an Array as you can see
    private GoogleConnection googleConnection;

    @Override
    public void update(Observable observable, Object data) {
        if (observable != googleConnection) {
            return;
        }
        switch ((State) data) {
            case SIGN_IN:
                break;
            case SIGNED_IN:
           /*     try {
                    String emailAddress = googleConnection.getAccountName();
                    Log.i("Lalalal", emailAddress);

                } catch (Exception ex) {
                    String exception = ex.getLocalizedMessage();
                    String exceptionString = ex.toString();
                }
                finish();  */
                break;
            case CLOSED:
                Intent intent = new Intent(AdviceActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case IN_PROGRESS:
                break;
        }
    }

    String TITLES[] = {"Home", "Sign out"};
    int ICONS[] = {R.drawable.ic_home,
            R.drawable.ic_signout};

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view
    String NAME = "Yellow boy";
    String EMAIL = "yellow@gmail.com";
    int PROFILE = R.drawable.aka;

    private Toolbar toolbar;                              // Declaring the Toolbar Object

    //    private RecyclerView mRecyclerView;                           // Declaring RecyclerView
//    private RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
//    private RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    private DrawerLayout drawerLayout;                                  // Declaring DrawerLayout
    private NavigationView navigationView;

    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar drawerLayout Toggle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
 
    /* Assinging the toolbar object to the view
       and setting the the Action bar to our toolbar
     */
        User user = new DBUserHandler(this).read();
        Log.i("AdviceAct", String.valueOf(user != null ? user.getIsConnected() : null));
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        googleConnection = GoogleConnection.getInstance(this);
        googleConnection.addObserver(this);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    //Replacing the main content with ContentFragment Which is our Inbox View;
             /*   case R.id.inbox:
                    Toast.makeText(getApplicationContext(), "Inbox Selected", Toast.LENGTH_SHORT).show();
                    ContentFragment fragment = new ContentFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame,fragment);
                    fragmentTransaction.commit();
                    return true;  */

                    // For rest of the options we just show a toast on click

                    case R.id.home:
                        Toast.makeText(getApplicationContext(), R.string.drawer_ic_home, Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.sign_out:
                        Toast.makeText(getApplicationContext(), R.string.drawer_ic_signout, Toast.LENGTH_SHORT).show();
                        googleConnection.revokeAccessAndDisconnect();
                        //        startActivityForResult(new Intent(AdviceActivity.this, GoogleLogoutActivity.class), 1);

                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;
                }

            }
        });
        //   mRecyclerView = (RecyclerView) findViewById(R.id.navigation); // Assigning the RecyclerView Object to the xml View

        //   mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        //    mAdapter = new MyAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        //    mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        //    mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        //    mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);        // drawerLayout object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }


        }; // drawerLayout Toggle Object Made

        drawerLayout.setDrawerListener(mDrawerToggle); // drawerLayout Listener set to the drawerLayout toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        //Checking if the item is in checked state or not, if not make it in checked state
        if (menuItem.isChecked()) {
            menuItem.setChecked(false);
        } else {
            menuItem.setChecked(true);
        }

        //Closing drawer on item click
        drawerLayout.closeDrawers();

        //Check to see which item was being clicked and perform appropriate action
        switch (menuItem.getItemId()) {

            //Replacing the main content with ContentFragment Which is our Inbox View;
             /*   case R.id.inbox:
                    Toast.makeText(getApplicationContext(), "Inbox Selected", Toast.LENGTH_SHORT).show();
                    ContentFragment fragment = new ContentFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame,fragment);
                    fragmentTransaction.commit();
                    return true;  */

            // For rest of the options we just show a toast on click

            case R.id.home:
                Toast.makeText(getApplicationContext(), R.string.drawer_ic_home, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sign_out:
                Toast.makeText(getApplicationContext(), R.string.drawer_ic_signout, Toast.LENGTH_SHORT).show();
                return true;
            default:
                Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                return true;
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("AdviceActivity", "onDestroy");
        super.onDestroy();
        googleConnection.deleteObserver(this);
        //    googleConnection.disconnect();
    }

}



/*
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
        // IF the view under inflation and population is header or Item
        private static final int TYPE_ITEM = 1;

        private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
        private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java

        private String name;        //String Resource for header View Name
        private int profile;        //int Resource for header view profile picture
        private String email;       //String Resource for header view email


        // Creating a ViewHolder which extends the RecyclerView View Holder
        // ViewHolder are used to to store the inflated views in order to recycle them

        public class ViewHolder extends RecyclerView.ViewHolder {
            int Holderid;

            TextView textView;
            ImageView imageView;
            ImageView profile;
            TextView Name;
            TextView email;


            public ViewHolder(View itemView,int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
                super(itemView);


                // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

                if(ViewType == TYPE_ITEM) {
                    textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                    imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                    Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
                }
                else{


                    Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
                    email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
                    profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                    Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
                }
            }


        }



        MyAdapter(String Titles[],int Icons[],String Name,String Email, int Profile){ // MyAdapter Constructor with titles and icons parameter
            // titles, icons, name, email, profile pic are passed from the main activity as we
            mNavTitles = Titles;                //have seen earlier
            mIcons = Icons;
            name = Name;
            email = Email;
            profile = Profile;                     //here we assign those passed values to the values we declared here
            //in adapter



        }



        //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
        //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
        // if the viewType is TYPE_HEADER
        // and pass it to the view holder

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == TYPE_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false); //Inflating the layout

                ViewHolder vhItem = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

                return vhItem; // Returning the created object

                //inflate your layout and pass it to view holder

            } else if (viewType == TYPE_HEADER) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_header,parent,false); //Inflating the layout

                ViewHolder vhHeader = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

                return vhHeader; //returning the object created


            }
            return null;

        }

        //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
        // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
        // which view type is being created 1 for item row
        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            if(holder.Holderid ==1) {                              // as the list view is going to be called after the header view so we decrement the
                // position by 1 and pass it to the holder while setting the text and image
                holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
                holder.imageView.setImageResource(mIcons[position -1]);// Settimg the image with array of our icons
            }
            else{

                holder.profile.setImageResource(profile);           // Similarly we set the resources for header view
                holder.Name.setText(name);
                holder.email.setText(email);
            }
        }

        // This method returns the number of items present in the list
        @Override
        public int getItemCount() {
            return mNavTitles.length+1; // the number of items in the list will be +1 the titles including the header view.
        }


        // Witht the following method we check what type of view is being passed
        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position))
                return TYPE_HEADER;

            return TYPE_ITEM;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

    }
*/
