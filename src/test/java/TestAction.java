import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.instanceOf;
import com.github.strattonbrazil.checklist.Action;
import java.util.concurrent.Callable;
import com.github.strattonbrazil.checklist.ChecklistContext;

public class TestAction {

   //private HelloWorld h;

   @Before
   public void setUp() throws Exception
   {
      //h = new HelloWorld();
   }

   @Test
   public void testAnonymous()
   {
       Action action = new Action() {
           public Callable<String> getWork(ChecklistContext ctx) { return null; }
       };
       assertThat(action, instanceOf(Action.class));
       //assertTrue(action.go());
   }

   @Test
   public void testAsyncFinished() {
       Action action = new Action() {
            public Callable<String> getWork(ChecklistContext ctx) {
                return new Callable<String>() {
                    public String call() {
                        return "foo";
                    }
                };
            };
       };

       try {
           ChecklistContext ctx = new ChecklistContext();
           Callable<String> callable = action.getWork(ctx);
           String ack = callable.call();
           assertEquals("string not equal", ack, "foo");
       } catch (Exception e) {
           fail("dummy callable threw an exception");
       }
   }

}
