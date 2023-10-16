/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.commons.core.edm.primitivetype;

import static org.junit.Assert.assertEquals;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

import org.apache.olingo.commons.api.edm.EdmPrimitiveType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.junit.Test;

public class EdmTimeOfDayTest extends PrimitiveTypeBaseTest {

  private final EdmPrimitiveType instance = EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.TimeOfDay);

  @Test
  public void toUriLiteral() throws Exception {
    assertEquals("11:12", instance.toUriLiteral("11:12"));
    assertEquals("11:12:13.012", instance.toUriLiteral("11:12:13.012"));
  }

  @Test
  public void fromUriLiteral() throws Exception {
    assertEquals("11:12", instance.fromUriLiteral("11:12"));
    assertEquals("11:12:13.012", instance.fromUriLiteral("11:12:13.012"));
  }

  @Test
  public void valueToString() throws Exception {
    Calendar dateTime = Calendar.getInstance();
    dateTime.clear();
    dateTime.setTimeZone(TimeZone.getTimeZone("GMT+11:30"));
    dateTime.set(1, 2, 3, 4, 5, 6);
    assertEquals("04:05:06", instance.valueToString(dateTime, null, null, null, null, null));

    dateTime.add(Calendar.MILLISECOND, 42);
    assertEquals("04:05:06.042", instance.valueToString(dateTime, null, null, 3, null, null));
    assertEquals("04:05:06.042", instance.valueToString(dateTime, null, null, 4, null, null));

    Calendar dateTime2 = Calendar.getInstance();
    dateTime2.clear();
    dateTime2.setTimeZone(TimeZone.getDefault());
    dateTime2.set(Calendar.HOUR, 5);
    dateTime2.set(Calendar.MINUTE, 59);
    dateTime2.set(Calendar.SECOND, 23);
    final Time time = new Time(dateTime2.getTimeInMillis());
    assertEquals("05:59:23", instance.valueToString(time, null, null, null, null, null));

    assertEquals("05:59:23", instance.valueToString(dateTime2.getTimeInMillis(), null, null, null, null, null));

    expectFacetsErrorInValueToString(instance, dateTime, null, null, null, null, null);
    expectFacetsErrorInValueToString(instance, dateTime, null, null, 2, null, null);
    Timestamp timestamp = new Timestamp(0);
    timestamp.setNanos(42);
    expectFacetsErrorInValueToString(instance, timestamp, null, null, 8, null, null);

    expectTypeErrorInValueToString(instance, 0);
  }

  @Test
  public void valueOfString() throws Exception {
    Calendar dateTime = Calendar.getInstance();
    dateTime.clear();

    assertEquals(dateTime, instance.valueOfString("00:00", null, null, null, null, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("00:00:00", null, null, null, null, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("00:00:00.000000000000", null, null, null, null, null,
        Calendar.class));

    final Time timeValue = instance.valueOfString("00:00:00.999", null, null, 3, null, null, Time.class);
    assertEquals(dateTime.getTimeInMillis(), timeValue.getTime());

    dateTime.set(Calendar.MILLISECOND, 999);
    assertEquals(dateTime, instance.valueOfString("00:00:00.999", null, null, 3, null, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("00:00:00.999", null, null, 3, null, null, Calendar.class));
    assertEquals(Long.valueOf(dateTime.getTimeInMillis()),
        instance.valueOfString("00:00:00.999", null, null, 3, null, null, Long.class));

    final Timestamp timestamp = instance.valueOfString("00:00:00.999888777", null, null, 9, null, null,
        Timestamp.class);
    assertEquals(dateTime.getTimeInMillis(), timestamp.getTime());
    assertEquals(999888777, timestamp.getNanos());

    expectFacetsErrorInValueOfString(instance, "11:12:13.123", null, null, null, null, null);
    expectFacetsErrorInValueOfString(instance, "11:12:13.123", null, null, 2, null, null);

    expectUnconvertibleErrorInValueOfString(instance, "11:12:13.1234", Calendar.class);
    expectUnconvertibleErrorInValueOfString(instance, "11:12:13.0123456789", Timestamp.class);

    expectContentErrorInValueOfString(instance, "24:32:02");
    expectContentErrorInValueOfString(instance, "011:12:13");
    expectContentErrorInValueOfString(instance, "11:12:13:14");
    expectContentErrorInValueOfString(instance, "111213");
    expectContentErrorInValueOfString(instance, "1:2:3");
    expectContentErrorInValueOfString(instance, "11:12:13.0.1");
    expectContentErrorInValueOfString(instance, "11:12:13.");
    expectContentErrorInValueOfString(instance, "11:12:13.0000000000000");

    expectTypeErrorInValueOfString(instance, "11:12:13");
  }

  @Test
  public void testRoundTripTime() throws Exception {
    java.sql.Time time = instance.valueOfString("04:05:06.002", true,
        4000, 3, 0, true, java.sql.Time.class);
    String val = instance.valueToString(time, true, 4000, 3, 0, true);
    assertEquals("04:05:06", val);
  }

  @Test
  public void tests() throws Exception {
    instance.validate("12:34:55", null, null, null, null, null);
  }

}
