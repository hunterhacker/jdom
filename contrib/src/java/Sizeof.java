// From http://www.javaworld.com/javatips/jw-javatip130_p.html

/**
 * A simple class to experiment with your JVM's garbage collector
 * and memory sizes for various data types.
 *
 * @author <a href="mailto:vlad@trilogy.com">Vladimir Roubtsov</a>
 */
public class Sizeof {
  public static void main(String [] args) throws Exception {
    // "warm up" all classes/methods that we are going to use:
    runGC();
    usedMemory();
        
    // array to keep strong references to allocated objects:
    final int count = 10000; // 10000 or so is enough for small ojects
    Object [] objects = new Object [count];
        
    long heap1 = 0;

    // allocate count+1 objects, discard the first one:
    for (int i = -1; i < count; ++ i) {
      Object object;
            
      // INSTANTIATE YOUR DATA HERE AND ASSIGN IT TO 'object':
            
      object = new Object(); // 8 bytes
      //object = new Integer(i); // 16 bytes
      //object = new Long(i); // same size as Integer?
      //object = createString(10); // 56 bytes? fine...
      //object = createString(9)+' '; // 72 bytes? the article explains why
      //object = new char [10]; // 32 bytes
      //object = new byte [32][1]; // 656 bytes?!
          
      if (i >= 0)
        objects [i] = object;
      else {
        object = null; // discard the "warmup" object
        runGC();
        heap1 = usedMemory(); // take a "before" heap snapshot
      }
    }

    runGC();
    long heap2 = usedMemory(); // take an "after" heap snapshot:
        
    final int size = Math.round(((float)(heap2 - heap1))/count);
    System.out.println("'before' heap: " + heap1 +
                        ", 'after' heap: " + heap2);
    System.out.println("heap delta: " + (heap2 - heap1) +
        ", {" + objects [0].getClass() + "} size = " + size + " bytes");
  }
    
  // a helper method for creating Strings of desired length
  // and avoiding getting tricked by String interning:
  public static String createString(final int length) {
    final char [] result = new char [length];
    for (int i = 0; i < length; ++ i)
      result [i] = (char) i;
        
    return new String(result);
  }

  // this is our way of requesting garbage collection to be run:
  // [how aggressive it is depends on the JVM to a large degree, but
  // it is almost always better than a single Runtime.gc() call]
  private static void runGC() throws Exception {
    // for whatever reason it helps to call Runtime.gc()
    // using several method calls:
    for (int r = 0; r < 4; ++ r)
      _runGC();
  }

  private static void _runGC() throws Exception {
    long usedMem1 = usedMemory(), usedMem2 = Long.MAX_VALUE;

    for (int i = 0; (usedMem1 < usedMem2) && (i < 1000); ++ i) {
      s_runtime.runFinalization();
      s_runtime.gc();
      Thread.currentThread().yield();
            
      usedMem2 = usedMem1;
      usedMem1 = usedMemory();
    }
  }

  private static long usedMemory() {
    return s_runtime.totalMemory() - s_runtime.freeMemory();
  }
    
  private static final Runtime s_runtime = Runtime.getRuntime();
}
