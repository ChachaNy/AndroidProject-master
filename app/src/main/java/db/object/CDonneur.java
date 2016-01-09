package db.object;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by Chacha on 27.10.2015.
 */
public class CDonneur{

    private int id;
    private String nom;
    private String prenom;
    private String sexe;
    private Date naissance;
    private String adresse;
    private int NPA;
    private String lieu;
    private String region;
    private String telephone;
    private String groupe;
    private Integer donsPossibles;
    private Date disponibilite;
    private int update;

    public CDonneur(){

    }

    public CDonneur(int id, String nom, String prenom, String sexe, Date naissance, String adresse, int NPA, String lieu, String region, String telephone, String groupe, Integer donsPossibles, Date disponibilite, int update){
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.naissance = naissance;
        this.adresse = adresse;
        this.NPA = NPA;
        this.lieu = lieu;
        this.region = region;
        this.telephone = telephone;
        this.groupe = groupe;
        this.donsPossibles = donsPossibles;
        this.disponibilite = disponibilite;
        this.update = update;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getUpdate() {
        return update;
    }

    public void setUpdate(int update) {
        this.update = update;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Date getNaissance() {
        return naissance;
    }

    public void setNaissance(Date naissance) {
        this.naissance = naissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getNPA() {
        return NPA;
    }

    public void setNPA(int NPA) {
        this.NPA = NPA;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public Integer getDonsPossibles() {
        return donsPossibles;
    }

    public void setDonsPossibles(Integer donsPossibles) {
        this.donsPossibles = donsPossibles;
    }

    public Date getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(Date disponibilite) {
        this.disponibilite = disponibilite;
    }

}
