import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.nio.file.Paths;
import java.util.concurrent.Callable;
import com.github.strattonbrazil.checklist.ChecklistContext;

public class TestTask {

   //private HelloWorld h;

   @Before
   public void setUp() throws Exception
   {
      //h = new HelloWorld();
   }

   @Test
   public void testAnonymous()
   {
       com.github.strattonbrazil.checklist.Task task = new com.github.strattonbrazil.checklist.Task() {
           public Callable<String> getWork(ChecklistContext ctx) { return null; }
       };
       assertThat(task, instanceOf(com.github.strattonbrazil.checklist.Task.class));
       //assertTrue(task.go());
   }

   @Test
   public void testAsyncFinished() {
       com.github.strattonbrazil.checklist.Task task = new com.github.strattonbrazil.checklist.Task() {
            public Callable<String> getWork(ChecklistContext ctx) {
                return new Callable<String>() {
                    public String call() {
                        return "foo";
                    }
                };
            };
       };

       try {
           ChecklistContext ctx = new ChecklistContext(Paths.get(""));
           Callable<String> callable = task.getWork(ctx);
           String ack = callable.call();
           assertEquals("string not equal", ack, "foo");
       } catch (Exception e) {
           fail("dummy callable threw an exception");
       }
   }

}
