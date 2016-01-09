package db.object;

import java.util.Date;

/**
 * Created by Suzanne on 30.10.2015.
 */
public class CSang {

    private int id;
    private int donneur;
    private Date dateDon;
    private Date peremption;
    private String region;
    private String groupe;
    private String statut;
    private int intervention;

    public CSang(){

    }

    public CSang(int id, int donneur, Date dateDon, Date peremption, String region, String groupe, String statut, int intervention){
        this.id = id;
        this.donneur = donneur;
        this.dateDon = dateDon;
        this.peremption = peremption;
        this.region = region;
        this.groupe = groupe;
        this.statut = statut;
        this.intervention = intervention;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDonneur() {
        return donneur;
    }

    public void setDonneur(int donneur) {
        this.donneur = donneur;
    }

    public Date getDateDon() {
        return dateDon;
    }

    public void setDateDon(Date dateDon) {
        this.dateDon = dateDon;
    }

    public Date getPeremption() {
        return peremption;
    }

    public void setPeremption(Date peremption) {
        this.peremption = peremption;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getIntervention() {
        return intervention;
    }

    public void setIntervention(int intervention) {
        this.intervention = intervention;
    }

}
