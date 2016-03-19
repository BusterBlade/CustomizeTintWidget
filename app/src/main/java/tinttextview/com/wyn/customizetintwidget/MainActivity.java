package tinttextview.com.wyn.customizetintwidget;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    final String image = "image";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        CustomizeTintTextView customizeTextView = (CustomizeTintTextView)findViewById(R.id.customizeTextView);
        customizeTextView.setSupportBackgroundTintList(new ColorStateList(new int[][]{
                new int[] {android.R.attr.state_pressed},
                new int[] {}
        },
                new int[]{
                        Color.parseColor("#3F51B5"),
                        Color.parseColor("#FF4081")
                }));
        String text = customizeTextView.getText().toString();
        SpannableStringBuilder ss = new SpannableStringBuilder(text);
        int startIndex = ss.length();
        ss.append(image);
        ss.setSpan(new CustomizeImageSpan(this,R.drawable.ic_launcher_settings), startIndex, ss.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        customizeTextView.setText(ss);
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
}
