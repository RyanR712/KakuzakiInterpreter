/**
 * Describes a Node in the AST.
 */

package ParserTools.Nodes;

public abstract class ASTNode
{
    public abstract String toString();

    /**
     * Returns the line number this ASTNode is on.
     * @return line number this ASTNode is on.
     */
    public abstract int getLineNumber();
}
