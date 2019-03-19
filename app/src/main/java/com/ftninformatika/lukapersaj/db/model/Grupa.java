package com.ftninformatika.lukapersaj.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = Grupa.DATABASE_TABLE_NAME)
public class Grupa {

    public static final String DATABASE_TABLE_NAME = "grupa";
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAZIV = "naziv";
    public static final String FIELD_DATUM_kreiranja = "datumKreiranja";
    public static final String FIELD_VREME_kreiranja = "vremeKreiranja";
    public static final String FIELD_LISTA_OZNAKA = "listaOznaka";
    public static final String FIELD_LISTA_ZADATKA = "listaZadatka";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAZIV)
    private String naziv;

    @DatabaseField(columnName = FIELD_DATUM_kreiranja)
    private String datumKreiranja;

    @DatabaseField(columnName = FIELD_VREME_kreiranja)
    private int vremeKreiranja;

    @DatabaseField(columnName = FIELD_LISTA_OZNAKA)
    private String oznaka;


    @ForeignCollectionField(columnName = FIELD_LISTA_ZADATKA, eager = true)
    private ForeignCollection<TodoZadatak> listaZadatka;

    public Grupa(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getDatumKreiranja() {
        return datumKreiranja;
    }

    public void setDatumKreiranja(String datumKreiranja) {
        this.datumKreiranja = datumKreiranja;
    }

    public int getVremeKreiranja() {
        return vremeKreiranja;
    }

    public void setVremeKreiranja(int vremeKreiranja) {
        this.vremeKreiranja = vremeKreiranja;
    }

    public String getOznaka() {
        return oznaka;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
    }

    public ForeignCollection<TodoZadatak> getListaZadatka() {
        return listaZadatka;
    }

    public void setListaZadatka(ForeignCollection<TodoZadatak> listaZadatka) {
        this.listaZadatka = listaZadatka;
    }
    public String toString(){
        return this.naziv;
    }
}
