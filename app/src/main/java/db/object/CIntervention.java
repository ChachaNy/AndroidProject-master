package db.object;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Suzanne on 25.10.2015.
 */
public class CIntervention extends Activity {

    private int id;
    private Date date;
    private String description;
    private int quantite;
    private String groupe;
    private String region;
    boolean selected;



    public CIntervention() {

    }

    public CIntervention(int id, Date date, String description, int quantite, String groupe, String region, boolean selected) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.quantite = quantite;
        this.groupe = groupe;
        this.region = region;
        this.selected = selected;

    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setId(int id) {this.id = id; }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantite() {
        return quantite;
    }

    public String getRegion() { return region; }

    public void setRegion(String region) { this.region = region; }

    public String getGroupe() {
        return groupe;
    }

    public int getId() {
        return id;
    }



}

