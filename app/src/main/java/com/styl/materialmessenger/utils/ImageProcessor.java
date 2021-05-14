package com.styl.materialmessenger.utils;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.styl.materialmessenger.BuildConfig;
import com.styl.materialmessenger.R;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ChomChom on 03-Nov-17
 */

public class ImageProcessor {

    public static final int PERMISSION_CAMERA_REQUEST_CODE = 100;
    public static final int PERMISSION_READ_REQUEST_CODE = 101;
    public static final int RESULT_TAKE_PHOTO = 0;
    public static final int RESULT_CHOOSE_IMAGE = 1;

    public static final int IMAGE_SIZE = 500;

    private static final int RESULT_CROP = 2;
    private static final String CROP_INTENT = "com.android.camera.action.CROP";
    private static String pathFile = "";

    public static String convertBitmapToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, arrayOutputStream);
            byte[] b = arrayOutputStream.toByteArray();
            return "data:image/png;base64," + Base64.encodeToString(b, Base64.DEFAULT);
        }
        return null;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        try {
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception ignored) {
        }
        return bitmap;
    }

    private static Bitmap cropImage(Bitmap bitmap) {
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            int size = Math.min(IMAGE_SIZE, bitmap.getHeight());
            return Bitmap.createBitmap(bitmap, bitmap.getWidth() / 2 - size / 2, 0, size, size);
        } else {
            int size = Math.min(IMAGE_SIZE, bitmap.getWidth());
            return Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() / 2 - size / 2, size, size);
        }
    }


    private static void performCrop(Uri picUri, Fragment fragment) {
        Intent cropIntent = new Intent(CROP_INTENT);
        cropIntent.setDataAndType(picUri, "image/*");

        List<ResolveInfo> list = fragment.getActivity().getPackageManager().queryIntentActivities(cropIntent, 0);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(fragment.getActivity(), fragment.getActivity().getString(R.string.dont_find_image), Toast.LENGTH_SHORT).show();
        } else {
            try {

                cropIntent.putExtra("crop", "true");
                cropIntent.putExtra("aspectX", 10);
                cropIntent.putExtra("aspectY", 9);
                cropIntent.putExtra("outputX", 500);
                cropIntent.putExtra("outputY", 500);
                cropIntent.putExtra("return-data", true);
                fragment.startActivityForResult(cropIntent, 2);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(fragment.getActivity(), fragment.getActivity().getString(R.string.not_support_crop), Toast.LENGTH_SHORT).show();
            }

        }
    }

    public static Uri requestPermissionsResult(int requestCode, int grantResults, androidx.fragment.app.Fragment fragment) {
        Uri outputImgUri = null;
        if (requestCode == PERMISSION_READ_REQUEST_CODE) {

            if (grantResults == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                fragment.startActivityForResult(intent, RESULT_CHOOSE_IMAGE);

            } else {
                Toast.makeText(fragment.getActivity(), fragment.getActivity().getResources().getString(R.string.deny_read_storage), Toast.LENGTH_SHORT).show();

            }

        } else if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults == PackageManager.PERMISSION_GRANTED) {

                outputImgUri = ImageProcessor.createImageUri(fragment.getActivity());

                if (outputImgUri != null) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputImgUri);
                    fragment.startActivityForResult(intent, RESULT_TAKE_PHOTO);
                }

            } else {

                Toast.makeText(fragment.getActivity(), fragment.getActivity().getResources().getString(R.string.deny_camera), Toast.LENGTH_SHORT).show();

            }
        }
        return outputImgUri;
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri)
            throws IOException {
        final InputStream imageStream = context.getContentResolver()
                .openInputStream(uri);
        try {
            return BitmapFactory.decodeStream(imageStream);
        } finally {
            imageStream.close();
        }
    }

    private static Uri startCrop(Intent result, Context context, Uri outputImgUri, androidx.fragment.app.Fragment fragment) {
        String destinationFileName = "SampleCropImage.jpg";
        Uri uri = result != null ? result.getData() : outputImgUri;
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(context.getCacheDir(), destinationFileName)));

        uCrop.withMaxResultSize(500, 500);
        uCrop = uCrop.withAspectRatio(10, 10);
        uCrop = advancedConfig(uCrop, context);

        uCrop.start(context, fragment);
        return uri;
    }

    private static void startCrop(Context context, File tempFile, androidx.fragment.app.Fragment fragment) {
        String destinationFileName = "SampleCropImage.jpg";
        UCrop uCrop = UCrop.of(Uri.fromFile(tempFile), Uri.fromFile(new File(context.getCacheDir(), destinationFileName)));

        uCrop.withMaxResultSize(500, 500);
        uCrop = uCrop.withAspectRatio(10, 10);
        uCrop = advancedConfig(uCrop, context);

        uCrop.start(context, fragment);
    }

    private static UCrop advancedConfig(UCrop uCrop, Context context) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);
        options.setCircleDimmedLayer(true);

        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(false);
        options.setShowCropGrid(false);
        options.setLogoColor(Color.WHITE);
        options.setStatusBarColor(Color.TRANSPARENT);
        options.setToolbarColor(context.getResources().getColor(R.color.line_gray));
        options.setToolbarWidgetColor(Color.WHITE);

        return uCrop.withOptions(options);
    }

    public static Uri handleCropResult(Intent result, androidx.fragment.app.Fragment fragment, ImageView imgAvatar) {
        Bitmap bitmap = null;
        try {
            Uri resultUri = UCrop.getOutput(result);
            bitmap = ImageProcessor.getBitmapFromUri(fragment.getActivity(), resultUri);
            return resultUri;
        } catch (IOException ignored) {
        }
        return null;
    }

    public static Uri activityImageResult(int resultCode, int requestCode, Intent data, final androidx.fragment.app.Fragment fragment, ImageView imgAvatar, Uri outputImgUri) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UCrop.REQUEST_CROP:
                    return handleCropResult(data, fragment, imgAvatar);
                case RESULT_TAKE_PHOTO:
                    return startCrop(data, fragment.requireActivity(), outputImgUri, fragment);
                default:
                    checkStartCrop(data, fragment, outputImgUri);
                    break;
            }
        }
        return null;
    }

    private static void checkStartCrop(Intent data, androidx.fragment.app.Fragment fragment, Uri outputImgUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Bitmap bitmap = null;
            if (data != null && data.getData() != null && fragment.getActivity() != null) {
                bitmap = getBitmap(fragment.getActivity(), data.getData());
            }

            if (bitmap != null) {
                File selectedImgFile = generateTempFile(fragment.getActivity());
                if (selectedImgFile != null) {
                    boolean isSuccess = convertBitmapToFile(selectedImgFile, bitmap);
                    if (isSuccess) {
                        startCrop(fragment.getActivity(), selectedImgFile, fragment);
                    }
                }
            }
        } else {
            startCrop(data, fragment.getActivity(), outputImgUri, fragment);
        }
    }

    private static File generateTempFile(Context context) {
        File pictureSaveFolderPath = context.getExternalCacheDir();
        String imageFileName = "tempOutputImage" + ".jpg";
        File outputImageFile = new File(pictureSaveFolderPath, imageFileName);

        boolean isSuccess = false;
        isSuccess = outputImageFile.exists();
        if (isSuccess) {
            isSuccess = outputImageFile.delete();
            try {
                if (isSuccess) {
                    isSuccess = outputImageFile.createNewFile();
                }
            } catch (IOException ignored) {
            }
        } else {
            try {
                isSuccess = outputImageFile.createNewFile();
            } catch (IOException ignored) {
            }
        }

        if (isSuccess) {
            return outputImageFile;
        }
        return null;
    }

    private static Bitmap getBitmap(Context context, Uri imageUri) {
        Bitmap bitmap = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                bitmap = ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                                context.getContentResolver(),
                                imageUri));

            } else {
                InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
                if (inputStream != null) {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }
            }
        } catch (IOException ignored) {
        }

        return bitmap;
    }

    private static boolean convertBitmapToFile(File destinationFile, Bitmap bitmap) {
        FileOutputStream fos = null;
        boolean isSuccess = false;
        try {
            isSuccess = destinationFile.exists();
            if (isSuccess) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] bitmapData = bos.toByteArray();
                fos = new FileOutputStream(destinationFile);
                fos.write(bitmapData);
            }
        } catch (IOException ignored) {
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                } catch (IOException ignored) {
                }

                try {
                    fos.close();
                } catch (IOException ignored) {
                }
            }
        }

        return isSuccess;
    }

    public static Uri createImageUri(Context context) {
        Uri uri = null;
        File pictureSaveFolderPath = context.getExternalCacheDir();

        String imageFileName = "outputImage" + ".jpg";

        File outputImageFile = new File(pictureSaveFolderPath, imageFileName);

        if (outputImageFile.exists()) {
            if (!outputImageFile.delete()) {

            }
        }

        try {
            if (!outputImageFile.createNewFile()) {

            }
        } catch (IOException ignored) {
        }

        pathFile = outputImageFile.getPath();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID, outputImageFile);
        } else {
            uri = Uri.fromFile(outputImageFile);
        }
        return uri;
    }

    public static String getFileExtension(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
