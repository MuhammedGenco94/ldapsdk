/*
 * Copyright 2009-2015 UnboundID Corp.
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
package com.unboundid.ldap.sdk.unboundidds;



import com.unboundid.util.StaticUtils;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;



/**
 * <BLOCKQUOTE>
 *   <B>NOTE:</B>  This class is part of the Commercial Edition of the UnboundID
 *   LDAP SDK for Java.  It is not available for use in applications that
 *   include only the Standard Edition of the LDAP SDK, and is not supported for
 *   use in conjunction with non-UnboundID products.
 * </BLOCKQUOTE>
 * This class provides information about the types of alert severities that may
 * be included in alert entries.
 */
@ThreadSafety(level=ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public enum AlertSeverity
{
  /**
   * The alert severity that indicates that the associated alert is
   * informational.
   */
  INFO("info"),



  /**
   * The alert severity that indicates that the associated alert indicates a
   * warning has occurred.
   */
  WARNING("warning"),



  /**
   * The alert severity that indicates that the associated alert indicates a
   * non-fatal error has occurred.
   */
  ERROR("error"),



  /**
   * The alert severity that indicates that the associated alert indicates a
   * fatal error has occurred.
   */
  FATAL("fatal");



  // The name for this alert severity.
  private final String name;



  /**
   * Creates a new alert severity with the specified name.
   *
   * @param  name  The name for this alert severity.
   */
  private AlertSeverity(final String name)
  {
    this.name = name;
  }



  /**
   * Retrieves the name for this alert severity.
   *
   * @return  The name for this alert severity.
   */
  public String getName()
  {
    return name;
  }



  /**
   * Retrieves the alert severity with the specified name.
   *
   * @param  name  The name of the alert severity to retrieve.
   *
   * @return  The alert severity with the specified name, or {@code null} if
   *          there is no alert severity with the given name.
   */
  public static AlertSeverity forName(final String name)
  {
    final String lowerName = StaticUtils.toLowerCase(name);

    if (lowerName.equals("error"))
    {
      return ERROR;
    }
    else if (lowerName.equals("fatal"))
    {
      return FATAL;
    }
    else if (lowerName.equals("info"))
    {
      return INFO;
    }
    else if (lowerName.equals("warning"))
    {
      return WARNING;
    }
    else
    {
      return null;
    }
  }



  /**
   * Retrieves a string representation of this alert severity.
   *
   * @return  A string representation of this alert severity.
   */
  @Override()
  public String toString()
  {
    return name;
  }
}
