// Posted to jdom-interest by Patrick Dowler

import java.net.URL;

class WhichClass {
  public static void main(String[] args) {
    String targetClass = null;
    if (args.length == 1) {
      targetClass = args[0];
    }
    else {
      printUsage();
      return;
    }

    try {
      Class.forName(targetClass);
      System.out.println("Found class '" + targetClass + "'");
    }
    catch (ClassNotFoundException ex){
      System.out.println("Failed to find class '" + targetClass + "'");
    }

    URL u = ClassLoader.getSystemResource(toPath(targetClass));
    if (u != null) {
      System.out.println("at URL '" + u + "'");
    }
  }

  private static String toPath(String className){
    StringBuffer sb = new StringBuffer(className);
    for (int i=0; i < sb.length(); i++) {
      if (sb.charAt(i) == '.') {
        sb.setCharAt(i, '/');
      }
    }
    sb.append(".class");
    return sb.toString();
  }

  private static void printUsage() {
    System.out.println("This program reports the location of a class file.");
    System.out.println("Usage: java WhichClass classname");
  }
}

