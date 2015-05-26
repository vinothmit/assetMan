package com.htfs.assettracking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

 import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.htfs.intentservice.GetaddressService;
import com.htfs.utilpackage.CheckInternetConnection;
import com.htfs.utilpackage.Checkloactionprovider;
import com.htfs.utilpackage.DialogBoxDisplay;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AssetTracking extends Activity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Button assetSubmitBtn, getPictureBtn;
    private ImageButton scanBarcode, getCalender;
    private CheckBox needLocationCB;
    private EditText assetCodeTx, assetuserTx, assetdateTx, assetCommnetTx, assetName;
    private ImageView assetpic;


    private String locationAddress = null;
    private static final String TAG = "AT1";
    private static final int GET_IMG_REQ = 100;
    private double assetlatitute, assetlogitude;
    private Bitmap astImage;
    private String currentpicname;
    private File fimg, temppath;


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    private ResponseReceiver responseReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_tracking);

        buildGoogleApiClient();

        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        responseReceiver = new ResponseReceiver();
        registerReceiver(responseReceiver, filter);

        assetSubmitBtn = (Button) findViewById(R.id.astconfirm);
        assetSubmitBtn.setOnClickListener(confirmAsset);

        getPictureBtn = (Button) findViewById(R.id.astretakepic);
        getPictureBtn.setOnClickListener(getAssetImage);

        scanBarcode = (ImageButton) findViewById(R.id.astscanCode);
        scanBarcode.setOnClickListener(scanAssetsCode);

        getCalender = (ImageButton) findViewById(R.id.showCal);
        getCalender.setOnClickListener(getassetDate);

        needLocationCB = (CheckBox) findViewById(R.id.astgetLoction);
        needLocationCB.setOnCheckedChangeListener(checkforLocation);

        assetCodeTx = (EditText) findViewById(R.id.astCode);
        assetCodeTx.setEnabled(false);
        assetName = (EditText) findViewById(R.id.astname);
        assetName.setEnabled(false);

        assetuserTx = (EditText) findViewById(R.id.astusername);

        assetdateTx = (EditText) findViewById(R.id.asttakendate);
        assetdateTx.setEnabled(false);
        assetdateTx.setOnClickListener(getassetDate);
        assetCommnetTx = (EditText) findViewById(R.id.astcomment);
        assetpic = (ImageView) findViewById(R.id.astviewpic);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public static final void displaySnackbar(final Context context, final Activity activity) {
        Snackbar.with(context)
                .text("Check Internet Connection")
                .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                .actionLabel("Try Again")
                .actionListener(new ActionClickListener() {
                    @Override
                    public void onActionClicked(Snackbar snackbar) {
                        if (CheckInternetConnection.checkConnection(context)) {
                            snackbar.dismiss();
                        }
                    }
                })
                .swipeToDismiss(false)
                .show(activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!CheckInternetConnection.checkConnection(getApplicationContext())) {
            displaySnackbar(getApplicationContext(), this);
        }
        mGoogleApiClient.connect();

        if (!Checkloactionprovider.checklocationService(this))
            DialogBoxDisplay.showGps(this);
        Log.d(TAG, String.valueOf(mGoogleApiClient.isConnected()));
        Log.d(TAG, "ON Resume:" + "|| asset longitute:" + assetlatitute + " asset longitute:" + assetlogitude);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected())
            stopLocationUpdates();
    }

    private View.OnClickListener getAssetImage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            currentpicname = "ASSET_" + assetCodeTx.getText().toString() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
            temppath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Asset");
            if (!temppath.exists())
                temppath.mkdirs();
            fimg = new File(temppath, currentpicname);
            Log.i("Path", fimg.getAbsolutePath());
            if (fimg != null) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fimg));
                i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(i, GET_IMG_REQ);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_IMG_REQ) {
            if (resultCode == RESULT_OK) {
                if (fimg != null) {
                    astImage = encodeImgFile(fimg);
                    MediaStore.Images.Media.insertImage(getContentResolver(), astImage, currentpicname, assetCodeTx.getText().toString());
                    assetpic.setImageBitmap(astImage);
                }
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            Log.d("Scan Result", intentResult.toString());
            assetCodeTx.setText(intentResult.getContents());
        } else {
            //Failed Scan
        }

    }

    private Bitmap encodeImgFile(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inSampleSize = 4;
        options.inJustDecodeBounds = false;
        Log.i("In", "encodeImgFile Method");
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    private View.OnClickListener getassetDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(AssetTracking.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            assetdateTx.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    private CompoundButton.OnCheckedChangeListener checkforLocation = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Log.d(TAG, String.valueOf(b));
            if (!b) {
                Log.d(TAG, "Checked false");
                stopLocationUpdates();
                assetlatitute = 0.0;
                assetlogitude = 0.0;
            } else {
                Log.d(TAG, "Checked true");
                startLocationUpdates();
            }
        }
    };

    private View.OnClickListener scanAssetsCode = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            IntentIntegrator integrator = new IntentIntegrator(AssetTracking.this);
            integrator.initiateScan();
        }
    };

    private View.OnClickListener confirmAsset = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (astImage != null)
                uploadAssetimage(astImage);

            Log.d(TAG, "assetlatitute:" + assetlatitute + "long:" + assetlogitude);
            AlertDialog alertDialog = new AlertDialog.Builder(AssetTracking.this)
                    .setMessage("Lat:" + assetlatitute + "\nLog:" + assetlogitude + "\nAddress :" + locationAddress)
                    .setCancelable(true)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).create();
            alertDialog.show();
        }
    };

    private boolean uploadAssetimage(Bitmap _assetimage) {
        return false;
    }


    @Override
    public void onLocationChanged(Location location) {
        assetlatitute = location.getLatitude();
        assetlogitude = location.getLongitude();
        Log.d(TAG, ">>assetlatitute:" + assetlatitute + "long:" + assetlogitude);
        startAddressServiceIntent();
    }

    @Override
    public void onConnected(Bundle bundle) {
//        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation != null) {
//            assetlatitute = mLastLocation.getLatitude();
//            assetlogitude = mLastLocation.getLongitude();
//
//        }
        Log.d(TAG, "In Onconnected");
        createLocationRequest();
        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void startAddressServiceIntent() {
        Intent getAddrIntent = new Intent(this, GetaddressService.class);
        getAddrIntent.putExtra(GetaddressService.PRAM_LAT, assetlatitute);
        getAddrIntent.putExtra(GetaddressService.PRAM_LONG, assetlogitude);
        startService(getAddrIntent);
    }

    protected void startLocationUpdates() {
        Log.d(TAG, "On Start Location Update");
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_asset_tracking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.viewallasset) {
            Intent allassets = new Intent(this, AllAssetTracking.class);
            startActivity(allassets);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "On Destroy Called");
        this.unregisterReceiver(responseReceiver);
        super.onDestroy();
    }


    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP = "com.htfs.intent.action.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent) {

            locationAddress = intent.getStringExtra(GetaddressService.ADDR);
            if (locationAddress.equals("error")) {
                displaySnackbar(getApplicationContext(), AssetTracking.this);
            }
            Log.d("ADDRESS FROM INTENT", intent.getStringExtra(GetaddressService.ADDR));
        }
    }

}
