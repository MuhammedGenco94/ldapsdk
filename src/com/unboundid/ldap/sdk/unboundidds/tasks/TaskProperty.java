/*
 * Copyright 2008-2015 UnboundID Corp.
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
package com.unboundid.ldap.sdk.unboundidds.tasks;



import java.io.Serializable;
import java.util.Date;

import com.unboundid.util.NotMutable;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;

import static com.unboundid.util.Validator.*;



/**
 * <BLOCKQUOTE>
 *   <B>NOTE:</B>  This class is part of the Commercial Edition of the UnboundID
 *   LDAP SDK for Java.  It is not available for use in applications that
 *   include only the Standard Edition of the LDAP SDK, and is not supported for
 *   use in conjunction with non-UnboundID products.
 * </BLOCKQUOTE>
 * This class provides information about a property that may be used to schedule
 * a task.  Elements of a task property include:
 * <UL>
 *   <LI>The name of the LDAP attribute used to store values for this
 *       property.</LI>
 *   <LI>A user-friendly display name for the property.</LI>
 *   <LI>A user-friendly description for the property.</LI>
 *   <LI>The expected data type for values of the property.</LI>
 *   <LI>An optional set of allowed values for the property.  If this is
 *       defined, then the property will not be allowed to have any value that
 *       is not included in this set.</LI>
 *   <LI>A flag that indicates whether the property is required when scheduling
 *       the corresponding type of task.</LI>
 *   <LI>A flag that indicates whether the property is allowed to have multiple
 *       values.</LI>
 *   <LI>A flag that indicates whether the property should be considered
 *       advanced and therefore may be hidden from users if a simplified
 *       interface is to be presented.</LI>
 * </UL>
 */
@NotMutable()
@ThreadSafety(level=ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public final class TaskProperty
       implements Serializable
{
  /**
   * The serial version UID for this serializable class.
   */
  private static final long serialVersionUID = 8438462010090371903L;



  // Indicates whether this task property is advanced.
  private final boolean advanced;

  // Indicates whether this task property is multivalued.
  private final boolean multiValued;

  // Indicates whether this task property is required.
  private final boolean required;

  // The data type for this task property.
  private final Class<?> dataType;

  // The set of allowed values for this task property.
  private final Object[] allowedValues;

  // The name of the LDAP attribute associated with this task property.
  private final String attributeName;

  // The human-readable description for this task property.
  private final String description;

  // The human-readable display name for this task property.
  private final String displayName;



  /**
   * Creates a new task property with the provided information.
   *
   * @param  attributeName  The name of the LDAP attribute associated with this
   *                        task property.  It must not be {@code null}.
   * @param  displayName    The human-readable display name for this task
   *                        property.  It must not be {@code null}.
   * @param  description    The human-readable description for this task
   *                        property.  It must not be {@code null}.
   * @param  dataType       A class representing the data type for this
   *                        property.  Allowed data type classes include:
   *                        {@code Boolean}, {@code Date}, {@code Long}, and
   *                        {@code String}.  It must not be {@code null}.
   * @param  required       Indicates whether this property must be provided
   *                        when scheduling a task.
   * @param  multiValued    Indicates whether this property is allowed to have
   *                        multiple values.
   * @param  advanced       Indicates whether this property may be considered
   *                        advanced and doesn't necessarily need to be
   *                        presented to the user.  Advanced properties must not
   *                        be required.
   */
  public TaskProperty(final String attributeName, final String displayName,
                      final String description, final Class<?> dataType,
                      final boolean required, final boolean multiValued,
                      final boolean advanced)
  {
    this(attributeName, displayName, description, dataType, required,
         multiValued, advanced, null);
  }



  /**
   * Creates a new task property with the provided information.
   *
   * @param  attributeName  The name of the LDAP attribute associated with this
   *                        task property.  It must not be {@code null}.
   * @param  displayName    The human-readable display name for this task
   *                        property.  It must not be {@code null}.
   * @param  description    The human-readable description for this task
   *                        property.  It must not be {@code null}.
   * @param  dataType       A class representing the data type for this
   *                        property.  Allowed data type classes include:
   *                        {@code Boolean}, {@code Date}, {@code Long}, and
   *                        {@code String}.  It must not be {@code null}.
   * @param  required       Indicates whether this property must be provided
   *                        when scheduling a task.
   * @param  multiValued    Indicates whether this property is allowed to have
   *                        multiple values.
   * @param  advanced       Indicates whether this property may be considered
   *                        advanced and doesn't necessarily need to be
   *                        presented to the user.  Advanced properties must not
   *                        be required.
   * @param  allowedValues  The set of allowed values for this task property.
   *                        It may be {@code null} if there is not a predefined
   *                        set of acceptable values.  If it is provided, then
   *                        all values must be objects of the class specified as
   *                        the data type.
   */
  public TaskProperty(final String attributeName, final String displayName,
                      final String description, final Class<?> dataType,
                      final boolean required, final boolean multiValued,
                      final boolean advanced, final Object[] allowedValues)
  {
    ensureNotNull(attributeName, displayName, description, dataType);
    ensureTrue(dataType.equals(Boolean.class) || dataType.equals(Date.class) ||
               dataType.equals(Long.class) || dataType.equals(String.class));
    ensureFalse(required && advanced,
                "TaskProperty.required and advanced must not both be true.");

    this.attributeName = attributeName;
    this.displayName   = displayName;
    this.description   = description;
    this.dataType      = dataType;
    this.required      = required;
    this.multiValued   = multiValued;
    this.advanced      = advanced;

    if ((allowedValues == null) || (allowedValues.length == 0))
    {
      this.allowedValues = null;
    }
    else
    {
      for (final Object o : allowedValues)
      {
        ensureTrue(dataType.equals(o.getClass()));
      }

      this.allowedValues = allowedValues;
    }
  }



  /**
   * Retrieves the name of the LDAP attribute associated with this task
   * property.
   *
   * @return  The name of the LDAP attribute associated with this task property.
   */
  public String getAttributeName()
  {
    return attributeName;
  }



  /**
   * Retrieves the human-readable display name for this task property.
   *
   * @return  The human-readable display name for this task property.
   */
  public String getDisplayName()
  {
    return displayName;
  }



  /**
   * Retrieves the human-readable description for this task property.
   *
   * @return  The human-readable description for this task property.
   */
  public String getDescription()
  {
    return description;
  }



  /**
   * Retrieves the data type for this task property, which represents the
   * expected data type for the value(s) of this property.  Supported data types
   * include {@code Boolean}, {@code Date}, {@code Long}, and {@code String}.
   *
   * @return  The data type for this task property.
   */
  public Class<?> getDataType()
  {
    return dataType;
  }



  /**
   * Indicates whether this task property is required to be provided in order to
   * schedule a task.
   *
   * @return  {@code true} if this task property is required, or {@code false}
   *          if it is not.
   */
  public boolean isRequired()
  {
    return required;
  }



  /**
   * Indicates whether this task property is allowed to have multiple values.
   *
   * @return  {@code true} if this task property is allowed to have multiple
   *          values, or {@code false} if it may only have a single value.
   */
  public boolean isMultiValued()
  {
    return multiValued;
  }



  /**
   * Indicates whether this task property is considered advanced.  Advanced
   * properties are not necessarily required to schedule the task and may be
   * hidden from the user if simplicity is desired over flexibility.
   *
   * @return  {@code true} if this task property is considered advanced, or
   *          {@code false} if not.
   */
  public boolean isAdvanced()
  {
    return advanced;
  }



  /**
   * Retrieves the set of values that may be used for this task property.
   *
   * @return  The set of values that may be used for this task property, or
   *          {@code null} if there is not a predefined set of allowed values.
   */
  public Object[] getAllowedValues()
  {
    return allowedValues;
  }



  /**
   * Retrieves a string representation of this task property.
   *
   * @return  A string representation of this task property.
   */
  @Override()
  public String toString()
  {
    final StringBuilder buffer = new StringBuilder();
    toString(buffer);
    return buffer.toString();
  }



  /**
   * Appends a string representation of this task property to the provided
   * buffer.
   *
   * @param  buffer  The buffer to which the string representation should be
   *                 appended.
   */
  public void toString(final StringBuilder buffer)
  {
    buffer.append("TaskProperty(attrName='");
    buffer.append(attributeName);
    buffer.append("', displayName='");
    buffer.append(displayName);
    buffer.append("', description='");
    buffer.append(description);
    buffer.append("', dataType='");
    buffer.append(dataType.getName());
    buffer.append("', required=");
    buffer.append(required);
    buffer.append("', multiValued=");
    buffer.append(multiValued);
    buffer.append("', advanced=");
    buffer.append(advanced);

    if (allowedValues != null)
    {
      buffer.append(", allowedValues={");
      for (int i=0; i < allowedValues.length; i++)
      {
        if (i > 0)
        {
          buffer.append(", ");
        }

        buffer.append('\'');
        buffer.append(allowedValues[i]);
        buffer.append('\'');
      }
      buffer.append('}');
    }

    buffer.append(')');
  }
}
