package com.dataservicios.ttauditvisibilidadnewstore.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dataservicios.ttauditvisibilidadnewstore.R;
import com.dataservicios.ttauditvisibilidadnewstore.db.DatabaseManager;
import com.dataservicios.ttauditvisibilidadnewstore.model.Company;
import com.dataservicios.ttauditvisibilidadnewstore.repo.CompanyRepo;
import com.dataservicios.ttauditvisibilidadnewstore.repo.RouteStoreTimeRepo;
import com.dataservicios.ttauditvisibilidadnewstore.repo.StoreRepo;
import com.dataservicios.ttauditvisibilidadnewstore.util.GPSTracker;
import com.dataservicios.ttauditvisibilidadnewstore.util.SessionManager;
import com.dataservicios.ttauditvisibilidadnewstore.view.NewStoreActivity;

import java.util.HashMap;


/**
 * Created by usuario on 08/01/2015.
 */
public class CreateStoreFragment extends Fragment {

    private static final String LOG_TAG = CreateStoreFragment.class.getSimpleName();
    // Movies json url

    private Activity activity;
    private ProgressDialog pDialog;
    private SessionManager      session;
    private String email_user, user_id, name_user;
    private Button btNew;
    private CompanyRepo         companyRepo;
    private RouteStoreTimeRepo  routeStoreTimeRepo;
    private Company             company;
    private GPSTracker          gpsTracker;


    public CreateStoreFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        activity = getActivity();
        DatabaseManager.init(activity);


        session = new SessionManager(getActivity());

        HashMap<String, String> user = session.getUserDetails();
        name_user = user.get(SessionManager.KEY_NAME);
        email_user = user.get(SessionManager.KEY_EMAIL);
        user_id = user.get(SessionManager.KEY_ID_USER);
        //Añadiendo parametros para pasar al Json por metodo POST


        companyRepo             = new CompanyRepo(activity);
        routeStoreTimeRepo      = new RouteStoreTimeRepo(activity);
        gpsTracker              = new GPSTracker(activity);

        company = (Company) companyRepo.findFirstReg();

        if(gpsTracker.canGetLocation()){
//            GlobalConstant.latitude_open = gpsTracker.getLatitude();
//            GlobalConstant.longitude_open= gpsTracker.getLongitude();
        }else{

            gpsTracker.showSettingsAlert();
        }

        View rootView = inflater.inflate(R.layout.fragment_create_store, container, false);
        btNew = (Button) rootView.findViewById(R.id.btNew);



        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.text_loading));
        pDialog.setCancelable(false);

        btNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StoreRepo storeRepo = new StoreRepo(activity);
                storeRepo.deleteAll();

                routeStoreTimeRepo.deleteAll();

                Intent intent;
                intent = new Intent(getActivity().getApplicationContext(), NewStoreActivity.class);
                //intent.putExtras(argRuta);
                startActivity(intent);
            }
        });


        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //hidePDialog();
    }



    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }




}
