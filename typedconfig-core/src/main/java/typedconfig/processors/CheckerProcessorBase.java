package typedconfig.processors;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import typedconfig.ConfigFolder;
import typedconfig.rule.RulesParser;


abstract public class CheckerProcessorBase extends AbstractProcessor {
  @Override
  public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
    if (annoations.size() == 0) {
      return false;
    }
    Set<? extends Element> elementSet = env.getElementsAnnotatedWith(ConfigFolder.class);
    Element annotatedElement = elementSet.iterator().next();

    AnnotationMirror annotationMirror = annotatedElement.getAnnotationMirrors().iterator().next();
    String annotationName = annotationMirror.getAnnotationType().toString();
    Map<? extends ExecutableElement, ? extends AnnotationValue> values = annotationMirror.getElementValues();

    System.out.println(annotationName + values.toString());
    String infoFile = getAnnotationInfoFile();

    System.out.println("------ test ----");
    System.out.println(infoFile);
    List<String> infoFileText = readFile(Paths.get(infoFile));
    RulesParser.parse(infoFileText);
    //
    List<Path> pathList = getPropertyPathfileList(annotationMirror.getElementValues());
    for (Path path : pathList) {
      readFile(path);
    }
    System.out.println("------ test ----");

    return true;
  }

  private List<String> readFile(Path path) {
    List<String> content = new ArrayList<>();
    try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
      String currentLine = null;
      while((currentLine = reader.readLine()) != null){//while there is content on the current line
        content.add(currentLine);
      }
      return content;
    }catch(IOException e){
      throw new RuntimeException(e);
    }
  }

  private List<Path> getPropertyPathfileList(Map<? extends ExecutableElement, ? extends AnnotationValue> map) {
    String path = map.values().iterator().next().getValue().toString();
    try (Stream<Path> paths = Files.walk(Paths.get(path))) {
      return paths.filter(Files::isRegularFile).collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  abstract protected String getAnnotationInfoFile();
}
