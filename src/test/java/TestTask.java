import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.concurrent.Callable;
import com.github.strattonbrazil.checklist.Task;
import com.github.strattonbrazil.checklist.TaskContext;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

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
       Task task = new Task() {
           public Callable<String> getWork(TaskContext ctx) { return null; }
       };
       assertThat(task, instanceOf(Task.class));

       //assertTrue(task.go());
   }

   @Test
   public void testAsyncFinished() {
       Task task = new Task() {
            public Callable<String> getWork(TaskContext ctx) {
                return new Callable<String>() {
                    public String call() {
                        return "foo";
                    }
                };
            };
       };

       try {
           TaskContext ctx = new TaskContext(Paths.get(""));
           Callable<String> callable = task.getWork(ctx);
           String ack = callable.call();
           assertEquals("string not equal", ack, "foo");
       } catch (Exception e) {
           fail("dummy callable threw an exception");
       }
   }

}
