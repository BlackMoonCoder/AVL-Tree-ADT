package dendrologist;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.Scanner;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.regex.*;

/**
 * A testbed for an augmented implementation of an AVL tree
 * 
 * @author William Duncan, Johnny Williams III
 * @see AVLTreeAPI, AVLTree
 * 
 *      <pre>
 * Date: 10-12-2023
 * Course: csc 3102 
 * Programming Project # 2
 * Instructor: Dr. Duncan
 *      </pre>
 */
public class Dendrologist {
	public static void main(String[] args) throws FileNotFoundException, AVLTreeException {
		String usage = "Dendrologist <order-code> <command-file>\n";
		usage += "  <order-code>:\n";
		usage += "  0 ordered by increasing string length, primary key, and reverse lexicographical order, secondary key\n";
		usage += "  -1 for reverse lexicographical order\n";
		usage += "  1 for lexicographical order\n";
		usage += "  -2 ordered by decreasing string\n";
		usage += "  2 ordered by increasing string\n";
		usage += "  -3 ordered by decreasing string length, primary key, and reverse lexicographical order, secondary key\n";
		usage += "  3 ordered by increasing string length, primary key, and lexicographical order, secondary key\n";
		if (args.length != 2) {
			System.out.println(usage);
			throw new IllegalArgumentException("There should be 2 command line arguments.");
		}
		Comparator<String> function = null;
		int order = Integer.parseInt(args[0]);

		switch (order) {
		case 0:
			function = (x, y) -> {
				if (x.length() != y.length()) {
					return x.length() - y.length();
				}
				return -x.compareTo(y);
			};
			break;
		case -1:
			function = (x, y) -> {
				return -x.compareTo(y);
			};
			break;
		case 1:
			function = (x, y) -> {
				return x.compareTo(y);
			};
			break;
		case -2:
			function = (x, y) -> {
				return y.length() - x.length();
			};
			break;
		case 2:
			function = (x, y) -> {
				return x.length() - y.length();
			};
			break;
		case -3:
			function = (x, y) -> {
				if (x.length() != y.length()) {
					return y.length() - x.length();
				}
				return -x.compareTo(y);
			};
			break;
		case 3:
			function = (x, y) -> {
				if (x.length() != y.length()) {
					return x.length() - y.length();
				}
				return x.compareTo(y);
			};
			break;
		}

		AVLTree tree = new AVLTree(function);
		Function<String, PrintStream> output = x -> System.out.printf("%s\n", x);
		String file = args[1];
		Scanner in = new Scanner(new FileReader(file));

		while (in.hasNextLine()) {
			String Scommand = in.nextLine();
			String[] commandS = new String[2];
			commandS = Scommand.split(" ");
			switch (commandS[0]) {
			case "insert":
				if (commandS[1] == null) {
					throw new AVLTreeException("Command requires a value!");
				} else {
					tree.insert(commandS[1]);
					System.out.println("Inserted: " + commandS[1]);
				}
				break;

			case "delete":
				if (commandS[1] == null) {
					throw new AVLTreeException("Command requires a value!");
				} else {
					System.out.println("Deleted: " + commandS[1]);
					tree.remove(commandS[1]);
				}
				break;
			case "traverse":
				System.out.println("PreOrder Traversal:");
				tree.preorderTraverse(output);
				System.out.println("InOrder Traversal:");
				tree.traverse(output);
				System.out.println("PostOrder Traversal:");
				tree.postorderTraverse(output);
				break;
			case "gen":
				if (commandS[1] == null) {
					throw new AVLTreeException("Command requires a value!");
				} else {
					if (!tree.inTree(commandS[1])) {
						System.out.println("Geneology: " + commandS[1] + "UNDEFINED");
					} else {
						System.out.println("Geneology: " + commandS[1]);
						String parent = tree.getParent(commandS[1]).toString();
						ArrayList<String> getChildren = tree.getChildren(commandS[1]);
						String lchild = (getChildren.get(0) != null ? getChildren.get(0) : "NONE");
						String rchild = (getChildren.get(1) != null ? getChildren.get(1) : "NONE");
						int ancestors = tree.ancestors(commandS[1]);
						int descendants = tree.descendants(commandS[1]);
						System.out.printf("parent = %s, left-child = %s, right-child = %s\n", parent, lchild, rchild);
						System.out.printf("#anscestors = %d, #descendents = %d\n", ancestors, descendants);
					}
				}
				break;
			case "props":
				System.out.println("Properties:");
				int treesize = tree.size();
				int treeheight = tree.height();
				int treediameter = tree.diameter();
				boolean treeisfib = tree.isFibonacci();
				boolean treeiscomplete = tree.isComplete();
				System.out.printf("size = %d, height = %d, diameter = %d\n", treesize, treeheight, treediameter);
				System.out.printf("fibonacci? = %b, complete? = %b\n", treeisfib, treeiscomplete);
				break;

			default:
				throw new IllegalArgumentException("File: ParseError");
			}
		}
	}
}
