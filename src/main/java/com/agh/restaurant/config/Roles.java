/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
