package com.agh.restaurant.config;

public class Roles {

    private Roles() {
    }

    static final String ANONYMOUS = "ANONYMOUS";
    static final String ADMIN = "ADMIN";
    static final String MANAGER = "MANAGER";
    static final String WAITER = "WAITER";
    static final String BARTENDER = "BARTENDER";
    static final String SUPPLIER = "SUPPLIER";
    static final String CUSTOMER = "CUSTOMER";
    static final String COOK = "COOK";

    private static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_ANONYMOUS = ROLE_PREFIX + ANONYMOUS;
    public static final String ROLE_ADMIN = ROLE_PREFIX + ADMIN;
    public static final String ROLE_MANAGER = ROLE_PREFIX + MANAGER;
    public static final String ROLE_WAITER = ROLE_PREFIX + WAITER;
    public static final String ROLE_BARTENDER = ROLE_PREFIX + BARTENDER;
    public static final String ROLE_SUPPLIER = ROLE_PREFIX + SUPPLIER;
    public static final String ROLE_CUSTOMER = ROLE_PREFIX + CUSTOMER;
    public static final String ROLE_COOK = ROLE_PREFIX + COOK;

}
