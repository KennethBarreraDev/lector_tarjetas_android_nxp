/*
 * *****************************************************************************************************************************
 * Copyright 2013-2020 NXP.
 * NXP Confidential. This software is owned or controlled by NXP and may only be used strictly in accordance with the applicable license terms.
 * By expressly accepting such terms or by downloading, installing, activating and/or otherwise using the software, you are agreeing that you have read, and that you agree to comply with and are bound by, such license terms.
 * If you do not agree to be bound by the applicable license terms, then you may not retain, install, activate or otherwise use the software.
 * ********************************************************************************************************************************
 *
 */


package com.apptest.kenneth;

import com.nxp.nfclib.interfaces.IKeyData;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;


/**
 * MainActivity has the business logic to initialize the taplinx library and use it for
 * identification of the cards
 */
public class Constants {
    /**
     * String Constants
     */


    static final String DEFAULT_SECTOR_CARD_KEY = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";

    static final String NEW_MASTER_KEY = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";

    static final String NEW_CONFIGURATION_KEY = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";

    static final String KENNETH_SECTOR_1_KEY = "AABBCCDDEEFF00112233445566778899";


    static final String TAG = "SampleTapLinx";
    static final String ALIAS_KEY_AES128 = "key_aes_128";
    static final String ALIAS_KEY_2KTDES = "key_2ktdes";
    static final String ALIAS_KEY_2KTDES_ULC = "key_2ktdes_ulc";
    static final String ALIAS_DEFAULT_FF = "alias_default_ff";
    static final String ALIAS_KEY_AES128_ZEROES = "alias_default_00";
    static final String EXTRA_KEYS_STORED_FLAG = "keys_stored_flag";

    /**
     * KEY_APP_MASTER key used for encrypting the data.
     */
    static final String KEY_APP_MASTER = "This is my key  ";

    /**
     * Constant for permission
     */
    static final int STORAGE_PERMISSION_WRITE = 113;
    static final String UNABLE_TO_READ = "Unable to read";
    static final char TOAST_PRINT = 'd';
    static final char TOAST = 't';
    static final char PRINT = 'n';
    static final String EMPTY_SPACE = " ";
    /**
     * Package Key.
     * Package Key : Copy your package key obtained from mifare.net below
     */
    static final String packageKey = "115e080eac3067f156bf87af4d4cd7e9";
    static final String ancillaryKey = "enbhl2sbUfKIyDSo0/dgU7BUWCreSaF5/Q7E0jJKfueFPx1o8CqrTlSBen6JKduDWQ4LVezx01GR/4QrtG8c2yBEYr6JtEbADGdSDe+7MMWZxyeNnYxHU0gFAPEOYrXVf5cDEfVtt4TORGSiRftbwOngoIFIvJDK3ZnsZpZeox4ykXNK+6o/3g7cNR+Nv1p98ot6aUnJO9NLs23OngMj9XnP8W7XLtp4V895C9hamRMqO7N5fuBUW5TvMHiOEqifurlx4L2W8gDwSM8/nZ2pc8caWsk8DKH9hMc+uG3fGsGgcaF7Jw4T/W6ZsKykCVmyacFDRTHTq3vMvPORVSsiTw==";


    static IKeyData objKEY_2KTDES_ULC = null;
    static IKeyData objKEY_2KTDES = null;
    static IKeyData objKEY_AES128 = null;
    static byte[] default_ff_key = null;
    static IKeyData default_zeroes_key = null;

    /**
     * Classic sector number set to 6.
     */
    static final int DEFAULT_SECTOR_CLASSIC = 6;

    static final byte DEFAULT_ICode_PAGE = (byte) 0x10;

    /**
     * bytes key.
     */
    static byte[] bytesKey = null;
    /**
     * Cipher instance.
     */
    static Cipher cipher = null;
    /**
     * Iv.
     */
    static IvParameterSpec iv = null;

    //16 byte Default AES Key
    static final byte[] KEY_AES128_DEFAULT =
            {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
}
