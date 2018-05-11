package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    
    private TextView mAlias;
    private TextView mPlaceOfOrigin;
    private TextView mDescription;
    private TextView mIngredients;
    private ImageView mMainImage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        
        //Set the text views and image view
        mAlias = findViewById(R.id.tv_alias);
        mPlaceOfOrigin = findViewById(R.id.tv_place_of__origin);
        mDescription = findViewById(R.id.tv_description);
        mIngredients = findViewById(R.id.tv_ingredients);
        mMainImage = findViewById(R.id.iv_main_image);
        
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }
        
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich;
        
        try {
            sandwich = JsonUtils.parseSandwichJson(json);
        } catch (JSONException e) {
            sandwich = null;
        }
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            
            //Return
            return;
        }
        
        //Populate UI
        populateUI(sandwich);
    }
    
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
    
    private void populateUI(Sandwich sandwich) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Set to the toolbar the name of the sandwich
        getSupportActionBar().setTitle(sandwich.getMainName());
        
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(mMainImage);
        
//        setTitle(sandwich.getMainName());
        getSupportActionBar().setTitle(sandwich.getMainName());
        
        /*
         * Proceed to verify possible empty values for the alias list and the place of origin
         * */
        //Sometimes there are no other names for the sandwich... verify that is not empty, if it is, make the mAlias text view as View.VISIBLE
        List<String> aliasList = sandwich.getAlsoKnownAs();
        if (!aliasList.isEmpty()) {
            mAlias.setVisibility(View.VISIBLE);
            //Append an initial space and the proper string
            appendWithBoldLabel(mAlias, String.valueOf(appendString(sandwich.getAlsoKnownAs())));
        }
        String placeOfOrigin = sandwich.getPlaceOfOrigin();
        if (!placeOfOrigin.isEmpty()) {
            mPlaceOfOrigin.setVisibility(View.VISIBLE);
            //Append an initial space and the proper string
            appendWithBoldLabel(mPlaceOfOrigin, sandwich.getPlaceOfOrigin());
        }
        
        //Append an initial space and the proper string
        appendWithBoldLabel(mIngredients, String.valueOf(appendString(sandwich.getIngredients())));
        appendWithBoldLabel(mDescription, sandwich.getDescription());
    }
    
    private void appendWithBoldLabel(TextView textView, String text) {
        SpannableStringBuilder str = new SpannableStringBuilder(textView.getText());
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, textView.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.append(" ").append(text);
        
        //Append the text to the string builder
        textView.setText(str);
    }
    
    private StringBuilder appendString(List list) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < list.size(); i++) {
            sb.append(" ").append(list.get(i));
            
            if (i < list.size() - 1) {
                sb.append(", ");
            }
        }
        
        return sb;
    }
}