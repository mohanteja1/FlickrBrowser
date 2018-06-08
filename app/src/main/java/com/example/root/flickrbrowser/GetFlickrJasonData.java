package com.example.root.flickrbrowser;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetFlickrJasonData extends AsyncTask<String,Void,List<Photo>> implements GetRawData.OnDownloadComplete {


    private static final String TAG = "GetFlickrJasonData";
    private List<Photo> mPhotoList =new ArrayList<>();
    private String mBaseUrl;
    private boolean mMatchAll;
    private String mLanguage;
    private boolean runInSameThread = false;

    private final OnDataAvailable mCallback;
    interface OnDataAvailable{
        void onDataAvailable(List<Photo> photoList,DownloadStatus downloadStatus);

    }

    public GetFlickrJasonData(OnDataAvailable callback, String baseUrl, boolean matchAll, String language ) {
        mBaseUrl = baseUrl;
        mMatchAll = matchAll;
        mLanguage = language;
        mCallback = callback;
    }


    void executeOnSameThread(String searchCriteria){
        runInSameThread=true;
        Log.d(TAG, "executeOnSameThread: start");
        String destinationUri = createUri(searchCriteria,mLanguage,mMatchAll);

        GetRawData rawData=new GetRawData(this);
        rawData.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread: on callback");


    }


    @Override
    protected void onPostExecute(List<Photo> photoList) {

        if(mCallback!=null)
        {
            mCallback.onDataAvailable(mPhotoList,DownloadStatus.OK);
        }


    }

    @Override
    protected List<Photo> doInBackground(String... strings) {

        String destinationUri = createUri(strings[0],mLanguage,mMatchAll);

        GetRawData rawData=new GetRawData(this);
        rawData.runInSameThread(destinationUri);

        return mPhotoList;
    }

    private String createUri(String searchCriteria, String lang, boolean matchAll)
    {
        Log.d(TAG, "createUri: starts");

        return Uri.parse(mBaseUrl).buildUpon()
                .appendQueryParameter("tags",searchCriteria)
                .appendQueryParameter("lang",lang)
                .appendQueryParameter("tagmode",matchAll ? "ALL":"Any")
                .appendQueryParameter("format","json")
                .appendQueryParameter("nojsoncallback","1")
                 .build().toString();


    }






    @Override
    public void onDownloadComplete(String data, DownloadStatus downloadStatus) {

        if(data!=null)
        {
            try{

                JSONObject jsonObject= new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("items");

                for(int i=0;i<jsonArray.length();i++) {
                     JSONObject jsonObject1=jsonArray.getJSONObject(i);
                     String title =jsonObject1.getString("title");
                     String author=jsonObject1.getString("author");
                     String authorId=jsonObject1.getString("author_id");
                     String tags=jsonObject1.getString("tags");

                     JSONObject jsonMedia = jsonObject1.getJSONObject("media");


                     String photoUrl= jsonMedia.getString("m");
                     String link = photoUrl.replaceFirst("_m.","_b.");
                     Photo photoObject= new Photo(title,author,authorId,tags,photoUrl);

                     if(photoObject!=null) {
                         Log.d(TAG, "onDownloadComplete: photo object =" + photoObject.toString());

                         mPhotoList.add(photoObject);
                     }

                }


            }
            catch(JSONException e) {

               // e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: error processing json" + e.getMessage() );
                downloadStatus=DownloadStatus.FAILED_OR_EMPTY;

            }
            catch(Exception e)
            {
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: io error:" + e.getMessage() );
            }


            if(mCallback!=null&&runInSameThread)
            {
                mCallback.onDataAvailable(mPhotoList,downloadStatus);
            }

           

        }


        Log.d(TAG, "onDownloadComplete: completed");

        
    }
}
