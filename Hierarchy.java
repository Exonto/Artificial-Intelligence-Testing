package com.gmail.username.tylersyme.survivor.utility;

import java.util.ArrayList;
import java.util.List;

public class Hierarchy {

	private TreeNode root;
	List<TreeNode> nodes = new ArrayList<TreeNode>();
	
	public Hierarchy(Object rootObj) {
		this.root = new TreeNode(rootObj, null);
		
		nodes.add(root);
	}
	
	public void add(Object child, Object parent) {
		TreeNode node = getTreeNodeOf(parent);
		if (node == null) return; //The parent object does not exist in the hierarchy
		
		TreeNode childNode = new TreeNode(child, node);
		
		node.children.add(childNode);
		
		nodes.add(childNode);
	}
	
	public void addAll(Object[] children, Object parent) {
		TreeNode node = getTreeNodeOf(parent);
		if (node == null) return; //The parent object does not exist in the hierarchy
		
		for (int xI = 0; xI < children.length; xI++) {
			Object child = children[xI];
			this.add(child, node);
		}
	}
	
	/**
	 * Will remove the object and all of that object's descendants.
	 * @param obj
	 */
	public void remove(Object obj) {
		TreeNode node = getTreeNodeOf(obj);
		remove(node);
	}
	
	/**
	 * Clears all but the root Object.
	 */
	public void clear() {
		for (int xI = 0; xI < this.root.children.size(); xI++) {
			TreeNode node = this.root.children.get(xI);
			this.remove(node);
			xI--;
		}
	}
	
	/**
	 * Will replace an existing Object with another Object.<p>
	 * This will not delete this Object's descendants.
	 * @param original
	 * @param replacement
	 */
	public void replace(Object original, Object replacement) { 
		TreeNode originalNode = getTreeNodeOf(original);
		TreeNode replacementNode = getTreeNodeOf(replacement);
		if (originalNode == null || replacementNode == null) { 
			throw new NullPointerException("Either your original Object or your replacement Object was null"); 
		}
		
		originalNode = replacementNode;
	}
	
	/**
	 * If the root object has no children this will return True.
	 * @return
	 */
	public boolean isEmpty() {
		return this.root.hasChildren() == false;
	}
	
	public int size() {
		return this.nodes.size();
	}
	
	public int getDepth() {
		int lowest = 0;
		for (int xI = 0; xI < nodes.size(); xI++) {
			TreeNode node = nodes.get(xI);
			if (node.depth > lowest) {
				lowest = node.depth;
			}
		}
		return lowest + 1;
	}
	
	public Integer getDepthOf(Object obj) {
		TreeNode node = this.getTreeNodeOf(obj);
		if (node == null) return null;
		
		return node.depth;
	}
	
	public boolean contains(Object obj) {
		return this.getTreeNodeOf(obj) != null;
	}
	
	/**
	 * Returns <b><i>all</i></b> descendants of the Object in a new Hierarchy. 
	 * If Object does not exist, this method will return null.
	 * @param fromObject
	 * @param toObject
	 * @return Hierarchy
	 */
	public Hierarchy getDescendants(Object fromObject) {
		Hierarchy h = new Hierarchy(fromObject);
		return this.getDescendants(fromObject, h);
	}
	
	/**
	 * Returns all descendants within the specified depth. 
	 * For Example, a depth of 0 will return a hierarchy with just the root object.
	 * If the Object does not exist, this method will return null.
	 * @param fromObject
	 * @param depth
	 * @return Hierarchy
	 */
	public Hierarchy getDescendants(Object fromObject, int depth) {
		Hierarchy h = new Hierarchy(fromObject);
		return this.getDescendants(fromObject, depth, 0, h);
	}
	
	/**
	 * Returns all children of the given parent as a linear list.<p>
	 * If the parent has no children, this method will return an empty list.
	 * If the parent does not exist, this method will return null.
	 * @param parent
	 * @return
	 */
	public List<Object> getChildren(Object parent) {
		TreeNode node = this.getTreeNodeOf(parent);
		if (node == null) return null;
		
		return node.toChildObjectList();
	}
	
	/**
	 * Returns all siblings of the given Object.<p>
	 * If the Object has no siblings, has no parent, or does not exist, this method will return null.
	 * @param obj
	 * @return
	 */
	public List<Object> getSiblings(Object obj) {
		TreeNode node = this.getTreeNodeOf(obj);
		if (node == null || node.hasParent() == false || node.parent.children.size() <= 1) return null;
		
		List<Object> siblings = node.parent.toChildObjectList();
		siblings.remove(obj);
		return siblings;
	}
	
	/**
	 * Returns the parent of the child.<p>
	 * If the child is also the root object of the hierarchy, or if the child does not exist,
	 * this method will return null. 
	 * @param child
	 * @return
	 */
	public Object getParent(Object child) {
		TreeNode node = this.getTreeNodeOf(child);
		if (node == null) return null;
		
		return node.parent.obj;
	}
	
	/**
	 * Returns all Objects at the given depth in the hierarchy.
	 * @param depth
	 * @return
	 */
	public List<Object> getObjectsAt(int depth) {
		List<Object> objects = new ArrayList<Object>();
		for (int xI = 0; xI < nodes.size(); xI++) {
			TreeNode node = nodes.get(xI);
			if (node.depth == depth) {
				objects.add(node.obj);
			}
		}
		
		return objects;
	}
	
	/**
	 * Returns all objects that do not have children.
	 * @return
	 */
	public List<Object> getLeafs() {
		List<Object> objects = new ArrayList<Object>();
		for (int xI = 0; xI < nodes.size(); xI++) {
			TreeNode node = nodes.get(xI);
			if (node.hasChildren() == false) {
				objects.add(node.obj);
			}
		}
		
		return objects;
	}
	
	/**
	 * Returns the root object of the Hierarchy
	 * @return
	 */
	public Object getRoot() {
		return this.root.obj;
	}
	
	/**
	 * Returns all elements in the hierarchy as a linear list in no particular order
	 * @return
	 */
	public List<Object> toUnorderedList() {
		List<Object> unorderedList = new ArrayList<Object>();
		for (int xI = 0; xI < nodes.size(); xI++) {
			TreeNode node = nodes.get(xI);
			unorderedList.add(node.obj);
		}
		
		return unorderedList;
	}
	
	/**
	 * Returns the TreeNode containing the given Object
	 * @param obj
	 * @return
	 */
	private TreeNode getTreeNodeOf(Object obj) {
		for (int xI = 0; xI < this.nodes.size(); xI++) {
			TreeNode node = nodes.get(xI);
			
			if (node.obj.equals(obj)) {
				return node;
			}
		}
		return null;
	}
	
	private void add(Object child, TreeNode parentNode) {
		TreeNode childNode = new TreeNode(child, parentNode);
		
		parentNode.children.add(childNode);
		
		nodes.add(childNode);
	}
	
	//Removes the node from the hierarchy and all subsequent children.
	private void remove(TreeNode node) {
		if (node.hasChildren()) {
			for (int xI = 0; xI < node.children.size(); xI++) {
				TreeNode child = node.children.get(xI);
				remove(child);
				xI--;
			}
		}
		nodes.remove(node);
		if (node.hasParent()) {
			node.parent.children.remove(node);
		}
	}
	
	//Returns all of the given object's descendants.
	private Hierarchy getDescendants(Object fromObject, Hierarchy h) {
		TreeNode focusNode = this.getTreeNodeOf(fromObject);
		if (focusNode == null) return null;
		
		if (focusNode.hasChildren()) {
			for (int xI = 0; xI < focusNode.children.size(); xI++) {
				TreeNode child = focusNode.children.get(xI);
				h.add(child.obj, focusNode.obj);
				if (child.hasChildren()) { //Used for performance purposes
					getDescendants(child.obj, h);
				}
			}
		}
		
		return h;
	}
	
	private Hierarchy getDescendants(Object fromObject, int depth, int currentDepth, Hierarchy h) {
		TreeNode focusNode = this.getTreeNodeOf(fromObject);
		if (focusNode == null) return null;
		
		if (focusNode.hasChildren()) {
			if (currentDepth < depth) {
				for (int xI = 0; xI < focusNode.children.size(); xI++) {
					TreeNode child = focusNode.children.get(xI);
					h.add(child.obj, focusNode.obj);
					if (child.hasChildren()) {
						getDescendants(child.obj, depth, currentDepth + 1, h);
					}
				}
			}
		}
		
		return h;
	}
	
}

class TreeNode {
	
	public Object obj;
	
	public TreeNode parent;
	public List<TreeNode> children = new ArrayList<TreeNode>();
	public int depth = 0;
	
	public TreeNode(Object obj, TreeNode parent) {
		this.obj = obj;
		this.parent = parent;
		
		this.depth = (parent == null) ? (0) : (this.parent.depth + 1);
	}
	
	public boolean hasChildren() {
		return this.children.size() > 0;
	}
	
	public boolean hasParent() {
		return parent != null;
	}
	
	public int getDepth() {
		return this.depth;
	}
	
	public String toString() {
		return this.obj.toString();
	}
	
	public List<Object> toChildObjectList() {
		List<Object> objects = new ArrayList<Object>();
		for (int xI = 0; xI < this.children.size(); xI++) {
			Object obj = this.children.get(xI).obj;
			objects.add(obj);
		}
		
		return objects;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreeNode other = (TreeNode) obj;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		return true;
	}
	
}












