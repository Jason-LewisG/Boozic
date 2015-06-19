package comjason_lewisg.httpsgithub.boozic;


//import android.animation.Animator;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.speech.RecognizerIntent;
import android.content.Intent;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.quinny898.library.persistentsearch.SearchBox;

import Handlers.DialogHandler;
import Handlers.FloatingActionButtonHandler;
import Handlers.NavigationDrawerHandler;
import io.codetail.animation.ReverseInterpolator;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
//import Handlers.SearchBox;

import com.quinny898.library.persistentsearch.SearchBox.MenuListener;
import com.quinny898.library.persistentsearch.SearchBox.SearchListener;
import com.quinny898.library.persistentsearch.SearchResult;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //create a Toolbar object
    public Toolbar toolbar;

    private MenuItem item;
    private ImageView refresh;
    private Animation rotation;
    private ArrayList<String> searchSuggest;

    public DialogHandler DHandle;
    public SearchBox search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creates a FAB for the bottom right corner of the main screen
        FloatingActionButtonHandler FButton = new FloatingActionButtonHandler();
        FButton.connectButton(this);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        search = (SearchBox) findViewById(R.id.searchbox);
        //search.enableVoiceRecognition(this);

        //Creates a Navigation Drawer
        //When you swipe from the left
        NavigationDrawerHandler Nav = new NavigationDrawerHandler();
        Nav.connectDrawer(this,toolbar);

        DHandle = new DialogHandler();
        //search.revealFrom();

        searchSuggest = new ArrayList<>();
        searchSuggest.add("Wine");
        searchSuggest.add("Vodka");
        searchSuggest.add("Beer");
        searchSuggest.add("Whiskey");
        searchSuggest.add("Scotch");
        searchSuggest.add("Hennessy");
        searchSuggest.add("Tequila");
        searchSuggest.add("Rum");
        searchSuggest.add("Brandy");
        searchSuggest.add("Gin");
        searchSuggest.add("Sake");
    }


    //Data Handlers//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        item = menu.findItem(R.id.action_refresh);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            refresh.startAnimation(rotation);
            Toast.makeText(this, "You have pressed refresh", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_search) {


            openSearch();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
    ////////////////

    public void openSearch() {
        toolbar.setTitle("Boozic");


        //Turn buttons off
        findViewById(R.id.action_search).setEnabled(false);
        findViewById(R.id.action_refresh).setEnabled(false);

        revealFromMenuItem(R.id.action_search, this);


        for (int x = 0; x < searchSuggest.size(); x++) {
            SearchResult option = new SearchResult(searchSuggest.get(x).toString(), getResources().getDrawable(
                    R.drawable.ic_action_history, null));
            search.addSearchable(option);
        }

        search.setLogoText("Search Boozic");
        search.setMenuListener(new MenuListener() {

            @Override
            public void onMenuClick() {
                // Hamburger has been clicked
                Toast.makeText(MainActivity.this, "Menu click",
                        Toast.LENGTH_LONG).show();
            }

        });
        search.setSearchListener(new SearchListener() {

            @Override
            public void onSearchOpened() {
                // Use this to tint the screen

            }

            @Override
            public void onSearchClosed() {
                // Use this to un-tint the screen
                closeSearch();
            }

            @Override
            public void onSearchTermChanged() {
                // React to the search term changing
                // Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {
                Toast.makeText(MainActivity.this, searchTerm + " Searched",
                        Toast.LENGTH_LONG).show();
                toolbar.setTitle(searchTerm);

            }

            @Override
            public void onSearchCleared() {

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    protected void closeSearch() {
        hideCircularly(this);
        //Turn buttons back on
        findViewById(R.id.action_search).setEnabled(true);
        findViewById(R.id.action_refresh).setEnabled(true);

        if(search.getSearchText().isEmpty())toolbar.setTitle(R.string.app_name);
    }

    public void revealFromMenuItem(int id, Activity activity) {
        findViewById(R.id.searchbox).setVisibility(View.VISIBLE);
        View menuButton = activity.findViewById(id);
        if (menuButton != null) {
            FrameLayout layout = (FrameLayout) activity.getWindow().getDecorView()
                    .findViewById(android.R.id.content);
            if (layout.findViewWithTag("searchBox") == null) {
                int[] location = new int[2];
                menuButton.getLocationInWindow(location);
                revealFrom(activity, search);
            }
        }
    }

    private void revealFrom(Activity a, SearchBox s) {
        FrameLayout layout = (FrameLayout) a.getWindow().getDecorView()
                .findViewById(android.R.id.content);
        RelativeLayout root = (RelativeLayout) s.findViewById(R.id.search_root);
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96,
                r.getDisplayMetrics());
        int cx = findViewById(R.id.toolbar).getWidth() / 2 + 320;
        int cy = findViewById(R.id.toolbar).getHeight() / 2 + 20;

        int finalRadius = (int) Math.max(layout.getWidth(), px);

        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
                root, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500);
        animator.addListener(new SupportAnimator.AnimatorListener(){

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationEnd() {
                search.toggleSearch();
            }

            @Override
            public void onAnimationRepeat() {

            }

            @Override
            public void onAnimationStart() {

            }

        });
        animator.start();
    }

    public void hideCircularly(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        final FrameLayout layout = (FrameLayout) activity.getWindow().getDecorView()
                .findViewById(android.R.id.content);
        RelativeLayout root = (RelativeLayout) findViewById(R.id.search_root);
        display.getSize(size);
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96,
                r.getDisplayMetrics());
        int cx = findViewById(R.id.toolbar).getWidth() / 2 + 320;
        int cy = findViewById(R.id.toolbar).getHeight() / 2 + 20;
        int finalRadius = (int) Math.max(layout.getWidth()*1.5, px);

        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
                root, cx, cy, 0, finalRadius);
        animator.setInterpolator(new ReverseInterpolator());
        animator.setDuration(500);
        animator.start();
        animator.addListener(new SupportAnimator.AnimatorListener(){

            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {
                findViewById(R.id.searchbox).setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationRepeat() {

            }

        });
    }
}
