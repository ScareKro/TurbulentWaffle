package com.example.paul.turbulentwaffle;

/**
 * Created by Yam on 12/15/2015.
 */
public class activitiesListItem {

            public activitiesListItem(String iType, String iActivity, double iMET) {
            type = iType;
            activity = iActivity;
            MET = iMET;
        }

        String type;
        String activity;
        double MET;

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

