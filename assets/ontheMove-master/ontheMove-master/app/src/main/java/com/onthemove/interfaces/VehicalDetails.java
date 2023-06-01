package com.onthemove.interfaces;

import com.onthemove.commons.baseClasses.BaseView;
import com.onthemove.responseClasses.VehicalHealthModel;
import com.onthemove.responseClasses.VehicalListModel;

import java.util.ArrayList;

public interface VehicalDetails {

    interface VehicalView extends BaseView
    {
        void vehicalList(ArrayList<VehicalListModel.VehicalListData> vehicalListData);

        void vehicalInteriorList(ArrayList<VehicalHealthModel.VehicleInteriorData> interiorData);

        void vehicalExteriorList(ArrayList<VehicalHealthModel.VehicleExteriorData> exteriorData);


    }
}
