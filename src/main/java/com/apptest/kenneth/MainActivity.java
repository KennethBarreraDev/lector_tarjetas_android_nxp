/*
 * *****************************************************************************************************************************
 * Copyright 2013-2024 NXP.
 * NXP Confidential. This software is owned or controlled by NXP and may only be used strictly in accordance with the applicable license terms.
 * By expressly accepting such terms or by downloading, installing, activating and/or otherwise using the software, you are agreeing that you have read, and that you agree to comply with and are bound by, such license terms.
 * If you do not agree to be bound by the applicable license terms, then you may not retain, install, activate or otherwise use the software.
 * ********************************************************************************************************************************
 *
 */


package com.apptest.kenneth;

import static com.apptest.kenneth.Constants.ALIAS_DEFAULT_FF;
import static com.apptest.kenneth.Constants.ALIAS_KEY_2KTDES;
import static com.apptest.kenneth.Constants.ALIAS_KEY_2KTDES_ULC;
import static com.apptest.kenneth.Constants.ALIAS_KEY_AES128;
import static com.apptest.kenneth.Constants.ALIAS_KEY_AES128_ZEROES;
import static com.apptest.kenneth.Constants.EMPTY_SPACE;
import static com.apptest.kenneth.Constants.EXTRA_KEYS_STORED_FLAG;
import static com.apptest.kenneth.Constants.KEY_AES128_DEFAULT;
import static com.apptest.kenneth.Constants.KEY_APP_MASTER;
import static com.apptest.kenneth.Constants.PRINT;
import static com.apptest.kenneth.Constants.STORAGE_PERMISSION_WRITE;
import static com.apptest.kenneth.Constants.TAG;
import static com.apptest.kenneth.Constants.TOAST;
import static com.apptest.kenneth.Constants.TOAST_PRINT;
import static com.apptest.kenneth.Constants.bytesKey;
import static com.apptest.kenneth.Constants.cipher;
import static com.apptest.kenneth.Constants.default_ff_key;
import static com.apptest.kenneth.Constants.default_zeroes_key;
import static com.apptest.kenneth.Constants.iv;
import static com.apptest.kenneth.Constants.objKEY_2KTDES;
import static com.apptest.kenneth.Constants.objKEY_2KTDES_ULC;
import static com.apptest.kenneth.Constants.objKEY_AES128;
import static com.apptest.kenneth.Constants.packageKey;
import static com.apptest.kenneth.Constants.ancillaryKey;
import static com.apptest.kenneth.Constants.DEFAULT_SECTOR_CARD_KEY;
import static com.apptest.kenneth.Constants.NEW_MASTER_KEY;
import static com.apptest.kenneth.Constants.NEW_CONFIGURATION_KEY;
import static com.apptest.kenneth.Constants.KENNETH_SECTOR_1_KEY;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nxp.nfclib.CardType;
import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.classic.ClassicFactory;
import com.nxp.nfclib.defaultimpl.KeyData;
import com.nxp.nfclib.desfire.DESFireFactory;
import com.nxp.nfclib.desfire.IDESFireEV2;
import com.nxp.nfclib.interfaces.IKeyData;
import com.nxp.nfclib.plus.IPlusEV2SL0;
import com.nxp.nfclib.plus.IPlusEV2SL1;
import com.nxp.nfclib.desfire.IDESFireEV3;
import com.nxp.nfclib.desfire.IDESFireEV3C;
import com.nxp.nfclib.desfire.IDESFireLight;
import com.nxp.nfclib.desfire.IMIFAREIdentity;
import com.nxp.nfclib.exceptions.NxpNfcLibException;
import com.nxp.nfclib.icode.ICodeFactory;
import com.nxp.nfclib.ntag.NTagFactory;
import com.nxp.nfclib.plus.IPlus;
import com.nxp.nfclib.plus.IPlusEV1SL0;
import com.nxp.nfclib.plus.IPlusEV1SL1;
import com.nxp.nfclib.plus.IPlusEV1SL3;
import com.nxp.nfclib.plus.IPlusEV2SL3;
import com.nxp.nfclib.plus.IPlusSL0;
import com.nxp.nfclib.plus.IPlusSL1;
import com.nxp.nfclib.plus.IPlusSL3;
import com.nxp.nfclib.plus.PlusFactory;
import com.nxp.nfclib.plus.PlusSL1Factory;
import com.nxp.nfclib.ultralight.UltralightFactory;
import com.nxp.nfclib.utils.NxpLogUtils;
import com.nxp.nfclib.utils.Utilities;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 * MainActivity has the business logic to initialize the taplinx library and use it for
 * identification of the cards
 */
public class MainActivity extends Activity {
    /**
     * NxpNfclib instance.
     */
    private NxpNfcLib libInstance = null;
    /**
     * text view instance.
     */
    private TextView information_textView = null;
    /**
     * Image view instance.
     */
    private ImageView logoAndCardImageView = null;

    private ImageView tapTagImageView;

    private final StringBuilder stringBuilder = new StringBuilder();

    static Object mString;

    CardLogic mCardLogic;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.d("MainActivity", "Generando main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tapTagImageView = findViewById(R.id.tap_tag_image);
        logoAndCardImageView = findViewById(R.id.nxp_logo_card_snap);

        boolean readPermission = (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (!readPermission) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_WRITE
            );
        }

        mCardLogic = CardLogic.getInstance();

        /* Initialize the library and register to this activity */
        initializeLibrary();
        Log.d("MainActivity", "Salio 1");
        initializeKeys();

        /* Initialize the Cipher and init vector of 16 bytes with 0xCD */
        initializeCipherinitVector();

        /* Get text view handle to be used further */
        initializeView();

        BottomNavigationView bottomNavigationView = findViewById(
                R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // Starting with Android Gradle Plugin 8.0.0, by default, resources (e.g. R.id. ...)
                        // are no longer declared final (i.e. constant expressions) for optimized build speed,
                        // which is a prerequisite to be used in switch statements.
                        // So If block is used instead of switch.
                        int itemId = item.getItemId();
                        if (itemId == R.id.text_write) {//An intent is an oject that provides run time binding between
                            // two components
                            Intent intent = new Intent(MainActivity.this,
                                    WriteActivity.class);
                            //this is used as activity class is subclass of Context
                            startActivity(intent);
                            finish();
                        } else if (itemId == R.id.text_about) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                    MainActivity.this);
                            alert.setTitle(getString(R.string.About));
                            alert.setCancelable(false);
                            String[] cards = libInstance.getSupportedCards();
                            // get TapLinx version.
                            String taplinxVersion = NxpNfcLib.getTaplinxVersion();
                            String message = getString(R.string.about_text);

                            message = Html.fromHtml(message).toString();
                            String alertMessage = message + "\n";

                            alertMessage += "\n";
                            String appVer = getApplicationVersion();
                            if (appVer != null) {
                                alertMessage += getString(R.string.Application_Version) + appVer
                                        + "\n";
                            }
                            //Display the current version of TapLinx library
                            alertMessage += "\n" + getString(R.string.TapLinx_Version)
                                    + taplinxVersion + "\n";

                            alertMessage += "\n" + getString(R.string.Supported_Cards)
                                    + Arrays.toString(cards) + "\n";

                            alert.setMessage(alertMessage);
                            alert.setIcon(R.mipmap.ic_launcher);
                            alert.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog,
                                                            final int whichButton) {

                                        }
                                    });
                            alert.show();
                        }
                        return false;
                    }
                });


    }

    private void initializeKeys() {
        KeyInfoProvider infoProvider = KeyInfoProvider.getInstance(getApplicationContext());

        SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        boolean keysStoredFlag = sharedPrefs.getBoolean(EXTRA_KEYS_STORED_FLAG, false);
        if (!keysStoredFlag) {
            //Set Key stores the key in persistent storage, this method can be called only once
            // if key for a given alias does not change.
            byte[] ulc24Keys = new byte[24];
            System.arraycopy(SampleAppKeys.KEY_2KTDES_ULC, 0, ulc24Keys, 0,
                    SampleAppKeys.KEY_2KTDES_ULC.length);
            System.arraycopy(SampleAppKeys.KEY_2KTDES_ULC, 0, ulc24Keys,
                    SampleAppKeys.KEY_2KTDES_ULC.length, 8);
            infoProvider.setKey(ALIAS_KEY_2KTDES_ULC, SampleAppKeys.EnumKeyType.EnumDESKey,
                    ulc24Keys);

            infoProvider.setKey(ALIAS_KEY_2KTDES, SampleAppKeys.EnumKeyType.EnumDESKey,
                    SampleAppKeys.KEY_2KTDES);
            infoProvider.setKey(ALIAS_KEY_AES128, SampleAppKeys.EnumKeyType.EnumAESKey,
                    SampleAppKeys.KEY_AES128);
            infoProvider.setKey(ALIAS_KEY_AES128_ZEROES, SampleAppKeys.EnumKeyType.EnumAESKey,
                    SampleAppKeys.KEY_AES128_ZEROS);
            infoProvider.setKey(ALIAS_DEFAULT_FF, SampleAppKeys.EnumKeyType.EnumMifareKey,
                    SampleAppKeys.KEY_DEFAULT_FF);

            sharedPrefs.edit().putBoolean(EXTRA_KEYS_STORED_FLAG, true).apply();
            //If you want to store a new key after key initialization above, kindly reset the
            // flag EXTRA_KEYS_STORED_FLAG to false in shared preferences.
        }
        try {

            objKEY_2KTDES_ULC = infoProvider.getKey(ALIAS_KEY_2KTDES_ULC,
                    SampleAppKeys.EnumKeyType.EnumDESKey);
            objKEY_2KTDES = infoProvider.getKey(ALIAS_KEY_2KTDES,
                    SampleAppKeys.EnumKeyType.EnumDESKey);
            objKEY_AES128 = infoProvider.getKey(ALIAS_KEY_AES128,
                    SampleAppKeys.EnumKeyType.EnumAESKey);
            default_zeroes_key = infoProvider.getKey(ALIAS_KEY_AES128_ZEROES,
                    SampleAppKeys.EnumKeyType.EnumAESKey);
            default_ff_key = infoProvider.getMifareKey(ALIAS_DEFAULT_FF);
        } catch (Exception e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ((ActivityManager) Objects.requireNonNull(
                        MainActivity.this.getSystemService(ACTIVITY_SERVICE)))
                        .clearApplicationUserData();
            }
        }
    }

    /**
     * Initializing the widget, and Get text view handle to be used further.
     */
    private void initializeView() {
        /* Get text view handle to be used further */
        information_textView = findViewById(R.id.info_textview);
        information_textView.setMovementMethod(new ScrollingMovementMethod());
        Typeface face = Typeface.SANS_SERIF;
        information_textView.setTypeface(face);
        information_textView.setTextColor(Color.BLACK);

        /* Get image view handle to be used further */
        logoAndCardImageView = findViewById(R.id.nxp_logo_card_snap);

    }

    /**
     * Initialize the library and register to this activity.
     */
    @TargetApi(19)
    private void initializeLibrary() {
        libInstance = NxpNfcLib.getInstance();
        try {
            libInstance.registerActivity(this, packageKey, ancillaryKey);
        } catch (NxpNfcLibException ex) {
            showMessage(ex.getMessage(), TOAST);
        } catch (Exception e) {
            // do nothing added to handle the crash if any
        }
    }

    /**
     * Initialize the Cipher and init vector of 16 bytes with 0xCD.
     */
    private void initializeCipherinitVector() {
        /* Initialize the Cipher */
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        /* set Application Master Key */
        bytesKey = KEY_APP_MASTER.getBytes();

        /* Initialize init vector of 16 bytes with 0xCD. It could be anything */
        byte[] ivSpec = new byte[16];
        Arrays.fill(ivSpec, (byte) 0xCD);
        iv = new IvParameterSpec(ivSpec);
    }

    /**
     * (non-Javadoc).
     *
     * @param intent NFC intent from the android framework.
     *               // @see android.app.Activity#onNewIntent(android.content.Intent)
     */
    @Override
    public void onNewIntent(final Intent intent) {
        Log.d(TAG, "On new intent");
        cardLogic(intent);
        super.onNewIntent(intent);
    }
    //This API identifies the card type and calls the specific operations
    private void cardLogic(final Intent intent) {
        final boolean updateKey = false;
        final boolean testBlockNumber = false;
        final boolean writeInfo = false;
        final boolean readInfo = true;

        CardType type = libInstance.getCardType(intent);
        Log.d("CardType", "Card type is: " + type.getTagName());

        //Tarjeta está en SL0
        if (type == CardType.PlusEV2SL0) {
            Log.d("CardTypeIf", "Card type is: " + type.getTagName());
            changeToSL3(type);
        }
        //Obtener dirección de un bloque (PRUEBAS)
        else if(type == CardType.PlusEV2SL3 && updateKey){
            Log.d("CardTypeChangingKey", "Card type is: " + type.getTagName());
            changeSector1Key();
        }
        //Tarjeta está en SL3 y queremos cambiar KEY de sector 1
        else if(type == CardType.PlusEV2SL3 && testBlockNumber){
            Log.d("CardTypeChangingKey", "Card type is: " + type.getTagName());
            testBlockNumber();
        }
        //Tarjeta está en SL3 y queremos escribir datos en sector 1
        else if(type == CardType.PlusEV2SL3 && writeInfo){
            Log.d("CardTypeChangingKey", "Card type is: " + type.getTagName());
            writeValue();

        }

        else if(type == CardType.PlusEV2SL3 && readInfo){
            Log.d("CardTypeChangingKey", "Card type is: " + type.getTagName());
            readValue();
        }
    }


    private void testBlockNumber(){
        IPlusEV2SL3 plusSL3 = PlusFactory.getInstance().getPlusEV2SL3(libInstance.getCustomModules());
        byte number = 1;
        final int address = plusSL3.sectorNumberToBlockNumberForAESKeys(number);
        Log.e("TEST_BLOCK", "Block address is " + address);

    }

    private void writeValue(){
        try{
        IPlusEV2SL3 plusSL3 = PlusFactory.getInstance().getPlusEV2SL3(libInstance.getCustomModules());

        // Clave AES actual (por defecto fábrica)
        byte[] defaultKey = hexStringToByteArray(KENNETH_SECTOR_1_KEY);

        // Crear objeto KeyData con la clave actual
        Key key = new SecretKeySpec(defaultKey, "AES");
        KeyData keyData = new KeyData();
        keyData.setKey(key);

        final int blockTrailerSector1 = 0x4002; // Dirección sector 1

        // Autenticarse primero en sector 1
        plusSL3.authenticateFirst(blockTrailerSector1, keyData, new byte[0]);

        plusSL3.write(IPlusSL3.WriteMode.Plain_ResponseNonMACed, 4, new byte[] {0x01, 0x02, 0x03, 0x04});
    Log.d("WRITE_VALUE_SUCCESS", "Value written succesfully");

    showMessage("Valor escrito exitosamente", PRINT);

    } catch (Exception e) {
            showMessage("Error escribiendo valor", PRINT);
        Log.e("WRITE_VALUE_ERROR", "Error writting in card", e);
    }
    }


    private void readValue(){
        try{
            IPlusEV2SL3 plusSL3 = PlusFactory.getInstance().getPlusEV2SL3(libInstance.getCustomModules());

            // Clave AES actual (por defecto fábrica)
            byte[] defaultKey = hexStringToByteArray(KENNETH_SECTOR_1_KEY);

            // Crear objeto KeyData con la clave actual
            Key key = new SecretKeySpec(defaultKey, "AES");
            KeyData keyData = new KeyData();
            keyData.setKey(key);

            final int blockTrailerSector1 = 0x4002; // Dirección sector 1

            // Autenticarse primero en sector 1
            plusSL3.authenticateFirst(blockTrailerSector1, keyData, new byte[0]);
            byte[] byData = plusSL3.read(IPlusSL3.ReadMode.Plain_ResponseNonMACed_CommandMACed, 4);

            showMessage("Valor leído exitosamente " + Utilities.byteToHexString(byData), PRINT);

        } catch (Exception e) {
            showMessage("Error leyendo valor", PRINT);
            Log.e("WRITE_VALUE_ERROR", "Error reading value", e);
        }
    }


    private void changeToSL3(CardType type){
        IPlusEV2SL0 plusSL0 = PlusFactory.getInstance().getPlusEV2SL0(
                libInstance.getCustomModules());
        try {
            //Esctribir clave maestra y clave de configuración
            byte[] cardConfigurationKey = hexStringToByteArray(NEW_CONFIGURATION_KEY);
            byte[] cardMasterKey = hexStringToByteArray(NEW_MASTER_KEY);

            Log.e("CHANGE_SECURITY_STEP_1", "Keys generated");

            // Escribir claves de configuración
            plusSL0.writePerso(0x9000, cardConfigurationKey); // CardConfigurationKey
            plusSL0.writePerso(0x9001, cardMasterKey); // CardMasterKey

            Log.e("CHANGE_SECURITY_STEP_2", "CAMBIO A SL3");

            // 2. Hacer el cambio a SL3
            plusSL0.commitPerso(true); // El parámetro true indica cambio directo a SL3
            Log.e("CHANGE_SECURITY_STEP_3", "CAMBIO  EXITOSO A SL3");

            showMessage("Tarjeta migrada exitosamente a SL3", PRINT);

            // 3. Después del commitPerso, necesitas reactivar la tarjeta para SL3
            // La reactivación normalmente se hace desconectando y volviendo a conectar la tarjeta

        } catch (Exception e) {
            showMessage("Error durante la migración a SL3: " + e.getMessage(), PRINT);
            Log.e("CHANGE_SECURITY_ERROR", "Error migrating to SL3", e);
        }
    }

    private void changeSector1Key() {
        try {

            IPlusEV2SL3 plusSL3 = PlusFactory.getInstance().getPlusEV2SL3(libInstance.getCustomModules());


            // Clave AES actual (por defecto fábrica)
            byte[] defaultKey = hexStringToByteArray(DEFAULT_SECTOR_CARD_KEY);

            // Crear objeto KeyData con la clave actual
            Key key = new SecretKeySpec(defaultKey, "AES");
            KeyData keyData = new KeyData();
            keyData.setKey(key);

            final int blockTrailerSector1 = 0x4002; // Dirección sector 1

            // Autenticarse primero en sector 1
            plusSL3.authenticateFirst(blockTrailerSector1, keyData, new byte[0]);

            // Nueva clave AES para sector 1
            byte[] newKey = hexStringToByteArray(KENNETH_SECTOR_1_KEY);

            // Indica si usar MAC (según tu tarjeta, generalmente false)
            boolean changeKeyMACed = false;

            // Cambiar la clave AES en el bloque trailer del sector 1
            plusSL3.changeKey(changeKeyMACed, blockTrailerSector1, newKey);

            showMessage("Clave del sector 1 cambiada correctamente", PRINT);
            Log.d("CHANGE_KEY", "Clave sector 1 actualizada");

        } catch (Exception e) {
            showMessage("Error al cambiar clave del sector 1: " + e.getMessage(), PRINT);
            Log.e("NEW_KEY_ERROR", "Error cambiando clave", e);
        }
    }





    // Método auxiliar para convertir strings hex a byte arrays
    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    @Override
    protected void onPause() {
        super.onPause();
        libInstance.stopForeGroundDispatch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        libInstance.startForeGroundDispatch();
    }

    /**
     * Update the card image on the screen.
     *
     * @param cardTypeId resource image id of the card image
     */

    private void showImageSnap(final int cardTypeId) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        logoAndCardImageView.getLayoutParams().width = (size.x * 2) / 3;
        logoAndCardImageView.getLayoutParams().height = size.y / 3;
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                logoAndCardImageView.setImageResource(cardTypeId);
                logoAndCardImageView.startAnimation(
                        AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomnrotate));
            }
        }, 1250);
        logoAndCardImageView.setImageResource(R.drawable.product_overview);
        logoAndCardImageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        logoAndCardImageView.setLayoutParams(layoutParams);
    }

    /**
     * This will display message in toast or logcat or on screen or all three.
     *
     * @param str           String to be logged or displayed
     * @param operationType 't' for Toast; 'n' for Logcat and Display in UI; 'd' for Toast, Logcat
     *                      and
     *                      Display in UI.
     */
    private void showMessage(final String str, final char operationType) {
        switch (operationType) {
            case TOAST:
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT)
                        .show();
                break;
            case PRINT:
                information_textView.setText(str);
                information_textView.setGravity(Gravity.START);
                NxpLogUtils.i(TAG, getString(R.string.Dump_data) + str);
                break;
            case TOAST_PRINT:
                Toast.makeText(MainActivity.this, "\n" + str, Toast.LENGTH_SHORT).show();
                information_textView.setText(str);
                information_textView.setGravity(Gravity.START);
                NxpLogUtils.i(TAG, "\n" + str);
                break;
            default:
                break;
        }
    }

    private String getApplicationVersion() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_WRITE  && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,
                        getString(R.string.Requested_permisiion_granted),
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this,
                        getString(R.string.App_permission_not_granted_message),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
