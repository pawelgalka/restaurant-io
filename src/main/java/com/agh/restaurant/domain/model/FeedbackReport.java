/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.RaportType;

import java.util.Map;

public class FeedbackReport<K,V> {
    public FeedbackReport(RaportType type, Map<K,V> report) {
        this.type = type;
        this.report = report;
    }

    private RaportType type;

    private Map<K,V> report;

    @Override public String toString() {
        return "FeedbackRaport{" +
                "type=" + type +
                ", raport=" + report +
                '}';
    }

    public RaportType getType() {
        return type;
    }

    public void setType(RaportType type) {
        this.type = type;
    }

    public Map<K, V> getReport() {
        return report;
    }

    public void setReport(Map<K, V> report) {
        this.report = report;
    }
}
