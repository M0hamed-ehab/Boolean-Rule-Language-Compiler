package ast;

import java.util.List;

public abstract class AbstractAstNode implements AstNode {
    @Override
    public List<AstNode> children() {
        return List.of();
    }
}
