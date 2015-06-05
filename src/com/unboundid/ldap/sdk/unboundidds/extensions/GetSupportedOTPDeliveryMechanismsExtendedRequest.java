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
package com.unboundid.ldap.sdk.unboundidds.extensions;



import com.unboundid.asn1.ASN1Element;
import com.unboundid.asn1.ASN1OctetString;
import com.unboundid.asn1.ASN1Sequence;
import com.unboundid.ldap.sdk.Control;
import com.unboundid.ldap.sdk.ExtendedRequest;
import com.unboundid.ldap.sdk.ExtendedResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.util.Debug;
import com.unboundid.util.NotMutable;
import com.unboundid.util.StaticUtils;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;

import static com.unboundid.ldap.sdk.unboundidds.extensions.ExtOpMessages.*;



/**
 * <BLOCKQUOTE>
 *   <B>NOTE:</B>  This class is part of the Commercial Edition of the UnboundID
 *   LDAP SDK for Java.  It is not available for use in applications that
 *   include only the Standard Edition of the LDAP SDK, and is not supported for
 *   use in conjunction with non-UnboundID products.
 * </BLOCKQUOTE>
 * This class provides an implementation of an extended request that can be used
 * to retrieve information about which one-time password delivery mechanisms are
 * supported for a user.
 * <BR><BR>
 * The OID for this extended request is "1.3.6.1.4.1.30221.2.6.47".  It must
 * have a value with the following encoding:
 * <BR><BR>
 * <PRE>
 *   GetSupportedOTPDeliveryMechanismsRequest ::= SEQUENCE {
 *        userDN     [0] LDAPDN,
 *        ... }
 * </PRE>
 *
 * @see  GetSupportedOTPDeliveryMechanismsExtendedResult
 */
@NotMutable()
@ThreadSafety(level=ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public final class GetSupportedOTPDeliveryMechanismsExtendedRequest
       extends ExtendedRequest
{
  /**
   * The OID (1.3.6.1.4.1.30221.2.6.47) for the get supported one-time password
   * delivery mechanisms extended request.
   */
  public static final String GET_SUPPORTED_OTP_DELIVERY_MECHANISMS_REQUEST_OID =
       "1.3.6.1.4.1.30221.2.6.47";



  /**
   * The BER type for the userDN element.
   */
  private static final byte TYPE_USER_DN = (byte) 0x80;



  /**
   * The serial version UID for this serializable class.
   */
  private static final long serialVersionUID = -1670631089524097883L;



  // THe DN of the user for whom to retrieve the supported delivery mechanisms.
  private final String userDN;



  /**
   * Creates a new instance of this get supported OTP delivery mechanisms
   * extended request with the provided information.
   *
   * @param  userDN    The DN of the user for whom to retrieve the list of
   *                   supported OTP delivery mechanisms.  It must not be
   *                   {@code null}.
   * @param  controls  The set of controls to include in the request.  It may be
   *                   {@code null} or empty if no controls should be included.
   */
  public GetSupportedOTPDeliveryMechanismsExtendedRequest(final String userDN,
              final Control... controls)
  {
    super(GET_SUPPORTED_OTP_DELIVERY_MECHANISMS_REQUEST_OID,
         encodeValue(userDN), controls);

    this.userDN = userDN;
  }



  /**
   * Decodes the provided extended request as a get supported OTP delivery
   * mechanisms request.
   *
   * @param  request  The extended request to be decoded as a get supported OTP
   *                  delivery mechanisms request.
   *
   * @throws  LDAPException  If the provided request cannot be decoded as a get
   *                         supported OTP delivery mechanisms request.
   */
  public GetSupportedOTPDeliveryMechanismsExtendedRequest(
              final ExtendedRequest request)
         throws LDAPException
  {
    super(request);

    final ASN1OctetString value = request.getValue();
    if (value == null)
    {
      throw new LDAPException(ResultCode.DECODING_ERROR,
           ERR_GET_SUPPORTED_OTP_MECH_REQUEST_NO_VALUE.get());
    }

    try
    {
      final ASN1Element[] elements =
           ASN1Sequence.decodeAsSequence(value.getValue()).elements();
      userDN = ASN1OctetString.decodeAsOctetString(elements[0]).stringValue();
    }
    catch (final Exception e)
    {
      Debug.debugException(e);
      throw new LDAPException(ResultCode.DECODING_ERROR,
           ERR_GET_SUPPORTED_OTP_MECH_REQUEST_CANNOT_DECODE.get(
                StaticUtils.getExceptionMessage(e)),
           e);
    }
  }



  /**
   * Encodes the provided information into an ASN.1 octet string suitable for
   * use as the value for this extended operation.
   *
   * @param  userDN  The DN of the user for whom to retrieve the list of
   *                 supported OTP delivery mechanisms.  It must not be
   *                 {@code null}.
   *
   * @return  The ASN.1 octet string containing the encoded control value.
   */
  private static ASN1OctetString encodeValue(final String userDN)
  {
    return new ASN1OctetString(new ASN1Sequence(
         new ASN1OctetString(TYPE_USER_DN, userDN)).encode());
  }



  /**
   * Retrieves the DN of the user for whom to retrieve the list of supported OTP
   * delivery mechanisms.
   *
   * @return  The DN of the user for whom to retrieve the list of supported OTP
   *          delivery mechanisms.
   */
  public String getUserDN()
  {
    return userDN;
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public GetSupportedOTPDeliveryMechanismsExtendedResult process(
              final LDAPConnection connection, final int depth)
         throws LDAPException
  {
    final ExtendedResult extendedResponse = super.process(connection, depth);
    return new GetSupportedOTPDeliveryMechanismsExtendedResult(
         extendedResponse);
  }



  /**
   * {@inheritDoc}.
   */
  @Override()
  public GetSupportedOTPDeliveryMechanismsExtendedRequest duplicate()
  {
    return duplicate(getControls());
  }



  /**
   * {@inheritDoc}.
   */
  @Override()
  public GetSupportedOTPDeliveryMechanismsExtendedRequest duplicate(
              final Control[] controls)
  {
    final GetSupportedOTPDeliveryMechanismsExtendedRequest r =
         new GetSupportedOTPDeliveryMechanismsExtendedRequest(userDN,
              controls);
    r.setResponseTimeoutMillis(getResponseTimeoutMillis(null));
    return r;
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public String getExtendedRequestName()
  {
    return INFO_GET_SUPPORTED_OTP_MECH_REQ_NAME.get();
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public void toString(final StringBuilder buffer)
  {
    buffer.append("GetSupportedOTPDeliveryMechanismsExtendedRequest(userDN='");
    buffer.append(userDN);
    buffer.append('\'');

    final Control[] controls = getControls();
    if (controls.length > 0)
    {
      buffer.append(", controls={");
      for (int i=0; i < controls.length; i++)
      {
        if (i > 0)
        {
          buffer.append(", ");
        }

        buffer.append(controls[i]);
      }
      buffer.append('}');
    }

    buffer.append(')');
  }
}
