package com.example.myflower;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class FlowerHome extends AppCompatActivity implements CheckForEmpty {

  List<Model> flowerList;
  RecyclerView recyclerView;
  FlowerAdapter adapter;
  RecyclerView.LayoutManager recylerViewLayoutManager;
  Context context;
  EditText searchBox;
  LinearLayout errorView;
  Sort sortType;
  FloatingActionButton fabFilter;
  public enum Sort{AtoZ, ZtoA}

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_flower_home);
    init();
    fetchDataFromServer();
  }

  private void init() {
    context = this;
    flowerList = new ArrayList<>();
    recyclerView = (RecyclerView) findViewById(R.id.list);
    searchBox = (EditText) findViewById(R.id.searchBox);
    errorView = (LinearLayout) findViewById(R.id.error_view);
    recylerViewLayoutManager = new LinearLayoutManager(context);
    recyclerView.setLayoutManager(recylerViewLayoutManager);
    searchBox.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        String text = searchBox.getText().toString().toLowerCase(Locale.getDefault());
        if (adapter != null) {
          adapter.filter(text);
        } else {
          Toast.makeText(FlowerHome.this, "Try changing search text, No flowers found!!!", Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    fabFilter = (FloatingActionButton) findViewById(R.id.fab_job_filter);
    fabFilterListener(fabFilter);

  }
  private void fabFilterListener(FloatingActionButton fabFilter) {
    fabFilter.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showRadioButtonDialog();
      }
    });
  }
  private void fetchDataFromServer() {
    String JSON_URL = "http://development.easystartup.org/NO/Backend/flower.php";
    StringRequest stringRequest = new StringRequest(JSON_URL,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            flowerList = Model.getListFromJsonString(response , FlowerHome.this);
            adapter = new FlowerAdapter(FlowerHome.this, flowerList);
            recyclerView.setAdapter(adapter);
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            recyclerView.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
          }
        });
    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(stringRequest);
  }

  @Override
  public void isListEmpty(boolean flag) {
    if (flag) {
      recyclerView.setVisibility(View.GONE);
      errorView.setVisibility(View.VISIBLE);
    } else {
      recyclerView.setVisibility(View.VISIBLE);
      errorView.setVisibility(View.GONE);
    }
  }

  private void showRadioButtonDialog() {

    // custom dialog
    final Dialog dialog = new Dialog(FlowerHome.this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.sort_radio_dailog);
    RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
    final RadioButton rb1 = (RadioButton) dialog.findViewById(R.id.radioAtoZ);
    final RadioButton rb2 = (RadioButton) dialog.findViewById(R.id.radioZtoA);
    fabFilter.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.al_job_filter_apply_white));
    if(sortType == Sort.AtoZ ){
      rb1.setChecked(true);
    }
    else if ( sortType == Sort.ZtoA){
      rb2.setChecked(true);
    }

    rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(rb2.getId() == checkedId){
          Toast.makeText(FlowerHome.this, "Z to A is checked", Toast.LENGTH_SHORT).show();
          Collections.sort(flowerList,new sortZtoA());
          adapter.notifyDataSetChanged();
          sortType = Sort.ZtoA;
          dialog.cancel();
        }
        else{
          Toast.makeText(FlowerHome.this, "A to Z is checked", Toast.LENGTH_SHORT).show();
          Collections.sort(flowerList,new sortAtoZ());
          adapter.notifyDataSetChanged();
          sortType = Sort.AtoZ;
          dialog.cancel();
        }
      }
    });
    dialog.show();
  }

  public class sortAtoZ implements Comparator<Model>{

    @Override
    public int compare(Model lhs, Model rhs) {
      return lhs.getName().compareTo(rhs.getName());
    }
  }
  public class sortZtoA implements Comparator<Model>{
    @Override
    public int compare(Model lhs, Model rhs) {
      return -lhs.getName().compareTo(rhs.getName());
    }
  }
}
