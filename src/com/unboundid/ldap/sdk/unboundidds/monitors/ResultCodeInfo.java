/*
 * Copyright 2014-2015 UnboundID Corp.
 * All Rights Reserved.
 */
/*
 * Copyright (C) 2015 UnboundID Corp.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPLv2 only)
 * or the terms of the GNU Lesser General Public License (LGPLv2.1 only)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 */
package com.unboundid.ldap.sdk.unboundidds.monitors;



import java.io.Serializable;

import com.unboundid.ldap.sdk.OperationType;
import com.unboundid.util.NotMutable;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;



/**
 * <BLOCKQUOTE>
 *   <B>NOTE:</B>  This class is part of the Commercial Edition of the UnboundID
 *   LDAP SDK for Java.  It is not available for use in applications that
 *   include only the Standard Edition of the LDAP SDK, and is not supported for
 *   use in conjunction with non-UnboundID products.
 * </BLOCKQUOTE>
 * This class provides a data structure that encapsulates information about a
 * result code included in the result code monitor entry.
 */
@NotMutable()
@ThreadSafety(level=ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public final class ResultCodeInfo
       implements Serializable
{
  /**
   * The serial version UID for this serializable class.
   */
  private static final long serialVersionUID = 1223217954357101681L;



  // The average response time, in milliseconds, for operations with this result
  // code.
  private final double averageResponseTimeMillis;

  // The percent of operations of the associated type with this result code.
  private final double percent;

  // The sum of all response times, in milliseconds, for operations with this
  // result code.
  private final double totalResponseTimeMillis;

  // The integer value for this result code.
  private final int intValue;

  // The total number of operations of the specified type with this result code.
  private final long count;

  // The operation type for which this information is maintained, or null if
  // it applies to all types of operations.
  private final OperationType operationType;

  // The name for this result code.
  private final String name;



  /**
   * Creates a new result code info object with the provided information.
   *
   * @param  intValue                   The integer value for this result code.
   * @param  name                       The name for this result code.
   * @param  operationType              The type of operation to which the
   *                                    statistics apply.  This may be
   *                                    {@code null} if the statistics apply to
   *                                    all types of operations.
   * @param  count                      The total number of operations of the
   *                                    specified type with this result code.
   * @param  percent                    The percent of operations of the
   *                                    specified type with this result code.
   * @param  totalResponseTimeMillis    The total response time, in
   *                                    milliseconds, for all operations of the
   *                                    specified type with this result code.
   * @param  averageResponseTimeMillis  The average response time, in
   *                                    milliseconds, for operations of the
   *                                    specified type with this result code.
   */
  ResultCodeInfo(final int intValue, final String name,
                 final OperationType operationType, final long count,
                 final double percent, final double totalResponseTimeMillis,
                 final double averageResponseTimeMillis)
  {
    this.intValue                  = intValue;
    this.name                      = name;
    this.operationType             = operationType;
    this.count                     = count;
    this.totalResponseTimeMillis   = totalResponseTimeMillis;
    this.averageResponseTimeMillis = averageResponseTimeMillis;
    this.percent                   = percent;
  }



  /**
   * Retrieves the integer value for this result code.
   *
   * @return  The integer value for this result code.
   */
  public int intValue()
  {
    return intValue;
  }



  /**
   * Retrieves the name for this result code.
   *
   * @return  The name for this result code.
   */
  public String getName()
  {
    return name;
  }



  /**
   * Retrieves the type of operation with which the result code statistics are
   * associated, if appropriate.
   *
   * @return  The type of operation with which the result code statistics are
   *          associated, or {@code null} if the statistics apply to all types
   *          of operations.
   */
  public OperationType getOperationType()
  {
    return operationType;
  }



  /**
   * The total number of operations of the associated type (or of all
   * operations if the operation type is {@code null}) with this result code.
   *
   * @return  The total number of operations of the associated type with this
   *          result code.
   */
  public long getCount()
  {
    return count;
  }



  /**
   * The percent of operations of the associated type (or of all operations if
   * the operation type is {@code null}) with this result code.
   *
   * @return  The percent of operations of the associated type with this result
   *          code.
   */
  public double getPercent()
  {
    return percent;
  }



  /**
   * The sum of the response times, in milliseconds, for all operations of the
   * associated type (or of all operations if the operation type is
   * {@code null}) with this result code.
   *
   * @return  The sum of the response times, in milliseconds, for all operations
   *          of the associated type with this result code.
   */
  public double getTotalResponseTimeMillis()
  {
    return totalResponseTimeMillis;
  }



  /**
   * The average response time, in milliseconds, for all operations of the
   * associated type (or of all operations if the operation type is
   * {@code null}) with this result code.
   *
   * @return  The average response time, in milliseconds, for all operations of
   *          the associated type with this result code.
   */
  public double getAverageResponseTimeMillis()
  {
    return averageResponseTimeMillis;
  }
}
