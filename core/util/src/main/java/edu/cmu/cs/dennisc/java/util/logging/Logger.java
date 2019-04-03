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

package edu.cmu.cs.dennisc.java.util.logging;

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.lang.ThrowableUtilities;

import java.util.logging.Level;

/**
 * @author Dennis Cosgrove
 */
public class Logger {
  private static final String LOGGER_NAME = "static";

  private static java.util.logging.Logger initializeLogger() {
    java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LOGGER_NAME);
    logger.setUseParentHandlers(false);

    String levelText = System.getProperty(LEVEL_KEY, "SEVERE");
    Level level = null;
    for (Level customLevel : new Level[] {THROWABLE, TODO,
        //TESTING
    }) {
      if (levelText.equalsIgnoreCase(customLevel.getName())) {
        level = customLevel;
        break;
      }
    }
    if (level != null) {
      //pass
    } else {
      level = Level.parse(levelText);
      if (level != null) {
        //pass
      } else {
        level = Level.SEVERE;
      }
    }
    logger.setLevel(level);

    SegregatingConsoleHandler consoleHandler = new SegregatingConsoleHandler();
    consoleHandler.setFormatter(new ConsoleFormatter());
    logger.addHandler(consoleHandler);
    return logger;
  }

  private static class InstanceHolder {
    private static java.util.logging.Logger instance = initializeLogger();
  }

  public static java.util.logging.Logger getInstance() {
    return InstanceHolder.instance;
  }

  private static final String LEVEL_KEY = Logger.class.getName() + ".Level";

  private static final Level THROWABLE = new Level("THROWABLE", Level.SEVERE.intValue() + 1) {
  };
  //  private static final java.util.logging.Level TESTING = new java.util.logging.Level( "TESTING", java.util.logging.Level.SEVERE.intValue() - 1 ) {};
  private static final Level TODO = new Level("TODO", Level.WARNING.intValue() - 1) {
  };

  private Logger() {
    throw new AssertionError();
  }

  public static Level getLevel() {
    return getInstance().getLevel();
  }

  public static void setLevel(Level level) {
    getInstance().setLevel(level);
  }

  private static String buildMessage(Object object) {
    return object != null ? object.toString() : null;
  }

  private static String buildMessage(Object[] objects) {
    StringBuilder sb = new StringBuilder();
    String separator = "";
    for (Object o : objects) {
      sb.append(separator);
      sb.append(ArrayUtilities.toString(o));
      separator = " ";
    }
    return sb.toString();
  }

  private static boolean isLoggable(Level level) {
    return getInstance().isLoggable(level);
  }

  private static void log(Level level, Object object, Throwable throwable) {
    if (isLoggable(level)) {
      String message = buildMessage(object);
      if (throwable != null) {
        getInstance().log(level, message, throwable);
      } else {
        getInstance().log(level, message);
      }
    }
  }

  private static void log(Level level, Object[] objects, Throwable throwable) {
    if (isLoggable(level)) {
      String message = buildMessage(objects);
      if (throwable != null) {
        getInstance().log(level, message, throwable);
      } else {
        getInstance().log(level, message);
      }
    }
  }

  private static void log(Level level, Object object) {
    log(level, object, null);
  }

  private static void log(Level level, Object[] objects) {
    log(level, objects, null);
  }

  public static void outln(Object object) {
    System.out.println(buildMessage(object));
  }

  public static void outln(Object... objects) {
    System.out.println(buildMessage(objects));
  }

  public static void errln(Object object) {
    System.err.println(buildMessage(object));
  }

  public static void errln(Object... objects) {
    System.err.println(buildMessage(objects));
  }

  public static void throwable(Throwable t, Object object) {
    Object[] array = {ThrowableUtilities.getStackTraceAsString(t), object};
    log(THROWABLE, array);
  }

  public static void throwable(Throwable t, Object... objects) {
    Object[] array = new Object[objects.length + 1];
    System.arraycopy(objects, 0, array, 0, objects.length);
    array[array.length - 1] = ThrowableUtilities.getStackTraceAsString(t);
    log(THROWABLE, array);
  }

  public static void todo(Object object) {
    log(TODO, object);
  }

  public static void todo(Object... objects) {
    log(TODO, objects);
  }

  public static void severe(Object object) {
    log(Level.SEVERE, object);
  }

  public static void severe(Object... objects) {
    log(Level.SEVERE, objects);
  }

  public static void warning(Object object) {
    log(Level.WARNING, object);
  }

  public static void warning(Object... objects) {
    log(Level.WARNING, objects);
  }

  public static void info(Object object) {
    log(Level.INFO, object);
  }

  public static void info(Object... objects) {
    log(Level.INFO, objects);
  }

  public static void config(Object object) {
    log(Level.CONFIG, object);
  }

  public static void config(Object... objects) {
    log(Level.CONFIG, objects);
  }

  public static void fine(Object object) {
    log(Level.FINE, object);
  }

  public static void fine(Object... objects) {
    log(Level.FINE, objects);
  }

  public static void finer(Object object) {
    log(Level.FINER, object);
  }

  public static void finer(Object... objects) {
    log(Level.FINER, objects);
  }

  public static void finest(Object object) {
    log(Level.FINEST, object);
  }

  public static void finest(Object... objects) {
    log(Level.FINEST, objects);
  }
}
