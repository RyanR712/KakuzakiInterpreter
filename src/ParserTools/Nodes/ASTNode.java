/**
 * Describes a Node in the AST.
 */

package ParserTools.Nodes;

public abstract class ASTNode
{
    private int lineNumber;

    public abstract String toString();

    public abstract int getLineNumber();
}
