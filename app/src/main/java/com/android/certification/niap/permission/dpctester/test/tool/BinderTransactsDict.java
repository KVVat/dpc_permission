package com.android.certification.niap.permission.dpctester.test.tool;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.android.certification.niap.permission.dpctester.test.log.StaticLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BinderTransactsDict {
    static Map<String,String> serviceNameSynonyms = new HashMap<>();

    static Map<String, Map<String,Integer>> indexDb = new HashMap<>();

    Context mContext;
    private static BinderTransactsDict instance = null;
    private BinderTransactsDict(){

    }
    public static BinderTransactsDict getInstance(){
        if(instance == null){
            // block the multiple access from multiple thread
            synchronized (BinderTransactsDict.class) {
                if(instance == null){
                    instance = new BinderTransactsDict();
                }
            }
        }
        return instance;
    }

    private void build(BinderTransactsDict.Builder builder) {
        this.mContext = builder.mContext;
        //Initialize Database According to the Database File
        try {
            AssetManager am = mContext.getResources().getAssets();
            //Change suffix depends on system version
            InputStream is = am.open("binderdb-35.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String allText = br.lines().collect(Collectors.joining());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(allText);

            JsonNode services_= root.get("services");
            JsonNode methods_ = root.get("methods");

            serviceNameSynonyms = mapper.readValue(services_.toString(),serviceNameSynonyms.getClass());
            indexDb = mapper.readValue(methods_.toString(),indexDb.getClass());
            /*for(String key : serviceNameSynonyms.keySet()){
                String actual = serviceNameSynonyms.get(key);
                if(!actual.equals(key)){
                    StaticLogger.info("public static final String "+key+" = \""+actual+"\";");
                }

            }*/
            //Log.d("tag",indexDb.toString());
            //Log.d("tag",serviceNameSynonyms.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public static class Builder {
        private final Context mContext; // Mandatory
        //private Builder(){}
        public Builder(Context context) {
            this.mContext = context;
        }
        public void build(){
            BinderTransactsDict.getInstance().build(this);
        }
    }

    public int getTransactId(String descriptor, String methodName){
        return indexDb.get(descriptor).get(methodName);
    }
}
