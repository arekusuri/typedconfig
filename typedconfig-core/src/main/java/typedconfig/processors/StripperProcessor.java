package typedconfig.processors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import typedconfig.ConfigClass;
import typedconfig.Key;


@SupportedAnnotationTypes({"typedconfig.ConfigClass", "typedconfig.Key", "typedconfig.PropertyFileCheck"})
public class StripperProcessor extends AbstractProcessor {
  private String stripInfofileName = "annotation-output.txt";
  private String stripInfoFileFullPathfile;

  private String generatedProcessClassName = "CheckerProcessor";

  private JavaFileObject javaFileObjectProcessor;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    try {
      javaFileObjectProcessor = processingEnv.getFiler().createSourceFile(generatedProcessClassName);
      String fileName = generatedProcessClassName + ".java";
      stripInfoFileFullPathfile = javaFileObjectProcessor.getName().replace(fileName, stripInfofileName);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
    if (annotations.size() == 0) {
      return false;
    }
    processEnhanceConfigClass(annotations, env);
    List<String> text = new ArrayList<>();
    for (Element annotatedElement : env.getElementsAnnotatedWith(Key.class)) {
      StringBuilder rule = new StringBuilder(annotatedElement.getEnclosingElement().getSimpleName().toString() + ":");
      for (AnnotationMirror annotationMirror : annotatedElement.getAnnotationMirrors()) {
        String annotationName = annotationMirror.getAnnotationType().toString();
        Map values = annotationMirror.getElementValues();
        rule.append(annotationName + "=");
        rule.append(values.entrySet().toString() + ";");
      }
      text.add(rule.toString());
    }

    this.writeRuleFile(text);
    this.generateChecker();
    return true;
  }

  public boolean processEnhanceConfigClass(Set<? extends TypeElement> annoations, RoundEnvironment env) {
    for (Element annotatedElement : env.getElementsAnnotatedWith(ConfigClass.class)) {

    }
    return true;
  }

  protected void writeRuleFile(List<String> text) {
    try {
      System.out.println("Info - generated annotation info file: ");
      System.out.println(stripInfoFileFullPathfile);
      File file = new File(stripInfoFileFullPathfile);
      Writer w = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
      PrintWriter pw = new PrintWriter(w);
      for (String line : text) {
        pw.println(line + "\n");
      }
      pw.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected void generateChecker() {
    String classTemplate = "import javax.annotation.processing.SupportedAnnotationTypes;\n"
        + "import typedconfig.processors.CheckerProcessorBase;\n"
        + "@SupportedAnnotationTypes({\"typedconfig.PropertyFileCheck\"})\n"
        + "public class CheckerProcessor extends CheckerProcessorBase{\n"
        + "  @Override\n"
        + "  protected String getAnnotationInfoFile() {\n"
        + "    return \"%s\";\n"
        + "  }\n"
        + "}\n";
    String classText = String.format(classTemplate, stripInfoFileFullPathfile);
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
