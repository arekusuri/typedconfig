package com.na.mw;

import java.util.List;
import javax.annotation.processing.SupportedAnnotationTypes;
import typedconfig.processors.StripperProcessor;


@SupportedAnnotationTypes({"typedconfig.Key", "typedconfig.PropertyFileCheck"})
public class FooStripperProcessor extends StripperProcessor {
  @Override
  protected void writeRuleFile(List<String> text) {
    super.writeRuleFile(text);
  }
}
