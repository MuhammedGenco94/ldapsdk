/*
 * Copyright 2015 UnboundID Corp.
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
package com.unboundid.ldap.sdk.unboundidds.jsonfilter;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.unboundid.util.Mutable;
import com.unboundid.util.StaticUtils;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;
import com.unboundid.util.Validator;
import com.unboundid.util.json.JSONArray;
import com.unboundid.util.json.JSONBoolean;
import com.unboundid.util.json.JSONException;
import com.unboundid.util.json.JSONObject;
import com.unboundid.util.json.JSONString;
import com.unboundid.util.json.JSONValue;

import static com.unboundid.ldap.sdk.unboundidds.jsonfilter.JFMessages.*;



/**
 * <BLOCKQUOTE>
 *   <B>NOTE:</B>  This class is part of the Commercial Edition of the UnboundID
 *   LDAP SDK for Java.  It is not available for use in applications that
 *   include only the Standard Edition of the LDAP SDK, and is not supported for
 *   use in conjunction with non-UnboundID products.
 * </BLOCKQUOTE>
 * This class provides an implementation of a JSON object filter that can be
 * used to identify JSON objects that have string value that matches a specified
 * substring.  At least one of the {@code startsWith}, {@code contains}, and
 * {@code endsWith} components must be included in the filter.  If multiple
 * substring components are present, then any matching value must contain all
 * of those components, and the components must not overlap.
 * <BR><BR>
 * The fields that are required to be included in a "substring" filter are:
 * <UL>
 *   <LI>
 *     {@code field} -- A field path specifier for the JSON field for which
 *     to make the determination.  This may be either a single string or an
 *     array of strings as described in the "Targeting Fields in JSON Objects"
 *     section of the class-level documentation for {@link JSONObjectFilter}.
 *   </LI>
 * </UL>
 * The fields that may optionally be included in a "substring" filter are:
 * <UL>
 *   <LI>
 *     {@code startsWith} -- A string that must appear at the beginning of
 *     matching values.
 *   </LI>
 *   <LI>
 *     {@code contains} -- A string, or an array of strings, that must appear in
 *     matching values.  If this is an array of strings, then a matching value
 *     must contain all of these strings in the order provided in the array.
 *   </LI>
 *   <LI>
 *     {@code endsWith} -- A string that must appear at the end of matching
 *     values.
 *   </LI>
 *   <LI>
 *     {@code caseSensitive} -- Indicates whether string values should be
 *     treated in a case-sensitive manner.  If present, this field must have a
 *     Boolean value of either {@code true} or {@code false}.  If it is not
 *     provided, then a default value of {@code false} will be assumed so that
 *     strings are treated in a case-insensitive manner.
 *   </LI>
 * </UL>
 * <H2>Examples</H2>
 * The following is an example of a substring filter that will match any JSON
 * object with a top-level field named "accountCreateTime" with a string value
 * that starts with "2015":
 * <PRE>
 *   { "filterType" : "substring",
 *     "field" : "accountCreateTime",
 *     "startsWith" : "2015" }
 * </PRE>
 * The above filter can be created with the code:
 * <PRE>
 *   SubstringJSONObjectFilter filter =
 *        new SubstringJSONObjectFilter("accountCreateTime", "2015", null,
 *             null);
 * </PRE>
 * <BR><BR>
 * The following is an example of a substring filter that will match any JSON
 * object with a top-level field named "fullName" that contains the substrings
 * "John" and "Doe", in that order, somewhere in the value:
 * <PRE>
 *   { "filterType" : "substring",
 *     "field" : "fullName",
 *     "contains" : [ "John", "Doe" ] }
 * </PRE>
 * The above filter can be created with the code:
 * <PRE>
 *   SubstringJSONObjectFilter filter =
 *        new SubstringJSONObjectFilter(Collections.singletonList("fullName"),
 *             null, Arrays.asList("John", "Doe"), null);
 * </PRE>
 */
@Mutable()
@ThreadSafety(level=ThreadSafetyLevel.NOT_THREADSAFE)
public final class SubstringJSONObjectFilter
       extends JSONObjectFilter
{
  /**
   * The value that should be used for the filterType element of the JSON object
   * that represents a "substring" filter.
   */
  public static final String FILTER_TYPE = "substring";



  /**
   * The name of the JSON field that is used to specify the field in the target
   * JSON object for which to make the determination.
   */
  public static final String FIELD_FIELD_PATH = "field";



  /**
   * The name of the JSON field that is used to specify a string that must
   * appear at the beginning of a matching value.
   */
  public static final String FIELD_STARTS_WITH = "startsWith";



  /**
   * The name of the JSON field that is used to specify one or more strings
   * that must appear somewhere in a matching value.
   */
  public static final String FIELD_CONTAINS = "contains";



  /**
   * The name of the JSON field that is used to specify a string that must
   * appear at the end of a matching value.
   */
  public static final String FIELD_ENDS_WITH = "endsWith";



  /**
   * The name of the JSON field that is used to indicate whether string matching
   * should be case-sensitive.
   */
  public static final String FIELD_CASE_SENSITIVE = "caseSensitive";



  /**
   * The pre-allocated set of required field names.
   */
  private static final Set<String> REQUIRED_FIELD_NAMES =
       Collections.unmodifiableSet(new HashSet<String>(
            Collections.singletonList(FIELD_FIELD_PATH)));



  /**
   * The pre-allocated set of optional field names.
   */
  private static final Set<String> OPTIONAL_FIELD_NAMES =
       Collections.unmodifiableSet(new HashSet<String>(
            Arrays.asList(FIELD_STARTS_WITH, FIELD_CONTAINS, FIELD_ENDS_WITH,
                 FIELD_CASE_SENSITIVE)));


  /**
   * The serial version UID for this serializable class.
   */
  private static final long serialVersionUID = 811514243548895420L;



  // Indicates whether string matching should be case-sensitive.
  private volatile boolean caseSensitive;

  // The minimum length that a string must have to match the substring
  // assertion.
  private volatile int minLength;

  // The substring(s) that must appear somewhere in matching values.
  private volatile List<String> contains;

  // The "contains" values that should be used for matching purposes.  If
  // caseSensitive is false, then this will be an all-lowercase version of
  // contains.  Otherwise, it will be the same as contains.
  private volatile List<String> matchContains;

  // The field path specifier for the target field.
  private volatile List<String> field;

  // The substring that must appear at the end of matching values.
  private volatile String endsWith;

  // The "ends with" value that should be used for matching purposes.  If
  // caseSensitive is false, then this will be an all-lowercase version of
  // endsWith.  Otherwise, it will be the same as endsWith.
  private volatile String matchEndsWith;

  // The "starts with" value that should be used for matching purposes.  If
  // caseSensitive is false, then this will be an all-lowercase version of
  // startsWith.  Otherwise, it will be the same as startsWith.
  private volatile String matchStartsWith;

  // The substring that must appear at the beginning of matching values.
  private volatile String startsWith;



  /**
   * Creates an instance of this filter type that can only be used for decoding
   * JSON objects as "substring" filters.  It cannot be used as a regular
   * "substring" filter.
   */
  SubstringJSONObjectFilter()
  {
    field = null;
    startsWith = null;
    contains = null;
    endsWith = null;
    caseSensitive = false;

    minLength = 0;
    matchStartsWith = null;
    matchContains = null;
    matchEndsWith = null;
  }



  /**
   * Creates a new instance of this filter type with the provided information.
   *
   * @param  field          The field path specifier for the target field.
   * @param  startsWith     The substring that must appear at the beginning of
   *                        matching values.
   * @param  contains       The substrings that must appear somewhere in
   *                        matching values.
   * @param  endsWith       The substring that must appear at the end of
   *                        matching values.
   * @param  caseSensitive  Indicates whether matching should be case sensitive.
   */
  private SubstringJSONObjectFilter(final List<String> field,
                                    final String startsWith,
                                    final List<String> contains,
                                    final String endsWith,
                                    final boolean caseSensitive)
  {
    this.field = field;
    this.caseSensitive = caseSensitive;

    setSubstringComponents(startsWith, contains, endsWith);
  }



  /**
   * Creates a new instance of this filter type with the provided information.
   * At least one {@code startsWith}, {@code contains}, or {@code endsWith}
   * value must be present.
   *
   * @param  field       The name of the top-level field to target with this
   *                     filter.  It must not be {@code null} .  See the
   *                     class-level documentation for the
   *                     {@link JSONObjectFilter} class for information about
   *                     field path specifiers.
   * @param  startsWith  An optional substring that must appear at the beginning
   *                     of matching values.  This may be {@code null} if
   *                     matching will be performed using only {@code contains}
   *                     and/or {@code endsWith} substrings.
   * @param  contains    An optional substring that must appear somewhere in
   *                     matching values.  This may be {@code null} if matching
   *                     will be performed using only {@code startsWith} and/or
   *                     {@code endsWith} substrings.
   * @param  endsWith    An optional substring that must appear at the end
   *                     of matching values.  This may be {@code null} if
   *                     matching will be performed using only
   *                     {@code startsWith} and/or {@code contains} substrings.
   */
  public SubstringJSONObjectFilter(final String field, final String startsWith,
                                   final String contains, final String endsWith)
  {
    this(Collections.singletonList(field), startsWith,
         ((contains == null) ? null : Collections.singletonList(contains)),
         endsWith);
  }



  /**
   * Creates a new instance of this filter type with the provided information.
   * At least one {@code startsWith}, {@code contains}, or {@code endsWith}
   * value must be present.
   *
   * @param  field       The field path specifier for this filter.  It must not
   *                     be {@code null} or empty.  See the class-level
   *                     documentation for the {@link JSONObjectFilter} class
   *                     for information about field path specifiers.
   * @param  startsWith  An optional substring that must appear at the beginning
   *                     of matching values.  This may be {@code null} if
   *                     matching will be performed using only {@code contains}
   *                     and/or {@code endsWith} substrings.
   * @param  contains    An optional set of substrings that must appear
   *                     somewhere in matching values.  This may be {@code null}
   *                     or empty if matching will be performed using only
   *                     {@code startsWith} and/or {@code endsWith} substrings.
   * @param  endsWith    An optional substring that must appear at the end
   *                     of matching values.  This may be {@code null} if
   *                     matching will be performed using only
   *                     {@code startsWith} and/or {@code contains} substrings.
   */
  public SubstringJSONObjectFilter(final List<String> field,
                                   final String startsWith,
                                   final List<String> contains,
                                   final String endsWith)
  {
    Validator.ensureNotNull(field);
    Validator.ensureFalse(field.isEmpty());

    this.field = Collections.unmodifiableList(new ArrayList<String>(field));
    caseSensitive = false;

    setSubstringComponents(startsWith, contains, endsWith);
  }



  /**
   * Retrieves the field path specifier for this filter.
   *
   * @return  The field path specifier for this filter.
   */
  public List<String> getField()
  {
    return field;
  }



  /**
   * Sets the field path specifier for this filter.
   *
   * @param  field  The field path specifier for this filter.  It must not be
   *                {@code null} or empty.  See the class-level documentation
   *                for the {@link JSONObjectFilter} class for information about
   *                field path specifiers.
   */
  public void setField(final String... field)
  {
    setField(StaticUtils.toList(field));
  }



  /**
   * Sets the field path specifier for this filter.
   *
   * @param  field  The field path specifier for this filter.  It must not be
   *                {@code null} or empty.  See the class-level documentation
   *                for the {@link JSONObjectFilter} class for information about
   *                field path specifiers.
   */
  public void setField(final List<String> field)
  {
    Validator.ensureNotNull(field);
    Validator.ensureFalse(field.isEmpty());

    this.field= Collections.unmodifiableList(new ArrayList<String>(field));
  }



  /**
   * Retrieves the substring that must appear at the beginning of matching
   * values, if defined.
   *
   * @return  The substring that must appear at the beginning of matching
   *          values, or {@code null} if no "starts with" substring has been
   *          defined.
   */
  public String getStartsWith()
  {
    return startsWith;
  }



  /**
   * Retrieves the list of strings that must appear somewhere in the value
   * (after any defined "starts with" value, and before any defined "ends with"
   * value).
   *
   * @return  The list of strings that must appear somewhere in the value, or
   *          an empty list if no "contains" substrings have been defined.
   */
  public List<String> getContains()
  {
    return contains;
  }



  /**
   * Retrieves the substring that must appear at the end of matching values, if
   * defined.
   *
   * @return  The substring that must appear at the end of matching values, or
   *          {@code null} if no "starts with" substring has been defined.
   */
  public String getEndsWith()
  {
    return endsWith;
  }



  /**
   * Specifies the substring components that must be present in matching values.
   * At least one {@code startsWith}, {@code contains}, or {@code endsWith}
   * value must be present.
   *
   * @param  startsWith  An optional substring that must appear at the beginning
   *                     of matching values.  This may be {@code null} if
   *                     matching will be performed using only {@code contains}
   *                     and/or {@code endsWith} substrings.
   * @param  contains    An optional substring that must appear somewhere in
   *                     matching values.  This may be {@code null} if matching
   *                     will be performed using only {@code startsWith} and/or
   *                     {@code endsWith} substrings.
   * @param  endsWith    An optional substring that must appear at the end
   *                     of matching values.  This may be {@code null} if
   *                     matching will be performed using only
   *                     {@code startsWith} and/or {@code contains} substrings.
   */
  public void setSubstringComponents(final String startsWith,
                                     final String contains,
                                     final String endsWith)
  {
    setSubstringComponents(startsWith,
         (contains == null) ? null : Collections.singletonList(contains),
         endsWith);
  }



  /**
   * Specifies the substring components that must be present in matching values.
   * At least one {@code startsWith}, {@code contains}, or {@code endsWith}
   * value must be present.
   *
   * @param  startsWith  An optional substring that must appear at the beginning
   *                     of matching values.  This may be {@code null} if
   *                     matching will be performed using only {@code contains}
   *                     and/or {@code endsWith} substrings.
   * @param  contains    An optional set of substrings that must appear
   *                     somewhere in matching values.  This may be {@code null}
   *                     or empty if matching will be performed using only
   *                     {@code startsWith} and/or {@code endsWith} substrings.
   * @param  endsWith    An optional substring that must appear at the end
   *                     of matching values.  This may be {@code null} if
   *                     matching will be performed using only
   *                     {@code startsWith} and/or {@code contains} substrings.
   */
  public void setSubstringComponents(final String startsWith,
                                     final List<String> contains,
                                     final String endsWith)
  {
    Validator.ensureFalse((startsWith == null) && (contains == null) &&
         (endsWith == null));

    minLength = 0;

    this.startsWith = startsWith;
    if (startsWith != null)
    {
      minLength += startsWith.length();
      if (caseSensitive)
      {
        matchStartsWith = startsWith;
      }
      else
      {
        matchStartsWith = StaticUtils.toLowerCase(startsWith);
      }
    }

    if (contains == null)
    {
      this.contains = Collections.emptyList();
      matchContains = this.contains;
    }
    else
    {
      this.contains =
           Collections.unmodifiableList(new ArrayList<String>(contains));

      final ArrayList<String> mcList = new ArrayList<String>(contains.size());
      for (final String s : contains)
      {
        minLength += s.length();
        if (caseSensitive)
        {
          mcList.add(s);
        }
        else
        {
          mcList.add(StaticUtils.toLowerCase(s));
        }
      }

      matchContains = Collections.unmodifiableList(mcList);
    }

    this.endsWith = endsWith;
    if (endsWith != null)
    {
      minLength += endsWith.length();
      if (caseSensitive)
      {
        matchEndsWith = endsWith;
      }
      else
      {
        matchEndsWith = StaticUtils.toLowerCase(endsWith);
      }
    }
  }



  /**
   * Indicates whether string matching should be performed in a case-sensitive
   * manner.
   *
   * @return  {@code true} if string matching should be case sensitive, or
   *          {@code false} if not.
   */
  public boolean caseSensitive()
  {
    return caseSensitive;
  }



  /**
   * Specifies whether string matching should be performed in a case-sensitive
   * manner.
   *
   * @param  caseSensitive  Indicates whether string matching should be
   *                        case sensitive.
   */
  public void setCaseSensitive(final boolean caseSensitive)
  {
    this.caseSensitive = caseSensitive;
    setSubstringComponents(startsWith, contains, endsWith);
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public String getFilterType()
  {
    return FILTER_TYPE;
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  protected Set<String> getRequiredFieldNames()
  {
    return REQUIRED_FIELD_NAMES;
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  protected Set<String> getOptionalFieldNames()
  {
    return OPTIONAL_FIELD_NAMES;
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public boolean matchesJSONObject(final JSONObject o)
  {
    final List<JSONValue> candidates = getValues(o, field);
    if (candidates.isEmpty())
    {
      return false;
    }

    for (final JSONValue v : candidates)
    {
      if (v instanceof JSONString)
      {
        if (matchesSubstring(v))
        {
          return true;
        }
      }
      else if (v instanceof JSONArray)
      {
        for (final JSONValue arrayValue : ((JSONArray) v).getValues())
        {
          if (matchesSubstring(arrayValue))
          {
            return true;
          }
        }
      }
    }

    return false;
  }



  /**
   * Indicates whether the provided JSON value matches the substring assertion
   * defined in this filter.
   *
   * @param  v  The value for which to make the determination.
   *
   * @return  {@code true} if the provided value matches this substring
   *          assertion, or {@code false} if not.
   */
  private boolean matchesSubstring(final JSONValue v)
  {
    if (! (v instanceof JSONString))
    {
      return false;
    }

    final String stringValue;
    if (caseSensitive)
    {
      stringValue = ((JSONString) v).stringValue();
    }
    else
    {
      stringValue = StaticUtils.toLowerCase(((JSONString) v).stringValue());
    }

    if (stringValue.length() < minLength)
    {
      return false;
    }

    final StringBuilder buffer = new StringBuilder(stringValue);
    if (matchStartsWith != null)
    {
      if (buffer.indexOf(matchStartsWith) != 0)
      {
        return false;
      }
      buffer.delete(0, matchStartsWith.length());
    }

    if (matchEndsWith != null)
    {
      final int lengthMinusEndsWith = buffer.length() - matchEndsWith.length();
      if (buffer.lastIndexOf(matchEndsWith) != lengthMinusEndsWith)
      {
        return false;
      }
      buffer.setLength(lengthMinusEndsWith);
    }

    for (final String s : matchContains)
    {
      final int index = buffer.indexOf(s);
      if (index < 0)
      {
        return false;
      }
      buffer.delete(0, (index+s.length()));
    }

    return true;
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public JSONObject toJSONObject()
  {
    final LinkedHashMap<String,JSONValue> fields =
         new LinkedHashMap<String,JSONValue>(6);

    fields.put(FIELD_FILTER_TYPE, new JSONString(FILTER_TYPE));

    if (field.size() == 1)
    {
      fields.put(FIELD_FIELD_PATH, new JSONString(field.get(0)));
    }
    else
    {
      final ArrayList<JSONValue> fieldNameValues =
           new ArrayList<JSONValue>(field.size());
      for (final String s : field)
      {
        fieldNameValues.add(new JSONString(s));
      }
      fields.put(FIELD_FIELD_PATH, new JSONArray(fieldNameValues));
    }

    if (startsWith != null)
    {
      fields.put(FIELD_STARTS_WITH, new JSONString(startsWith));
    }

    if (! contains.isEmpty())
    {
      if (contains.size() == 1)
      {
        fields.put(FIELD_CONTAINS, new JSONString(contains.get(0)));
      }
      else
      {
        final ArrayList<JSONValue> containsValues =
             new ArrayList<JSONValue>(contains.size());
        for (final String s : contains)
        {
          containsValues.add(new JSONString(s));
        }
        fields.put(FIELD_CONTAINS, new JSONArray(containsValues));
      }
    }

    if (endsWith != null)
    {
      fields.put(FIELD_ENDS_WITH, new JSONString(endsWith));
    }

    if (caseSensitive)
    {
      fields.put(FIELD_CASE_SENSITIVE, JSONBoolean.TRUE);
    }

    return new JSONObject(fields);
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  protected SubstringJSONObjectFilter decodeFilter(
                                           final JSONObject filterObject)
            throws JSONException
  {
    final List<String> fieldPath =
         getStrings(filterObject, FIELD_FIELD_PATH, false, null);

    final String subInitial = getString(filterObject, FIELD_STARTS_WITH, null,
         false);

    final List<String> subAny = getStrings(filterObject, FIELD_CONTAINS, true,
         Collections.<String>emptyList());

    final String subFinal = getString(filterObject, FIELD_ENDS_WITH, null,
         false);

    if ((subInitial == null) && (subFinal == null) && subAny.isEmpty())
    {
      throw new JSONException(ERR_SUBSTRING_FILTER_NO_COMPONENTS.get(
           String.valueOf(filterObject), FILTER_TYPE, FIELD_STARTS_WITH,
           FIELD_CONTAINS, FIELD_ENDS_WITH));
    }

    final boolean isCaseSensitive = getBoolean(filterObject,
         FIELD_CASE_SENSITIVE, false);

    return new SubstringJSONObjectFilter(fieldPath, subInitial, subAny,
         subFinal, isCaseSensitive);
  }
}
