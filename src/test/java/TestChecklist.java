import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.hamcrest.CoreMatchers.instanceOf;
import com.github.strattonbrazil.checklist.Checklist;
import com.github.strattonbrazil.checklist.ActionNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.lang.UnsupportedOperationException;


public class TestChecklist {


   @Before
   public void setUp() throws Exception
   {
   }

   @Test
   public void testGetSortedTasks()
   {
       Checklist checklist = new Checklist();
       ActionNode a1 = checklist.addAction("a1", toList(new String[] { "a2", "a3" }), null);
       ActionNode a2 = checklist.addAction("a2", toList(new String[] { "a3" }), null);
       ActionNode a3 = checklist.addAction("a3", null);
       ArrayList<ActionNode> nodes = checklist.getSortedTasks();
       assertTrue("a2 comes before a1", nodes.indexOf(a1) > nodes.indexOf(a2));
       assertTrue("a3 comes before a1", nodes.indexOf(a1) > nodes.indexOf(a3));
       assertTrue("a3 comes before a2", nodes.indexOf(a2) > nodes.indexOf(a3));
   }

   @Test(expected=UnsupportedOperationException.class)
   public void testGetSortedCyclicTasks()
   {
       Checklist checklist = new Checklist();
       ActionNode b1 = checklist.addAction("b1", toList(new String[] { "b2" }), null);
       ActionNode b2 = checklist.addAction("b2", toList(new String[] { "b3" }), null);
       ActionNode b3 = checklist.addAction("b3", toList(new String[] { "b1" }), null);
       ArrayList<ActionNode> nodes = checklist.getSortedTasks();
   }

   @Test
   public void testAddDependencies()
   {
       Checklist checklist = new Checklist();

       HashMap<String,ActionNode> nodes = new HashMap<String,ActionNode>();
       nodes.put("a1", new ActionNode("a1", toList(new String[] { "a2", "a3" }), null));
       nodes.put("a2", new ActionNode("a2", toList(new String[] { "a3", "a3" }), null));
       nodes.put("a3", new ActionNode("a3", toList(new String[] {}), null));

       HashSet<String> requiredTasks1 = new HashSet<String>();
       checklist.addDependencies("a1", nodes, requiredTasks1);
       assertEquals("a1 should depend on a2 and a3", 3, requiredTasks1.size());
       assertTrue(requiredTasks1.contains("a1"));
       assertTrue(requiredTasks1.contains("a2"));
       assertTrue(requiredTasks1.contains("a3"));

       HashSet<String> requiredTasks2 = new HashSet<String>();
       checklist.addDependencies("a2", nodes, requiredTasks2);
       assertEquals("a2 should depend on a3", 2, requiredTasks2.size());
       assertTrue(requiredTasks2.contains("a2"));
       assertTrue(requiredTasks2.contains("a3"));

       HashSet<String> requiredTasks3 = new HashSet<String>();
       checklist.addDependencies("a3", nodes, requiredTasks3);
       assertEquals("a2 should not have any dependencies", 1, requiredTasks3.size());
       assertTrue(requiredTasks3.contains("a3"));

   }

   private ArrayList<String> toList(String[] items) {
       return new ArrayList<String>(Arrays.asList(items));
   }
}
