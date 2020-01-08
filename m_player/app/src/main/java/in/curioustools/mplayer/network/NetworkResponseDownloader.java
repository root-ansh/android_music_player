package in.curioustools.mplayer.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class NetworkResponseDownloader {
    private static final String TAG ="RespDownloader>>";

    public static void loadAdDetails(String api, Context ctx, Response.Listener<String> responseListener){
        RequestQueue queue = Volley.newRequestQueue(ctx);
        Response.ErrorListener errorListener =
                error -> Log.e(TAG, "loadAdDetails:Error is: "+ error);

        StringRequest stringRequest =
                new StringRequest(
                        Request.Method.GET,
                        api,
                        responseListener ,
                        errorListener
                );

        queue.add(stringRequest);

    }

}
