/**
 * Describes a Node in the AST.
 */

package CrossStageTools.CrossStageNodes;

import CrossStageTools.tokenType;

public abstract class ASTNode
{
    public abstract String toString();

    /**
     * Returns the line number this ASTNode is on.
     * @return line number this ASTNode is on.
     */
    public abstract int getLineNumber();

    /**
     * Returns this DataTypeNode's data representation in a tokenType.
     *
     * @return This DataTypeNode's data representation in a tokenType.
     */
    public abstract tokenType getType();
}
