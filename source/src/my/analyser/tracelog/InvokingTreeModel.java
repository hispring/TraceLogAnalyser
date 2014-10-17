package my.analyser.tracelog;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class InvokingTreeModel extends DefaultTreeModel{
	
	private static class InvokingTreeNode implements TreeNode{
		
		private InvokingNode ivkNode;
		
		private TreeNode parent;
		
		private TreeNode[] children;
		
		public InvokingTreeNode(TreeNode parent, InvokingNode ivkNode) {
			this.parent = parent;
			this.ivkNode = ivkNode;
		}
		
		private TreeNode[] getChildren(){
			if (children == null) {
				ArrayList<TreeNode> childNodes = new ArrayList<TreeNode>();
				for (int i = 0; i < ivkNode.getNextSteps().count(); i++) {
					childNodes.add(new InvokingTreeNode(this, ivkNode.getNextSteps().get(i)));
				}
				children = childNodes.toArray(new TreeNode[0]);
			}
			return children;
		}

		@Override
		public TreeNode getChildAt(int childIndex) {
			return getChildren()[childIndex];
		}

		@Override
		public int getChildCount() {
			return ivkNode.getNextSteps().count();
		}

		@Override
		public TreeNode getParent() {
			return parent;
		}

		@Override
		public int getIndex(TreeNode node) {
			int index = -1;
			for (TreeNode nd : getChildren()) {
				index++;
				if (nd==node) {
					return index;
				}
			}
			return -1;
		}

		@Override
		public boolean getAllowsChildren() {
			return true;
		}

		@Override
		public boolean isLeaf() {
			return getChildCount() == 0;
		}

		@Override
		public Enumeration children() {
			return new Enumeration<TreeNode>() {
				
				private int index = 0;

				@Override
				public boolean hasMoreElements() {
					return index < getChildren().length;
				}

				@Override
				public TreeNode nextElement() {
					return getChildren()[index++];
				}
			};
		}
		
		@Override
		public String toString() {
			return ivkNode.getName();
		}
	}
	
	private static class InvokingTreeRoot implements TreeNode{
		private InvokingMap invokingMap;
		private TreeNode[] children;
		
		public InvokingTreeRoot(InvokingMap invokingMap) {
			this.invokingMap = invokingMap;
			InvokingNode[] rootIvkNodes = invokingMap.getInvokingRoots();
			children = new TreeNode[rootIvkNodes.length];
			for (int i = 0; i < rootIvkNodes.length; i++) {
				InvokingNode invokingNode = rootIvkNodes[i];
				children[i] = new InvokingTreeNode(this, invokingNode);
			}
		}

		@Override
		public TreeNode getChildAt(int childIndex) {
			return children[childIndex];
		}

		@Override
		public int getChildCount() {
			return children.length;
		}

		@Override
		public TreeNode getParent() {
			return null;
		}

		@Override
		public int getIndex(TreeNode node) {
			int index = -1;
			for (TreeNode nd : children) {
				index++;
				if (nd == node) {
					return index;
				}
			}
			return -1;
		}

		@Override
		public boolean getAllowsChildren() {
			return true;
		}

		@Override
		public boolean isLeaf() {
			return getChildCount() == 0;
		}

		@Override
		public Enumeration children() {
			return new Enumeration<TreeNode>() {
				
				private int index = 0;

				@Override
				public boolean hasMoreElements() {
					return index < children.length;
				}

				@Override
				public TreeNode nextElement() {
					return children[index++];
				}
			};
		}
		
	}

	public InvokingTreeModel(InvokingMap invokingMap) {
		super(new InvokingTreeRoot(invokingMap));
	}

}
