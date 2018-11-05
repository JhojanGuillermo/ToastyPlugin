package com.stanleyidesis.cordova.plugin;
// The native Toast API
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.Toast;
// Cordova-required packages
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jnbis.api.Jnbis;

import java.io.File;
import java.util.Base64;



public class ToastyPlugin extends CordovaPlugin {
    private static final String DURATION_LONG = "long";
    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public boolean execute(String action, JSONArray args,
                           final CallbackContext callbackContext) {
        // Verify that the user sent a 'show' action
        if (!action.equals("show")) {
            callbackContext.error("\"" + action + "\" is not a recognized action.");
            return false;
        }
        String message;
        String duration;
        try {
            JSONObject options = args.getJSONObject(0);
            message = options.getString("message");
            duration = options.getString("duration");

            byte[] filedata=Base64.getDecoder().decode(message);
            File gifBytes = new File(Jnbis.wsq()
                    .decode(filedata)
                    .toPng()
                    .toString());;
            message = "el mensaje es :"+ gifBytes;


        } catch (JSONException e) {
            callbackContext.error("Error encountered: " + e.getMessage());
            return false;
        }
        // Create the toast
        Toast toast = Toast.makeText(cordova.getActivity(), message,
                DURATION_LONG.equals(duration) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        // Display toast
        toast.show();
        // Send a positive result to the callbackContext
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
        callbackContext.sendPluginResult(pluginResult);
        return true;
    }
}
