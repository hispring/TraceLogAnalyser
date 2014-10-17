package my.analyser.tracelog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InvokingMap {
	
	/**
	 * 以 InvokingPoint 的 clazzName+methodName 为 key；
	 */
	private Map<String, InvokingNodeItem> nodeMap = new HashMap<String, InvokingMap.InvokingNodeItem>();
	
	private List<InvokingNodeItem> rootNodes = new ArrayList<InvokingNodeItem>();
	
	private class InvokingStepSetWrapper implements InvokingNodeSet{
		
		private InvokingTreeNode[] nodes;
		
		private InvokingStepSetWrapper(InvokingTreeNode[] nodes) {
			this.nodes = nodes;
		}
		
		@Override
		public int count() {
			return nodes.length;
		}

		@Override
		public InvokingNode get(int index) {
			return nodes[index];
		}
		
	}
	
	private class InvokingTreeNode implements InvokingNode{
		
		private String baseName;
		
		private int level;
		
		protected InvokingNodeItem nodeItem;
		
		private InvokingNodeSet children;
		
		private int fromLineNumber;
		
		private HashSet<InvokingStack> contextStacks = new HashSet<InvokingStack>();
		
		public InvokingTreeNode(String baseName, InvokingNodeItem nodeItem, int level, int fromLineNumber) {
			this.baseName = baseName;
			this.nodeItem = nodeItem;
			this.level = level;
			this.fromLineNumber = fromLineNumber;
		}
		
		public void addContextStack(InvokingStack stack){
			contextStacks.add(stack);
		}
		
		private boolean isContextStack(InvokingStack stack){
			return contextStacks.contains(stack);
		}

		@Override
		public String key() {
			return nodeItem.key;
		}

		@Override
		public int getTargetInvokingCount() {
			return nodeItem.getTargetInvokingCount();
		}
		
		protected int getContextTargetInvokingCount(){
			return this.contextStacks.size();
		}

		@Override
		public InvokingNodeSet getNextSteps() {
			if (children == null) {
				InvokingStack[] stacks = nodeItem.getInvokingStacks();
				//按方法名称分类子节点；
				LinkedHashMap<String, InvokingTreeNode> nextNodes = new LinkedHashMap<String, InvokingMap.InvokingTreeNode>();
				for (InvokingStack stack : stacks) {
					if (!isContextStack(stack)) {
						//限制在父节点的调用上下文中；
						continue;
					}
					InvokingPoint currInvokingPoint = nodeItem.getCurrent(stack, level);
					InvokingPoint nextInvokingPoint = nodeItem.getNext(stack, level);
					if (nextInvokingPoint != null) {
						String nextKey = getIndexKey(nextInvokingPoint);
						InvokingTreeNode nextNode;
						if (nextNodes.containsKey(nextKey)) {
							nextNode = nextNodes.get(nextKey);
						}else{
							InvokingNodeItem nextNodeItem = nodeMap.get(nextKey);
							nextNode = new InvokingTreeNode(getIndexKey(nextInvokingPoint), nextNodeItem, level+1, currInvokingPoint.getLineNumber());
							nextNodes.put(nextKey, nextNode);
						}
						nextNode.addContextStack(stack);
					}
				}
				children = new InvokingStepSetWrapper(nextNodes.values().toArray(new InvokingTreeNode[nextNodes.size()]));
			}
			return children;
		}

		@Override
		public String getName() {
			return formatTreeNodeName(getContextTargetInvokingCount(), nodeItem.getTargetInvokingCount(), fromLineNumber, baseName);
		}

		
	}
	
	private class InvokingTreeRootNode extends InvokingTreeNode{
		
		public InvokingTreeRootNode(InvokingNodeItem nodeItem, InvokingPoint currInvokingPoint) {
			super(getIndexKey(currInvokingPoint), nodeItem, 0, currInvokingPoint.getLineNumber());
		}
		
	}
	
	private class InvokingNodeItem {
		
		private final String key;
		
		private Map<InvokingStack, HashSet<Integer>> stacks = new LinkedHashMap<InvokingStack, HashSet<Integer>>();
		
		public InvokingNodeItem(InvokingStack stack, int level) {
			this.key = getIndexKey(stack.getInvokingPoint(level));
			recordInvoking(stack, level);
		}
		
		public void recordInvoking(InvokingStack stack, int level){
			HashSet<Integer> invokingLevelRecords = stacks.get(stack);
			if (invokingLevelRecords == null) {
				invokingLevelRecords = new HashSet<Integer>();
				stacks.put(stack, invokingLevelRecords);
			}
			invokingLevelRecords.add(level);
		}
		
//		public InvokingPoint getInvokingPoint(){
//			return invokingPoint;
//		}

		@SuppressWarnings("unused")
		public String key() {
			return key;
		}


		public int getTargetInvokingCount() {
			return stacks.size();
		}
		
		
		public InvokingStack[] getInvokingStacks(){
			return stacks.keySet().toArray(new InvokingStack[stacks.size()]);
		}
		
		public InvokingPoint getCurrent(InvokingStack stack, int currLevel){
			HashSet<Integer> levelRecords = stacks.get(stack);
			if (levelRecords != null && levelRecords.contains(currLevel)) {
				if (currLevel+1 < stack.getMaxLevel()) {
					return stack.getInvokingPoint(currLevel);
				}
			}
			return null;
		}
		
		public InvokingPoint getNext(InvokingStack stack, int currLevel){
			HashSet<Integer> levelRecords = stacks.get(stack);
			if (levelRecords != null && levelRecords.contains(currLevel)) {
				if (currLevel+1 < stack.getMaxLevel()) {
					return stack.getInvokingPoint(currLevel+1);
				}
			}
			return null;
		}
	}
	
	
	private static String getIndexKey(InvokingPoint invokingPoint){
		return invokingPoint.getClazzName() + "." + invokingPoint.getMethodName();
	}
	
	private static String formatTreeNodeName(int contextTargetInvokingCount, int targetInvokingCount, int fromLineNumber, String methodName){
		return String.format("[%s/%s]-:%s->%s", contextTargetInvokingCount, targetInvokingCount, fromLineNumber, methodName);
	}
	
	
	public void addInvokingStack(InvokingStack stack){
		InvokingNodeItem rootNode = nodeMap.get(getIndexKey(stack.getInvokingPoint(0)));
		if (rootNode == null) {
			rootNode = new InvokingNodeItem(stack, 0);
			nodeMap.put(rootNode.key, rootNode);
			rootNodes.add(rootNode);
		}else{
			rootNode.recordInvoking(stack, 0);
		}
		int maxLevel = stack.getMaxLevel();
		InvokingNodeItem node = null;
		for (int i = 1; i < maxLevel; i++) {
			node = nodeMap.get(getIndexKey(stack.getInvokingPoint(i)));
			if (node == null) {
				node = new InvokingNodeItem(stack, i);
				nodeMap.put(node.key, node);
			}else{
				node.recordInvoking(stack, i);
			}
		}
	}
	
	public InvokingNode[] getInvokingRoots(){
		ArrayList<InvokingNode> roots = new ArrayList<InvokingNode>();
		
		for (InvokingNodeItem rootNodeItem : rootNodes) {
			InvokingStack[] stacks = rootNodeItem.getInvokingStacks();
			LinkedHashMap<Integer, InvokingTreeNode> lineNodes = new LinkedHashMap<Integer, InvokingTreeNode>();
			for (InvokingStack st : stacks) {
				InvokingPoint currInvokingPoint = rootNodeItem.getCurrent(st, 0);
				InvokingTreeNode rootNode;
				if (lineNodes.containsKey(currInvokingPoint.getLineNumber())) {
					rootNode = lineNodes.get(currInvokingPoint.getLineNumber());
				}else{
					rootNode = new InvokingTreeRootNode(rootNodeItem, currInvokingPoint);
					roots.add(rootNode);
					lineNodes.put(currInvokingPoint.getLineNumber(), rootNode);
				}
				rootNode.addContextStack(st);
			}
		}
		return roots.toArray(new InvokingNode[roots.size()]);
	}
	
}
