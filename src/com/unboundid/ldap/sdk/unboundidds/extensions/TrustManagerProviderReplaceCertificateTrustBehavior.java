/*
 * Copyright 2021 Ping Identity Corporation
 * All Rights Reserved.
 */
/*
 * Copyright 2021 Ping Identity Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Copyright (C) 2021 Ping Identity Corporation
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
import com.unboundid.util.NotNull;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;
import com.unboundid.util.Validator;



/**
 * This class provides a {@link ReplaceCertificateTrustBehavior} implementation
 * to indicate that the server should update a specified trust manager provider.
 * <BR>
 * <BLOCKQUOTE>
 *   <B>NOTE:</B>  This class, and other classes within the
 *   {@code com.unboundid.ldap.sdk.unboundidds} package structure, are only
 *   supported for use against Ping Identity, UnboundID, and
 *   Nokia/Alcatel-Lucent 8661 server products.  These classes provide support
 *   for proprietary functionality or for external specifications that are not
 *   considered stable or mature enough to be guaranteed to work in an
 *   interoperable way with other types of LDAP servers.
 * </BLOCKQUOTE>
 */
@ThreadSafety(level=ThreadSafetyLevel.INTERFACE_THREADSAFE)
public final class TrustManagerProviderReplaceCertificateTrustBehavior
       extends ReplaceCertificateTrustBehavior
{
  /**
   * The BER type to use for the ASN.1 element containing an encoded
   * representation of this trust behavior object.
   */
  static final byte TYPE_TRUST_BEHAVIOR = (byte) 0x84;



  /**
   * The serial version UID for this serializable class.
   */
  private static final long serialVersionUID = 6812233987373145427L;



  // The name of the file-based trust manager provider to be updated with
  // information about the new listener certificate.
  @NotNull private final String providerName;



  /**
   * Creates a new trust manager provider trust behavior object with the
   * provided information.
   *
   * @param  providerName  The name of the file-based trust manager provider
   *                       to be updated with information about the new
   *                       listener certificate.  It must not be {@code null}.
   */
  public TrustManagerProviderReplaceCertificateTrustBehavior(
              @NotNull final String providerName)
  {
    Validator.ensureNotNullOrEmpty(providerName,
         "TrustManagerProviderReplaceCertificateTrustBehavior.providerName " +
              "must mot be null or empty.");

    this.providerName = providerName;
  }



  /**
   * Retrieves the name of the trust manager provider to be updated with
   * information about the new listener certificate.
   *
   * @return  The name of the trust manager provider to be updated with
   *          information about the new listener certificate.
   */
  @NotNull()
  public String getProviderName()
  {
    return providerName;
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  @NotNull()
  public ASN1Element encode()
  {
    return new ASN1OctetString(TYPE_TRUST_BEHAVIOR, providerName);
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public void toString(@NotNull final StringBuilder buffer)
  {
    buffer.append("TrustManagerProviderReplaceCertificateTrustBehavior(" +
         "providerName='");
    buffer.append(providerName);
    buffer.append("')");
  }
}
