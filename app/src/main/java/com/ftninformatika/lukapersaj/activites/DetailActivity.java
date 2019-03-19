package com.ftninformatika.lukapersaj.activites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ftninformatika.lukapersaj.R;
import com.ftninformatika.lukapersaj.adapter.DrawerAdapter;
import com.ftninformatika.lukapersaj.db.DataBaseHelper;
import com.ftninformatika.lukapersaj.db.model.Grupa;
import com.ftninformatika.lukapersaj.db.model.TodoZadatak;
import com.ftninformatika.lukapersaj.dialog.AboutDialog;
import com.ftninformatika.lukapersaj.model.NavigationItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.ForeignCollection;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.ftninformatika.lukapersaj.R.id.izmena_grupe_action_delete;

public class DetailActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;

    Grupa grupa;

    ForeignCollection<TodoZadatak> zadatak;
    private AlertDialog dialog;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private RelativeLayout drawerPane;
    private ArrayList<NavigationItem> drawerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        try {
            prikaziDetaljeGrupe();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        navigationDrawer();
    }

    @SuppressLint("SetTextI18n")
    private void prikaziDetaljeGrupe() throws SQLException{
        Intent intent = getIntent();
        int id = intent.getExtras().getInt("position");

        grupa  = getDatabaseHelper().getGrupaDao().queryForId(id);

        TextView nazivGrupe  = findViewById(R.id.detail_naziv);
        nazivGrupe.setText("Naziv Grupe" + grupa.getNaziv());

        TextView datumGrupe = findViewById(R.id.detail_datum);
        datumGrupe.setText("Datum kreiranja" + grupa.getDatumKreiranja());

        TextView vremeGrupe = findViewById(R.id.detail_vremeK);
        vremeGrupe.setText("Vreme kreiranja" + grupa.getVremeKreiranja());

        TextView oznaka = findViewById(R.id.detail_oznaka);
        oznaka.setText("Oznaka: " + grupa.getOznaka());

        ListView list_zadaci = findViewById(R.id.lista_zadataka);
        ForeignCollection<TodoZadatak> todoZadatakForeignCollection = getDatabaseHelper().getGrupaDao().queryForId(id).getListaZadatka();
        List<TodoZadatak> listaZadataka = new ArrayList<>(todoZadatakForeignCollection);
        ArrayAdapter<TodoZadatak> arrayAdapter = new ArrayAdapter<>(DetailActivity.this, android.R.layout.simple_list_item_1, listaZadataka);
        list_zadaci.setAdapter(arrayAdapter);
    }

    private void delete(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.obrisi_grupu);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button btnYes = dialog.findViewById(R.id.obrisi_btnYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = getIntent();
                    int id = intent.getExtras().getInt("position");

                    grupa = getDatabaseHelper().getGrupaDao().queryForId(id);
                    getDatabaseHelper().getGrupaDao().delete(grupa);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DetailActivity.this);
                    boolean showMessage = sharedPreferences.getBoolean("toast_settings", true);
                    if (showMessage) {
                        Toast.makeText(DetailActivity.this, "Uspesno IZBRISANO", Toast.LENGTH_LONG).show();
                    }
                    Intent intent1 = new Intent(DetailActivity.this, MainActivity.class);
                    startActivity(intent1);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        Button btnNo = dialog.findViewById(R.id.obrisi_btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private void update(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.izmeni_grupu);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final EditText nazivGrupe = dialog.findViewById(R.id.dialog_izmeni_naziv);
        final EditText datumGrupe = dialog.findViewById(R.id.dialog_izmeni_datum);
        final EditText vremeKreiranja = dialog.findViewById(R.id.dialog_izmeni_vreme);
        final EditText oznakaGrupe = dialog.findViewById(R.id.dialog_izmeni_oznaku);

        Button btnYes = dialog.findViewById(R.id.dialog_izmeni_btnConfirm);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datumGrupe.getText().toString().isEmpty() || !isValidDate(datumGrupe.getText().toString())) {
                    Toast.makeText(DetailActivity.this, "Date can't be EMPTY - Date Format: dd-MM-yyyy", Toast.LENGTH_LONG).show();
                    return;
                }
                grupa.setNaziv(nazivGrupe.getText().toString());
                grupa.setDatumKreiranja(datumGrupe.getText().toString());
                grupa.setVremeKreiranja(Integer.parseInt(vremeKreiranja.getText().toString()));
                grupa.setOznaka(oznakaGrupe.getText().toString());

                try {
                    getDatabaseHelper().getGrupaDao().update(grupa);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DetailActivity.this);
                    boolean showMassage = sharedPreferences.getBoolean("toast_settings", true);
                    if (showMassage) {
                        Toast.makeText(DetailActivity.this, "Izmena uspesno zavrsena", Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    private void navigationDrawer(){
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
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
        drawerItems.add(new NavigationItem("Dialog", "About dialog", R.drawable.ic_launcher_background));

        DrawerAdapter drawerAdapter = new DrawerAdapter(this, drawerItems);
        drawerListView = findViewById(R.id.nav_list_detail);
        drawerListView.setAdapter(drawerAdapter);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        drawerTitle = getTitle();
        drawerLayout = findViewById(R.id.detail_drawer_layout);
        drawerPane = findViewById(R.id.drawer_pane_detail);

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
                Intent intent = new Intent(DetailActivity.this, SettingsActivity.class);
                startActivity(intent);
            }else if(position == 2){
                if (dialog == null){
                    dialog = new AboutDialog(DetailActivity.this).prepareDialog();
                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

                dialog.show();
            }
            drawerLayout.closeDrawer(drawerPane);
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.izmena_grupe_action_update:
                update();
                break;
            case R.id.izmena_grupe_action_delete:
                delete();
                break;
            case R.id.dodaj_zadatak_action_add_zadatak:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public DataBaseHelper getDatabaseHelper() {
        if (dataBaseHelper == null) {
            dataBaseHelper = OpenHelperManager.getHelper(this, DataBaseHelper.class);
        }
        return dataBaseHelper;
    }
}
