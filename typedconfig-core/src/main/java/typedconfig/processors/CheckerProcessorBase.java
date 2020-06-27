package typedconfig.processors;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import typedconfig.PropertyFileCheck;


abstract public class CheckerProcessorBase extends AbstractProcessor {
  @Override
  public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
    if (annoations.size() == 0) {
      return false;
    }
    Set<? extends Element> elementSet = env.getElementsAnnotatedWith(PropertyFileCheck.class);
    Element annotatedElement = elementSet.iterator().next();

    AnnotationMirror annotationMirror = annotatedElement.getAnnotationMirrors().iterator().next();
    String annotationName = annotationMirror.getAnnotationType().toString();
    Map<? extends ExecutableElement, ? extends AnnotationValue> values = annotationMirror.getElementValues();
    String propertyPathfile = getPropertyPathfile(annotationMirror.getElementValues());

    System.out.println(annotationName + values.toString());
    String infoFile = getAnnotationInfoFile();

    System.out.println("------ test ----");
    System.out.println(propertyPathfile);
    System.out.println(infoFile);
    System.out.println("------ test ----");

    return true;
  }

  private String getPropertyPathfile(Map<? extends ExecutableElement, ? extends AnnotationValue> map) {
    return map.values().iterator().next().toString();
  }

  abstract protected String getAnnotationInfoFile();
}
