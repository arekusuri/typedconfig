package com.na.mw;

import java.util.Properties;
import typedconfig.Default;
import typedconfig.Key;
import typedconfig.TypedConfig;


public class SfConfig2 extends TypedConfig {
  public SfConfig2(Properties prop) {
    super(prop);
  }

  @Key("salesforce.fetchRetryLimit")@Default("5")
  public int fetchRetryLimit;

}
