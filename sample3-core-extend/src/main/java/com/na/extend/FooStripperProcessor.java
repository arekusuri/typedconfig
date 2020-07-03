package com.na.extend;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import typedconfig.processors.StripperProcessor;


@SupportedAnnotationTypes({"typedconfig.Key", "typedconfig.ConfigFodler"})
public class FooStripperProcessor extends StripperProcessor {
  @Override
  protected void outputRule(Set<? extends Element > elements, String outputFile) {
    super.outputRule(elements, outputFile);
    List<Annotation> list = new ArrayList<>();
    for (Element annotatedElement : elements) {
      StringBuilder rule = new StringBuilder();
      for (AnnotationMirror annotationMirror : annotatedElement.getAnnotationMirrors()) {
        String annotationName = annotationMirror.getAnnotationType().toString();
        Map values = annotationMirror.getElementValues();
        rule.append(annotationName + "=");
        rule.append(values.entrySet().toString() + ";");
      }
//      list.add(rule.toString());
    }
    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ " + elements);
//    writeFile(text);
  }
}
