package com.github.strattonbrazil.checklist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.HashSet;

public class Checklist
{
    private ArrayList<ActionNode> _actionNodes = new ArrayList<ActionNode>();

    public void addAction(String name, Action action) {
        // TODO: check for duplicate task names
        _actionNodes.add(new ActionNode(name, new ArrayList<String>(), action));
    }

    public void addAction(String name, ArrayList<String> dependencies, Action action) {
        // TODO: check for duplicate task names
        _actionNodes.add(new ActionNode(name, dependencies, action));
    }

    private class ActionNode {
        final String name;
        final ArrayList<String> dependencies;
        final Action action;

        public ActionNode(String name, ArrayList<String> deps, Action action) {
            this.name = name;
            this.dependencies = deps;
            this.action = action;
        }
    }

    private ArrayList<ActionNode> getSortedTasks() {
        HashMap<String,ActionNode> nodes = new HashMap<String,ActionNode>();
        HashMap<String,HashSet<String>> dependentNodes = new HashMap<String,HashSet<String>>();
        Stack<ActionNode> noDependencyNodes = new Stack<ActionNode>();

        // map action names to nodes
        for (ActionNode node : _actionNodes) {
            nodes.put(node.name, node);
            dependentNodes.put(node.name, new HashSet<String>());
        }

        // gather nodes with no dependencies
        for (ActionNode node : _actionNodes) {
            if (node.dependencies.size() == 0) {
                noDependencyNodes.push(node);
            }
            // map nodes to dependencies
            for (String dependencyName : node.dependencies) {
                dependentNodes.get(dependencyName).add(node.name);
            }
        }

        // add nodes with no dependencies to list; if all of a node's
        // dependencies have been added, it can be added
        //
        ArrayList<ActionNode> sortedNodes = new ArrayList<ActionNode>();
        while (!noDependencyNodes.isEmpty()) {
            ActionNode nNode = noDependencyNodes.pop();
            sortedNodes.add(nNode);

            // for each node "m" dependent on "n"
            for (String mNodeName : dependentNodes.get(nNode.name)) {
                ActionNode mNode = nodes.get(mNodeName);
                mNode.dependencies.remove(nNode.name);
                if (mNode.dependencies.isEmpty()) {
                    noDependencyNodes.push(mNode);
                }
            }
        }

        // TODO: check for cyclic dependencies

        return sortedNodes;
    }

    public void run() {
        getSortedTasks();
    }
}
