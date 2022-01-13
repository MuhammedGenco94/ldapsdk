/*
 * Copyright 2021-2022 Ping Identity Corporation
 * All Rights Reserved.
 */
/*
 * Copyright 2021-2022 Ping Identity Corporation
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
 * Copyright (C) 2021-2022 Ping Identity Corporation
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



import java.util.Collections;

import org.testng.annotations.Test;

import com.unboundid.asn1.ASN1OctetString;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSDKTestCase;
import com.unboundid.util.StaticUtils;



/**
 * This class provides a set of test cases for the
 * {@code ReplaceCertificateKeyStoreContent} class.
 */
public final class ReplaceCertificateKeyStoreContentTestCase
       extends LDAPSDKTestCase
{
  /**
   * Tests to ensure that it's possible to decode key store content in which
   * the store should be obtained from a server-side file.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testDecodeKeyStoreFileContent()
         throws Exception
  {
    final KeyStoreFileReplaceCertificateKeyStoreContent c =
         new KeyStoreFileReplaceCertificateKeyStoreContent("test-path",
              "test-pin", null, null, null);

    final ReplaceCertificateKeyStoreContent decoded =
         ReplaceCertificateKeyStoreContent.decode(c.encode());
    assertNotNull(decoded);
    assertTrue(
         decoded instanceof KeyStoreFileReplaceCertificateKeyStoreContent);

    assertNotNull(decoded.toString());
  }



  /**
   * Tests to ensure that it's possible to decode key store content in which
   * the store should be obtained from a client-provided key store.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testDecodeKeyStoreDataContent()
         throws Exception
  {
    final byte[] keyStoreData = StaticUtils.getBytes("test-key-store-data");

    final KeyStoreDataReplaceCertificateKeyStoreContent c =
         new KeyStoreDataReplaceCertificateKeyStoreContent(keyStoreData,
              "test-pin", null, null, null);

    final ReplaceCertificateKeyStoreContent decoded =
         ReplaceCertificateKeyStoreContent.decode(c.encode());
    assertNotNull(decoded);
    assertTrue(
         decoded instanceof KeyStoreDataReplaceCertificateKeyStoreContent);

    assertNotNull(decoded.toString());
  }



  /**
   * Tests to ensure that it's possible to decode key store content in which
   * the store should be obtained from a client-provided certificate data.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testDecodeCertificateDataContent()
         throws Exception
  {
    final byte[] certificateBytes = StaticUtils.getBytes("test-certificate");
    final byte[] keyBytes = StaticUtils.getBytes("test-private-key");

    final CertificateDataReplaceCertificateKeyStoreContent c =
         new CertificateDataReplaceCertificateKeyStoreContent(
              Collections.singletonList(certificateBytes), keyBytes);

    final ReplaceCertificateKeyStoreContent decoded =
         ReplaceCertificateKeyStoreContent.decode(c.encode());
    assertNotNull(decoded);
    assertTrue(
         decoded instanceof CertificateDataReplaceCertificateKeyStoreContent);

    assertNotNull(decoded.toString());
  }



  /**
   * Tests the behavior when trying to decode malformed content.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testDecodeMalformedContent()
         throws Exception
  {
    try
    {
      ReplaceCertificateKeyStoreContent.decode(
           new ASN1OctetString("malformed-content"));
      fail("Expected an exception when trying to decode malformed content");
    }
    catch (final LDAPException e)
    {
      // This was expected.
    }
  }
}