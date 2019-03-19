package com.ftninformatika.lukapersaj.activites;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ftninformatika.lukapersaj.R;
import com.ftninformatika.lukapersaj.adapter.DrawerAdapter;
import com.ftninformatika.lukapersaj.db.DataBaseHelper;
import com.ftninformatika.lukapersaj.db.model.Grupa;
import com.ftninformatika.lukapersaj.model.NavigationItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.query.In;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;

     Grupa grupa;

    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private RelativeLayout drawerPane;
    private ArrayList<NavigationItem> drawerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            prikaziSveGrupe();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        navigationDrawer();
    }
    private void prikaziSveGrupe()throws SQLException{
        final ListView listView = findViewById(R.id.lista_grupa);
        final List<Grupa> listaGrupa = getDataBaseHelper().getGrupaDao().queryForAll();
        ArrayAdapter<Grupa> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, listaGrupa);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                grupa = (Grupa)listView.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("position", grupa.getId());
                startActivity(intent);
            }
        });

    }
    private void refresh() throws  SQLException{
        ListView listView = findViewById(R.id.lista_grupa);
        if(listView != null){
            ArrayAdapter<Grupa> arrayAdapter = (ArrayAdapter<Grupa>) listView.getAdapter();
            if(arrayAdapter != null){
                arrayAdapter.clear();
                List<Grupa> listaGrupa = getDataBaseHelper().getGrupaDao().queryForAll();
                arrayAdapter.addAll(listaGrupa);
                arrayAdapter.notifyDataSetChanged();
            }
        }
    }
    private void addGrupa(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dodaj_grupu);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final EditText nazivGrupe = dialog.findViewById(R.id.dialog_dodaj_naziv);
        final EditText datumGrupe = dialog.findViewById(R.id.dialog_dodaj_datum);
        final EditText vremeKreiranja = dialog.findViewById(R.id.dialog_dodaj_vreme);
        final EditText oznakaGrupe = dialog.findViewById(R.id.dialog_dodaj_oznaku);

        Button btnConfirm = dialog.findViewById(R.id.dialog_dodaj_btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datumGrupe.getText().toString().isEmpty() || !isValidDate(datumGrupe.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Date can't be EMPTY - Date Format: dd-MM-yyyy", Toast.LENGTH_LONG).show();
                    return;
                }
                String nazivG = nazivGrupe.getText().toString();
                String datumG = datumGrupe.getText().toString();
                int vremeK = Integer.parseInt(vremeKreiranja.getText().toString());
                String oznakaG = oznakaGrupe.getText().toString();




                    grupa = new Grupa();
                    grupa.setNaziv(nazivG);
                    grupa.setDatumKreiranja(datumG);
                    grupa.setVremeKreiranja(vremeK);
                    grupa.setOznaka(oznakaG);




                try {
                    getDataBaseHelper().getGrupaDao().create(grupa);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    boolean showMassage = sharedPreferences.getBoolean("toast_settings", true);
                    if(showMassage){
                        Toast.makeText(MainActivity.this, "Glumac uspesno dodat", Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                    refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        Button btnDecline = dialog.findViewById(R.id.dialog_dodaj_btnDecline);
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dodaj_grupu_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_addGrupa:
                addGrupa();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    private void navigationDrawer(){
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_format_list);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }

        drawerItems.add(new NavigationItem("Prikaz glumaca", "Prikazuje sve grupe", R.drawable.ic_group_black_24dp));
        drawerItems.add(new NavigationItem("Podesavanja", "Podesavanja aplikacije", R.drawable.ic_settings_black_24dp));

        DrawerAdapter drawerAdapter = new DrawerAdapter(this, drawerItems);
        drawerListView = findViewById(R.id.nav_list);
        drawerListView.setAdapter(drawerAdapter);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        drawerTitle = getTitle();
        drawerLayout = findViewById(R.id.main_drawer_layout);
        drawerPane = findViewById(R.id.main_dawer_pane);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                super.onDrawerClosed(drawerView);
            }
        };


    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position == 0){
                getIntent();
            }else if(position == 1){
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
            drawerLayout.closeDrawer(drawerPane);
        }

    }

    public static boolean isValidDate(String inDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        try{
            dateFormat.parse(inDate.trim());
        }catch (ParseException pe){
            return false;
        }
        return true;
    }

    public DataBaseHelper getDataBaseHelper(){
        if(dataBaseHelper == null){
            dataBaseHelper = OpenHelperManager.getHelper(this, DataBaseHelper.class);
        }
        return dataBaseHelper;
    }

    @Override
    protected void onDestroy() {
        if(dataBaseHelper != null){
            OpenHelperManager.releaseHelper();
            dataBaseHelper = null;
        }
        super.onDestroy();
    }

}
