package comjason_lewisg.httpsgithub.boozic;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //create a Toolbar object
    protected Toolbar toolbar;

    private ImageView refresh;
    private Animation rotation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creates a FAB for the bottom right corner of the main screen
        FloatActionButton FButton = new FloatActionButton();
        FButton.connectButton(this);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Creates a Navigation Drawer
        //When you swipe from the left
        NavigationDrawer Nav = new NavigationDrawer();
        Nav.connectDrawer(this,toolbar);
    }

    //Data Handlers//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        final MenuItem item = menu.findItem(R.id.action_refresh);

        item.setActionView(R.layout.nav_refresh);
        refresh = (ImageView) item.getActionView().findViewById(R.id.refreshButton);
        rotation = AnimationUtils.loadAnimation(this, R.anim.rotation);

        refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onOptionsItemSelected(item);
            }
        });


        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.rotation);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            refresh.startAnimation(rotation);
            Toast.makeText(this, "You have pressed refresh", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
    ////////////////
}
