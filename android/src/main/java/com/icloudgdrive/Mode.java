package com.icloudgdrive;

public enum Mode {
  Appdata(0),
  Documents(1),
  Both(2);

  private final int value;
  Mode(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static Mode fromValue(int value) {
    for (Mode mode : values()) {
      if (mode.getValue() == value) {
        return mode;
      }
    }
    throw new IllegalArgumentException("Invalid mode value");
  }
}
