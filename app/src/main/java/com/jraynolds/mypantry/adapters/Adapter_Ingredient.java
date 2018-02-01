package com.jraynolds.mypantry.adapters;

/**
 * Created by Jasper on 1/24/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jraynolds.mypantry.dialogs.IngredientView;
import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.layout.ImageView_Square;
import com.jraynolds.mypantry.main.Globals;
import com.jraynolds.mypantry.objects.Ingredient;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Adapter_Ingredient extends ChildViewHolder {

    public final LinearLayout row;
    public final CheckBox checkPantry, checkList;
    public final TextView titleView, descriptionView;
    public final ImageView_Square imageView;
    public final Context context;

    public Adapter_Ingredient(View view, Context context) {
        super(view);

        this.context = context;

        row = view.findViewById(R.id.row);
        checkPantry = view.findViewById(R.id.ingredient_isInPantry);
        checkList = view.findViewById(R.id.ingredient_isOnList);
        titleView = view.findViewById(R.id.ingredient_title);
        descriptionView = view.findViewById(R.id.ingredient_description);
        imageView = view.findViewById(R.id.ingredient_image);
    }

    public void setIngredient(final Ingredient i) {
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("switchActivity", "clicked!");
                Intent intent = new Intent(row.getContext(), IngredientView.class);
                intent.putExtra("Ingredient", (Parcelable) i);
                row.getContext().startActivity(intent);
            }
        });
        checkPantry.setChecked(i.isInPantry);
        checkList.setChecked(i.isOnList);

        if(i.imageUrl != null) {
            AssetManager am = context.getAssets();
            try {
                Resources res = context.getResources();
                InputStream is = am.open("images/" + i.imageUrl);
                Bitmap b = BitmapFactory.decodeStream(is);
                RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(context.getResources(), b);
//                dr.setCornerRadius(Math.max(b.getWidth(), b.getHeight()) / 2.0f);
                imageView.setImageDrawable(dr);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        titleView.setText(i.title);
        descriptionView.setText(i.description);

        checkPantry.setOnClickListener(new checkboxPantryClick(i));
        checkList.setOnClickListener(new checkboxListClick(i));
    }

    public class checkboxPantryClick implements View.OnClickListener {

        private Ingredient i;
        public checkboxPantryClick(Ingredient i) {
            this.i = i;
        }

        @Override
        public void onClick(View view) {
            i.isInPantry = !i.isInPantry;
            Globals.updateLists();
        }
    }

    public class checkboxListClick implements View.OnClickListener {

        private Ingredient i;
        public checkboxListClick(Ingredient i) {
            this.i = i;
        }

        @Override
        public void onClick(View view) {
            i.isOnList = !i.isOnList;
            Globals.updateLists();
        }
    }

    public void setImage() {

    }

}