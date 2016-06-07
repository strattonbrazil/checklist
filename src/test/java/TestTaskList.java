import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.github.strattonbrazil.checklist.TaskNode;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.lang.UnsupportedOperationException;


public class TestTaskList {


   @Before
   public void setUp() throws Exception
   {
   }

   @Test
   public void testGetSortedTasks()
   {
       com.github.strattonbrazil.checklist.TaskList taskList = new com.github.strattonbrazil.checklist.TaskList(Paths.get(""));
       TaskNode a1 = taskList.addTask("a1", toList(new String[] { "a2", "a3" }), null);
       TaskNode a2 = taskList.addTask("a2", toList(new String[] { "a3" }), null);
       TaskNode a3 = taskList.addTask("a3", null);
       ArrayList<TaskNode> nodes = taskList.getSortedTasks();
       assertTrue("a2 comes before a1", nodes.indexOf(a1) > nodes.indexOf(a2));
       assertTrue("a3 comes before a1", nodes.indexOf(a1) > nodes.indexOf(a3));
       assertTrue("a3 comes before a2", nodes.indexOf(a2) > nodes.indexOf(a3));
   }

   @Test(expected=UnsupportedOperationException.class)
   public void testGetSortedCyclicTasks()
   {
       com.github.strattonbrazil.checklist.TaskList taskList = new com.github.strattonbrazil.checklist.TaskList(Paths.get(""));
       TaskNode b1 = taskList.addTask("b1", toList(new String[] { "b2" }), null);
       TaskNode b2 = taskList.addTask("b2", toList(new String[] { "b3" }), null);
       TaskNode b3 = taskList.addTask("b3", toList(new String[] { "b1" }), null);
       ArrayList<TaskNode> nodes = taskList.getSortedTasks();
   }

   @Test
   public void testAddDependencies()
   {
       com.github.strattonbrazil.checklist.TaskList taskList = new com.github.strattonbrazil.checklist.TaskList(Paths.get(""));

       HashMap<String, TaskNode> nodes = new HashMap<String, TaskNode>();
       nodes.put("a1", new TaskNode("a1", toList(new String[] { "a2", "a3" }), null));
       nodes.put("a2", new TaskNode("a2", toList(new String[] { "a3", "a3" }), null));
       nodes.put("a3", new TaskNode("a3", toList(new String[] {}), null));

       HashSet<String> requiredTasks1 = new HashSet<String>();
       taskList.addDependencies("a1", nodes, requiredTasks1);
       assertEquals("a1 should depend on a2 and a3", 3, requiredTasks1.size());
       assertTrue(requiredTasks1.contains("a1"));
       assertTrue(requiredTasks1.contains("a2"));
       assertTrue(requiredTasks1.contains("a3"));

       HashSet<String> requiredTasks2 = new HashSet<String>();
       taskList.addDependencies("a2", nodes, requiredTasks2);
       assertEquals("a2 should depend on a3", 2, requiredTasks2.size());
       assertTrue(requiredTasks2.contains("a2"));
       assertTrue(requiredTasks2.contains("a3"));

       HashSet<String> requiredTasks3 = new HashSet<String>();
       taskList.addDependencies("a3", nodes, requiredTasks3);
       assertEquals("a2 should not have any dependencies", 1, requiredTasks3.size());
       assertTrue(requiredTasks3.contains("a3"));

   }

   private ArrayList<String> toList(String[] items) {
       return new ArrayList<String>(Arrays.asList(items));
   }
}
