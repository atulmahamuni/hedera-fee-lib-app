package com.hedera.node.app.hapi.fees.apis.common;

public final class FeeConstants {
    private FeeConstants() {}  // prevent instantiation

    public static final int MIN_GAS = 21_000;
    public static final int MAX_GAS = 15_000_000;

    public static final int MIN_KEYS = 1;
    public static final int MAX_KEYS = 100;

    public static final int HCS_FREE_BYTES = 256;
    public static final int HCS_MIN_BYTES = 1;
    public static final int HCS_MAX_BYTES = 1024;

    public static final int FREE_ALLOWANCES = 1;
    public static final int MIN_ALLOWANCES = 1;
    public static final int MAX_ALLOWANCES = 10;
}