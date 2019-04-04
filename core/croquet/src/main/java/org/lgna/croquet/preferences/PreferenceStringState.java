/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/

package org.lgna.croquet.preferences;

import edu.cmu.cs.dennisc.java.util.Lists;
import org.apache.axis.encoding.Base64;
import org.lgna.croquet.Group;
import org.lgna.croquet.StringState;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.UUID;
import java.util.prefs.Preferences;

/**
 * @author Dennis Cosgrove
 */
public abstract class PreferenceStringState extends StringState {
  private static final String NULL_VALUE = "__null__";

  private static final String CHARSET_NAME = "UTF-8";

  private static Cipher getCypher(byte[] encryptionKey, int mode) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException {
    final String ALGORITHM = "DES";
    DESKeySpec keySpec = new DESKeySpec(encryptionKey);
    SecretKey secretKey = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(mode, secretKey);
    return cipher;
  }

  private static String getInitialValue(String preferenceKey, String defaultInitialValue, byte[] encryptionKey) {
    Preferences userPreferences = PreferenceManager.getUserPreferences();
    if (userPreferences != null) {
      if (defaultInitialValue != null) {
        //pass
      } else {
        defaultInitialValue = NULL_VALUE;
      }
      String rv = userPreferences.get(preferenceKey, defaultInitialValue);
      if (encryptionKey != null) {
        try {
          Cipher cipher = getCypher(encryptionKey, Cipher.DECRYPT_MODE);
          byte[] base64 = Base64.decode(rv);
          byte[] bytes = cipher.doFinal(base64);
          rv = new String(bytes, CHARSET_NAME);
        } catch (Exception e) {
          e.printStackTrace();
          rv = defaultInitialValue;
        }
      }
      if (NULL_VALUE.equals(rv)) {
        rv = null;
      }
      return rv;
    } else {
      return defaultInitialValue;
    }
  }

  private static List<PreferenceStringState> instances = Lists.newCopyOnWriteArrayList();

  public static final void preserveAll(Preferences userPreferences) {
    for (PreferenceStringState state : instances) {
      String key = state.getPreferenceKey();
      String value = state.getValue();
      if (value != null) {
        //pass
      } else {
        value = NULL_VALUE;
      }
      String possiblyEncryptedValue;
      if (state.encryptionKey != null) {
        try {
          Cipher cipher = getCypher(state.encryptionKey, Cipher.ENCRYPT_MODE);
          byte[] bytes = cipher.doFinal(value.getBytes(CHARSET_NAME));
          possiblyEncryptedValue = Base64.encode(bytes);
        } catch (Exception e) {
          possiblyEncryptedValue = null;
        }
      } else {
        possiblyEncryptedValue = value;
      }
      if (possiblyEncryptedValue != null) {
        if (state.isStoringPreferenceDesired()) {
          userPreferences.put(key, possiblyEncryptedValue);
        } else {
          userPreferences.remove(key);
        }
      }
    }
  }

  private final String preferenceKey;
  private final byte[] encryptionKey;

  protected static byte[] getEncryptionKey(String s) {
    if (s != null) {
      try {
        return s.getBytes(CHARSET_NAME);
      } catch (UnsupportedEncodingException uee) {
        throw new RuntimeException(CHARSET_NAME, uee);
      }
    } else {
      return null;
    }
  }

  public PreferenceStringState(Group group, UUID migrationId, String initialValue, String preferenceKey, byte[] encryptionKey) {
    super(group, migrationId, getInitialValue(preferenceKey, initialValue, encryptionKey));
    this.preferenceKey = preferenceKey;
    this.encryptionKey = encryptionKey;
    assert instances.contains(this) == false;
    instances.add(this);
  }

  public PreferenceStringState(Group group, UUID migrationId, String initialValue, byte[] encryptionKey) {
    this(group, migrationId, initialValue, migrationId.toString(), encryptionKey);
  }

  public PreferenceStringState(Group group, UUID migrationId, String initialValue) {
    this(group, migrationId, initialValue, migrationId.toString(), getEncryptionKey(null));
  }

  public String getPreferenceKey() {
    return this.preferenceKey;
  }

  protected boolean isStoringPreferenceDesired() {
    return true;
  }
}
