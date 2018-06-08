package com.example.root.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus{IDLE,PROCESSING,NOT_INITIALISED,FAILED_OR_EMPTY,OK}

class GetRawData extends AsyncTask<String,Void,String>{
    private static final String TAG = "GetRawData";

    public DownloadStatus mdownloadStatus;
    private final OnDownloadComplete mcallBack;

    public GetRawData(OnDownloadComplete callback) {
        this.mdownloadStatus = DownloadStatus.IDLE;
        this.mcallBack=callback;

    }

    interface OnDownloadComplete{
        void onDownloadComplete(String data,DownloadStatus downloadStatus);

    }


    public  void runInSameThread(String s){
        Log.d(TAG, "runInSameThread: start");
        onPostExecute(doInBackground(s));
        Log.d(TAG, "runInSameThread: end");



    }


    @Override
    protected void onPostExecute(String s) {
       // Log.d(TAG, "onPostExecute: parameter passed:" + s);
        if(mcallBack!=null)
        {
            mcallBack.onDownloadComplete(s,mdownloadStatus);
            Log.d(TAG, "onPostExecute: download status =" + mdownloadStatus);
            
        }

        Log.d(TAG, "onPostExecute: return ");
    }


    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader =null;

         if(strings==null)
         {

             this.mdownloadStatus=DownloadStatus.NOT_INITIALISED;
             return null;
         }

        Log.d(TAG, "get raw data ====  doInBackground: url recieved======" + strings[0]);



         try{
             URL url = new URL(strings[0]);
             httpURLConnection = (HttpURLConnection) url.openConnection();
             reader= new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
             int responseCode = httpURLConnection.getResponseCode();
             Log.d(TAG, "doInBackground: responsecode"+ responseCode);

             StringBuilder rawData = new StringBuilder();
             String tempLine;


             while(null!=(tempLine=reader.readLine())){
                 rawData.append(tempLine).append("\n");

             }


             this.mdownloadStatus=DownloadStatus.OK;

             return rawData.toString();


         }
         catch(MalformedURLException e){
             Log.e(TAG, "doInBackground: error url" + e.getMessage() );
         }
         catch(IOException e){
             Log.e(TAG, "doInBackground: error io" + e.getMessage() );

         }
         catch(SecurityException e)
         {
             Log.e(TAG, "doInBackground: error security:" + e.getMessage() );
         }
         finally {
             if(reader!=null)
             try {
                 reader.close();
             }
             catch(IOException e)
             {
                 Log.e(TAG, "doInBackground: reader error :" + e.getMessage() );
             }
             if(httpURLConnection != null)
             httpURLConnection.disconnect();
         }







         this.mdownloadStatus = DownloadStatus.FAILED_OR_EMPTY;

         return null;
    }
}
