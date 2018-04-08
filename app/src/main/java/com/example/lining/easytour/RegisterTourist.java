package com.example.lining.easytour;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by lining on 2018/3/24.
 */

public class RegisterTourist extends Activity {
    private EditText et_r_t_user_name;
    private EditText et_r_t_psw;
    private EditText et_r_t_conform_psw;
    private EditText et_r_t_phone;
    private Button btn_r_t_ok;
    private String username;
    private String psw;
    private String confirm_psw;
    private String tel;
//    /*POPWINDOW*/
//    private PopupWindow mPopWindow;
//    private RegisterTourist context;
//    /*PHOTO TEST*/
//    public static final int TAKE_PHOTO = 1;
//
//    public static final int CHOOSE_PHOTO = 2;
//
//    public static final int CROP_PHOTO = 3;
//    private  String cachPath;
//    private File cacheFile;
//    private  File cameraFile;
//    private Uri imageUri;
//    ImageView showImageView;
//    //裁剪后显示照片
//    private ImageView imageView;
//    //动态获取权限监听
//    private static PermissionListener mListener;
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_tourist);

        init();

        btn_r_t_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(RegisterTourist.this,"Coding",Toast.LENGTH_SHORT).show();
                username = et_r_t_user_name.getText().toString().trim();
                psw = et_r_t_psw.getText().toString().trim();
                confirm_psw = et_r_t_conform_psw.getText().toString().trim();
                tel = et_r_t_phone.getText().toString().trim();
                if(username.equals("")){
                    Toast.makeText(RegisterTourist.this,"用户名未输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(psw.equals("")){
                    Toast.makeText(RegisterTourist.this,"密码未输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(confirm_psw.equals("")){
                    Toast.makeText(RegisterTourist.this,"确认密码未输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(tel.equals("")){
                    Toast.makeText(RegisterTourist.this,"手机号码未输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                //测试时先注释
//                if(!isMobile(tel)){
//                    Toast.makeText(RegisterTourist.this,"手机号码格式错误",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                new TousirtRegister().execute();
            }
        });

    }
    /*
    when pushed the back button , go back
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    /*
    changed the layout delete the cancel button and rename the id of views
     */
    public void init(){
        et_r_t_user_name = findViewById(R.id.et_tourist_user_name);
        et_r_t_psw = findViewById(R.id.et_tourist__psw);
        et_r_t_conform_psw = findViewById(R.id.et_tourist_confirm_psw);
        et_r_t_phone = findViewById(R.id.et_tourist_tel);
        btn_r_t_ok = findViewById(R.id.btn_tourist_ok);

//        this.context = this;
//        showImageView = (ImageView) findViewById(R.id.register_tiv_usericon);
//
//        cachPath=getDiskCacheDir(this)+ "/crop_image.jpg";
//        cacheFile =getCacheFile(new File(getDiskCacheDir(this)),"crop_image.jpg");
//        imageView= (ImageView) findViewById(R.id.register_tiv_usericon);

    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

//    public void btnChangeTUserIcon(View view) {
//        showUploadAvatarDialog();
//    }
//
//    private void showUploadAvatarDialog() {
//        View view = getLayoutInflater().inflate(R.layout.popu_photo, null);
//        int screenWith = context.getWindowManager().getDefaultDisplay().getWidth();
//        int screenHeiht = context.getWindowManager().getDefaultDisplay().getHeight();
//        mPopWindow = new PopupWindow(view, screenWith, screenHeiht);
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
//        // 我觉得这里是API的一个bug
//        mPopWindow.setBackgroundDrawable(dw);
//        mPopWindow.setTouchable(true);
//        mPopWindow.setFocusable(true);
//        mPopWindow.showAtLocation(showImageView, Gravity.BOTTOM, 0, 0);
//        mPopWindow.setOutsideTouchable(true);
//        TextView takePhoto = (TextView) view.findViewById(R.id.tv_take_photo_popu);
//        TextView photoAlbum = (TextView) view.findViewById(R.id.tv_photo_album_popu);
//        TextView cancel = (TextView) view.findViewById(R.id.tv_photo_cancle_popu);
//
//
//        takePhoto.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (mPopWindow.isShowing()) {
//                    mPopWindow.dismiss();
//                    takePhotoForCamera();
//                }
//                return false;
//            }
//        });
//        photoAlbum.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (mPopWindow.isShowing()) {
//                    mPopWindow.dismiss();
//                    takePhotoForAlbum();
//                }
//                return false;
//            }
//        });
//
//        cancel.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (mPopWindow.isShowing()) {
//                    mPopWindow.dismiss();
//                }
//                return false;
//            }
//        });
//    }
//    private void takePhotoForAlbum() {
//
//        String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
//        requestRuntimePermission(permissions, new PermissionListener() {
//            @Override
//            public void onGranted() {
//                openAlbum();
//            }
//
//            @Override
//            public void onDenied(List<String> deniedPermission) {
//                //没有获取到权限，什么也不执行，看你心情
//            }
//        });
//    }
//    private void openAlbum() {
//        Intent intent = new Intent("android.intent.action.GET_CONTENT");
//        intent.setType("image/*");
//        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
//    }
//    private void takePhotoForCamera() {
//        String[] permissions={Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        requestRuntimePermission(permissions, new PermissionListener() {
//            @Override
//            public void onGranted() {
//                openCamera();
//            }
//
//            @Override
//            public void onDenied(List<String> deniedPermission) {
//                //有权限被拒绝，什么也不做好了，看你心情
//            }
//        });
//
//    }
//    private void openCamera() {
//
//        cameraFile = getCacheFile(new File(getDiskCacheDir(this)),"output_image.jpg");
//
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//            imageUri = Uri.fromFile(cameraFile);
//        } else {
//
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            imageUri = FileProvider.getUriForFile(RegisterTourist.this, "com.wing.phototest.fileprovider", cameraFile);
//
//        }
//        // 启动相机程序
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, TAKE_PHOTO);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_CANCELED) {
//            return;
//        }
//        switch (requestCode) {
//            case TAKE_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    try {
//
//                        // 将拍摄的照片显示出来
//
//                        startPhotoZoom(cameraFile,350);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
//
//            case CHOOSE_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    // 判断手机系统版本号
//                    if (Build.VERSION.SDK_INT >= 19) {
//                        // 4.4及以上系统使用这个方法处理图片
//                        handleImageOnKitKat(data);
//                    } else {
//                        // 4.4以下系统使用这个方法处理图片
//                        handleImageBeforeKitKat(data);
//                    }
//                }
//                break;
//
//            case CROP_PHOTO:
//                try {
//                    if (resultCode==RESULT_OK){
//                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(cachPath))));
//                        imageView.setImageBitmap(bitmap);
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//                break;
//        }
//    }
//    private  File getCacheFile(File parent, String child) {
//        // 创建File对象，用于存储拍照后的图片
//        File file = new File(parent, child);
//
//        if (file.exists()) {
//            file.delete();
//        }
//        try {
//            file.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return file;
//    }
//    @TargetApi(19)
//    private void handleImageOnKitKat(Intent data) {
//        Uri uri = data.getData();
//        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
//        String imagePath= uriToPath(uri);
////        displayImage(imagePath); // 根据图片路径显示图片
//
//        Log.i("TAG","file://"+imagePath+"选择图片的URI"+uri);
//        startPhotoZoom(new File(imagePath),350);
//    }
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private String uriToPath(Uri uri) {
//        String path=null;
//        if (DocumentsContract.isDocumentUri(this, uri)) {
//            // 如果是document类型的Uri，则通过document id处理
//            String docId = DocumentsContract.getDocumentId(uri);
//            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
//                String id = docId.split(":")[1]; // 解析出数字格式的id
//                String selection = MediaStore.Images.Media._ID + "=" + id;
//                path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
//            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
//                path = getImagePath(contentUri, null);
//            }
//        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            // 如果是content类型的Uri，则使用普通方式处理
//            path = getImagePath(uri, null);
//        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            // 如果是file类型的Uri，直接获取图片路径即可
//            path = uri.getPath();
//        }
//        return  path;
//    }
//    private void handleImageBeforeKitKat(Intent data) {
//        Uri uri = data.getData();
//        String imagePath = getImagePath(uri, null);
////        displayImage(imagePath);
//        Log.i("TAG","file://"+imagePath+"选择图片的URI"+uri);
//        startPhotoZoom(new File(imagePath),350);
//    }
//    private String getImagePath(Uri uri, String selection) {
//        String path = null;
//        // 通过Uri和selection来获取真实的图片路径
//        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            }
//            cursor.close();
//        }
//        return path;
//    }
//    /**
//     * 剪裁图片
//     */
//    private void startPhotoZoom(File file, int size) {
//        Log.i("TAG",getImageContentUri(this,file)+"裁剪照片的真实地址");
//        try {
//            Intent intent = new Intent("com.android.camera.action.CROP");
//            intent.setDataAndType(getImageContentUri(this,file), "image/*");//自己使用Content Uri替换File Uri
//            intent.putExtra("crop", "true");
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 1);
//            intent.putExtra("outputX", 180);
//            intent.putExtra("outputY", 180);
//            intent.putExtra("scale", true);
//            intent.putExtra("return-data", false);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(cacheFile));//定义输出的File Uri
//            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//            intent.putExtra("noFaceDetection", true);
//            startActivityForResult(intent, CROP_PHOTO);
//        } catch (ActivityNotFoundException e) {
//            String errorMessage = "Your device doesn't support the crop action!";
//            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public String getDiskCacheDir(Context context) {
//        String cachePath = null;
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
//                || !Environment.isExternalStorageRemovable()) {
//            cachePath = context.getExternalCacheDir().getPath();
//        } else {
//            cachePath = context.getCacheDir().getPath();
//        }
//        return cachePath;
//    }
//    public static Uri getImageContentUri(Context context, File imageFile) {
//        String filePath = imageFile.getAbsolutePath();
//        Cursor cursor = context.getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                new String[] { MediaStore.Images.Media._ID },
//                MediaStore.Images.Media.DATA + "=? ",
//                new String[] { filePath }, null);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            int id = cursor.getInt(cursor
//                    .getColumnIndex(MediaStore.MediaColumns._ID));
//            Uri baseUri = Uri.parse("content://media/external/images/media");
//            return Uri.withAppendedPath(baseUri, "" + id);
//        } else {
//            if (imageFile.exists()) {
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.Images.Media.DATA, filePath);
//                return context.getContentResolver().insert(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            } else {
//                return null;
//            }
//        }
//    }
//    public void requestRuntimePermission(String[] permissions, PermissionListener listener) {
//
//        mListener = listener;
//        List<String> permissionList = new ArrayList<>();
//        for (String permission : permissions) {
//            if (ContextCompat.checkSelfPermission(RegisterTourist.this, permission) != PackageManager.PERMISSION_GRANTED) {
//                permissionList.add(permission);
//            }
//        }
//        if (!permissionList.isEmpty()) {
//            ActivityCompat.requestPermissions(RegisterTourist.this, permissionList.toArray(new String[permissionList.size()]), 1);
//        } else {
//            mListener.onGranted();
//        }
//    }
//    @TargetApi(23)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 1:
//                if (grantResults.length > 0) {
//                    List<String> deniedPermissions = new ArrayList<>();
//                    for (int i = 0; i < grantResults.length; i++) {
//                        int grantResult = grantResults[i];
//                        String permission = permissions[i];
//                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
//                            deniedPermissions.add(permission);
//                        }
//                    }
//                    if (deniedPermissions.isEmpty()) {
//                        mListener.onGranted();
//                    } else {
//                        mListener.onDenied(deniedPermissions);
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//    }
//    public interface PermissionListener {
//        /**
//         * 成功获取权限
//         */
//        void onGranted();
//
//        /**
//         * 为获取权限
//         * @param deniedPermission
//         */
//        void onDenied(List<String> deniedPermission);
//
//    }
//
//
//    /**
// *更改头像
// */



    private class TousirtRegister extends AsyncTask<String,Void,Integer>{
        @Override
        protected Integer doInBackground(String... strings) {
            String uri = "http://118.89.18.136/EasyTour-bk/touristregister.php/";
            String result = null;
            HttpPost httpRequest = new HttpPost(uri);
            List params = new ArrayList();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", psw));
            params.add(new BasicNameValuePair("telephone",tel));
            try {
                httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                // 服务器发给客户端的相应，有一个相应码： 相应码为200，正常； 相应码为404，客户端错误； 相应码为505，服务器端错误。
                Log.i("getStatusCode():","------>"+httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    result = EntityUtils.toString(httpResponse.getEntity());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("result:", "------------>"+result);
            if (result.equals("register successful"))
                return 1;
            if(result.equals("username exists"))
                return  2;
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer == 0){
                Toast.makeText(RegisterTourist.this,"注册失败,服务器错误",Toast.LENGTH_SHORT).show();
            }
            if(integer == 1){
                Toast.makeText(RegisterTourist.this,"注册成功",Toast.LENGTH_LONG).show();
                finish();
            }
            if(integer == 2){
                Toast.makeText(RegisterTourist.this,"用户名已存在",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
