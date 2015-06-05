/*
 * Copyright 2010-2015 UnboundID Corp.
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
package com.unboundid.ldap.sdk.unboundidds.examples;



import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.unboundid.ldap.sdk.ExtendedResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.Version;
import com.unboundid.ldap.sdk.unboundidds.extensions.
            GetSubtreeAccessibilityExtendedRequest;
import com.unboundid.ldap.sdk.unboundidds.extensions.
            GetSubtreeAccessibilityExtendedResult;
import com.unboundid.ldap.sdk.unboundidds.extensions.
            SetSubtreeAccessibilityExtendedRequest;
import com.unboundid.ldap.sdk.unboundidds.extensions.
            SubtreeAccessibilityRestriction;
import com.unboundid.ldap.sdk.unboundidds.extensions.SubtreeAccessibilityState;
import com.unboundid.util.Debug;
import com.unboundid.util.LDAPCommandLineTool;
import com.unboundid.util.StaticUtils;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;
import com.unboundid.util.args.ArgumentException;
import com.unboundid.util.args.ArgumentParser;
import com.unboundid.util.args.BooleanArgument;
import com.unboundid.util.args.DNArgument;
import com.unboundid.util.args.StringArgument;



/**
 * <BLOCKQUOTE>
 *   <B>NOTE:</B>  This class is part of the Commercial Edition of the UnboundID
 *   LDAP SDK for Java.  It is not available for use in applications that
 *   include only the Standard Edition of the LDAP SDK, and is not supported for
 *   use in conjunction with non-UnboundID products.
 * </BLOCKQUOTE>
 * This class provides a utility that can be used to query and update the set of
 * subtree accessibility restrictions defined in the Directory Server.
 * <BR><BR>
 * The APIs demonstrated by this example include:
 * <UL>
 *   <LI>The use of the get/set subtree accessibility extended operations</LI>
 *   <LI>The LDAP command-line tool API.</LI>
 *   <LI>Argument parsing.</LI>
 * </UL>
 */
@ThreadSafety(level=ThreadSafetyLevel.NOT_THREADSAFE)
public final class SubtreeAccessibility
       extends LDAPCommandLineTool
       implements Serializable
{
  /**
   * The set of allowed subtree accessibility state values.
   */
  private static final Set<String> ALLOWED_ACCESSIBILITY_STATES;
  static
  {
    final LinkedHashSet<String> stateValues = new LinkedHashSet<String>(4);

    stateValues.add(SubtreeAccessibilityState.ACCESSIBLE.getStateName());
    stateValues.add(
         SubtreeAccessibilityState.READ_ONLY_BIND_ALLOWED.getStateName());
    stateValues.add(
         SubtreeAccessibilityState.READ_ONLY_BIND_DENIED.getStateName());
    stateValues.add(SubtreeAccessibilityState.HIDDEN.getStateName());

    ALLOWED_ACCESSIBILITY_STATES = Collections.unmodifiableSet(stateValues);
  }



  /**
   * The serial version UID for this serializable class.
   */
  private static final long serialVersionUID = 3703682568143472108L;



  // Indicates whether the set of subtree restrictions should be updated rather
  // than queried.
  private BooleanArgument set;

  // The argument used to specify the base DN for the target subtree.
  private DNArgument baseDN;

  // The argument used to specify the DN of a user who can bypass restrictions
  // on the target subtree.
  private DNArgument bypassUserDN;

  // The argument used to specify the accessibility state for the target
  // subtree.
  private StringArgument accessibilityState;



  /**
   * Parse the provided command line arguments and perform the appropriate
   * processing.
   *
   * @param  args  The command line arguments provided to this program.
   */
  public static void main(final String[] args)
  {
    final ResultCode resultCode = main(args, System.out, System.err);
    if (resultCode != ResultCode.SUCCESS)
    {
      System.exit(resultCode.intValue());
    }
  }



  /**
   * Parse the provided command line arguments and perform the appropriate
   * processing.
   *
   * @param  args       The command line arguments provided to this program.
   * @param  outStream  The output stream to which standard out should be
   *                    written.  It may be {@code null} if output should be
   *                    suppressed.
   * @param  errStream  The output stream to which standard error should be
   *                    written.  It may be {@code null} if error messages
   *                    should be suppressed.
   *
   * @return  A result code indicating whether the processing was successful.
   */
  public static ResultCode main(final String[] args,
                                final OutputStream outStream,
                                final OutputStream errStream)
  {
    final SubtreeAccessibility tool =
         new SubtreeAccessibility(outStream, errStream);
    return tool.runTool(args);
  }



  /**
   * Creates a new instance of this tool.
   *
   * @param  outStream  The output stream to which standard out should be
   *                    written.  It may be {@code null} if output should be
   *                    suppressed.
   * @param  errStream  The output stream to which standard error should be
   *                    written.  It may be {@code null} if error messages
   *                    should be suppressed.
   */
  public SubtreeAccessibility(final OutputStream outStream,
                              final OutputStream errStream)
  {
    super(outStream, errStream);

    set                = null;
    baseDN             = null;
    bypassUserDN       = null;
    accessibilityState = null;
  }




  /**
   * Retrieves the name of this tool.  It should be the name of the command used
   * to invoke this tool.
   *
   * @return  The name for this tool.
   */
  @Override()
  public String getToolName()
  {
    return "subtree-accessibility";
  }



  /**
   * Retrieves a human-readable description for this tool.
   *
   * @return  A human-readable description for this tool.
   */
  @Override()
  public String getToolDescription()
  {
    return "List or update the a set of subtree accessibility restrictions " +
         "defined in the Directory Server.";
  }



  /**
   * Retrieves the version string for this tool.
   *
   * @return  The version string for this tool.
   */
  @Override()
  public String getToolVersion()
  {
    return Version.NUMERIC_VERSION_STRING;
  }



  /**
   * Adds the arguments needed by this command-line tool to the provided
   * argument parser which are not related to connecting or authenticating to
   * the directory server.
   *
   * @param  parser  The argument parser to which the arguments should be added.
   *
   * @throws  ArgumentException  If a problem occurs while adding the arguments.
   */
  @Override()
  public void addNonLDAPArguments(final ArgumentParser parser)
         throws ArgumentException
  {
    set = new BooleanArgument('s', "set", 1,
         "Indicates that the set of accessibility restrictions should be " +
              "updated rather than retrieved.");
    parser.addArgument(set);


    baseDN = new DNArgument('b', "baseDN", false, 1, "{dn}",
         "The base DN of the subtree for which an accessibility restriction " +
              "is to be updated.");
    parser.addArgument(baseDN);


    accessibilityState = new StringArgument('S', "state", false, 1, "{state}",
         "The accessibility state to use for the accessibility restriction " +
              "on the target subtree.  Allowed values:  " +
              SubtreeAccessibilityState.ACCESSIBLE.getStateName() + ", " +
              SubtreeAccessibilityState.READ_ONLY_BIND_ALLOWED.getStateName() +
              ", " +
              SubtreeAccessibilityState.READ_ONLY_BIND_DENIED.getStateName() +
              ", " + SubtreeAccessibilityState.HIDDEN.getStateName() + '.',
         ALLOWED_ACCESSIBILITY_STATES);
    parser.addArgument(accessibilityState);


    bypassUserDN = new DNArgument('B', "bypassUserDN", false, 1, "{dn}",
         "The DN of a user who is allowed to bypass restrictions on the " +
              "target subtree.");
    parser.addArgument(bypassUserDN);


    // The baseDN, accessibilityState, and bypassUserDN arguments can only be
    // used if the set argument was provided.
    parser.addDependentArgumentSet(baseDN, set);
    parser.addDependentArgumentSet(accessibilityState, set);
    parser.addDependentArgumentSet(bypassUserDN, set);


    // If the set argument was provided, then the base DN and accessibilityState
    // arguments must also be given.
    parser.addDependentArgumentSet(set, baseDN);
    parser.addDependentArgumentSet(set, accessibilityState);
  }



  /**
   * Performs the core set of processing for this tool.
   *
   * @return  A result code that indicates whether the processing completed
   *          successfully.
   */
  @Override()
  public ResultCode doToolProcessing()
  {
    // Get a connection to the target directory server.
    final LDAPConnection connection;
    try
    {
      connection = getConnection();
    }
    catch (final LDAPException le)
    {
      Debug.debugException(le);
      err("Unable to establish a connection to the target directory server:  ",
           StaticUtils.getExceptionMessage(le));
      return le.getResultCode();
    }

    try
    {
      // See whether to do a get or set operation and call the appropriate
      // method.
      if (set.isPresent())
      {
        return doSet(connection);
      }
      else
      {
        return doGet(connection);
      }
    }
    finally
    {
      connection.close();
    }
  }



  /**
   * Does the work necessary to retrieve the set of subtree accessibility
   * restrictions defined in the server.
   *
   * @param  connection  The connection to use to communicate with the server.
   *
   * @return  A result code with information about the result of operation
   *          processing.
   */
  private ResultCode doGet(final LDAPConnection connection)
  {
    final GetSubtreeAccessibilityExtendedResult result;
    try
    {
      result = (GetSubtreeAccessibilityExtendedResult)
           connection.processExtendedOperation(
                new GetSubtreeAccessibilityExtendedRequest());
    }
    catch (final LDAPException le)
    {
      Debug.debugException(le);
      err("An error occurred while attempting to invoke the get subtree " +
           "accessibility request:  ", StaticUtils.getExceptionMessage(le));
      return le.getResultCode();
    }

    if (result.getResultCode() != ResultCode.SUCCESS)
    {
      err("The server returned an error for the get subtree accessibility " +
           "request:  ", result.getDiagnosticMessage());
      return result.getResultCode();
    }

    final List<SubtreeAccessibilityRestriction> restrictions =
         result.getAccessibilityRestrictions();
    if ((restrictions == null) || restrictions.isEmpty())
    {
      out("There are no subtree accessibility restrictions defined in the " +
           "server.");
      return ResultCode.SUCCESS;
    }

    if (restrictions.size() == 1)
    {
      out("1 subtree accessibility restriction was found in the server:");
    }
    else
    {
      out(restrictions.size(),
           " subtree accessibility restrictions were found in the server:");
    }

    for (final SubtreeAccessibilityRestriction r : restrictions)
    {
      out("Subtree Base DN:      ", r.getSubtreeBaseDN());
      out("Accessibility State:  ", r.getAccessibilityState().getStateName());

      final String bypassDN = r.getBypassUserDN();
      if (bypassDN != null)
      {
        out("Bypass User DN:       ", bypassDN);
      }

      out("Effective Time:       ", r.getEffectiveTime());
      out();
    }

    return ResultCode.SUCCESS;
  }



  /**
   * Does the work necessary to update a subtree accessibility restriction
   * defined in the server.
   *
   * @param  connection  The connection to use to communicate with the server.
   *
   * @return  A result code with information about the result of operation
   *          processing.
   */
  private ResultCode doSet(final LDAPConnection connection)
  {
    final SubtreeAccessibilityState state =
         SubtreeAccessibilityState.forName(accessibilityState.getValue());
    if (state == null)
    {
      // This should never happen.
      err("Unsupported subtree accessibility state ",
           accessibilityState.getValue());
      return ResultCode.PARAM_ERROR;
    }

    final SetSubtreeAccessibilityExtendedRequest request;
    switch (state)
    {
      case ACCESSIBLE:
        request = SetSubtreeAccessibilityExtendedRequest.
             createSetAccessibleRequest(baseDN.getStringValue());
        break;
      case READ_ONLY_BIND_ALLOWED:
        request = SetSubtreeAccessibilityExtendedRequest.
             createSetReadOnlyRequest(baseDN.getStringValue(), true,
                  bypassUserDN.getStringValue());
        break;
      case READ_ONLY_BIND_DENIED:
        request = SetSubtreeAccessibilityExtendedRequest.
             createSetReadOnlyRequest(baseDN.getStringValue(), false,
                  bypassUserDN.getStringValue());
        break;
      case HIDDEN:
        request = SetSubtreeAccessibilityExtendedRequest.createSetHiddenRequest(
             baseDN.getStringValue(), bypassUserDN.getStringValue());
        break;
      default:
        // This should never happen.
        err("Unsupported subtree accessibility state ", state.getStateName());
        return ResultCode.PARAM_ERROR;
    }

    final ExtendedResult result;
    try
    {
      result = connection.processExtendedOperation(request);
    }
    catch (final LDAPException le)
    {
      Debug.debugException(le);
      err("An error occurred while attempting to invoke the set subtree " +
           "accessibility request:  ", StaticUtils.getExceptionMessage(le));
      return le.getResultCode();
    }

    if (result.getResultCode() == ResultCode.SUCCESS)
    {
      out("Successfully set an accessibility state of ", state.getStateName(),
           " for subtree ", baseDN.getStringValue());
    }
    else
    {
      out("Unable to set an accessibility state of ", state.getStateName(),
           " for subtree ", baseDN.getStringValue(), ":  ",
           result.getDiagnosticMessage());
    }

    return result.getResultCode();
  }



  /**
   * Retrieves a set of information that may be used to generate example usage
   * information.  Each element in the returned map should consist of a map
   * between an example set of arguments and a string that describes the
   * behavior of the tool when invoked with that set of arguments.
   *
   * @return  A set of information that may be used to generate example usage
   *          information.  It may be {@code null} or empty if no example usage
   *          information is available.
   */
  @Override()
  public LinkedHashMap<String[],String> getExampleUsages()
  {
    final LinkedHashMap<String[],String> exampleMap =
         new LinkedHashMap<String[],String>(2);

    final String[] getArgs =
    {
      "--hostname", "server.example.com",
      "--port", "389",
      "--bindDN", "uid=admin,dc=example,dc=com",
      "--bindPassword", "password",
    };
    exampleMap.put(getArgs,
         "Retrieve information about all subtree accessibility restrictions " +
              "defined in the server.");

    final String[] setArgs =
    {
      "--hostname", "server.example.com",
      "--port", "389",
      "--bindDN", "uid=admin,dc=example,dc=com",
      "--bindPassword", "password",
      "--set",
      "--baseDN", "ou=subtree,dc=example,dc=com",
      "--state", "read-only-bind-allowed",
      "--bypassUserDN", "uid=bypass,dc=example,dc=com"
    };
    exampleMap.put(setArgs,
         "Create or update the subtree accessibility state definition for " +
              "subtree 'ou=subtree,dc=example,dc=com' so that it is " +
              "read-only for all users except 'uid=bypass,dc=example,dc=com'.");

    return exampleMap;
  }
}
