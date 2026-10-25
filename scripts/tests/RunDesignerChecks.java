import javax.tools.ToolProvider;
import java.nio.file.*;
import java.net.URLClassLoader;
/** Run from the project root with JDK 17 or newer. */
public class RunDesignerChecks {
 public static void main(String[] args)throws Exception {
  Path temp=Files.createTempDirectory("signplates-designer-checks-");
  Path classes=temp.resolve("classes");Files.createDirectories(classes);
  Path stub=temp.resolve("signplates/SignPlatesMod.java");Files.createDirectories(stub.getParent());
  Files.writeString(stub,"package signplates; public final class SignPlatesMod { public static final Logger LOGGER=new Logger(); public static final class Logger { public void warn(String message,Object... arguments) {} } }");
  int result=ToolProvider.getSystemJavaCompiler().run(null,null,null,"-d",classes.toString(),
   "targets/neoforge-1.21.1/src/main/java/signplates/StyleCost.java",
   "targets/neoforge-1.21.1/src/main/java/signplates/SignPlatesConfig.java",
   stub.toString(),"scripts/tests/StyleCostTest.java","scripts/tests/ThemeConfigTest.java");
  if(result!=0)throw new AssertionError("Compilation failed");
  try(var loader=new URLClassLoader(new java.net.URL[]{classes.toUri().toURL()})) {
   loader.loadClass("StyleCostTest").getMethod("main",String[].class).invoke(null,(Object)new String[0]);
  }
  int exit=new ProcessBuilder(Path.of(System.getProperty("java.home"),"bin/java").toString(),"-cp",classes.toString(),"ThemeConfigTest")
   .directory(temp.toFile()).inheritIO().start().waitFor();
  if(exit!=0)throw new AssertionError("Config checks failed");
 }
}
