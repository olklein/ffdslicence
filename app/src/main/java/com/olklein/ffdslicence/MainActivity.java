package com.olklein.ffdslicence;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.graphics.Bitmap.createBitmap;

/**
 * Created by olklein on 06/07/2017.
 *
 *
 *    This program is free software: you can redistribute it and/or  modify
 *    it under the terms of the GNU Affero General Public License, version 3,
 *    as published by the Free Software Foundation.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *    As a special exception, the copyright holders give permission to link the
 *    code of portions of this program with the OpenSSL library under certain
 *    conditions as described in each individual source file and distribute
 *    linked combinations including the program with the OpenSSL library. You
 *    must comply with the GNU Affero General Public License in all respects
 *    for all of the code used other than as permitted herein. If you modify
 *    file(s) with this exception, you may extend this exception to your
 *    version of the file(s), but you are not obligated to do so. If you do not
 *    wish to do so, delete this exception statement from your version. If you
 *    delete this exception statement from all source files in the program,
 *    then also delete it in the license file.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static Uri LicenceURI = null;
    private static final int MYWRITEREQUEST  = 224;
    private static int maxSize=2048;
    private static  int nbLicences;
    private static final int IMPORT_REQUEST   = 202;
    private static final int IMPORT_WDSF_REQUEST   = 302;
    private static final int SETTINGS_REQUEST = 402;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Activity activity = this;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MYWRITEREQUEST);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String nbLicencesString = settings.getString("nb_licences","2");
        nbLicences = Integer.parseInt(nbLicencesString);

        // Get screen size to guess the best(max) bitmap size
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        maxSize = Math.max(metrics.heightPixels,metrics.widthPixels);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            openQuitDialog();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        View rootView =null;
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Context context = getContext();
            rootView = inflater.inflate(R.layout.content_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.textView2);
            int page = getArguments().getInt(ARG_SECTION_NUMBER);

            textView.setText(getString(R.string.page_text, page));

            ImageView imv = (ImageView) rootView.findViewById(R.id.imageView2);
            if ((LicenceURI=ReloadPage(context,page)) != null) {
                imv.setImageURI(LicenceURI);
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show total pages.
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String nbLicencesString = settings.getString("nb_licences","2");
            nbLicences = Integer.parseInt(nbLicencesString);
            return nbLicences;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Resources resource = getApplicationContext().getResources();

        if (id == R.id.nav_manage) {
            Intent si = new Intent(Intent.ACTION_GET_CONTENT);
            si.setType("*/*");
            startActivityForResult(si, IMPORT_REQUEST);

        } else if (id == R.id.nav_send) {
            Intent email = new Intent(Intent.ACTION_SEND);
            int page = (mViewPager.getCurrentItem()+1);
            email.putExtra(Intent.EXTRA_SUBJECT, resource.getString(R.string.mail_subjet,page));
            email.putExtra(Intent.EXTRA_TEXT, resource.getString(R.string.mail_text));
            email.setType("message/rfc822");
            LicenceURI = ReloadPage(getApplicationContext(),page);
            if (LicenceURI != null) {
                email.putExtra(Intent.EXTRA_STREAM, LicenceURI);
            }
            startActivity(Intent.createChooser(email, resource.getString(R.string.Send)));
        }else if(id == R.id.nav_download){
            if (!isNetworkAvailable()){
                Toast toast = Toast.makeText(getApplicationContext(), resource.getString(R.string.No_network), Toast.LENGTH_LONG);
                toast.show();
            }else {
                Intent webIntent = new Intent(this, FFDSWeb.class);
                webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(webIntent);
            }
        }if (id == R.id.action_settings) {
            Intent si = new Intent(this, SettingsActivity.class);

            startActivityForResult(si, SETTINGS_REQUEST);
        } else if (id == R.id.import_wdsf) {
            Intent si = new Intent(Intent.ACTION_GET_CONTENT);
            si.setType("*/*");
            startActivityForResult(si, IMPORT_WDSF_REQUEST);
        }else if (id == R.id.nav_Licence_Logiciel) {
            AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
            quitDialog.setTitle(R.string.action_Licence_Logiciel);
            quitDialog.setMessage(R.string.action_Licence_Logiciel_Info);

            quitDialog.setPositiveButton(R.string.OK, new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            quitDialog.setIcon(R.mipmap.ic_launcher_round);
            quitDialog.show();




        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== IMPORT_REQUEST) {
            if (resultCode == RESULT_OK) {

                Uri uri = data.getData();
                InputStream is;
                String filename = "";
                int page = mViewPager.getCurrentItem() + 1;
                try {
                    is = getContentResolver().openInputStream(uri);
                    Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

                    returnCursor.moveToFirst();
                    filename = returnCursor.getString(nameIndex);
                    returnCursor.close();
                    LicenceURI = convert(this, is, filename, page,false);
                } catch (IOException | DocumentException e) {
                    e.printStackTrace();
                }

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor edit = settings.edit();
                edit.putString("licence" + page, filename);
                edit.putString("licence" + page + "Path", LicenceURI.getPath());
                edit.apply();
                recreate();
            }
        }
        if (requestCode== IMPORT_WDSF_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                InputStream is;
                String filename = "";
                int page = mViewPager.getCurrentItem() + 1;
                try {
                    is = getContentResolver().openInputStream(uri);
                    Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

                    returnCursor.moveToFirst();
                    filename = returnCursor.getString(nameIndex);
                    returnCursor.close();
                    LicenceURI = convert(this, is, filename, page,true);
                } catch (IOException | DocumentException e) {
                    e.printStackTrace();
                }

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor edit = settings.edit();
                edit.putString("licence" + page, filename);
                edit.putString("licence" + page + "Path", LicenceURI.getPath());
                edit.apply();
                recreate();
            }
        }
        if (requestCode== SETTINGS_REQUEST) {
            mViewPager.getAdapter().notifyDataSetChanged();
        }
    }

    private static PdfRenderer mPdfRenderer;


    /**
     * {@link android.graphics.pdf.PdfRenderer} to render the PDF.
     */


    /**
     * Page that is currently shown on the screen.
     */
    private static PdfRenderer.Page mCurrentPage;


    private static Uri convert(Context context, InputStream asset, String filename, int page, boolean isWDSF) throws IOException, DocumentException {
        if (!filename.contains(".pdf")) return null;
        File file = new File(context.getCacheDir(), filename);

            // Since PdfRenderer cannot handle the compressed asset file directly, we copy it into
            // the cache directory.
            FileOutputStream output = new FileOutputStream(file);
            final byte[] buffer = new byte[1024];
            int size;
            while ((size = asset.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }

            asset.close();
            output.close();

        File file2 = new File(context.getExternalFilesDir("/Download"), filename);
        String outputFileName= file2.getAbsolutePath().replace(".pdf","_extract.pdf");

        clipPDF.clip(file.getAbsolutePath(), outputFileName,isWDSF);
        File fileIn = new File(outputFileName);
        ParcelFileDescriptor mFileDescriptor = ParcelFileDescriptor.open(fileIn, ParcelFileDescriptor.MODE_READ_ONLY);
        mPdfRenderer = new PdfRenderer(mFileDescriptor);
        return CreatePageImageExtract(context, page,isWDSF);

//        if (!isWDSF){
//            // Without iText
//            ParcelFileDescriptor mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
//            // This is the PdfRenderer we use to render the PDF.
//            mPdfRenderer = new PdfRenderer(mFileDescriptor);
//            return CreatePageImage(context,page);
//        }else {
//            // Without iText
//            ParcelFileDescriptor mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
//            // This is the PdfRenderer we use to render the PDF.
//            mPdfRenderer = new PdfRenderer(mFileDescriptor);
//            return CreatePageImageWDSF(context,page);
//        }
    }


    /**
     * Closes the {@link android.graphics.pdf.PdfRenderer} and related resources.
     *
     * @throws java.io.IOException When the PDF file cannot be closed.
     */
    private static void closeRenderer()  {
        if (null != mCurrentPage) {
            mCurrentPage.close();
            mCurrentPage = null;
        }
        mPdfRenderer.close();
    }

    private static Uri CreatePageImageWDSF(Context context, int page) {
        if (mPdfRenderer == null) {
            return null;
        }
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        mCurrentPage = mPdfRenderer.openPage(0);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        float wor = Float.parseFloat(settings.getString("Licence_wdsf_ho", "0.058"));
        float hor = Float.parseFloat(settings.getString("Licence_wdsf_vo", "0.037"));
        float hr = Float.parseFloat(settings.getString("Licence_wdsf_hauteur","0.185"));
        float wr = Float.parseFloat(settings.getString("Licence_wdsf_largeur","0.79"));
        int ir = Integer.parseInt(settings.getString("image_ratio","0"));

        int maxBitMapSize = 1024*ir;
        if (ir==0){
            if (maxSize>=2048) maxBitMapSize= 4096;
            else if (maxSize>=1024) maxBitMapSize= 2048;
        }

        float ratioV = (float) Math.ceil(maxBitMapSize / mCurrentPage.getHeight());

        float w;
        float h = Math.min(maxBitMapSize,mCurrentPage.getHeight() * ratioV);
        w = (float) (h/Math.sqrt(2));
        Bitmap bitmap = createBitmap((int) w, (int) h, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(0xffFFFFFF);

        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        closeRenderer();

        String path = settings.getString("licence" + page + "Path", "______");
        if (!path.equals("______")) {
            Uri previousUri = Uri.parse("content://media" + path);
            context.getContentResolver().delete(previousUri, null, null);
        }
        //
        bitmap = createBitmap(bitmap, (int) (w * wor), (int) (h * hor), (int) (w * wr), (int) (h * hr));

        path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "WDSF IDCard"+page, "licence"+page);
        return Uri.parse(path);
    }


    private static Uri CreatePageImageExtract(Context context, int page, boolean isWDSF) {
        if (mPdfRenderer == null) {
            return null;
        }
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        mCurrentPage = mPdfRenderer.openPage(0);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        int ir = Integer.parseInt(settings.getString("image_ratio","0"));

        int maxBitMapSize = 1024*ir;
        if (ir==0){
            if (maxSize>=2048) maxBitMapSize= 4096;
            else if (maxSize>=1024) maxBitMapSize= 2048;
        }

        float w= maxBitMapSize;

        float h;
        h = (float) (w*(float)mCurrentPage.getHeight()/(float)mCurrentPage.getWidth());
        Bitmap bitmap = createBitmap((int) w, (int) h, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(0xffFFFFFF);

        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        closeRenderer();

        String path = settings.getString("licence" + page + "Path", "______");
        if (!path.equals("______")) {
            Uri previousUri = Uri.parse("content://media" + path);
            try {
                context.getContentResolver().delete(previousUri, null, null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        if (isWDSF){
            path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "WDSF IDCard"+page, "licence"+page);
        }else{
            path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Licence FFD"+page, "Licence"+page);
        }
        return Uri.parse(path);
    }

    private static Uri ReloadPage(Context context, int page) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String path = settings.getString("licence" + page + "Path", "______");
        return Uri.parse("content://media" + path);
    }

    private static Uri CreatePageImage(Context context, int page) {
        if (mPdfRenderer == null) {
            return null;
        }
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        mCurrentPage = mPdfRenderer.openPage(0);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        float wor = Float.parseFloat(settings.getString("Licence_ho", "0.121"));
        float hor = Float.parseFloat(settings.getString("Licence_vo", "0.614"));
        float hr = Float.parseFloat(settings.getString("Licence_hauteur","0.178"));
        float wr = Float.parseFloat(settings.getString("Licence_largeur","0.776"));
        int ir = Integer.parseInt(settings.getString("image_ratio","0"));

        int maxBitMapSize = 1024*ir;
        if (ir==0){
            if (maxSize>=2048) maxBitMapSize= 4096;
            else if (maxSize>=1024) maxBitMapSize= 2048;
        }

        float ratioV = (float) Math.ceil(maxBitMapSize / mCurrentPage.getHeight());

        float w;
        float h = Math.min(maxBitMapSize,mCurrentPage.getHeight() * ratioV);
        w = (float) (h/Math.sqrt(2));
        Bitmap bitmap = createBitmap((int) w, (int) h, Bitmap.Config.ARGB_8888);
        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        closeRenderer();

        String path = settings.getString("licence" + page + "Path", "______");
        if (!path.equals("______")) {
            Uri previousUri = Uri.parse("content://media" + path);
            context.getContentResolver().delete(previousUri, null, null);
        }
        //
        bitmap = createBitmap(bitmap, (int) (w * wor), (int) (h * hor), (int) (w * wr), (int) (h * hr));
        path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Licence FFD"+page, "Licence"+page);
        return Uri.parse(path);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        return networkInfo != null && networkInfo.isConnected();
    }

    private void openQuitDialog(){
        AlertDialog.Builder quitDialog    = new AlertDialog.Builder(MainActivity.this);
        quitDialog.setTitle(R.string.button_confirm);
        quitDialog.setNegativeButton(R.string.menu_cancel, new android.content.DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }});
        quitDialog.setPositiveButton(R.string.menu_exit, new android.content.DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finishAffinity();
            }});
        quitDialog.setIcon(R.mipmap.ic_launcher);
        quitDialog.show();
    }
}
