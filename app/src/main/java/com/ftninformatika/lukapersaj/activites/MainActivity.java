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
import com.ftninformatika.lukapersaj.db.DataBaseHelper;
import com.ftninformatika.lukapersaj.db.model.Grupa;
import com.ftninformatika.lukapersaj.model.NavigationItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;

    private Grupa grupa;

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

        final EditText nazivG = dialog.findViewById(R.id.dialog_dodaj_naziv);
        final EditText datumG = dialog.findViewById(R.id.dialog_dodaj_datum);
        final EditText vremeG = dialog.findViewById(R.id.dialog_dodaj_vreme);
        final EditText oznakaG = dialog.findViewById(R.id.dialog_dodaj_oznaku);

        Button btnConfrim = dialog.findViewById(R.id.dialog_dodaj_btnConfirm);
        btnConfrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datumG.getText().toString().isEmpty() || !isValidDate(datumG.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Date can't be EMPTY - Date Format: dd-MM-yyyy", Toast.LENGTH_LONG).show();
                    return;
                }
                if (nazivG.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Naziv ne sme biti prazan", Toast.LENGTH_SHORT).show();
                    return; // return obavezno , itd tako za dalje aham znaci treba za sve?
                }
                if(vremeG.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Vreme ne sme biti prazno", Toast.LENGTH_SHORT).show();
                }
                if(oznakaG.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Oznaka ne sme biti prazna", Toast.LENGTH_SHORT).show();
                }
                String nazivGrupe = nazivG.getText().toString();
                String datumKreiranja = nazivG.getText().toString();
                String vremeKreiranja = vremeG.getText().toString();
                String oznakaGrupe = oznakaG.getText().toString();

                grupa = new Grupa();
                grupa.setNaziv(nazivGrupe);
                grupa.setDatumKreiranja(datumKreiranja);
                grupa.setVremeKreiranja(vremeKreiranja);
                grupa.setOznaka(oznakaGrupe);

                try {
                    getDataBaseHelper().getGrupaDao().create(grupa);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    boolean showMassage = sharedPreferences.getBoolean("toast_settings", true);
                    if(showMassage){
                        Toast.makeText(MainActivity.this, "Grupa uspesno dodata", Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                    refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


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
}
