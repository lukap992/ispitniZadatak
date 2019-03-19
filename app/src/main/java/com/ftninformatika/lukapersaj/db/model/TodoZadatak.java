package com.ftninformatika.lukapersaj.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = TodoZadatak.DATABASE_TABLE_NAME)
public class TodoZadatak {

    public static final String DATABASE_TABLE_NAME = "zadatak";

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAZIV = "naziv";
    public static final String FIELD_OPIS = "opis";
    public static final String FIELD_PRIORITET = "prioritet";
    public static final String FIELD_DATUM_kreiranja ="datumKreiranja";
    public static final String FIELD_VREME_kreiranja = "vremeKreiranja";
    public static final String FIELD_VREME_zavrsetka = "vremeZavrsetka";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_GRUPA = "grupa";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAZIV)
    private String naziv;

    @DatabaseField(columnName = FIELD_OPIS)
    private String opis;

    @DatabaseField(columnName = FIELD_PRIORITET)
    private String prioritet;

    @DatabaseField(columnName = FIELD_DATUM_kreiranja)
    private String datumKreiranja;

    @DatabaseField(columnName = FIELD_VREME_kreiranja)
    private String vremeKreiranja;

    @DatabaseField(columnName = FIELD_VREME_zavrsetka)
    private String vremeZavrsetka;

    @DatabaseField(columnName = FIELD_STATUS)
    private String status;

    @DatabaseField(columnName = FIELD_GRUPA, foreign = true, foreignAutoRefresh = true)
    private Grupa grupa;

    public TodoZadatak(){

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

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getPrioritet() {
        return prioritet;
    }

    public void setPrioritet(String prioritet) {
        this.prioritet = prioritet;
    }

    public String getDatumKreiranja() {
        return datumKreiranja;
    }

    public void setDatumKreiranja(String datumKreiranja) {
        this.datumKreiranja = datumKreiranja;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Grupa getGrupa() {
        return grupa;
    }

    public void setGrupa(Grupa grupa) {
        this.grupa = grupa;
    }

    public String toString(){
        return this.naziv;
    }
}
