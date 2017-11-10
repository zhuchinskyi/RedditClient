package sample.reddit;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static sample.reddit.util.Constants.FILE_NAME_KEY;
import static sample.reddit.util.Constants.IMG_DST_FOLDER_NAME;
import static sample.reddit.util.Constants.IMG_MIME_TYPE;
import static sample.reddit.util.Constants.URL_KEY;


public class PreviewActivity extends Activity {
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;

    String url;
    String fileName;
    String imagePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        url = getIntent().getStringExtra(URL_KEY);
        fileName = getIntent().getStringExtra(FILE_NAME_KEY);

        Glide.with(this)
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivPhoto);
    }

    @OnClick(R.id.ivSave)
    void save() {
        if (shouldAskPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            saveImage(fileName, IMG_DST_FOLDER_NAME);
        }
    }

    private boolean shouldAskPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage(fileName, IMG_DST_FOLDER_NAME);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.title_permission_dialog);
                    builder.setMessage(getString(R.string.permission_explonation));
                    builder.setPositiveButton(R.string.grant, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(PreviewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), R.string.unable_get_permission, Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private void saveImage(String name, String destDir) {
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        Toast.makeText(this, R.string.download_msg, Toast.LENGTH_SHORT).show();
        File direct =
                new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath() + File.pathSeparator + destDir + File.pathSeparator);

        imagePath = direct.getPath() + name;
        if (!direct.exists()) {
            direct.mkdir();
        }

        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setTitle(name)
                .setMimeType(IMG_MIME_TYPE)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                        File.separator + destDir + File.separator + name);

        dm.enqueue(request);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(PreviewActivity.this, String.format("%s: %s", getString(R.string.saved), imagePath), Toast.LENGTH_SHORT).show();
            unregisterReceiver(this);
        }
    };
}

