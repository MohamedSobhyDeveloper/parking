package com.park.optech.parking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.park.optech.parking.sharedpref.MySharedPref;
import com.park.optech.parking.parkingfind;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView=null;
    Toolbar toolbar=null;
    boolean doubleBackToExitPressedOnce = false;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String car = MySharedPref.getData(MainActivity.this, "car", null);



        System.out.println("car "+car);
        if (car.equals("null")||car.equals("")||car.equals(null)){
            Toast.makeText(this, "Please Complete Your Profile", Toast.LENGTH_LONG).show();
            profile profile=new profile();
            android.support.v4.app.FragmentTransaction fragmentTransaction=
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mm,profile);
            fragmentTransaction.commit();

        }else if (car.equals("mb")){

            mycar mycar = new mycar();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mm, mycar);
            fragmentTransaction.commit();
        }

        else{

            parkingfind parkingfind = new parkingfind();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
          fragmentTransaction.replace(R.id.mm, parkingfind);
            fragmentTransaction.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
             if (id == R.id.myprofie) {
                startActivity(new Intent(MainActivity.this,MainActivity.class));
                finish();

            } else if (id == R.id.car) {
                startActivity(new Intent(MainActivity.this,MainActivity.class));

            } else if (id == R.id.history) {
                startActivity(new Intent(MainActivity.this,MainActivity.class));

            } else if (id == R.id.wallet) {
                startActivity(new Intent(MainActivity.this,MainActivity.class));

            } else if (id == R.id.myticket) {
                startActivity(new Intent(MainActivity.this,MainActivity.class));

            } else if (id == R.id.pticket) {
                 startActivity(new Intent(MainActivity.this,MainActivity.class));

             }else {
                 Intent intent = new Intent(Intent.ACTION_MAIN);
                 intent.addCategory(Intent.CATEGORY_HOME);
                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(intent);
                 finish();
                 System.exit(0);
             }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
         id = item.getItemId();

        if (id == R.id.myprofie) {
            profile profile=new profile();
            android.support.v4.app.FragmentTransaction fragmentTransaction=
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mm,profile);
            fragmentTransaction.commit();

            // Handle the camera action
        } else if (id == R.id.parkingfinder) {
            startActivity(new Intent(MainActivity.this,MainActivity.class));


        } else if (id == R.id.car) {
            mycar mycar=new mycar();
            android.support.v4.app.FragmentTransaction fragmentTransaction=
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mm,mycar);
            fragmentTransaction.commit();

        } else if (id == R.id.history) {
            history history=new history();
            android.support.v4.app.FragmentTransaction fragmentTransaction=
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mm,history);
            fragmentTransaction.commit();

        } else if (id == R.id.wallet) {
            wallet wallet = new wallet();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mm, wallet);
            fragmentTransaction.commit();

        }else if (id == R.id.myticket) {
            MyTicket myTicket = new MyTicket();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mm, myTicket);
            fragmentTransaction.commit();
        }else if (id == R.id.pticket) {
            printticket myTicket = new printticket();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mm, myTicket);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
