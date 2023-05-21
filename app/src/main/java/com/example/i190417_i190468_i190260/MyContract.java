package com.example.i190417_i190468_i190260;

import android.provider.BaseColumns;

public class MyContract {
    private MyContract() {}
    public static class Users implements BaseColumns{
        public static final String TABLE_NAME = "users";
        public static final String _EMAIL = "email";
        public static final String _PASS = "pass";
        public static final String _NAME = "name";
    }
}
