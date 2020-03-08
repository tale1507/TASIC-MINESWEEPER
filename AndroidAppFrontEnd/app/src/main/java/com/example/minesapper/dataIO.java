package com.example.minesapper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class dataIO {
    final RequestQueue queue;
public String resultProvera="";

    API APIlikovi=new API();
      public  Context ctx;
        dataIO(Context ctx){
            this.ctx=ctx;
            queue = Volley.newRequestQueue(ctx);
        }







        public void upisReg(HashMap data, final TextView obavestenje){
            JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, APIlikovi.local+APIlikovi.upisKorisnika, new JSONObject(data),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {JSONArray jsonArray=response.getJSONArray("rez");

                                JSONObject clan= jsonArray.getJSONObject(0);

                                obavestenje.setText(clan.getString("status"));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }},
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
                //here I want to post data to sever
            };
            queue.add(jsonobj);



        }

        public void insertTime(HashMap data,final ServerCallBack callback){
            String url=APIlikovi.insertTime;

            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {




                            callback.onSuccess(response);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {

            };

            queue.add(jsObjRequest);


        }

        public void proveraKorisnika(String id,String provera,final ServerCallBack callback) {

            String url="";
            if(provera.equals("username")) {
                Log.d("pratim","uso i ovde");
                 url = APIlikovi.local + APIlikovi.proveraPostojanjaKorisnika + id;
            }else if(provera.equals("email")){
                 url = APIlikovi.local + APIlikovi.proveraPostojanjaKorisnika+"email/" + id;

            }




            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {




                            callback.onSuccess(response);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {

            };

            queue.add(jsObjRequest);


        }
        public void rangiranje(String user,final ServerCallBack callback){
            String url = APIlikovi.rank;
            HashMap data = new HashMap();
            data.put("username",user);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            callback.onSuccess(response);




                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {

            };

            queue.add(jsObjRequest);

        }

        public void PrijaviSe(String user,String pass,final ServerCallBack callback){
            final HashMap data=new HashMap();

            data.put("username",user);
            data.put("lozinka",pass);
            String url=APIlikovi.local+APIlikovi.logIn;
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                        callback.onSuccess(response);




                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {

            };

            queue.add(jsObjRequest);

        }


}
