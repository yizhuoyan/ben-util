/*
 * @(#)MMEncoderException.java	1.1 
 *
 * Copyright (c) Nokia Corporation 2002
 *
 */

package com.ikeepglove.util.mms;

/**
 * Thrown when an error occurs encoding a buffer representing a
 * Multimedia Message
 *
 */

public class MMSException extends RuntimeException {

  public MMSException(String errormsg) {
    super(errormsg);
  }

  }


