package typedconfig.processors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import lombok.SneakyThrows;
import typedconfig.ConfigClass;
import typedconfig.Key;


@SupportedAnnotationTypes({"typedconfig.ConfigClass", "typedconfig.Key", "typedconfig.ConfigFolder"})
public class StripperProcessor extends AbstractProcessor {
  private String stripInfofileName = "annotation-output.txt";
  private String generatedProcessClassName = "CheckerProcessor";

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
    if (annotations.size() == 0) {
      return false;
    }
    if (!isSpecifiedProcessor(env)) {
      return false;
    }
    try {
      JavaFileObject javaFileObjectProcessor = processingEnv.getFiler().createSourceFile(generatedProcessClassName);
      String fileName = generatedProcessClassName + ".java";
      String outputFile = javaFileObjectProcessor.getName().replace(fileName, stripInfofileName);
      this.processEnhanceConfigClass(annotations, env);
      Set<? extends Element> elements = env.getElementsAnnotatedWith(Key.class);
      this.outputRule(elements, outputFile);
      this.generateChecker(outputFile, javaFileObjectProcessor);
      return true;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean isSpecifiedProcessor(RoundEnvironment env) {
    Set<? extends Element> elementsConfigClass = env.getElementsAnnotatedWith(ConfigClass.class);
    if (elementsConfigClass.size() == 0) {
      return false;
    }
    Element element = elementsConfigClass.iterator().next();
    AnnotationMirror annotationMirror = element.getAnnotationMirrors().iterator().next();
    AnnotationValue values = annotationMirror.getElementValues().values().iterator().next();
    String typeName = values.getValue().toString();
    if(typeName.equals(void.class.getName()) || typeName.equals(this.getClass().getName())){
        return true;
    }
    return false;
  }

  public boolean processEnhanceConfigClass(Set<? extends TypeElement> annotations, RoundEnvironment env) {
    for (Element annotatedElement : env.getElementsAnnotatedWith(ConfigClass.class)) {

    }
    return true;
  }

  protected void outputRule(Set<? extends Element> elements, String outputFile) {
    List<String> text = new ArrayList<>();
    for (Element annotatedElement : elements) {
      StringBuilder rule = new StringBuilder(annotatedElement.getEnclosingElement().getSimpleName().toString() + ":");
      for (AnnotationMirror annotationMirror : annotatedElement.getAnnotationMirrors()) {
        String annotationName = annotationMirror.getAnnotationType().toString();
        Map values = annotationMirror.getElementValues();
        rule.append(annotationName + "=");
        rule.append(values.entrySet().toString() + ";");
      }
      text.add(rule.toString());
    }
    writeFile(text, outputFile);
  }

  protected void writeFile(List<String> text, String path) {
    try {
      System.out.println("Store to file: \n");
      System.out.println(path);
      File file = new File(path);
      Writer w = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
      PrintWriter pw = new PrintWriter(w);
      for (String line : text) {
        pw.println(line);
      }
      pw.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected void generateChecker(String outputFile, JavaFileObject javaFileObjectProcessor) {
    String classTemplate = "import javax.annotation.processing.SupportedAnnotationTypes;\n"
        + "import typedconfig.processors.CheckerProcessorBase;\n"
        + "@SupportedAnnotationTypes({\"typedconfig.PropertyFileCheck\"})\n"
        + "public class CheckerProcessor extends CheckerProcessorBase{\n"
        + "  @Override\n"
        + "  protected String getAnnotationInfoFile() {\n"
        + "    return \"%s\";\n"
        + "  }\n"
        + "}\n";
    String classText = String.format(classTemplate, outputFile);
    try {
      Writer writer = javaFileObjectProcessor.openWriter();
      writer.write(classText);
      writer.close();
      System.out.println("===============" + javaFileObjectProcessor.getName());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
