package com.github.strattonbrazil.checklist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.HashSet;

public class Checklist
{
    private ArrayList<ActionNode> _actionNodes = new ArrayList<ActionNode>();

    public void addAction(String name, Action action) {
        _actionNodes.add(new ActionNode(name, new ArrayList<String>(), action));
    }

    public void addAction(String name, ArrayList<String> dependencies, Action action) {
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

    public void run() {
        // map action names to actions
        HashMap<String,ActionNode> nodes = new HashMap<String,ActionNode>();

        HashMap<String,HashSet<String>> dependentNodes = new HashMap<String,HashSet<String>>();

        Stack<ActionNode> noDependencyNodes = new Stack<ActionNode>();
        for (ActionNode node : _actionNodes) {
            nodes.put(node.name, node);
            dependentNodes.put(node.name, new HashSet<String>());
        }

        for (ActionNode node : _actionNodes) {

            if (node.dependencies.size() == 0) {
                noDependencyNodes.push(node);
            } else {
                for (String dependencyName : node.dependencies) {
                    dependentNodes.get(dependencyName).add(node.name);
                }
            }
        }

        ArrayList<ActionNode> sortedNodes = new ArrayList<ActionNode>();
        while (!noDependencyNodes.isEmpty()) {
            ActionNode nNode = noDependencyNodes.pop();
            sortedNodes.add(nNode);

            System.out.println("adding task to list: " + nNode.name);

            // for each node "m" dependent on "n"
            System.out.println("getting tasks dependent on " + nNode.name);
            for (String mNodeName : dependentNodes.get(nNode.name)) {
                ActionNode mNode = nodes.get(mNodeName);
                System.out.println("  " + mNode.name + " is dependent on " + nNode.name);
                mNode.dependencies.remove(nNode.name);
                if (mNode.dependencies.isEmpty()) {
                    noDependencyNodes.push(mNode);
                }
            }
        }

        System.out.println("# of nodes: " + sortedNodes.size());
        System.out.println("sorted order: ");
        for (ActionNode a : sortedNodes) {
            System.out.println("  " + a.name);
        }


    }
}
