package com.tekit.software.ongc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tekit.software.ongc.Adapter.LocationAdapter;
import com.tekit.software.ongc.Model.LocationData;
import com.tekit.software.ongc.Sql.DataBaseAdapter;
import com.tekit.software.ongc.Utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WellLocationActivity extends BaseActivity implements LocationAdapter.LocationAdapterListener, TextWatcher {

    public static final String TAG = WellLocationActivity.class.getName();
    // location updates interval - 10sec

    public static String WELL_LATITUDE = "WELL_LATITUDE";
    public static String WELL_LONGITUDE = "WELL_LONGITUDE";
    public static String WELL_ID = "WELL_ID";
    public static String WELL_RELEASE_NAME = "WELL_RELEASE_NAME";
    public static String WELL_SHORT_NAME = "WELL_SHORT_NAME";
    public static String WELL_UWI = "WELL_UWI";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.et_search_well)
    EditText editTextSearchWell;
    @BindView(R.id.iv_clear_search)
    ImageView imageViewClearSearch;


    private LocationAdapter mLocationAdapter;
    private DataBaseAdapter mDataBaseAdapter;
    private List<LocationData> mLocationDataList;
    private List<LocationData> mLocationDataListFilterd = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        SessionManager.setContext(this);
        mDataBaseAdapter = new DataBaseAdapter(this);

        getLocationData();
        setRecyclerView();
        setListener();
    }

    @Override
    protected void setListener() {
        editTextSearchWell.addTextChangedListener(this);
        editTextSearchWell.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        imageViewClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSearchWell.setText("");
                mLocationAdapter.updateList(mLocationDataList);
            }
        });

    }

    @Override
    protected void setResources() {

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_well_location_search;
    }

    @Override
    protected String getToolBarTitle() {
        return getResources().getString(R.string.toolbar_well_location);
    }

    @Override
    protected void startActivity(Class<?> cls) {

    }




    private void setRecyclerView() {

        mLocationAdapter = new LocationAdapter(this, mLocationDataList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mLocationAdapter);


    }


    @Override
    public void onLocationSelected(int position) {
        LocationData locationData = mLocationDataList.get(position);
        Intent intent = new Intent();
        intent.putExtra(WELL_LATITUDE, locationData.getLat());
        intent.putExtra(WELL_LONGITUDE, locationData.getLongitude());
        intent.putExtra(WELL_ID, locationData.getWellId());
        intent.putExtra(WELL_UWI, locationData.getUWI());
        intent.putExtra(WELL_RELEASE_NAME, locationData.getReleaseName());
        intent.putExtra(WELL_SHORT_NAME, locationData.getShortName());
        setResult(RESULT_OK, intent);
        finish();

    }


    private void getLocationData() {
        mLocationDataList = mDataBaseAdapter.getLocationData();
    }


    void filter(String text) {
        /*List<LocationData> mLocationDataList= new ArrayList();*/
        mLocationDataListFilterd.clear();
        for (LocationData locationData : mLocationDataList) {

            if (locationData.getWellId() != null && locationData.getWellId().toUpperCase().contains(text)) {
                mLocationDataListFilterd.add(locationData);
            }
        }

        mLocationAdapter.updateList(mLocationDataListFilterd);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        filter(editable.toString());

    }
}
