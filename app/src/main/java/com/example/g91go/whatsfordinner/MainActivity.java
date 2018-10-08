package com.example.g91go.whatsfordinner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    int sortSelection =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendSelected(View view){
        Intent intent = new Intent(this, DisplayResultsActivity.class);
        int resultIndex;
        ArrayList<CheckBox> SelectedFilters = new ArrayList() ;

        CheckBox Mexican = (CheckBox) findViewById(R.id.checkBox_Mexican);
        if (Mexican.isChecked())
            SelectedFilters.add(Mexican);
        CheckBox Italian = (CheckBox) findViewById(R.id.checkBox_Italian);
        if (Italian.isChecked())
            SelectedFilters.add(Italian);
        CheckBox Chinese = (CheckBox) findViewById(R.id.checkBox_Chinese);
        if (Chinese.isChecked())
            SelectedFilters.add(Chinese);
        CheckBox Vietnamese = (CheckBox) findViewById(R.id.checkBox_Vietnamese);
        if (Vietnamese.isChecked())
            SelectedFilters.add(Vietnamese);
        CheckBox Japanese = (CheckBox) findViewById(R.id.checkBox_Japanese);
        if (Japanese.isChecked())
            SelectedFilters.add(Japanese);
        CheckBox French = (CheckBox) findViewById(R.id.checkBox_French);
        if (French.isChecked())
            SelectedFilters.add(French);
        CheckBox Korean = (CheckBox) findViewById(R.id.checkBox_Korean);
        if (Korean.isChecked())
            SelectedFilters.add(Korean);
        CheckBox American = (CheckBox) findViewById(R.id.checkBox_American);
        if (American.isChecked())
            SelectedFilters.add(American);

        if (SelectedFilters.size() < 2){
            Context context = getApplicationContext();
            CharSequence text = "Please Select at least 2 filters";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else {

            resultIndex = RandomSelect(SelectedFilters);
            CheckBox Choice = SelectedFilters.get(resultIndex);
            String resultFilter = Choice.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, resultFilter);
            intent.putExtra("SORT_SELECTION", sortSelection);
            startActivity(intent);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        MenuItem distance = menu.findItem(R.id.distance);
        MenuItem rating = menu.findItem(R.id.rating);
        MenuItem count = menu.findItem(R.id.count);

        if(sortSelection==1) {
            distance.setChecked(true);
        }
        else if(sortSelection == 2) {
            rating.setChecked(true);
        }
        else if(sortSelection == 3) {
            count.setChecked(true);
        }
    }

    protected int RandomSelect(ArrayList<CheckBox> selectedFilters) {
        Random selectionIndex = new Random();
        int resultIndex = selectionIndex.nextInt(selectedFilters.size());
        return resultIndex;
    }

    public void advFilters(View view){
        registerForContextMenu(view);
        openContextMenu(view);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch(item.getItemId()){

            case R.id.distance:
                Toast.makeText(getApplicationContext(), "Distance Selected",Toast.LENGTH_LONG).show();
                item.setChecked(true);
                sortSelection = 1;
                return true;

            case R.id.rating:
                Toast.makeText(getApplicationContext(), "Rating Selected",Toast.LENGTH_LONG).show();
                item.setChecked(true);
                sortSelection = 2;
                return true;

            case R.id.count:
                Toast.makeText(getApplicationContext(), "Review Amount Selected",Toast.LENGTH_LONG).show();
                item.setChecked(true);
                sortSelection = 3;
                return true;
        }

        return true;
    }
}
