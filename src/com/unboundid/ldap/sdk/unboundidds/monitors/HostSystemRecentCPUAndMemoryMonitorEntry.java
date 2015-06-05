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



import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.unboundid.ldap.sdk.Entry;
import com.unboundid.util.NotMutable;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;

import static com.unboundid.ldap.sdk.unboundidds.monitors.MonitorMessages.*;



/**
 * <BLOCKQUOTE>
 *   <B>NOTE:</B>  This class is part of the Commercial Edition of the UnboundID
 *   LDAP SDK for Java.  It is not available for use in applications that
 *   include only the Standard Edition of the LDAP SDK, and is not supported for
 *   use in conjunction with non-UnboundID products.
 * </BLOCKQUOTE>
 * This class defines a monitor entry that provides information about the recent
 * CPU and memory utilization of the underlying system.
 */
@NotMutable()
@ThreadSafety(level=ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public final class HostSystemRecentCPUAndMemoryMonitorEntry
       extends MonitorEntry
{
  /**
   * The structural object class used in host system recent CPU and memory
   * monitor entries.
   */
  static final String HOST_SYSTEM_RECENT_CPU_AND_MEMORY_MONITOR_OC =
       "ds-host-system-cpu-memory-monitor-entry";



  /**
   * The name of the attribute that contains the recent CPU idle percentage.
   */
  private static final String ATTR_RECENT_CPU_IDLE = "recent-cpu-idle";



  /**
   * The name of the attribute that contains the recent CPU I/O wait percentage.
   */
  private static final String ATTR_RECENT_CPU_IOWAIT = "recent-cpu-iowait";



  /**
   * The name of the attribute that contains the recent CPU system percentage.
   */
  private static final String ATTR_RECENT_CPU_SYSTEM = "recent-cpu-system";



  /**
   * The name of the attribute that contains the recent CPU total busy
   * percentage.
   */
  private static final String ATTR_RECENT_TOTAL_CPU_BUSY = "recent-cpu-used";



  /**
   * The name of the attribute that contains the recent CPU user percentage.
   */
  private static final String ATTR_RECENT_CPU_USER = "recent-cpu-user";



  /**
   * The name of the attribute that contains the recent amount of free system
   * memory, in gigabytes.
   */
  private static final String ATTR_RECENT_MEMORY_FREE_GB =
       "recent-memory-free-gb";



  /**
   * The name of the attribute that contains the recent percent of system memory
   * that is currently free.
   */
  private static final String ATTR_RECENT_MEMORY_FREE_PCT =
       "recent-memory-pct-free";



  /**
   * The name of the attribute that contains the time the information was
   * last updated.
   */
  private static final String ATTR_TIMESTAMP = "timestamp";



  /**
   * The name of the attribute that contains the total amount of system memory,
   * in gigabytes.
   */
  private static final String ATTR_TOTAL_MEMORY_GB = "total-memory-gb";



  /**
   * The serial version UID for this serializable class.
   */
  private static final long serialVersionUID = -4408434740529394905L;



  // The time the CPU and memory usage information was last updated.
  private final Date timestamp;

  // The recent CPU idle percent.
  private final Double recentCPUIdle;

  // The recent CPU I/O wait percent.
  private final Double recentCPUIOWait;

  // The recent CPU system percent.
  private final Double recentCPUSystem;

  // The recent CPU total percent busy.
  private final Double recentCPUTotalBusy;

  // The recent CPU user percent.
  private final Double recentCPUUser;

  // The recent free memory, in gigabytes.
  private final Double recentMemoryFreeGB;

  // The recent free memory percent..
  private final Double recentMemoryPercentFree;

  // The total amount of system memory, in gigabytes.
  private final Double totalMemoryGB;



  /**
   * Creates a new host system recent CPU and memory monitor entry from the
   * provided entry.
   *
   * @param  entry  The entry to be parsed as a host system recent CPU and
   *                memory monitor entry.  It must not be {@code null}.
   */
  public HostSystemRecentCPUAndMemoryMonitorEntry(final Entry entry)
  {
    super(entry);

    timestamp = getDate(ATTR_TIMESTAMP);
    recentCPUIdle = getDouble(ATTR_RECENT_CPU_IDLE);
    recentCPUIOWait = getDouble(ATTR_RECENT_CPU_IOWAIT);
    recentCPUSystem = getDouble(ATTR_RECENT_CPU_SYSTEM);
    recentCPUUser = getDouble(ATTR_RECENT_CPU_USER);
    recentCPUTotalBusy = getDouble(ATTR_RECENT_TOTAL_CPU_BUSY);
    recentMemoryFreeGB = getDouble(ATTR_RECENT_MEMORY_FREE_GB);
    recentMemoryPercentFree = getDouble(ATTR_RECENT_MEMORY_FREE_PCT);
    totalMemoryGB = getDouble(ATTR_TOTAL_MEMORY_GB);
  }



  /**
   * Retrieves the time that the CPU and memory utilization data was last
   * updated, if available.
   *
   * @return  The time that the CPU and system memory utilization data was
   *          last updated, or {@code null} if it was not included in the
   *          monitor entry.
   */
  public Date getUpdateTime()
  {
    return timestamp;
  }



  /**
   * Retrieves the total percentage of recent CPU time spent in user, system, or
   * I/O wait states, if available.
   *
   * @return  The total percentage of recent CPU time spent in user, system, or
   *          I/O wait states, or {@code null} if it was not included in the
   *          monitor entry.
   */
  public Double getRecentCPUTotalBusyPercent()
  {
    return recentCPUTotalBusy;
  }



  /**
   * Retrieves the percentage of recent CPU time spent in the user state, if
   * available.
   *
   * @return  The percentage of recent CPU time spent in the user state, or
   *          {@code null} if it was not included in the monitor entry.
   */
  public Double getRecentCPUUserPercent()
  {
    return recentCPUUser;
  }



  /**
   * Retrieves the percentage of recent CPU time spent in the system state, if
   * available.
   *
   * @return  The percentage of recent CPU time spent in the system state, or
   *          {@code null} if it was not included in the monitor entry.
   */
  public Double getRecentCPUSystemPercent()
  {
    return recentCPUSystem;
  }



  /**
   * Retrieves the percentage of recent CPU time spent in the I/O wait state, if
   * available.
   *
   * @return  The percentage of recent CPU time spent in the I/O wait state, or
   *          {@code null} if it was not included in the monitor entry.
   */
  public Double getRecentCPUIOWaitPercent()
  {
    return recentCPUIOWait;
  }



  /**
   * Retrieves the percentage of recent CPU idle time, if available.
   *
   * @return  The percentage of recent CPU idle time, or {@code null} if it was
   *          not included in the monitor entry.
   */
  public Double getRecentCPUIdlePercent()
  {
    return recentCPUIdle;
  }



  /**
   * Retrieves the total amount of system memory in gigabytes, if available.
   *
   * @return  The total amount of system memory in gigabytes, or {@code null} if
   *          it was not included in the monitor entry.
   */
  public Double getTotalSystemMemoryGB()
  {
    return totalMemoryGB;
  }



  /**
   * Retrieves the recent amount of free system memory in gigabytes, if
   * available.
   *
   * @return  The recent amount of free system memory in gigabytes, or
   *          {@code null} if it was not included in the monitor entry.
   */
  public Double getRecentSystemMemoryFreeGB()
  {
    return recentMemoryFreeGB;
  }



  /**
   * Retrieves the recent percentage of free system memory, if available.
   *
   * @return  The recent percentage of free system memory, or {@code null} if it
   *          was not included in the monitor entry.
   */
  public Double getRecentSystemMemoryPercentFree()
  {
    return recentMemoryPercentFree;
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public String getMonitorDisplayName()
  {
    return INFO_CPU_MEM_MONITOR_DISPNAME.get();
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public String getMonitorDescription()
  {
    return INFO_CPU_MEM_MONITOR_DESC.get();
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public Map<String,MonitorAttribute> getMonitorAttributes()
  {
    final LinkedHashMap<String,MonitorAttribute> attrs =
         new LinkedHashMap<String,MonitorAttribute>(9);

    if (timestamp != null)
    {
      addMonitorAttribute(attrs,
           ATTR_TIMESTAMP,
           INFO_CPU_MEM_DISPNAME_TIMESTAMP.get(),
           INFO_CPU_MEM_DESC_TIMESTAMP.get(),
           timestamp);
    }

    if (recentCPUTotalBusy != null)
    {
      addMonitorAttribute(attrs,
           ATTR_RECENT_TOTAL_CPU_BUSY,
           INFO_CPU_MEM_DISPNAME_RECENT_CPU_TOTAL_BUSY.get(),
           INFO_CPU_MEM_DESC_RECENT_CPU_TOTAL_BUSY.get(),
           recentCPUTotalBusy);
    }

    if (recentCPUUser != null)
    {
      addMonitorAttribute(attrs,
           ATTR_RECENT_CPU_USER,
           INFO_CPU_MEM_DISPNAME_RECENT_CPU_USER.get(),
           INFO_CPU_MEM_DESC_RECENT_CPU_USER.get(),
           recentCPUUser);
    }

    if (recentCPUSystem != null)
    {
      addMonitorAttribute(attrs,
           ATTR_RECENT_CPU_SYSTEM,
           INFO_CPU_MEM_DISPNAME_RECENT_CPU_SYSTEM.get(),
           INFO_CPU_MEM_DESC_RECENT_CPU_SYSTEM.get(),
           recentCPUSystem);
    }

    if (recentCPUIOWait != null)
    {
      addMonitorAttribute(attrs,
           ATTR_RECENT_CPU_IOWAIT,
           INFO_CPU_MEM_DISPNAME_RECENT_CPU_IOWAIT.get(),
           INFO_CPU_MEM_DESC_RECENT_CPU_IOWAIT.get(),
           recentCPUIOWait);
    }

    if (recentCPUIdle != null)
    {
      addMonitorAttribute(attrs,
           ATTR_RECENT_CPU_IDLE,
           INFO_CPU_MEM_DISPNAME_RECENT_CPU_IDLE.get(),
           INFO_CPU_MEM_DESC_RECENT_CPU_IDLE.get(),
           recentCPUIdle);
    }

    if (totalMemoryGB != null)
    {
      addMonitorAttribute(attrs,
           ATTR_TOTAL_MEMORY_GB,
           INFO_CPU_MEM_DISPNAME_TOTAL_MEM.get(),
           INFO_CPU_MEM_DESC_TOTAL_MEM.get(),
           totalMemoryGB);
    }

    if (recentMemoryFreeGB != null)
    {
      addMonitorAttribute(attrs,
           ATTR_RECENT_MEMORY_FREE_GB,
           INFO_CPU_MEM_DISPNAME_FREE_MEM_GB.get(),
           INFO_CPU_MEM_DESC_FREE_MEM_GB.get(),
           recentMemoryFreeGB);
    }

    if (recentMemoryPercentFree != null)
    {
      addMonitorAttribute(attrs,
           ATTR_RECENT_MEMORY_FREE_PCT,
           INFO_CPU_MEM_DISPNAME_FREE_MEM_PCT.get(),
           INFO_CPU_MEM_DESC_FREE_MEM_PCT.get(),
           recentMemoryPercentFree);
    }

    return Collections.unmodifiableMap(attrs);
  }
}
