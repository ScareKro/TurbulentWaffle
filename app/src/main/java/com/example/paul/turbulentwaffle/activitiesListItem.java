package com.example.paul.turbulentwaffle;

/**
 * Created by Yam on 12/15/2015.
 *
 * Takes the three values from the Exercise Screen and returns any individual one required.
 */
public class activitiesListItem {

    String type;
    String activity;
    double MET;

    public activitiesListItem(String iType, String iActivity, double iMET) {
            type = iType;
            activity = iActivity;
            MET = iMET;
    }

    public String getType() {
            return type;
        }

    public String getActivity() {
            return activity;
        }

    public double getMET() {
            return MET;
        }
}

